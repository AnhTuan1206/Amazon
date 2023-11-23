package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tuan.amazon.databinding.ItemContainerLoimoikbBinding;
import com.tuan.amazon.models.User;

import java.util.List;

public class LoiMoiKetBanAdapter extends RecyclerView.Adapter<LoiMoiKetBanAdapter.LMKB> {
    private List<User> list;

    public LoiMoiKetBanAdapter(List<User> list) {
        this.list = list;
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
        }
    }
    private Bitmap getImageSetData(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
