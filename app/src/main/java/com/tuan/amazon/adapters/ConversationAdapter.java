package com.tuan.amazon.adapters;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.databinding.ItemContainerListChatBinding;
import com.tuan.amazon.listeners.DanhSachBanBeDeChatListener;
import com.tuan.amazon.models.ChatMessage;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
    private List<ChatMessage> list;
    private DanhSachBanBeDeChatListener listener;

    public ConversationAdapter(List<ChatMessage> list, DanhSachBanBeDeChatListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(ItemContainerListChatBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerListChatBinding binding;
        public ConversationViewHolder(ItemContainerListChatBinding itemContainerListChatBinding) {
            super(itemContainerListChatBinding.getRoot());
            this.binding = itemContainerListChatBinding;
        }

        void setData(ChatMessage chatMessage){
            if (chatMessage.getSenderId().equals(userCurrentID)){
                binding.tvChat.setText("Bạn: " + chatMessage.getLastMessage());
            }else
                binding.tvChat.setText(chatMessage.getLastMessage());
            binding.tvName.setText(chatMessage.getConversionName());
            binding.image.setImageBitmap(setBitmap(chatMessage.getConversionImage()));

            binding.layout.setOnClickListener(view -> {
                listener.goToChatFromConversation(chatMessage);
            });
        }
    }

    private Bitmap setBitmap(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
