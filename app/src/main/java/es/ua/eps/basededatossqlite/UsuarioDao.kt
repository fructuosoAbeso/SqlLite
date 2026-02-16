package es.ua.eps.basededatossqlite.room

import android.database.Cursor
import androidx.room.*

@Dao
interface UsuarioDao {
    // --- Métodos existentes ---
    @Query("SELECT * FROM usuarios")
    fun getAll(): List<UsuarioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(usuario: UsuarioEntity): Long

    @Query("DELETE FROM usuarios")
    fun deleteAll(): Int

    // --- Métodos para el ContentProvider ---
    @Query("SELECT * FROM usuarios")
    fun getAllCursor(): Cursor

    @Query("SELECT * FROM usuarios WHERE nombre_usuario = :user AND password = :pass")
    fun loginCursor(user: String, pass: String): Cursor

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getByIdCursor(id: Long): Cursor

    // --- LAS TRES FUNCIONES QUE DABAN ERROR (CORREGIDAS) ---

    // 1. Necesita la anotación @Query y especificar que devuelve un UsuarioEntity (o null)
    @Query("SELECT * FROM usuarios WHERE nombre_usuario = :username LIMIT 1")
    fun getByUsername(username: String): UsuarioEntity?

    // 2. Necesita la anotación @Delete y el tipo correcto (UsuarioEntity)
    @Delete
    fun delete(usuario: UsuarioEntity): Int

    // 3. Necesita la anotación @Update
    @Update
    fun update(usuario: UsuarioEntity): Int
}