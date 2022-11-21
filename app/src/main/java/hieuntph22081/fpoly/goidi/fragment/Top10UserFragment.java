package hieuntph22081.fpoly.goidi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.UserAdapter;
import hieuntph22081.fpoly.goidi.model.Dish;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.User;

public class Top10UserFragment extends Fragment {
    private DatabaseReference datebaseRef;
    private List<String> listIdUser = new ArrayList<>();
    private List<User> listUser = new ArrayList<>();
    private int soLan =0;
    private UserAdapter adapter;
    private RecyclerView recyclerView;

    public Top10UserFragment() {
        // Required empty public constructor
    }

    public static Top10UserFragment newInstance() {
        Top10UserFragment fragment = new Top10UserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top10, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.topUserRecycleView);
        adapter = new UserAdapter(getActivity(),listUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        getTop10User();
        getListUserBySoLan();
    }

    public void getListUserBySoLan(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("soLan").startAfter(0).limitToFirst(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    listUser.add(0,user);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void getTop10User() {

        listIdUser.clear();
        datebaseRef = FirebaseDatabase.getInstance().getReference().child("orders");
        datebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot != null){
                        Order order = dataSnapshot.getValue(Order.class);
                        if(order.getStatus()==2){
                            datebaseRef.child(order.getId() + "/user/id").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue() != null){
                                        String orderUserId = snapshot.getValue().toString();
                                        listIdUser.add(orderUserId);
                                        datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/"+orderUserId+"/id");
                                        datebaseRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                soLan=0;
                                                String idUser = snapshot2.getValue().toString();
                                                for(int i=0;i<listIdUser.size();i++){
                                                    if(idUser.equals(listIdUser.get(i))){
                                                        soLan++;
                                                    }
                                                }
                                                datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/"+idUser+"/soLan");
                                                datebaseRef.setValue(soLan);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}



