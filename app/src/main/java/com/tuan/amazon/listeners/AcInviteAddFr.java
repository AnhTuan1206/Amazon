package com.tuan.amazon.listeners;

import com.tuan.amazon.models.User;

public interface AcInviteAddFr {
    void Accept(User user);
    void Remove(User user);

}
