package es.ua.eps.basededatossqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityNewUserBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioEntity
import java.util.concurrent.Executors

class NewUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewUserBinding
    // Eliminamos la referencia a DBHelper
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Nuevo Usuario"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        // Guardar usuario
        binding.btnSave.setOnClickListener {
            val user = binding.etUser.text.toString().trim()
            val pass = binding.etPass.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ ÚNICO GUARDADO: Usamos solo Room en un hilo secundario
            executor.execute {
                try {
                    val dao = AppDatabase.getDatabase(this@NewUserActivity).usuarioDao()

                    val nuevoUsuario = UsuarioEntity(
                        nombre_usuario = user,
                        password = pass,
                        nombre_completo = name,
                        email = email
                    )

                    dao.insert(nuevoUsuario)

                    // Volvemos al hilo principal para avisar al usuario
                    runOnUiThread {
                        Toast.makeText(this@NewUserActivity, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@NewUserActivity, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}