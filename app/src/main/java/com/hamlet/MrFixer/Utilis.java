package com.hamlet.MrFixer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Utilis {
    public static Task<Void> removeValue(String userId,String itemId){
    Task<Void> task = FirebaseDatabase.getInstance().getReference("usersWishList").child(userId).child(itemId).removeValue();
    return task;
    }

    public static Task<Void> removeCart(String userId,String itemId){
        Task<Void> task = FirebaseDatabase.getInstance().getReference("usersCartList").child(userId).child(itemId).removeValue();
        return task;
    }
}
