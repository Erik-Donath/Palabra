package de.palabra.palabra.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.Gson
import de.palabra.palabra.db.LektionWithVocabs
import java.io.File
import java.io.FileWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ExportVocab(
    val word: String,
    val toWord: String,
    val correctCount: Int,
    val wrongCount: Int
)

data class ExportLektion(
    val dbVersion: Int,
    val title: String,
    val fromLangCode: String,
    val toLangCode: String,
    val description: String?,
    val vocabs: List<ExportVocab>
)

object ExportUtil {
    fun exportAndShareLektion(context: Context, lektionWithVocabs: LektionWithVocabs, dbVersion: Int) {
        val lektion = lektionWithVocabs.lektion
        val vocabs = lektionWithVocabs.vocabs.map {
            ExportVocab(
                word = it.word,
                toWord = it.toWord,
                correctCount = it.correctCount,
                wrongCount = it.wrongCount
            )
        }
        val exportLektion = ExportLektion(
            dbVersion = dbVersion,
            title = lektion.title,
            fromLangCode = lektion.fromLangCode,
            toLangCode = lektion.toLangCode,
            description = lektion.description,
            vocabs = vocabs
        )
        val gson = Gson()
        val json = gson.toJson(exportLektion)

        val fileName = lektion.title.replace(" ", "_") + ".palabra"
        val file = File(context.cacheDir, fileName)
        file.writeText(json)

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export Lektion"))
    }
}