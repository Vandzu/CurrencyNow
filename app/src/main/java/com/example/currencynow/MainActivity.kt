package com.example.currencynow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        val retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val spinner: Spinner = findViewById(R.id.spinner);
        val options = listOf("USD", "EUR", "JPY", "GBP", "CHF", "CAD", "AUD", "NOK", "DKK", "CNY");
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = options[position]
                GlobalScope.launch {
                    Log.d("PDM", selectedItem);
                    val result = apiService.getCurrencyList(selectedItem);
                    Log.d("PDM", result.toString());
                    runOnUiThread {
                        renderTable(result.rates);
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada selecionado
            }
        }
    }

    fun renderTable(rates: Map<String, Double>) {
        val table = findViewById<TableLayout>(R.id.tableValues)
        var count = 0

        for ((currencyCode, rate) in rates.entries) {
            val row = TableRow(this);
            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.layoutParams = lp;

            val textViewCode = TextView(this);
            textViewCode.text = currencyCode;
            textViewCode.width = 250;
            row.addView(textViewCode);

            val textViewRate = TextView(this);
            textViewRate.setText(rate.toString());
            textViewRate.width = 250;
            row.addView(textViewRate);

            table.addView(row, count);
            count += 1
        }
    }

}