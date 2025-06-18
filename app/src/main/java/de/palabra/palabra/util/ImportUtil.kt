package de.palabra.palabra.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.gson.Gson
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import de.palabra.palabra.db.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object ImportUtil {
    suspend fun importLektionFromUri(
        context: Context,
        uri: Uri,
        repository: Repository
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver

            val json = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                ?: return@withContext false

            // Parse the formatVersion field as raw JSON first
            val formatVersion = try {
                JSONObject(json).optInt("formatVersion", 0) // default to 0 if not present => Unknown File Format Error
            } catch (e: Exception) {
                0 // fallback for super old files
            }

            when (formatVersion) {
                FileFormatV1.FORMAT_VERSION -> importLektionV1(context, json, repository)
                // Add additional cases for future format versions
                else -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.unknown_format, formatVersion), Toast.LENGTH_LONG).show()
                    }
                    false
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.import_error, e.localizedMessage ?: "Unknown error"), Toast.LENGTH_LONG).show()
            }
            false
        }
    }

    private suspend fun importLektionV1(
        context: Context,
        json: String,
        repository: Repository
    ): Boolean {
        val gson = Gson()
        val importLektion = gson.fromJson(json, FileFormatV1.Lektion::class.java)

        val lektionId = repository.insertLektion(
            Lektion(
                title = importLektion.title,
                fromLangCode = importLektion.fromLangCode,
                toLangCode = importLektion.toLangCode,
                description = importLektion.description ?: ""
            )
        ).toInt()

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
            Toast.makeText(context, context.getString(R.string.import_success), Toast.LENGTH_SHORT).show()
        }
        return true
    }
}