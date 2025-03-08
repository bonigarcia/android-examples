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
package es.uc3m.android.content

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import es.uc3m.android.content.MyDatabaseHelper.Companion.TABLE_NAME

class MyContentProvider : ContentProvider() {

    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(): Boolean {
        dbHelper = MyDatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val db = dbHelper.readableDatabase
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val db = dbHelper.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper.writableDatabase
        return db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.dir/vnd.com.example.provider.${TABLE_NAME}"
    }
}