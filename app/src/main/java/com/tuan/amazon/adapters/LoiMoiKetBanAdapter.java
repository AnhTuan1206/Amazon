package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tuan.amazon.databinding.ItemContainerLoimoikbBinding;
import com.tuan.amazon.listeners.AcInviteAddFr;
import com.tuan.amazon.models.User;

import java.util.List;

public class LoiMoiKetBanAdapter extends RecyclerView.Adapter<LoiMoiKetBanAdapter.LMKB> {
    private List<User> list;
    private AcInviteAddFr acInviteAddFr;

    public LoiMoiKetBanAdapter(List<User> list, AcInviteAddFr acInviteAddFr) {
        this.list = list;
        this.acInviteAddFr = acInviteAddFr;
    }

    @NonNull
    @Override
    public LMKB onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerLoimoikbBinding binding = ItemContainerLoimoikbBinding
                .inflate(LayoutInflater.from(parent.getContext())
                ,parent
                ,false);
        return new LMKB(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LMKB holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LMKB extends RecyclerView.ViewHolder{
        private ItemContainerLoimoikbBinding binding;
        public LMKB(ItemContainerLoimoikbBinding itemContainerLoimoikbBinding) {
            super(itemContainerLoimoikbBinding.getRoot());
            binding = itemContainerLoimoikbBinding;
        }

        void setData(User user){
            binding.tvName.setText(user.getName());
            binding.imageProfile.setImageBitmap(getImageSetData(user.getImage()));
            binding.btnAccept.setOnClickListener(v -> {
                acInviteAddFr.Accept(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xác nhận lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });
            binding.btnRemove.setOnClickListener(v -> {
                acInviteAddFr.Remove(user);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnRemove.setVisibility(View.GONE);
                binding.tvThongBao.setText("Đã xoá lời mời");
                binding.tvThongBao.setVisibility(View.VISIBLE);
            });
        }
    }

    private Bitmap getImageSetData(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
