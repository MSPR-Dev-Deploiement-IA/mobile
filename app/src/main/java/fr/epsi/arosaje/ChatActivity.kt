package fr.epsi.arosaje

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageEditText = findViewById(R.id.messageEditText)
        val sendMessageButton: FloatingActionButton = findViewById(R.id.sendMessageButton)

        apiService = ApiService(this)

        val chatRecyclerView: RecyclerView = findViewById(R.id.chatRecyclerView)
        chatAdapter = ChatAdapter(mutableListOf())
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendMessageButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotBlank()) {
                apiService.postMessage(message) { success ->
                    if (success) {
                        messageEditText.text.clear()
                        Toast.makeText(this, "Message envoyé", Toast.LENGTH_SHORT).show()
                        updateChat()
                    }
                }
            }
        }

        updateChat()
    }

    private fun updateChat() {
        findViewById<View>(R.id.chatLoading).visibility = View.VISIBLE
        apiService.getAllMessages { messages ->
            if (messages != null) {
                chatAdapter.updateChat(messages)
                findViewById<View>(R.id.chatLoading).visibility = View.GONE
            }
        }
    }
}
