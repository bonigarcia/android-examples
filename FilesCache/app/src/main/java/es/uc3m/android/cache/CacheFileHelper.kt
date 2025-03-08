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
package es.uc3m.android.cache

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CacheFileHelper(private val context: Context) {

    // Write to a file in the cache directory
    fun writeToCache(fileName: String, content: String): Boolean {
        return try {
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { stream ->
                stream.write(content.toByteArray())
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    // Read from a file in the cache directory
    fun readFromCache(fileName: String): String {
        return try {
            val file = File(context.cacheDir, fileName)
            file.bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    // Delete a file from the cache directory
    fun deleteFromCache(fileName: String): Boolean {
        val file = File(context.cacheDir, fileName)
        return file.delete()
    }
}