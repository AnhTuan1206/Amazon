package com.tuan.amazon.adapters;


import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ItemContainerCommnetBinding;
import com.tuan.amazon.listeners.CommentListener;
import com.tuan.amazon.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> list;
    private CommentListener listener;

    public CommentAdapter(List<Comment> list, CommentListener listener) {
        this.list = list;
        this.listener = listener;
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

                binding.tvComment.setOnLongClickListener(view -> {
                    if(comment.getIdUser().equals(userCurrentID)){
                        binding.tvComment.setBackgroundResource(R.drawable.background_sent_message);
                        binding.btnEditComment.setVisibility(View.VISIBLE);
                        binding.btnDeleteComment.setVisibility(View.VISIBLE);
                    }
                    return false;
                });

                binding.btnDeleteComment.setOnClickListener(view -> {
                    listener.DeleteComment(comment);
                });
                binding.btnEditComment.setOnClickListener(view -> {
                    listener.EditComment(comment);
                });

                binding.imgAvatar.setOnClickListener(view -> {
                    listener.goToProfile(comment.getIdUser(), comment.getImgAvatar(), comment.getName());
                });
            }
        }
    }

    private Bitmap setImg(String img){
        byte[] bytes = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
