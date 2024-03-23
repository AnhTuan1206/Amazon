package com.tuan.amazon.adapters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ItemContainerFriendBinding;
import com.tuan.amazon.databinding.ItemContainerListUserToChatBinding;
import com.tuan.amazon.databinding.ItemContainerLoimoikbBinding;
import com.tuan.amazon.databinding.ItemContainerPeopleYouMayKnowBinding;
import com.tuan.amazon.listeners.DanhSachBanBeDeChatListener;
import com.tuan.amazon.listeners.FriendListener;
import com.tuan.amazon.listeners.GoiYKetBanListener;
import com.tuan.amazon.listeners.LoiMoiKetBanListener;
import com.tuan.amazon.models.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<User> list;
    private GoiYKetBanListener goiYKetBanListener;
    private LoiMoiKetBanListener loiMoiKetBanListener;
    private FriendListener friendListener;
    private DanhSachBanBeDeChatListener danhSachBanBeDeChat;
    private int VIEW_TYPE ;

    public UserAdapter(List<User> list, int VIEW_TYPE, @Nullable GoiYKetBanListener goiYKetBanListener,
                       @Nullable LoiMoiKetBanListener loiMoiKetBanListener,
                       @Nullable FriendListener friendListener,
                       @Nullable DanhSachBanBeDeChatListener danhSachBanBeDeChat) {
        this.list = list;
        this.VIEW_TYPE = VIEW_TYPE;
        this.goiYKetBanListener = goiYKetBanListener;
        this.loiMoiKetBanListener = loiMoiKetBanListener;
        this.friendListener = friendListener;
        this.danhSachBanBeDeChat = danhSachBanBeDeChat;
    }

    public void setFilteredList(List<User> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
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
            case 4:
                return new DanhSachBanBeDeChat(ItemContainerListUserToChatBinding.inflate(
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
            case 4:
                ((DanhSachBanBeDeChat) holder).setData(list.get(position));
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
                goiYKetBanListener.inviteAddFriend(user);
                setBooleanForInviteAddFriend(user);
            });
            binding.btnCancel.setOnClickListener(V ->{
                goiYKetBanListener.huyGuiloiKetBan(user);
                setBooleanForInviteAddFriend(user);
            });
            binding.btnGo.setOnClickListener(view -> {
               goiYKetBanListener.goKhoiDanhSach(user);
            });
            binding.imageProfile.setOnClickListener(v ->{
                goiYKetBanListener.goProfilePersional(user);
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
                loiMoiKetBanListener.Accept(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xác nhận lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });
            binding.btnRemove.setOnClickListener(v -> {
               loiMoiKetBanListener.Remove(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xoá lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });

            binding.imageProfile.setOnClickListener(v ->{
                loiMoiKetBanListener.goToProfile(user);
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
                friendListener.goToProfile(user);
            });
        }
    }

    class DanhSachBanBeDeChat extends RecyclerView.ViewHolder{
        private ItemContainerListUserToChatBinding binding;
        public DanhSachBanBeDeChat(ItemContainerListUserToChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.img.setImageBitmap(setImage(user.getImage()));

            binding.img.setOnClickListener( view -> {
                danhSachBanBeDeChat.goToChat(user);
            });
        }
    }

    private Bitmap setImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
