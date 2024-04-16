package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.adapters.ChatAdapter;
import com.tuan.amazon.databinding.FragmentChatBinding;
import com.tuan.amazon.models.ChatMessage;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private FirebaseFirestore firestore;
    private static String userId;
    private static String image ;
    private static String name ;
    private static String fcm ;
    private PreferenceManager preferenceManager;
    private ChatAdapter adapter;
    private List<ChatMessage> list;
    private String conversationId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, null, false);
        initView();
        eventsClick();
        init();
        listenChatMessage();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());
        list = new ArrayList<>();
        adapter = new ChatAdapter(list, getBitmapFromEncode(image), userCurrentID);
        binding.recyclerChat.setAdapter(adapter);
        if(conversationId == null){
            setConversationId();
        }
    }

    private void initView(){
        PreferenceManager preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_CHECK_CHAT_FROM_PP)){
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            name = bundle.getString(Constants.KEY_NAME, "");
            image = bundle.getString(Constants.KEY_USER_IMAGE, "");
            userId = bundle.getString(Constants.KEY_USER_ID,"");
            preferenceManager.putBoolean(Constants.KEY_CHECK_CHAT_FROM_PP, false);
        }else {
            name = getArguments().getString(Constants.KEY_NAME);
            image = getArguments().getString(Constants.KEY_USER_IMAGE);
            userId = getArguments().getString(Constants.KEY_USER_ID);
        }
        binding.tvName.setText(name);
        binding.img.setImageBitmap(setImage(image));
    }

    private void eventsClick(){
        binding.btnSend.setOnClickListener(view -> {
            sentChatMessage();
        });
    }


    private void sentChatMessage(){
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_SENDER_ID, userCurrentID);
        map.put(Constants.KEY_CHECK_POST_SHARE, false);
        map.put(Constants.KEY_RECEIVER_ID, userId);
        map.put(Constants.KEY_CHAT_MESSAGE, binding.etChatMessage.getText().toString());
        map.put(Constants.KEY_TIME_SEND, new Date());
        firestore.collection(Constants.KEY_CHAT_MESSAGE).add(map);
        map.clear();

        map.put(Constants.KEY_SENDER_ID, userCurrentID);
        map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
        map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_USER_IMAGE));
        map.put(Constants.KEY_RECEIVER_NAME, name);
        map.put(Constants.KEY_RECEIVER_IMAGE, image);
        map.put(Constants.KEY_RECEIVER_ID, userId);
        map.put(Constants.KEY_LAST_MESSAGE, binding.etChatMessage.getText().toString());
        map.put(Constants.KEY_TIME_SEND, new Date());
        addConversation(map);

        binding.etChatMessage.setText(null);
        if (fcm == null){
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            fcm = task.getResult().getString(Constants.KEY_FCM_TOKEN);
                        }
                    });
        }
    }

    private void listenChatMessage(){
        firestore.collection(Constants.KEY_CHAT_MESSAGE)
                .whereEqualTo(Constants.KEY_SENDER_ID, userCurrentID)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
                .addSnapshotListener(listener);
        firestore.collection(Constants.KEY_CHAT_MESSAGE)
                .whereEqualTo(Constants.KEY_SENDER_ID, userId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userCurrentID)
                .addSnapshotListener(listener);
    }

    private final EventListener<QuerySnapshot> listener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            int count = list.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSharePost(documentChange.getDocument().getBoolean(Constants.KEY_CHECK_POST_SHARE));
                    if(Boolean.TRUE.equals(documentChange.getDocument().getBoolean(Constants.KEY_CHECK_POST_SHARE))){
                        chatMessage.setImgPost(documentChange.getDocument().getString(Constants.KEY_IMAGE_POST));
                        chatMessage.setIdPost(documentChange.getDocument().getString(Constants.KEY_ID_POST));
                    }else {
                        chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_CHAT_MESSAGE));
                    }
                    chatMessage.setSenderId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverID( documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND));
                    chatMessage.setDateTime(getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND)));
                    list.add(chatMessage);
                }
                list.sort((obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));
                if(count == 0){
                    adapter.notifyDataSetChanged();
                }else {
                    adapter.notifyItemRangeRemoved(list.size(), list.size());
                    binding.recyclerChat.smoothScrollToPosition(list.size() -1);
                }
            }
        }
    };

    private String setConversationId(){
        if(userCurrentID.compareTo(userId) > 0){
            return conversationId = userCurrentID + userId;
        }else return conversationId = userId + userCurrentID;
    }


    private void addConversation(HashMap<String,Object> conversation){
        firestore.collection(Constants.KEY_CONVERSATION)
                .document(conversationId)
                .set(conversation);
    }

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private Bitmap setImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private Bitmap getBitmapFromEncode(String img){
        if(img != null){
            byte[] bytes = Base64.decode(img, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        }
        return null;
    }
}