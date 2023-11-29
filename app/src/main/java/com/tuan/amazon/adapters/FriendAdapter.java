package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.databinding.ItemContainerFriendBinding;
import com.tuan.amazon.models.User;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ItemFiend>{
    private List<User> list;

    public FriendAdapter(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemFiend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerFriendBinding binding = ItemContainerFriendBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent
                ,false);
        return new ItemFiend(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFiend holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemFiend extends RecyclerView.ViewHolder{
        private ItemContainerFriendBinding binding;
        public ItemFiend(ItemContainerFriendBinding itemContainerFriendBinding) {
            super(itemContainerFriendBinding.getRoot());
            binding = itemContainerFriendBinding;
        }

        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.imageProfile.setImageBitmap(getDataToImage(user.getImage()));
        }

    }

    private Bitmap getDataToImage(String encoded){
        byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
