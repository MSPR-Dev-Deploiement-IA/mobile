package fr.epsi.arosaje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatItems: MutableList<ChatItem>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatItems[position])
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pseudoTextView: TextView = itemView.findViewById(R.id.pseudo_text)
        private val messageTextView: TextView = itemView.findViewById(R.id.message_text)

        fun bind(chatItem: ChatItem) {
            pseudoTextView.text = chatItem.pseudo
            messageTextView.text = chatItem.message
        }
    }
}
