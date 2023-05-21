package fr.epsi.arosaje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private var chatItems: List<ChatItem>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatItems.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatItems[position])
    }

    fun updateChat(newChatItems: List<ChatItem>) {
        this.chatItems = newChatItems
        notifyDataSetChanged()
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val senderTextView: TextView = view.findViewById(R.id.senderTextView)
        private val messageTextView: TextView = view.findViewById(R.id.messageTextView)

        fun bind(chatItem: ChatItem) {
            senderTextView.text = chatItem.userId.toString()
            messageTextView.text = chatItem.messageText
        }
    }
}
