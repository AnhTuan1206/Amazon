package com.tuan.amazon.listeners;

import com.tuan.amazon.models.User;

public interface ActivityInviteAddFr {
    void Accept(User user);
    void Remove(User user);

}
