package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import android.util.Log
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    //ATIVIDADE 1 - fun que é chamada quando algum dos botoes forem chamados, indentifica o botao e acrescentar o valor do botao ao texto do TextField
    fun inserirTexto(view: View){
        var texto = findViewById(R.id.text_calc) as TextView

        var textoExistente: String = texto.text.toString()
        var caracter = ""

        when (view.getId()) {
            R.id.btn_7 -> {
                caracter = "7"
            }
            R.id.btn_8 -> {
                caracter = "8"
            }
            R.id.btn_9 -> {
                caracter = "9"
            }
            R.id.btn_Divide -> {
                caracter = "/"
            }
            R.id.btn_4 -> {
                caracter = "4"
            }
            R.id.btn_5 -> {
                caracter = "5"
            }
            R.id.btn_6 -> {
                caracter = "6"
            }
            R.id.btn_Multiply -> {
                caracter = "*"
            }
            R.id.btn_1 -> {
                caracter = "1"
            }
            R.id.btn_2 -> {
                caracter = "2"
            }
            R.id.btn_3 -> {
                caracter = "3"
            }
            R.id.btn_Subtract -> {
                caracter = "-"
            }
            R.id.btn_Dot -> {
                caracter = "."
            }
            R.id.btn_0 -> {
                caracter = "0"
            }
            R.id.btn_Equal -> {


                try {
                    //ATIVIDADE 2 - Caso responsavel em chamar eval e calcular a operação
                    caracter = eval(textoExistente).toString()
                    textoExistente = ""
                } catch (e: Exception) {
                    //ATIVIDADE 3 - Mostrar o erro em caso de formato incorreto de operação
                    Toast.makeText(this, "Operação incorreta", Toast.LENGTH_SHORT).show()
                }




            }
            R.id.btn_Add -> {
                caracter = "+"
            }
            R.id.btn_LParen -> {
                caracter = "("
            }
            R.id.btn_RParen -> {
                caracter = ")"
            }
            R.id.btn_Power -> {
                caracter = "ˆ"
            }
            R.id.btn_Clear -> {
                caracter = ""
                textoExistente = ""
            }
            else -> {}
        }

        texto.setText(textoExistente + caracter)



    }





    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
