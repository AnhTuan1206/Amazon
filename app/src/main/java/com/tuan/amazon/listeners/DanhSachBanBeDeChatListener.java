package com.tuan.amazon.listeners;


import com.tuan.amazon.models.ChatMessage;
import com.tuan.amazon.models.User;

public interface DanhSachBanBeDeChatListener {
    void goToChat(User user);
    void goToChatFromConversation(ChatMessage chatMessage);
}
