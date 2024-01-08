package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.activities.ChatActivity;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.FragmentUserChatBinding;
import com.tuan.amazon.listeners.DanhSachBanBeDeChatListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class UserChatFragment extends Fragment implements DanhSachBanBeDeChatListener {

    private FragmentUserChatBinding binding;
    private FirebaseFirestore firestore;
    private List<User> list;
    private UserAdapter adapter;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserChatBinding.inflate(inflater, null, false);
        chatFromProfilePersonal();
        initView();
        return binding.getRoot();
    }

    private void chatFromProfilePersonal(){
        ChatActivity chatActivity = (ChatActivity) getActivity();
        if(preferenceManager.getBoolean(Constants.KEY_CHECK_CHAT_FROM_PP)){
            NavHostFragment.findNavController(this).navigate(R.id.chatFragment);
        }
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
    }

    private void initView(){
        list = new ArrayList<>();
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

    @Override
    public void goToChat(User user) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_NAME, user.getName());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        NavHostFragment.findNavController(this).navigate(R.id.chatFragment, bundle);
    }

}