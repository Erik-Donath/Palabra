package de.palabra.palabra.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.google.gson.Gson
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import de.palabra.palabra.db.PalabraDatabase
import de.palabra.palabra.db.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ImportVocab(
    val word: String,
    val toWord: String,
    val correctCount: Int,
    val wrongCount: Int
)

data class ImportLektion(
    val dbVersion: Int,
    val title: String,
    val fromLangCode: String,
    val toLangCode: String,
    val description: String,
    val vocabs: List<ImportVocab>
)

object ImportUtil {
    suspend fun importLektionFromUri(
        context: Context,
        uri: Uri,
        repository: Repository
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            if (!isPalabraFile(contentResolver, uri)) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Bitte wähle eine gültige .palabra Datei aus.", Toast.LENGTH_LONG).show()
                }
                return@withContext false
            }

            val json = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                ?: return@withContext false

            val gson = Gson()
            val importLektion = gson.fromJson(json, ImportLektion::class.java)
            val dbVersion = PalabraDatabase.getInstance(context).openHelper.readableDatabase.version

            if (importLektion.dbVersion != dbVersion) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Datenbank-Version stimmt nicht überein. Import nicht möglich.", Toast.LENGTH_LONG).show()
                }
                return@withContext false
            }

            // Insert Lektion
            val lektionId = repository.insertLektion(
                Lektion(
                    title = importLektion.title,
                    fromLangCode = importLektion.fromLangCode,
                    toLangCode = importLektion.toLangCode,
                    description = importLektion.description
                )
            ).toInt()

            // Insert Vocabs
            importLektion.vocabs.forEach { v ->
                repository.insertVocab(
                    Vocab(
                        lektionId = lektionId,
                        word = v.word,
                        toWord = v.toWord,
                        correctCount = v.correctCount,
                        wrongCount = v.wrongCount
                    )
                )
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Lektion importiert!", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fehler beim Import: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
            false
        }
    }

    private fun isPalabraFile(contentResolver: ContentResolver, uri: Uri): Boolean {
        val fileName = getFileNameFromUri(contentResolver, uri)
        return fileName != null && fileName.endsWith(".palabra", ignoreCase = true)
    }

    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
        cursor?.moveToFirst()
        val name = if (nameIndex >= 0) cursor?.getString(nameIndex) else null
        cursor?.close()
        return name
    }
}