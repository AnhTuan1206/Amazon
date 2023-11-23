package com.tuan.amazon.activities;

import static com.tuan.amazon.fragments.FriendFragment.listAiDoGuiDenBanLoiMoi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.adapters.LoiMoiKetBanAdapter;
import com.tuan.amazon.databinding.ActivityInviteAddFriendBinding;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;
import java.util.ArrayList;
import java.util.List;

public class InviteAddFriendActivity extends AppCompatActivity {

    private ActivityInviteAddFriendBinding binding;
    private FirebaseFirestore firestore;
    private List<User> list;
    private LoiMoiKetBanAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        int count = 0;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(listAiDoGuiDenBanLoiMoi.contains(queryDocumentSnapshot.getId())
                                 && count < listAiDoGuiDenBanLoiMoi.size()){
                                    User user = new User();
                                    user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                    user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                    list.add(user);
                                    count ++;
                                }
                        }
                        if(list.size() > 0){
                            adapter = new LoiMoiKetBanAdapter(list);
                            binding.recyclerInviteAddFriend.setAdapter(adapter);
                        }
                    }
                });
    }
}