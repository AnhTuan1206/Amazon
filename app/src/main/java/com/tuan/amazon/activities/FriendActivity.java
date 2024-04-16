package com.tuan.amazon.activities;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;
import static com.tuan.amazon.activities.MainActivity.userCurrentName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.ActivityFriendBinding;
import com.tuan.amazon.listeners.FriendListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity implements FriendListener {
    private ActivityFriendBinding binding;
    private List<User> list;
    private FirebaseFirestore firestore;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initView();
        event();
    }

    private void initView(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(userCurrentName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void event(){
        searchFriend();
    }

    private void searchFriend(){
        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String txt){
        List<User> filterList = new ArrayList<>();
        for (User user : list){
            if(user.getName().toUpperCase().contains(txt.toUpperCase())){
                filterList.add(user);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this,"Không tìm thấy tên bạn bè tương ứng", Toast.LENGTH_LONG).show();
        }else {
            userAdapter.setFilteredList(filterList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
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
                            userAdapter = new UserAdapter(list,3, null, null, this, null, null, null);
                            binding.recyclerFriend.setAdapter(userAdapter);
                        }
                    });
        }
    }

    @Override
    public void goToProfile(User user) {
        goProfileActivity(user);
    }

    @Override
    public void removeFriend(User user) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xoá bạn bè");
        alertDialog.setMessage("Bạn có chắc muốn huỷ bạn bè với "+user.getName() +" không?");

        alertDialog.setPositiveButton("Có", (dialogInterface, i) -> {
            removeFr(user);
            listMyFriend.remove(user);
            list.remove(user);
            userAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Huỷ bạn bè với "+user.getName()+" thành công",Toast.LENGTH_SHORT).show();
        });

        alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {

        });
        alertDialog.show();
    }

    private void removeFr(User user){
        firestore.collection(Constants.KEY_FRIEND)
                .document(userCurrentID)
                .collection(Constants.KEY_YOUR_FRIENDS)
                .document(user.getId())
                .delete();
        firestore.collection(Constants.KEY_FRIEND)
                .document(user.getId())
                .collection(Constants.KEY_YOUR_FRIENDS)
                .document(userCurrentID)
                .delete();
    }

    private void goProfileActivity(User user){
        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_NAME, user.getName());
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);

    }
}