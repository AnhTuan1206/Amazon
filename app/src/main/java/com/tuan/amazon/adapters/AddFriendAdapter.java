package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.databinding.ItemContainerPeopleYouMayKnowBinding;
import com.tuan.amazon.models.User;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{

    private  final List<User> list;

    public AddFriendAdapter(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AddFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerPeopleYouMayKnowBinding binding = ItemContainerPeopleYouMayKnowBinding
                .inflate(LayoutInflater.from(parent.getContext())
                ,parent
                ,false);
        return new AddFriendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AddFriendViewHolder extends RecyclerView.ViewHolder {
        ItemContainerPeopleYouMayKnowBinding binding;

        public AddFriendViewHolder(ItemContainerPeopleYouMayKnowBinding itemContainerPeopleYouMayKnowBinding) {
            super(itemContainerPeopleYouMayKnowBinding.getRoot());
            binding = itemContainerPeopleYouMayKnowBinding;
        }

        void setData(User user){
            binding.imageProfile.setImageBitmap(getImageSetData(user.getImage()));
            binding.tvName.setText(user.getName());
        }
    }

    private Bitmap getImageSetData(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
