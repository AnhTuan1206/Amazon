package com.tuan.amazon.activities;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;
import static com.tuan.amazon.fragments.GoiYKetBanFragment.listAiDoGuiDenBanLoiMoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.ActivityInviteAddFriendBinding;
import com.tuan.amazon.listeners.LoiMoiKetBanListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteAddFriendActivity extends AppCompatActivity implements LoiMoiKetBanListener {

    private ActivityInviteAddFriendBinding binding;
    private FirebaseFirestore firestore;
    private List<User> list;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    private void initView(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lời mời kết bạn");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        int count = 0;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (listAiDoGuiDenBanLoiMoi.contains(queryDocumentSnapshot.getId())
                                    && count < listAiDoGuiDenBanLoiMoi.size()) {
                                User user = new User();
                                user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                user.setId(queryDocumentSnapshot.getString(Constants.KEY_USER_ID));
                                list.add(user);
                                count++;
                            }
                        }
                        if (list.size() > 0) {
//                            adapter = new LoiMoiKetBanAdapter(list, this);
//                            binding.recyclerInviteAddFriend.setAdapter(adapter);
                            userAdapter = new UserAdapter(list, 2, null,this, null, null, null, null);
                            binding.recyclerInviteAddFriend.setAdapter(userAdapter);
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void removeLoiMoiKB(User user){
        firestore.collection(Constants.KEY_LMKB)
                .document(userCurrentID)
                .collection(Constants.KEY_NG)
                .document(user.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        firestore.collection(Constants.KEY_LMKB)
                                .document(user.getId())
                                .collection(Constants.KEY_GD)
                                .document(userCurrentID)
                                .delete();
                    }
                });
    }

    @Override
    public void Accept(User user) {
        Map map = new HashMap();
        map.put(Constants.KEY_ID, user.getId());
        firestore.collection(Constants.KEY_FRIEND)
                .document(userCurrentID)
                .collection(Constants.KEY_YOUR_FRIENDS)
                .document(user.getId())
                .set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        map.clear();
                        map.put(Constants.KEY_ID, userCurrentID);
                        firestore.collection(Constants.KEY_FRIEND)
                                .document(user.getId())
                                .collection(Constants.KEY_YOUR_FRIENDS)
                                .document(userCurrentID)
                                .set(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                            removeLoiMoiKB(user);
                                    } else {
                                        showToast("Chấp nhận thất bại");
                                    }
                                });
                    }
                });
        listAiDoGuiDenBanLoiMoi.remove(user.getId());
        listMyFriend.add(user.getId());
    }
    @Override
    public void Remove(User user) {
        removeLoiMoiKB(user);
        listAiDoGuiDenBanLoiMoi.remove(user.getId());
    }

    @Override
    public void goToProfile(User user) {
        goProfileActivity(user);
    }

    private void goProfileActivity(User user){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_NAME, user.getName());
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);
    }
}
