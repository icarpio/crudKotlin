package com.example.crudkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.crudkotlin.db.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnView = findViewById<Button>(R.id.btnView)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toIntOrNull()
            val message = etMessage.text.toString()


            if (name.isNotBlank() && age != null && message.isNotBlank()) {
                if (dbHelper.insertCustomer(name, age, message)) {
                    Toast.makeText(this, "Person Added", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etAge.text.clear()
                    etMessage.text.clear()
                } else {
                    Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
            }
        }

        btnView.setOnClickListener {
            val cursor = dbHelper.getAllCustomers()
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))
                    // Usa los datos como necesites
                    Toast.makeText(this, "ID: $id, Name: $name, Age: $age", Toast.LENGTH_SHORT).show()
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

}
