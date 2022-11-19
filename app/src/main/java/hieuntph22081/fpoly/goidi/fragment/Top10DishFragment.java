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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.MonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.adapter.UserAdapter;
import hieuntph22081.fpoly.goidi.model.Dish;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.OrderDish;
import hieuntph22081.fpoly.goidi.model.User;


public class Top10DishFragment extends Fragment {
    private DatabaseReference datebaseRef;
    private List<String> listIdDish = new ArrayList<>();
    private List<Dish> listDish = new ArrayList<>();
    private MonAnRecycleAdapter adapter;
    private RecyclerView recyclerView;
    List<Order> orders = new ArrayList<>();
    List<Dish> dishes = new ArrayList<>();
//    List<Dish> dishesInOrders = new ArrayList<>();
    List<Integer> dishOccurs = new ArrayList<>();
    List<String> dishesId = new ArrayList<>();

    public Top10DishFragment() {
        // Required empty public constructor
    }


    public static Top10DishFragment newInstance() {
        Top10DishFragment fragment = new Top10DishFragment();
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
        return inflater.inflate(R.layout.fragment_top10_dish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datebaseRef = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recyclerViewTop10Dish);
        adapter = new MonAnRecycleAdapter(getContext(), dish -> {
        });
//        adapter.setData(dishes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getTop10Dish();
    }

    public void getTop10Dish() {
        datebaseRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    orders.add(order);
                }
                datebaseRef.child("Dish").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishes.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Dish dish = dataSnapshot.getValue(Dish.class);
                            dishes.add(dish);
                        }
                        for (Order order : orders) {
                            for (OrderDish orderDish : order.getDishes()) {
                                for (int i = 0; i < orderDish.getQuantity(); i++) {
                                    dishesId.add(orderDish.getDish().getId());
                                }
                            }
                        }

                        for (Dish dish : dishes) {
                            int occurrence = Collections.frequency(dishesId, dish.getId());
                            dishOccurs.add(occurrence);
                        }

                        Log.e("TAG", "getTop10Dish: " + dishOccurs.get(1));
                        for (int i = 0; i < dishes.size()-1; i++) {
                            for (int j = i + 1; j < dishes.size(); j++) {
                                if (dishOccurs.get(i) < dishOccurs.get(j)) {
                                    int dupTemp = dishOccurs.get(i);
                                    dishOccurs.set(i, dishOccurs.get(j));
                                    dishOccurs.set(j, dupTemp);
                                    Dish dishTemp = dishes.get(i);
                                    dishes.set(i, dishes.get(j));
                                    dishes.set(j, dishTemp);
                                }
                            }
                        }
                        adapter.setData(dishes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    public void getTop10Dish() {
//        listIdDish.clear();
//        datebaseRef = FirebaseDatabase.getInstance().getReference().child("orders");
//        datebaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if(dataSnapshot != null){
//                        Order order = dataSnapshot.getValue(Order.class);
//                        datebaseRef = FirebaseDatabase.getInstance().getReference().child("orders/"+order.getId()+"/dishes/"+0);
//                        datebaseRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot != null){
//                                    datebaseRef.child("quantity").addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if(snapshot.getValue() != null){
//                                                String soLuong  = snapshot.getValue().toString();;
//                                                Soluong+= Integer.parseInt(soLuong);
//                                            }
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//
//                                    datebaseRef.child("dish").addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if(snapshot != null){
//                                                Dish dish = snapshot.getValue(Dish.class);
////                                        datebaseRef = FirebaseDatabase.getInstance().getReference().child("Dish/"+dish.getId()+"soLuong");
////                                        datebaseRef.setValue(soLan);
//                                                Log.e("id",dish.getId());
//                                                Log.e("soLuong",Soluong+"");
//                                            }
//
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                        }
//                                    });
//                                }
//
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//
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