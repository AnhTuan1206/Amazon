package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.databinding.ItemContainerCommnetBinding;
import com.tuan.amazon.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> list;

    public CommentAdapter(List<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(ItemContainerCommnetBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerCommnetBinding binding;
        public CommentViewHolder(ItemContainerCommnetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Comment comment){
            if(list.size()>0){
                binding.imgAvatar.setImageBitmap(setImg(comment.getImgAvatar()));
                binding.tvComment.setText(comment.getComment());
                binding.tvTimeComment.setText(comment.getDateTime());
            }
        }
    }

    private Bitmap setImg(String img){
        byte[] bytes = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
