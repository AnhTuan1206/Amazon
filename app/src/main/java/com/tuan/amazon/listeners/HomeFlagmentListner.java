package com.tuan.amazon.listeners;

import com.tuan.amazon.models.Post;

public interface HomeFlagmentListner {

    void like(Post post);
    void comment(Post post);
}
