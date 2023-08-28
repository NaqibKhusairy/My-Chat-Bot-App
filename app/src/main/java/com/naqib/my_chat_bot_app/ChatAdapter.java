package com.naqib.my_chat_bot_app;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> messages = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rv, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());

        if ("User".equals(message.getSender())) {
            holder.senderTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else if ("Chatbot".equals(message.getSender())) {
            holder.senderTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        holder.senderTextView.setText(message.getSender());
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
