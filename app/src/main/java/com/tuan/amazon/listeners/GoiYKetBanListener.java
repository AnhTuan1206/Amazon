package com.tuan.amazon.listeners;

import com.tuan.amazon.models.User;

public interface GoiYKetBanListener {
    void inviteAddFriend(User user);
    void huyGuiloiKetBan(User user);
    void goKhoiDanhSach(User user);
    void goProfilePersional(User user);
}
