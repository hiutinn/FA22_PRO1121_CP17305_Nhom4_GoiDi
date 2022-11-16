package hieuntph22081.fpoly.goidi.fragment;

import android.annotation.SuppressLint;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.OrderAdapter;
import hieuntph22081.fpoly.goidi.model.Dish;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.OrderDish;
import hieuntph22081.fpoly.goidi.model.Table;
import hieuntph22081.fpoly.goidi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    RecyclerView recyclerViewOrder;
    OrderAdapter adapter;
    List<Order> list = new ArrayList<>();
    User user;
    List<OrderDish> dishes = new ArrayList<>();
    public OrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
       
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
        initData();
    }

    private void initData() {
        adapter = new OrderAdapter(getContext(), list);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        getOrderFromDB();
        recyclerViewOrder.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Order order = new Order();
        order.setId("1");

        order.setDate("16/11/2022");
        order.setStartTime("17:00");
        order.setEndTime("19:00");
        order.setTable(new Table(4));
        order.setStatus(0);
        order.setUser(new User("user1668565419268","Hieu", "0379103490", 0));

        Dish dish = new Dish("id1668572509373", "Lau rieu cua",
                "https://firebasestorage.googleapis.com/v0/b/goidi-f919e.appspot.com/o/image%2Fdish1668577048011?alt=media&token=6e59ded3-8826-4dca-8a1b-d27556ea0491",
                0, 100000);
        dishes.add(new OrderDish(dish,3));
        order.setDishes(dishes);
        Log.e("dishes", order.getDishes().size()+"" );
        order.setTotal();
        myRef.child("orders").child(order.getId()).setValue(order);
    }

    private void getOrderFromDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orders");
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                if (order != null) {
                    list.add(order);
                    adapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                if (order == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (order.getId().equals(list.get(i).getId())) {
                        list.set(i, order);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Order order = snapshot.getValue(Order.class);
                if (order == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (order.getId().equals(list.get(i).getId())) {
                        list.remove(list.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}