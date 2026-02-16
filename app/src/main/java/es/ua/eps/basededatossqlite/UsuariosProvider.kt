package es.ua.eps.basededatossqlite

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import es.ua.eps.basededatossqlite.room.AppDatabase

class UsuariosProvider : ContentProvider() {
    private lateinit var database: AppDatabase

    companion object {
        const val AUTHORITY = "es.ua.eps.basededatossqlite.provider"
        private const val USUARIOS = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "usuarios", USUARIOS)
        }
    }

    override fun onCreate(): Boolean {
        database = AppDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val dao = database.usuarioDao()
        return when (uriMatcher.match(uri)) {
            USUARIOS -> {
                if (selectionArgs != null && selectionArgs.size == 2) {
                    dao.loginCursor(selectionArgs[0], selectionArgs[1])
                } else {
                    dao.getAllCursor()
                }
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, s: String?, sa: Array<String>?): Int = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, sa: Array<String>?): Int = 0
}