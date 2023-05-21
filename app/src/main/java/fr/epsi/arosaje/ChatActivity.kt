package fr.epsi.arosaje

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private val chatItems = mutableListOf<ChatItem>()
    private lateinit var sharedPreferences: SharedPreferences
    //working on new ChatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        sharedPreferences = getSharedPreferences("ForumMessages", Context.MODE_PRIVATE)

        val chatRecyclerView = findViewById<RecyclerView>(R.id.chat_recyclerview)
        val chatAdapter = ChatAdapter(chatItems)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        val commentEditText = findViewById<EditText>(R.id.comment_edittext)
        val sendButton = findViewById<Button>(R.id.send_button)

        val userPseudo = "Utilisateur"

        // Restaurer les messages précédemment sauvegardés
        val savedMessages = sharedPreferences.getStringSet("messages", emptySet())
        if (savedMessages != null) {
            for (savedMessage in savedMessages) {
                val parts = savedMessage.split(":")
                if (parts.size == 2) {
                    val pseudo = parts[0]
                    val message = parts[1]
                    val chatItem = ChatItem(pseudo, message)
                    chatItems.add(chatItem)
                }
            }
        }
        chatAdapter.notifyDataSetChanged()

        sendButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                val chatItem = ChatItem(userPseudo, comment)
                chatItems.add(chatItem)
                chatAdapter.notifyItemInserted(chatItems.size - 1)
                commentEditText.text.clear()

                chatRecyclerView.scrollToPosition(chatItems.size - 1)

                // Sauvegarder les messages
                val editor = sharedPreferences.edit()
                val messageSet = mutableSetOf<String>()
                for (item in chatItems) {
                    val savedMessage = "${item.pseudo}:${item.message}"
                    messageSet.add(savedMessage)
                }
                editor.putStringSet("messages", messageSet)
                editor.apply()
            }
        }
    }
}
