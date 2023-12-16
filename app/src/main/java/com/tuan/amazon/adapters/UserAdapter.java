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
import com.tuan.amazon.databinding.ItemContainerLoimoikbBinding;
import com.tuan.amazon.databinding.ItemContainerPeopleYouMayKnowBinding;
import com.tuan.amazon.listeners.UserListener;
import com.tuan.amazon.models.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<User> list;
    private UserListener userListener;
    private int VIEW_TYPE ;

    public UserAdapter(List<User> list, int VIEW_TYPE, UserListener userListener) {
        this.list = list;
        this.VIEW_TYPE = VIEW_TYPE;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (VIEW_TYPE){
            case 1:
                return new GoiYKetBan(ItemContainerPeopleYouMayKnowBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                        , parent, false
                ));
            case 2:
                return new LoiMoiKetBan(ItemContainerLoimoikbBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                        , parent, false
                ));
            default:
                return new BanBe(ItemContainerFriendBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                        , parent, false
                ));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (VIEW_TYPE){
            case 1:
                ((GoiYKetBan) holder).setData(list.get(position));
                break;
            case 2:
                ((LoiMoiKetBan) holder).setData(list.get(position));
                break;
            case 3:
                ((BanBe) holder).setData(list.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GoiYKetBan extends RecyclerView.ViewHolder {
        private ItemContainerPeopleYouMayKnowBinding binding;
        public GoiYKetBan(ItemContainerPeopleYouMayKnowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.imageProfile.setImageBitmap(setImage(user.getImage()));

            binding.btnAddFriend.setOnClickListener(v -> {
                userListener.inviteAddFriend(user);
                setBooleanForInviteAddFriend(user);
            });
            binding.btnCancel.setOnClickListener(V ->{
                userListener.huyGuiloiKetBan(user);
                setBooleanForInviteAddFriend(user);
            });
            binding.btnGo.setOnClickListener(view -> {
                userListener.go(user);
            });
            binding.imageProfile.setOnClickListener(v ->{
                userListener.goProfilePersional(user);
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

    class LoiMoiKetBan extends RecyclerView.ViewHolder {
        private ItemContainerLoimoikbBinding binding;
        public LoiMoiKetBan(ItemContainerLoimoikbBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.imageProfile.setImageBitmap(setImage(user.getImage()));

            binding.btnAccept.setOnClickListener(v -> {
                userListener.Accept(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xác nhận lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });
            binding.btnRemove.setOnClickListener(v -> {
               userListener.Remove(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xoá lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });
        }
    }

    class BanBe extends RecyclerView.ViewHolder {
        private ItemContainerFriendBinding binding;
        public BanBe(ItemContainerFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.imageProfile.setImageBitmap(setImage(user.getImage()));

            binding.imageProfile.setOnClickListener(v ->{
                userListener.goProfilePersional(user);
            });
        }
    }

    private Bitmap setImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
