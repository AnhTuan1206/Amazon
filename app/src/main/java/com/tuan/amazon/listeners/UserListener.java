package com.tuan.amazon.listeners;

import com.tuan.amazon.models.User;

public interface UserListener {

    void inviteAddFriend(User user);
    void huyGuiloiKetBan(User user);
    void go(User user);
    void goProfilePersional(User user);
    void Accept(User user);
    void Remove(User user);
}
