package fr.epsi.arosaje

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private val chatMessages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatRecyclerView = findViewById<RecyclerView>(R.id.chat_recyclerview)
        val chatAdapter = ChatAdapter(chatMessages)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        val commentEditText = findViewById<EditText>(R.id.comment_edittext)
        val sendButton = findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                chatMessages.add(comment)
                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                commentEditText.text.clear()

                chatRecyclerView.scrollToPosition(chatMessages.size - 1)
            }
        }
    }
}
