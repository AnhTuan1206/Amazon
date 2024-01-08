package com.tuan.amazon.fragments;


import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.activities.FriendActivity;
import com.tuan.amazon.activities.InviteAddFriendActivity;
import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.FragmentGoiYKetBanBinding;
import com.tuan.amazon.listeners.GoiYKetBanListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GoiYKetBanFragment extends Fragment implements GoiYKetBanListener {

    private FragmentGoiYKetBanBinding binding;
    private FirebaseFirestore firestore;
    private List<User> list;
    private UserAdapter userAdapter;
    public static List<String> listBanGuiLoiKetBanDen;
    public static List<String> listAiDoGuiDenBanLoiMoi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGoiYKetBanBinding.inflate(inflater, container, false);
        eventClicks();
        return binding.getRoot();
    }

    private void eventClicks(){
        binding.btnInviteAddFriend.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getApplicationContext(), InviteAddFriendActivity.class));
        });

        binding.btnFriend.setOnClickListener(v ->{
            startActivity(new Intent(getActivity().getApplicationContext(), FriendActivity.class));
        });
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        listBanGuiLoiKetBanDen = new ArrayList();
        listAiDoGuiDenBanLoiMoi = new ArrayList();

        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_GD)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            listBanGuiLoiKetBanDen.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                        }
                    }
                });

        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_NG)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            listAiDoGuiDenBanLoiMoi.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                        }
                    }
                });
    }

    private void getUser(){
        if(listAiDoGuiDenBanLoiMoi != null
            || listBanGuiLoiKetBanDen != null
                || listMyFriend != null){
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(userCurrentID.equals(queryDocumentSnapshot.getId())
                                        || listAiDoGuiDenBanLoiMoi.contains(queryDocumentSnapshot.getId())
                                        || listMyFriend.contains(queryDocumentSnapshot.getId())
                                        || listBanGuiLoiKetBanDen.contains(queryDocumentSnapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                user.setId(queryDocumentSnapshot.getId());
                                list.add(user);
                            }
                            if(list.size() > 0){
                                userAdapter = new UserAdapter(list,1, this, null, null, null);
                                binding.recyclerListFriendFragment.setAdapter(userAdapter);
                            }
                        }
                    });
        }
        else {
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(userCurrentID.equals(queryDocumentSnapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                user.setId(queryDocumentSnapshot.getId());
                                list.add(user);
                            }
                            if(list.size() > 0){
                                userAdapter= new UserAdapter(list, 1, this, null, null, null);
                                binding.recyclerListFriendFragment.setAdapter(userAdapter);
                            }
                        }
                    });
        }
    }

    @Override
    public void inviteAddFriend(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_ID,user.getId());
        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_GD)
                .document(user.getId())
                .set(map)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        map.clear();
                        map.put(Constants.KEY_ID,userCurrentID);
                        firestore.collection(Constants.KEY_LMKB)
                                .document(user.getId())
                                .collection(Constants.KEY_NG)
                                .document(userCurrentID)
                                .set(map)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        showToast("Bạn đã gửi lời mời kết bạn đến: "+user.getName());
                                    }
                                });

                    }else {
                        showToast("Gửi lời kết bạn đến: "+user.getName()+" thất bại");
                    }
                });
        user.setInviteAddFriend(true);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void huyGuiloiKetBan(User user) {
        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_GD)
                .document(user.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        listBanGuiLoiKetBanDen.remove(user);
                        firestore.collection(Constants.KEY_LMKB)
                                .document(user.getId())
                                .collection(Constants.KEY_NG)
                                .document(userCurrentID)
                                .delete()
                                .addOnCompleteListener(task1 -> {
                                    if(task.isSuccessful()){
                                        showToast("Đã huỷ lời mời kết bạn với: "+user.getName());
                                    }
                                    else {
                                        showToast("Huỷ lời mời kết bạn với: "+user.getName()+" không thành công");
                                    }
                                });
                    }
                });
        user.setInviteAddFriend(false);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void goKhoiDanhSach(User user) {
        list.remove(user);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void goProfilePersional(User user) {
        goProfileActivity(user);
    }


    private void showToast(String message){
        Toast.makeText(getActivity().getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void goProfileActivity(User user){
        Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_NAME, user.getName());
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);
    }

}