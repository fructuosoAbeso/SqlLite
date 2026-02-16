package es.ua.eps.basededatossqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityUpdateUserBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioEntity
import java.util.concurrent.Executors

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var currentUsuario: UsuarioEntity
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Update User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        val username = intent.getStringExtra("username")
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "No se seleccionó ningún usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cargamos los datos usando el executor
        loadUserData(username)

        binding.btnUpdate.setOnClickListener {
            val newPassword = binding.etNewPass.text.toString().trim()
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (newPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            executor.execute {
                val dao = AppDatabase.getDatabase(this@UpdateUserActivity).usuarioDao()

                // Usamos .copy() para mantener el ID original del usuario
                val updatedUser = currentUsuario.copy(
                    password = newPassword,
                    nombre_completo = fullName,
                    email = email
                )

                dao.update(updatedUser)

                runOnUiThread {
                    Toast.makeText(this@UpdateUserActivity, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun loadUserData(username: String) {
        executor.execute {
            val dao = AppDatabase.getDatabase(this@UpdateUserActivity).usuarioDao()

            // Forzamos el tipo de dato explícitamente aquí
            val usuario: UsuarioEntity? = dao.getByUsername(username)

            runOnUiThread {
                if (usuario != null) {
                    currentUsuario = usuario // Guardamos la referencia

                    // Ahora 'usuario' es reconocido como UsuarioEntity y no dará error
                    binding.etlogin.setText(usuario.nombre_usuario)
                    binding.etlogin.isEnabled = false
                    binding.etNewPass.setText(usuario.password)
                    binding.etFullName.setText(usuario.nombre_completo)
                    binding.etEmail.setText(usuario.email)
                } else {
                    Toast.makeText(this@UpdateUserActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}