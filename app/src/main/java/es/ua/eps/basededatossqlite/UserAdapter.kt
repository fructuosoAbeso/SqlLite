package es.ua.eps.basededatossqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.basededatossqlite.room.UsuarioEntity

// Cambiamos 'val' por 'var' para poder actualizar la lista internamente
class UserAdapter(
    private var userList: List<UsuarioEntity>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvUsername.text = user.nombre_usuario
        holder.tvEmail.text = user.email
    }

    override fun getItemCount(): Int = userList.size

    // MÉTODO NUEVO: Para actualizar la lista desde la Activity
    fun updateUsers(newList: List<UsuarioEntity>) {
        this.userList = newList
        notifyDataSetChanged() // Notifica al RecyclerView que los datos cambiaron
    }
}