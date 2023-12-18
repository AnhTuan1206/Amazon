package com.tuan.amazon.listeners;

import com.tuan.amazon.models.User;

public interface LoiMoiKetBanListener {
    void Accept(User user);
    void Remove(User user);
    void goToProfile(User user);
}
