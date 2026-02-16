package es.ua.eps.basededatossqlite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.basededatossqlite.databinding.ActivityUserDataBinding

class UserDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        binding.tvWelcome.text = "Bienvenido, $username"

        binding.btnBack.setOnClickListener { finish() }
    }
}
