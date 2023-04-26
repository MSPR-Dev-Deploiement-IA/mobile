package fr.epsi.arosaje

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private val chatItems = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatRecyclerView = findViewById<RecyclerView>(R.id.chat_recyclerview)
        val chatAdapter = ChatAdapter(chatItems)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        val commentEditText = findViewById<EditText>(R.id.comment_edittext)
        val sendButton = findViewById<Button>(R.id.send_button)

        // Changer cette valeur pour le pseudo de l'utilisateur
        val userPseudo = "Utilisateur"

        sendButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                val chatItem = ChatItem(userPseudo, comment)
                chatItems.add(chatItem)
                chatAdapter.notifyItemInserted(chatItems.size - 1)
                commentEditText.text.clear()

                chatRecyclerView.scrollToPosition(chatItems.size - 1)
            }
        }
    }
}
