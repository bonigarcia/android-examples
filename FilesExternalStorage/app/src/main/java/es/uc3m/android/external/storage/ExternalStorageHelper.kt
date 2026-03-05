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
package es.uc3m.android.external.storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.IOException

private const val TAG = "ExternalStorageHelper"

class ExternalStorageHelper(private val context: Context) {

    // 1. App-specific external storage
    fun writeToAppSpecificStorage(fileName: String, content: String): Boolean {
        return try {
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(content)
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error writing to app-specific storage", e)
            false
        }
    }

    fun readFromAppSpecificStorage(fileName: String): String {
        return try {
            val file = File(context.getExternalFilesDir(null), fileName)
            file.readText()
        } catch (e: IOException) {
            Log.e(TAG, "Error reading from app-specific storage", e)
            ""
        }
    }

    // 2. MediaStore: Saving an image
    fun saveImageToMediaStore(bitmap: Bitmap, displayName: String): Uri? {
        val resolver = context.contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        return try {
            val uri = resolver.insert(imageCollection, contentValues)
            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    if (outputStream == null || !bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        throw IOException("Failed to save bitmap")
                    }
                }
            }
            uri
        } catch (e: IOException) {
            Log.e(TAG, "Error saving image to MediaStore", e)
            null
        }
    }

    fun readImageFromUri(uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading image from MediaStore", e)
            null
        }
    }

    // 3. SAF (Storage Access Framework) logic
    fun writeToUri(uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri).use { outputStream ->
                outputStream?.write(content.toByteArray())
            }
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error writing to URI via SAF", e)
            false
        }
    }

    fun readFromUri(uri: Uri): String {
        return try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                inputStream?.bufferedReader()?.use { reader ->
                    reader.readText()
                } ?: ""
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading from URI via SAF", e)
            ""
        }
    }
}
