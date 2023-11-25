package com.tuan.amazon.fragments;


import static com.tuan.amazon.activities.MainActivity.userCurrentID;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.activities.InviteAddFriendActivity;
import com.tuan.amazon.adapters.GoiYKetBanAdapter;
import com.tuan.amazon.databinding.FragmentFriendBinding;
import com.tuan.amazon.listeners.AdddFriendFMListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FriendFragment extends Fragment implements AdddFriendFMListener {

    private FragmentFriendBinding binding;
    private View view;
    private FirebaseFirestore firestore;
    private List<User> list;
    private GoiYKetBanAdapter adapter;
    public static List<String> listBanGuiLoiKetBanDen;
    public static List<String> listAiDoGuiDenBanLoiMoi;
    public static List<String> listFriend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(inflater, container, false);
        eventClicks();
        view = binding.getRoot();
        return view;
    }

    private void eventClicks(){
        binding.btnInviteAddFriend.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getApplicationContext(), InviteAddFriendActivity.class));
            getActivity().finish();
        });
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        listBanGuiLoiKetBanDen = new ArrayList();
        listAiDoGuiDenBanLoiMoi = new ArrayList();
        listFriend = new ArrayList<>();

        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_GD)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            listBanGuiLoiKetBanDen.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                        }
                    }
                }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });

        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_NG)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            listAiDoGuiDenBanLoiMoi.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                        }
                    }
                }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });

        firestore.collection(Constants.KEY_FRIEND)
                .document(userCurrentID)
                .collection(Constants.KEY_YOUR_FRIENDS)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            listFriend.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                        }
                    }
                });


    }


    private void getUser(){
        if(listAiDoGuiDenBanLoiMoi != null
            || listBanGuiLoiKetBanDen != null
                || listFriend != null){
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(userCurrentID.equals(queryDocumentSnapshot.getId())
                                        || listAiDoGuiDenBanLoiMoi.contains(queryDocumentSnapshot.getId())
                                        || listFriend.contains(queryDocumentSnapshot.getId())
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
                                adapter = new GoiYKetBanAdapter(list, this);
                                binding.recyclerListFriendFragment.setAdapter(adapter);
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
                                adapter = new GoiYKetBanAdapter(list, this);
                                binding.recyclerListFriendFragment.setAdapter(adapter);
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
        adapter.notifyDataSetChanged();
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
                                        showToast("Đả huỷ lời mời kết bạn với: "+user.getName());
                                    }
                                    else {
                                        showToast("Huỷ lời mời kết bạn với: "+user.getName()+" không thành công");
                                    }
                                });
                    }
                });
        user.setInviteAddFriend(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void go(User user) {
        list.remove(user);
        adapter.notifyDataSetChanged();
    }

    private void showToast(String message){
        Toast.makeText(getActivity().getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}