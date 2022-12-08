package hieuntph22081.fpoly.goidi.fragment;

import android.app.DatePickerDialog;
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
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.UserAdapter;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.User;

public class Top10UserFragment extends Fragment {
    private DatabaseReference datebaseRef;
    private List<User> getListUser = new ArrayList<>();
    private List<User> listUser1 = new ArrayList<>();
    private List<User> listUser2 = new ArrayList<>();
    private int soLan = 0;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private TextInputEditText ed_tuNgay, ed_denNgay;
    private int day, month, year;

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
        return inflater.inflate(R.layout.fragment_top10_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.topUserRecycleView);
        ed_tuNgay = view.findViewById(R.id.ed_TopUser_tuNgay);
        ed_denNgay = view.findViewById(R.id.ed_TopUser_denNgay);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        ed_tuNgay.setOnClickListener(v -> {
            datePickerDialog(calendar, ed_tuNgay);
        });
        ed_denNgay.setOnClickListener(v -> {
            datePickerDialog(calendar, ed_denNgay);
        });
        view.findViewById(R.id.btn_topUser).setOnClickListener(v -> {
            getListUser();
            getListUserBySoLan();
        });
        adapter = new UserAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void getListUserBySoLan() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("soLan").startAfter(0).limitToFirst(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListUser.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    getListUser.add(0, user);
                }
                adapter.setData(getListUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getListUser() {
        listUser2.clear();
        datebaseRef = FirebaseDatabase.getInstance().getReference().child("orders");
        datebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Order order = snapshot1.getValue(Order.class);
                    listUser2 = getlistUserByDate(order);
                    datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/" + order.getUser().getId() + "/id");
                    datebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!= null){
                                soLan = 0;
                                String idUser = snapshot.getValue().toString();
                                for (int i = 0; i < listUser2.size(); i++) {
                                    if (idUser.equals(listUser2.get(i).getId())) {
                                        soLan++;
                                    }
                                }
                                datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/" + idUser + "/soLan");
                                datebaseRef.setValue(soLan);
                            }
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

    private List<User> getlistUserByDate(Order order) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //13/11 so sanh 17/11 =-1
        //17/11 so sanh 13/11 = 1
        try {
            Date fromDate = format.parse(ed_tuNgay.getText().toString());
            Date toDate = format.parse(ed_denNgay.getText().toString());
            Date orderDate = format.parse(order.getDate());
            if (orderDate.compareTo(fromDate) >= 0 && orderDate.compareTo(toDate) <= 0 && order.getStatus() == 2) {
                listUser1.add(order.getUser());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return listUser1;
    }

    private void datePickerDialog(Calendar calendar, EditText editText) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }


//    public void getTop10User() {
//
//        listIdUser.clear();
//        datebaseRef = FirebaseDatabase.getInstance().getReference().child("orders");
//        datebaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if(dataSnapshot != null){
//                        Order order = dataSnapshot.getValue(Order.class);
//                        if(order.getStatus()==2){
//                            Log.e("order",order.getId()+"");
//                            datebaseRef.child(order.getId() + "/user/id").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.getValue() != null){
//                                        String orderUserId = snapshot.getValue().toString();
//                                        Log.e("order",orderUserId+"");
//                                        listIdUser.add(orderUserId);
//
//                                        datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/"+orderUserId+"/id");
//                                        datebaseRef.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                Log.e("size",listIdUser.size()+"");
//                                                soLan=0;
//                                                String idUser = snapshot.getValue().toString();
//                                                for(int i=0;i<listIdUser.size();i++){
//                                                    if(idUser.equals(listIdUser.get(i))){
//                                                        soLan++;
//                                                    }
//                                                }
//                                                datebaseRef = FirebaseDatabase.getInstance().getReference().child("users/"+idUser+"/soLan");
//                                                datebaseRef.setValue(soLan);
//                                            }
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//                                            }
//                                        });
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }


}



