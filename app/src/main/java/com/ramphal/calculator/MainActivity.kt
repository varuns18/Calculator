package com.ramphal.calculator

import android.R.attr.text
import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var inputOutput: TextView? = null
    var lastDot: Boolean = false
    var lastOperator: Boolean = false
    var isInputEmpty: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inputOutput = findViewById(R.id.inputOutput)

    }

    fun onDigitClick(view: View) {
        inputOutput?.append((view as Button).text)
        if (lastOperator){
            lastDot = false
            lastOperator = false
        }
        isInputEmpty = false
    }

    fun onClearClick(view: View) {
        inputOutput?.text = ""
        lastDot = false
        lastOperator = false
        isInputEmpty = true
    }

    fun onDecimalPointClick(view: View) {

        if (!lastDot && !isInputEmpty && !lastOperator) {
            inputOutput?.append(".")
            lastDot = true
        } else if (!lastDot && isInputEmpty || lastOperator) {
            inputOutput?.append("0.")
            lastDot = true
            lastOperator = false
        }

    }

    fun onOperatorClick(view: View) {
        if (!isInputEmpty){
            onEqualClick(view = view)
            if (!lastOperator) {
                inputOutput?.append((view as Button).text)
                lastOperator = true
                lastDot = false
            } else {
                inputOutput?.text = inputOutput?.text.toString().dropLast(1)
                inputOutput?.append((view as Button).text)
            }
        }
    }

    fun onEqualClick(view: View) {
        var text = inputOutput?.text.toString()
        // Skip if last press was operator or no operator present
        if (!lastOperator && isOperatorAdded(text)) {
            try {
                var Prefix = ""
                if (text.startsWith("-")) {
                    Prefix = "-"
                    text = text.substring(1)
                }

                val operator = listOf('+','-','*','/').first { text.contains(it) }

                // Split on that operator (escape in regex if needed)
                val parts = text.split(operator)
                var one = parts[0].toDouble()
                val two = parts[1].toDouble()

                if (Prefix.isNotEmpty()){
                    one = one * -1.0
                    Prefix = ""
                }

                // Compute result based on operator
                val result = when (operator) {
                    '+' -> one + two
                    '-' -> one - two
                    '*' -> one * two
                    '/' -> one / two
                    else -> 0.0
                }

                // Display result
                inputOutput?.text = removeZero(result.toString())
            } catch (e: Exception) {
                onClearClick(view = view)
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        var valueIn = value
        if (valueIn.startsWith("-")) {
            valueIn = valueIn.substring(1)
        }
        // Check for any of the four operators
        return valueIn.any { it == '+' || it == '-' || it == '*' || it == '/' }
    }

    private fun removeZero(value: String): String{
        var myValue = value
        if (myValue.contains(".0")) {
            myValue = myValue.substring(0, myValue.length - 2)
        }
        return myValue
    }



}
























