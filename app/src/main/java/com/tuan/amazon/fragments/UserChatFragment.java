package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.activities.ChatActivity;
import com.tuan.amazon.adapters.ConversationAdapter;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.FragmentUserChatBinding;
import com.tuan.amazon.listeners.DanhSachBanBeDeChatListener;
import com.tuan.amazon.models.ChatMessage;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UserChatFragment extends Fragment implements DanhSachBanBeDeChatListener {

    private FragmentUserChatBinding binding;
    private FirebaseFirestore firestore;
    private List<User> list;
    private UserAdapter adapter;
    private PreferenceManager preferenceManager;
    private ConversationAdapter conversationAdapter;
    private List<ChatMessage> chatMessageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserChatBinding.inflate(inflater, null, false);
        init();
        chatFromProfilePersonal();
        initView();
        listenConservation();
        return binding.getRoot();
    }

    private void chatFromProfilePersonal(){
        if(preferenceManager.getBoolean(Constants.KEY_CHECK_CHAT_FROM_PP)){
            NavHostFragment.findNavController(this).navigate(R.id.chatFragment);
        }
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        chatMessageList = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(chatMessageList, this);
        binding.recyclerConservation.setAdapter(conversationAdapter);
    }

    private void initView(){
        list = new ArrayList<>();
        getListFriendToChat();
    }

    private void getListFriendToChat(){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        int count = 0;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(listMyFriend.contains(queryDocumentSnapshot.getId()) && count < listMyFriend.size()){
                                User user = new User();
                                user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                user.setId(queryDocumentSnapshot.getId());
                                list.add(user);
                                count++;
                            }
                        }
                        if(list.size() > 0){
                            adapter = new UserAdapter(list,4,null,null,null,this);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                            binding.recyclerListUserToChat.setLayoutManager(layoutManager);
                            binding.recyclerListUserToChat.setAdapter(adapter);
                        }
                    }
                });
    }

    private void listenConservation(){
        firestore.collection(Constants.KEY_CONVERSATION)
                .whereEqualTo(Constants.KEY_SENDER_ID, userCurrentID)
                .addSnapshotListener(listener);
        firestore.collection(Constants.KEY_CONVERSATION)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userCurrentID)
                .addSnapshotListener(listener);
    }

    private final EventListener<QuerySnapshot> listener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverID(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatMessage.setConservationId(documentChange.getDocument().getId());
                    if(userCurrentID.equals(chatMessage.getSenderId())){
                        chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                        chatMessage.setConversionImage(documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE));
                    }else {
                        chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                        chatMessage.setConversionImage(documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE));
                    }
                    chatMessage.setLastMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND));
                    chatMessage.setDateTime(getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND)));
                    chatMessageList.add(chatMessage);
                }

            }
            chatMessageList.sort((obj1, obj2) -> obj2.getDateObject().compareTo(obj1.getDateObject()));
            conversationAdapter.notifyDataSetChanged();
        }
    };

    private void getUserToConservation(String id, User user){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user.setName(task.getResult().getString(Constants.KEY_USER_ID));
                        user.setImage(task.getResult().getString(Constants.KEY_USER_IMAGE));
                    }
                });
    }

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @Override
    public void goToChat(User user) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_NAME, user.getName());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        NavHostFragment.findNavController(this).navigate(R.id.chatFragment, bundle);
    }

    @Override
    public void goToChatFromConversation(ChatMessage chatMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_NAME, chatMessage.getConversionName());
        bundle.putString(Constants.KEY_USER_IMAGE, chatMessage.getConversionImage());
        if(chatMessage.getSenderId().equals(userCurrentID)){
            bundle.putString(Constants.KEY_USER_ID, chatMessage.getReceiverID());
        }else {
            bundle.putString(Constants.KEY_USER_ID, chatMessage.getSenderId());
        }
        NavHostFragment.findNavController(this).navigate(R.id.chatFragment, bundle);
    }

}