package es.ua.eps.basededatossqlite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import es.ua.eps.basededatossqlite.databinding.ActivityListUsersBinding
import es.ua.eps.basededatossqlite.room.AppDatabase
import es.ua.eps.basededatossqlite.room.UsuarioDao
import es.ua.eps.basededatossqlite.room.UsuarioEntity

class ListUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListUsersBinding
    private lateinit var dao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "List Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)

        dao = AppDatabase.getDatabase(this).usuarioDao()

        val usuarios: List<UsuarioEntity> = dao.getAll()

        binding.recyclerViewUsers.adapter = UserAdapter(usuarios)
    }
}
