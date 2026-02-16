package es.ua.eps.basededatossqlite

import android.content.Context
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioEntity
import java.io.File
import java.io.FileWriter

class BackupManager(private val context: Context) {
    private val dao = AppDatabase.getDatabase(context).usuarioDao()

    fun createBackup(): Boolean {
        return try {
            val file = File(context.getExternalFilesDir(null), "backup.txt")
            val usuarios = dao.getAll()
            FileWriter(file).use { writer ->
                usuarios.forEach { u ->
                    writer.append("${u.id};${u.nombre_usuario};${u.password};${u.nombre_completo};${u.email}\n")
                }
            }
            true
        } catch (e: Exception) { false }
    }

    fun restoreBackup(): Boolean {
        val file = File(context.getExternalFilesDir(null), "backup.txt")
        if (!file.exists()) return false
        return try {
            dao.deleteAll()
            file.forEachLine { line ->
                val p = line.split(";")
                if (p.size >= 5) {
                    dao.insert(UsuarioEntity(p[0].toInt(), p[1], p[2], p[3], p[4]))
                }
            }
            true
        } catch (e: Exception) { false }
    }
}