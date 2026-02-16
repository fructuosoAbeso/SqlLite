package es.ua.eps.basededatossqlite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityLoginBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioEntity // Asegúrate de importar la entidad
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var backupManager: BackupManager
    private var isLogged = false
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backupManager = BackupManager(this)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Login"

        binding.btnLogin.setOnClickListener {
            val user = binding.etUser.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            executor.execute {
                val roomDao = AppDatabase.getDatabase(this@LoginActivity).usuarioDao()

                // Forzamos el tipo de dato para que el IDE no se confunda
                val roomUser: UsuarioEntity? = roomDao.getByUsername(user)

                if (roomUser != null) {
                    // Ahora roomUser ya no es Unit, es un UsuarioEntity real
                    if (roomUser.password == pass) {
                        runOnUiThread {
                            isLogged = true
                            Toast.makeText(this@LoginActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, UserDataActivity::class.java)
                            intent.putExtra("username", user)
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnClose.setOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isLogged) {
            Toast.makeText(this, "Debes iniciar sesión primero", Toast.LENGTH_SHORT).show()
            return true
        }

        when (item.itemId) {
            R.id.menu_backup -> {
                if (backupManager.createBackup()) {
                    Toast.makeText(this, "Backup creado", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            R.id.menu_restore -> {
                executor.execute {
                    if (backupManager.restoreBackup()) {
                        runOnUiThread { Toast.makeText(this, "Backup restaurado", Toast.LENGTH_SHORT).show() }
                    }
                }
                return true
            }
            R.id.menu_manage_users -> {
                startActivity(Intent(this, UserManagementActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}