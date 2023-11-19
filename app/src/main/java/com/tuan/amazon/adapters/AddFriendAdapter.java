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
import com.tuan.amazon.listeners.AdddFriendFMListener;
import com.tuan.amazon.models.User;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{

    private  final List<User> list;
    private final AdddFriendFMListener adddFriendFMListener;

    public AddFriendAdapter(List<User> list, AdddFriendFMListener adddFriendFMListener) {
        this.list = list;
        this.adddFriendFMListener = adddFriendFMListener;
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
            binding.btnAddFriend.setOnClickListener(v -> {
                adddFriendFMListener.inviteAddFriend(user);
                setBooleanForInviteAddFriend(user);
            });
        }

        private void setBooleanForInviteAddFriend(User user){
            if(user.getInviteAddFriend()){
                binding.btnAddFriend.setVisibility(View.GONE);
                binding.btnGo.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.VISIBLE);
            }else {
                binding.btnAddFriend.setVisibility(View.VISIBLE);
                binding.btnGo.setVisibility(View.VISIBLE);
                binding.btnCancel.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap getImageSetData(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
