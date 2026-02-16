package es.ua.eps.basededatossqlite

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityUserManagementBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioDao
import es.ua.eps.basededatossqlite.room.UsuarioEntity
import java.util.concurrent.Executors

class UserManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserManagementBinding
    private lateinit var dao: UsuarioDao
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "User Management"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        dao = AppDatabase.getDatabase(this).usuarioDao()

        // 1. NUEVO USUARIO
        binding.btnNew.setOnClickListener {
            startActivity(Intent(this, NewUserActivity::class.java))
        }

        // 2. ACTUALIZAR USUARIO
        binding.btnUpdate.setOnClickListener {
            val username = binding.spinnerUsers.selectedItem as? String
            if (username == null) {
                Toast.makeText(this, "Selecciona un usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, UpdateUserActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // 3. ELIMINAR USUARIO
        binding.btnDelete.setOnClickListener {
            val username = binding.spinnerUsers.selectedItem as? String
            if (username == null) return@setOnClickListener

            AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que quieres eliminar a $username?")
                .setPositiveButton("OK") { _, _ ->
                    executor.execute {
                        val usuario = dao.getByUsername(username)
                        if (usuario != null) {
                            dao.delete(usuario)
                            runOnUiThread {
                                Toast.makeText(this@UserManagementActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                loadUsersIntoSpinner() // Recargamos la lista
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // 4. LISTAR USUARIOS
        binding.btnList.setOnClickListener {
            startActivity(Intent(this, ListUsersActivity::class.java))
        }
    }

    // Usamos onResume para que si vuelves de "Nuevo Usuario", el spinner se refresque solo
    override fun onResume() {
        super.onResume()
        loadUsersIntoSpinner()
    }

    private fun loadUsersIntoSpinner() {
        executor.execute {
            val usuarios = dao.getAll()
            val nombres = usuarios.map { it.nombre_usuario }

            runOnUiThread {
                if (nombres.isEmpty()) {
                    binding.spinnerUsers.adapter = null
                } else {
                    val adapter = ArrayAdapter(
                        this@UserManagementActivity,
                        android.R.layout.simple_spinner_item,
                        nombres
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerUsers.adapter = adapter
                }
            }
        }
    }
}