package es.ua.eps.basededatossqlite.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre_usuario: String,
    val password: String,
    val nombre_completo: String,
    val email: String
)