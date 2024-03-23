package com.tuan.amazon.listeners;

import com.tuan.amazon.models.Comment;

public interface CommentListener {
    void EditComment(Comment comment);
    void DeleteComment(Comment comment);
    void goToProfile(String idUser, String img, String name);
}
