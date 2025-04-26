package de.palabra.palabra

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.widget.Button
import android.widget.ImageButton

class LearnActivity : AppCompatActivity() {

    private val word = "Holla"
    private val solutions =  arrayOf("Hallo", "Tasse", "Kuchen")

    override fun onCreate(savedInstanceState: Bundle?) {
        println("Learn Activaty started")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        val guess = findViewById<TextView>(R.id.guess);
        val solution1 = findViewById<Button>(R.id.solution1);
        val solution2 = findViewById<Button>(R.id.solution2);
        val solution3 = findViewById<Button>(R.id.solution3);
        val homeBtn = findViewById<ImageButton>(R.id.homeBtn)
        val listBtn = findViewById<ImageButton>(R.id.listBtn)

        guess.text = word
        solution1.text = solutions[0]
        solution2.text = solutions[1]
        solution3.text = solutions[2]

        solution1.setOnClickListener { answer(0) }
        solution2.setOnClickListener { answer(1) }
        solution3.setOnClickListener { answer(2) }

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        listBtn.setOnClickListener {
            println("List")
        }
    }

    private fun answer(index: Int) {
        println("Solution Index: $index")
        //val intent = Intent(this, ResultActivity::class.java)
        //intent.putExtra("word", word)
        //intent.putExtra("antwort", solutions[gewaehlt])
        //intent.putExtra("richtig", gewaehlteAntwort == vokabel.richtigeAntwort)
        //startActivity(intent)
        //finish()
    }
}