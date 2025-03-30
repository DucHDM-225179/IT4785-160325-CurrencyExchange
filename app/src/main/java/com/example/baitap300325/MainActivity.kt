package com.example.baitap300325

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    val currencies = listOf(
        "VND", "USD", "JPY", "AED", "NOK"
    );
    val exchangeRate = listOf(
        listOf(1.0, 0.000039, 0.0059, 0.00014, 0.00041), // VND
        listOf(25575.00, 1.0, 150.06, 3.67, 10.49), // USD
        listOf(170.61, 0.0067, 1.0, 0.024, 0.070), // JPY
        listOf(6962.88, 0.27, 40.95, 1.0, 2.86), // AED
        listOf(2437.92, 0.095, 14.30, 0.35, 1.0),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currencyText1 = findViewById<EditText>(R.id.currencyText1)
        val currencySelection1 = findViewById<Spinner>(R.id.currencySelection1)
        val currencyText2 = findViewById<EditText>(R.id.currencyText2)
        val currencySelection2 = findViewById<Spinner>(R.id.currencySelection2)
        val currencyExchangeRate = findViewById<TextView>(R.id.currencyExchangeRate)

        val updateCurrencyExchangeRate: () -> Unit = {
            val c1: Int = currencySelection1.selectedItemPosition
            var S = "1.0 ${currencies[c1]} =\n"
            for (i in 0 until exchangeRate[c1].size) {
                if (i == c1) {
                    continue
                }
                S += "${exchangeRate[c1][i].toBigDecimal().toPlainString()} ${currencies[i]}\n"
            }
            currencyExchangeRate.text = S
        }
        val updateCurrency1BasedOn2 : () -> Unit = updateCurrency1BasedOn2Exit@ {
            val money2Text = currencyText2.text.toString()
            if (money2Text.isBlank() || money2Text.isEmpty()) {
                currencyText1.setText("")
                return@updateCurrency1BasedOn2Exit
            }
            val money2 = BigDecimal(money2Text)
            val c2 = currencySelection2.selectedItemPosition
            val c1 = currencySelection1.selectedItemPosition
            val money1 = money2.multiply(BigDecimal(
                exchangeRate[c2][c1]
            ))
            currencyText1.setText( money1.setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString() )
        }
        val updateCurrency2BasedOn1 : () -> Unit = updateCurrency2BasedOn1Exit@{
            val money1Text = currencyText1.text.toString()
            if (money1Text.isBlank() || money1Text.isEmpty()) {
                currencyText2.setText("")
                return@updateCurrency2BasedOn1Exit
            }
            val money1 = BigDecimal(money1Text)
            val c1 = currencySelection1.selectedItemPosition
            val c2 = currencySelection2.selectedItemPosition
            val money2 = money1.multiply(BigDecimal(
                exchangeRate[c1][c2]
            ))
            currencyText2.setText( money2.setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString() )
        }

        currencyText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!currencyText1.hasFocus()) {
                    // we got update from 2, don't update back
                    return
                }
                updateCurrency2BasedOn1()
                Log.e("Update from 1", "Update 2")
            }
        })
        currencyText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!currencyText2.hasFocus()) {
                    // we have update from 1, don't update back
                    return
                }
                updateCurrency1BasedOn2()
                Log.e("Update from 2", "Update 1")
            }
        })

        val currencyAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            currencies
        )
        currencySelection1.setAdapter(currencyAdapter)
        currencySelection1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("Item 1 Selected", currencies[position])
                currencyText1.requestFocus()
                updateCurrency2BasedOn1()
                updateCurrencyExchangeRate()
            }
        }

        currencySelection2.setAdapter(currencyAdapter)
        currencySelection2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //void
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currencyText2.requestFocus()
                updateCurrency1BasedOn2()
            }
        }
    }
}