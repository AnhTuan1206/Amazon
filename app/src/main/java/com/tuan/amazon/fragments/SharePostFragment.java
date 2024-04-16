package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.play.integrity.internal.c;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.FragmentSharePostBinding;
import com.tuan.amazon.listeners.SharePost;
import com.tuan.amazon.models.Post;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SharePostFragment extends BottomSheetDialogFragment implements SharePost {

    private FragmentSharePostBinding binding;
    private UserAdapter adapter;
    private List<User> list;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    public SharePostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSharePostBinding.inflate(inflater, null, false);
        initView();
        return binding.getRoot();
    }

    private void initView(){
        list = new ArrayList<>();
        if(listMyFriend != null){
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
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
                        }
                        if(list.size() > 0){
                            adapter = new UserAdapter(list,5, null, null, null, null, this, null);
                            binding.recyclerFriend.setAdapter(adapter);
                        }
                    });
        }
    }

    @Override
    public void sharePost(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_CHECK_POST_SHARE,true);
        map.put(Constants.KEY_ID_POST,getArguments().getString(Constants.KEY_ID_POST));
        map.put(Constants.KEY_IMAGE_POST,getArguments().getString(Constants.KEY_IMAGE_POST));
        map.put(Constants.KEY_SENDER_ID, userCurrentID);
        map.put(Constants.KEY_RECEIVER_ID, user.getId());
        map.put(Constants.KEY_TIME_SEND, new Date());
        firestore.collection(Constants.KEY_CHAT_MESSAGE).add(map);

        map.clear();

        map.put(Constants.KEY_SENDER_ID, userCurrentID);
        map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
        map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_USER_IMAGE));
        map.put(Constants.KEY_RECEIVER_NAME, user.getName());
        map.put(Constants.KEY_RECEIVER_IMAGE, user.getImage());
        map.put(Constants.KEY_RECEIVER_ID, user.getId());
        map.put(Constants.KEY_LAST_MESSAGE, "Đã chia sẻ bài viết");
        map.put(Constants.KEY_TIME_SEND, new Date());
        String conversationId = setConversationId(user.getId());
        firestore.collection(Constants.KEY_CONVERSATION)
                .document(conversationId)
                .set(map);
    }

    private String setConversationId(String userId){
        if(userCurrentID.compareTo(userId) > 0){
            return userCurrentID + userId;
        }else return userId + userCurrentID;
    }

}