package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.databinding.ItemContainerReceivedMessageBinding;
import com.tuan.amazon.databinding.ItemContainerSentMessageBinding;
import com.tuan.amazon.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> list;
    private final Bitmap receiverImg;
    private final String senderId;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYP_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> list, Bitmap receiverImg, String senderId) {
        this.list = list;
        this.receiverImg = receiverImg;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
           return new SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(
                   LayoutInflater.from(parent.getContext()), parent, false)
           );
        }else {
            return new ReceiverMassageViewHolder(ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(list.get(position));
        }else {
            ((ReceiverMassageViewHolder) holder).setData(list.get(position), receiverImg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getSenderId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else
            return VIEW_TYP_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }
        void setData(ChatMessage chatMessage){
            if(chatMessage.getSharePost()){
                binding.textMessage.setVisibility(View.GONE);
                binding.tvThongBaoShare.setVisibility(View.VISIBLE);
                if(chatMessage.getImgPost()!=null){
                    binding.imgPost.setVisibility(View.VISIBLE);
                    binding.imgPost.setImageBitmap(setImage(chatMessage.getImgPost()));
                }
            }else {
                binding.textMessage.setText(chatMessage.getMessage());
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.tvThongBaoShare.setVisibility(View.GONE);
                binding.imgPost.setVisibility(View.GONE);
            }
            binding.textDateTime.setText(chatMessage.getDateTime());
        }
    }
    static class ReceiverMassageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;
        public ReceiverMassageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }
        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            if(chatMessage.getSharePost()){
                binding.textMessage.setVisibility(View.GONE);
                binding.tvThongBaoShare.setVisibility(View.VISIBLE);
                if(chatMessage.getImgPost()!=null){
                    binding.imgPost.setVisibility(View.VISIBLE);
                    binding.imgPost.setImageBitmap(setImage(chatMessage.getImgPost()));
                }
            }else {
                binding.textMessage.setText(chatMessage.getMessage());
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.tvThongBaoShare.setVisibility(View.GONE);
                binding.imgPost.setVisibility(View.GONE);
            }
            if(receiverProfileImage != null){
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
            binding.textDateTime.setText(chatMessage.getDateTime());
        }
    }

    private static Bitmap setImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
