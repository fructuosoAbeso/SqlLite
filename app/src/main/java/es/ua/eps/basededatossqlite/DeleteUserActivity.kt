package es.ua.eps.basededatossqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityDeleteUserBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioDao
import java.util.concurrent.Executors

class DeleteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteUserBinding
    private lateinit var dao: UsuarioDao
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getDatabase(this).usuarioDao()

        binding.btnDelete.setOnClickListener {
            val username = binding.etUser.text.toString().trim()
            if (username.isEmpty()) return@setOnClickListener

            executor.execute {
                val usuario = dao.getByUsername(username)
                if (usuario != null) {
                    dao.delete(usuario)
                    runOnUiThread {
                        Toast.makeText(this, "Eliminado: $username", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "No encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}