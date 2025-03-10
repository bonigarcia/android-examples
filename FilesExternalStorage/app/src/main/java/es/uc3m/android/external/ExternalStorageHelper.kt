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
package es.uc3m.android.external

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExternalStorageHelper(private val context: Context) {

    // Get the public documents directory
    fun getPublicDocumentsDir(): File? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    }

    // Write to a file in external storage
    fun writeToExternalStorage(fileName: String, content: String): Boolean {
        return try {
            val documentsDir = getPublicDocumentsDir()
            if ((documentsDir != null) && !documentsDir.exists()) {
                documentsDir.mkdirs()
            }
            val file = File(documentsDir, fileName)
            FileOutputStream(file).use { stream ->
                stream.write(content.toByteArray())
            }
            true
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            false
        }
    }

    // Read from a file in external storage
    fun readFromExternalStorage(fileName: String): String {
        return try {
            val documentsDir = getPublicDocumentsDir()
            val file = File(documentsDir, fileName)
            Toast.makeText(context, file.absolutePath, Toast.LENGTH_LONG).show()
            file.bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            ""
        }
    }
}