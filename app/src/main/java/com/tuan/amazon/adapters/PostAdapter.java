package com.tuan.amazon.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ItemPostBinding;
import com.tuan.amazon.listeners.HomeFlagmentListner;
import com.tuan.amazon.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostsViewHolder>{

    private List<Post> list;
    private HomeFlagmentListner listener;
    public PostAdapter(List<Post> list, HomeFlagmentListner listener) {
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostsViewHolder(ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;
        public PostsViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Post post){
            if(post.getImgCreator().equals("null")){
                return;
            }else {
                binding.imgAvatar.setImageBitmap(setImg(post.getImgCreator()));
            }
            if(post.getLike()){
                binding.imgLike.setBackgroundResource(R.drawable.ic_liked_btn);
            }else
            {
                binding.imgLike.setBackgroundResource(R.drawable.ic_like_btn);
            }
            binding.tvName.setText(post.getCreatorName());
            binding.tvCaption.setText(post.getCaption());
            if(!post.getImagePost().equals("null")){
                binding.imgPost.setImageBitmap(setImg(post.getImagePost()));
                binding.imgPost.setVisibility(View.VISIBLE);
            }else binding.imgPost.setVisibility(View.GONE);
            binding.tvNumberCountComment.setText(String.valueOf(post.getCountComment()));
            binding.tvNumberCountLike.setText(String.valueOf(post.getCountLike()));

            binding.layoutLike.setOnClickListener(v ->{
                if(!post.getLike()){
                    post.setLike(true);
                    binding.imgLike.setBackgroundResource(R.drawable.ic_liked_btn);
                    post.setCountLike(post.getCountLike()+1);
                    binding.tvNumberCountLike.setText(String.valueOf(post.getCountLike()));
                }else {
                    post.setLike(false);
                    binding.imgLike.setBackgroundResource(R.drawable.ic_like_btn);
                    post.setCountLike(post.getCountLike()-1);
                    binding.tvNumberCountLike.setText(String.valueOf(post.getCountLike()));
                }
                listener.like(post);
            });
            binding.layoutCom.setOnClickListener(view -> {
                listener.comment(post);
            });
        }
    }

    private Bitmap setImg (String img){
        byte[] bytes = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
