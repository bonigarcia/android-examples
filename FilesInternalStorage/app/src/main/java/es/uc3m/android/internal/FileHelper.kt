/*
 * (C) Copyright 2025 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package es.uc3m.android.internal

import android.content.Context
import android.widget.Toast
import java.io.IOException

class FileHelper(private val context: Context) {

    // Write to a file in internal storage
    fun writeToFile(fileName: String, content: String): Boolean {
        return try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream ->
                stream.write(content.toByteArray())
            }
            true
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            false
        }
    }

    // Read from a file in internal storage
    fun readFromFile(fileName: String): String {
        return try {
            context.openFileInput(fileName).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            ""
        }
    }

    // List all files in internal storage
    fun listFiles(): Array<String> {
        return context.fileList()
    }

    // Delete a file in internal storage
    fun deleteFile(fileName: String): Boolean {
        return context.deleteFile(fileName)
    }
}