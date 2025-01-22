package com.vairagicodes.externalstorageexample

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton: Button = findViewById(R.id.btn_save_file)
        val readButton: Button = findViewById(R.id.btn_read_file)

        val fileNameEt: EditText = findViewById(R.id.file_name_et)
        val fileDataEt: EditText = findViewById(R.id.file_data_et)
        val dataTextView: TextView = findViewById(R.id.data_tv)

        saveButton.setOnClickListener {
            if (isExternalStorageReadOnly() || !isExternalStorageAvailable()) {
                Toast.makeText(this, "Storage is not available", Toast.LENGTH_SHORT).show()
            } else {
                val file = File(getExternalFilesDir("VairagiCodes"), fileNameEt.text.toString())
                writeData(file, fileDataEt.text.toString())
            }
        }

        readButton.setOnClickListener {
            if (isExternalStorageReadOnly() || !isExternalStorageAvailable()) {
                Toast.makeText(this, "Storage is not available", Toast.LENGTH_SHORT).show()
            } else {
                val file = File(getExternalFilesDir("VairagiCodes"), fileNameEt.text.toString())
                val data = readData(file)
                dataTextView.visibility = View.VISIBLE
                dataTextView.text = data
            }
        }
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

    private fun writeData(file: File, fileData: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(fileData.toByteArray())
            fileOutputStream.flush()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            fileOutputStream?.close()
        }
    }

    private fun readData(file: File): String {
        return try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder = StringBuilder()
            var text: String?

            while (bufferedReader.readLine().also { text = it } != null) {
                stringBuilder.append(text)
            }

            stringBuilder.toString()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}

