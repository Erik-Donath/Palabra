package de.palabra.palabra.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.Gson
import de.palabra.palabra.db.LektionWithVocabs
import java.io.File

object ExportUtil {
    const val EXPORT_FILE_FORMAT = FileFormatV1.FORMAT_VERSION

    fun exportAndShareLektion(context: Context, lektionWithVocabs: LektionWithVocabs) {
        val lektion = lektionWithVocabs.lektion
        val vocabs = lektionWithVocabs.vocabs.map {
            FileFormatV1.Vocab(
                word = it.word,
                toWord = it.toWord,
                correctCount = it.correctCount,
                wrongCount = it.wrongCount
            )
        }
        val exportLektion = FileFormatV1.Lektion(
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