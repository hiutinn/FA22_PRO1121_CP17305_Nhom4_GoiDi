package hieuntph22081.fpoly.goidi.database;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.model.User;

public class UserDAO {
//    FirebaseDatabase database;
//    DatabaseReference myRef ;
//    Context context;
//    List<User> list = new ArrayList<>();
//    public UserDAO(Context context) {
//        this.database = FirebaseDatabase.getInstance();
//        this.myRef = this.database.getReference("users");
//        this.context = context;
//    }
//
//    public void insertUser(User user) {
//        myRef.child(String.valueOf(user.getId())).setValue(user).addOnSuccessListener(unused ->
//                Toast.makeText(context, "Insert successfully", Toast.LENGTH_SHORT).show());
//    }
//
//    public void updateUser(User user) {
//        myRef.child(String.valueOf(user.getId())).updateChildren(user.toMap()).addOnSuccessListener(unused ->
//                Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show());
//    }
//
//    public void deleteUser(int id) {
//        myRef.child(String.valueOf(id)).removeValue().addOnSuccessListener(unused ->
//                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show());
//    }
//
//    public List<User> getAllUser() {
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot s : snapshot.getChildren()) {
//                    User user = s.getValue(User.class);
//                    if (user != null) {
//                        list.add(user);
////                        Toast.makeText(context, "" + list.size(), Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(context, "user Null", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, "Error" + error, Toast.LENGTH_SHORT).show();
//            }
//        });
////        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//        return list;
//    }
}
