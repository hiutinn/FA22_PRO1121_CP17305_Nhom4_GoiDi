package hieuntph22081.fpoly.goidi.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.OrderAdapter;
import hieuntph22081.fpoly.goidi.adapter.OrderDishAdapter;
import hieuntph22081.fpoly.goidi.adapter.SpinnerAdapter;
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
    RecyclerView recyclerView_orderDish;
    OrderAdapter adapter;
    List<Order> list = new ArrayList<>();
    FloatingActionButton fab;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<User> users = new ArrayList<>();
    List<Table> tables = new ArrayList<>();
    List<Dish> dishes = new ArrayList<>();
    List<OrderDish> orderDishes = new ArrayList<>();
    OrderDishAdapter dishAdapter = new OrderDishAdapter(getContext());
    int mHour, mMinute;
    SpinnerAdapter spinnerAdapter;
    EditText edt_search;

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
        edt_search = view.findViewById(R.id.edtSearch_order);
        fab = view.findViewById(R.id.fabOrder);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        initData();
        fab.setOnClickListener(v -> openOrderDialog());
        search(edt_search);
    }

    private void initData() {
        adapter = new OrderAdapter(getContext(), list);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        getOrderFromDB();
        recyclerViewOrder.setAdapter(adapter);
    }

    private void getOrderFromDB() {
        myRef.child("orders").addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                if (order != null) {
                    list.add(0,order);
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

    public void openOrderDialog() {
        Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        TextView dialogUserTitle = dialog.findViewById(R.id.dialogUserTitle);
        dialogUserTitle.setText("Thêm Order");

        Spinner spnOrderUser = dialog.findViewById(R.id.spnOrderUser);
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null){
                    for (DataSnapshot s : snapshot.getChildren()) {
                        User user = s.getValue(User.class);
                        users.add(user);
                    }
                    List<String> userNames = new ArrayList<>();
                    for (User u : users) {
                        userNames.add(u.getName());
                    }
                    spnOrderUser.setAdapter(new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, userNames));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        EditText edtOrderDate = dialog.findViewById(R.id.edtOrderDate);
        edtOrderDate.setOnClickListener(v -> {datePickerDialog(edtOrderDate);});
        EditText edtOrderStartTime = dialog.findViewById(R.id.edtOrderStartTime);
        edtOrderStartTime.setOnClickListener(v -> timePickerDialog(edtOrderStartTime));
        EditText edtOrderEndTime = dialog.findViewById(R.id.edtOrderEndTime);
        edtOrderEndTime.setOnClickListener(v -> timePickerDialog(edtOrderEndTime));

        Spinner spnOrderStatus = dialog.findViewById(R.id.spnOrderStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Đang chờ");
        statuses.add("Đang dùng");
        statuses.add("Đã thanh toán");
        statuses.add("Hủy");
        spnOrderStatus.setAdapter(new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statuses));
        Button btnOrderDish = dialog.findViewById(R.id.btnOrderDish);
        btnOrderDish.setOnClickListener(v -> {
            orderDishes = openDishDialog();
        });

        orderDishes.clear();
        dishAdapter.setData(orderDishes);
        recyclerView_orderDish = dialog.findViewById(R.id.recycleView_orderDish);
        recyclerView_orderDish.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView_orderDish.setAdapter(dishAdapter);


        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(v -> {
            Order order = new Order();
            order.setId("order" + Calendar.getInstance().getTimeInMillis());
            order.setUser(users.get(spnOrderUser.getSelectedItemPosition()));
            order.setDate(edtOrderDate.getText().toString());
            order.setStartTime(edtOrderStartTime.getText().toString());
            order.setEndTime(edtOrderEndTime.getText().toString());
            order.setStatus(spnOrderStatus.getSelectedItemPosition());
            order.setDishes(orderDishes);
            order.setTotal();

            myRef.child("orders").child(order.getId()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Thêm hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    protected List<OrderDish> openDishDialog() {
        Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dish_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        Spinner spnDish = dialog.findViewById(R.id.spnDish);



        myRef.child("Dish").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishes.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Dish dish = s.getValue(Dish.class);
                   // dishNames.add(dish.getTen());
                    dishes.add(dish);
                }
                spinnerAdapter = new SpinnerAdapter(getActivity(), dishes);
                spnDish.setAdapter(spinnerAdapter);
                //spnDish.setAdapter(new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dishNames));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TextView edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(dishAdapter);
        btnAdd.setOnClickListener(v -> {
            OrderDish orderDish = new OrderDish();
            try {
                if (edtQuantity.getText().toString().trim().length() == 0) {
                    Toast.makeText(getContext(), "Hãy nhập số lượng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderDish.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Số lượng phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }
            orderDish.setDish(dishes.get(spnDish.getSelectedItemPosition()));

            for (OrderDish orderDish1 : orderDishes) {
                if (orderDish1.getDish().getId().equals(orderDish.getDish().getId())) {
                    orderDish1.setQuantity(orderDish1.getQuantity() + orderDish.getQuantity());
                    dishAdapter.setData(orderDishes);
                    return;
                }
            }
            double tong = 0;
            for (OrderDish orderDish1 : orderDishes) {
                tong += orderDish1.getDish().getGia() * orderDish1.getQuantity();
            }
            Log.e("TAG", "Tong tien: " + tong );
            orderDishes.add(orderDish);
            dishAdapter.setData(orderDishes);
        });

        btnOk.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
        return orderDishes;
    }

    private void datePickerDialog(EditText editText){
        Calendar calendar = Calendar.getInstance();
        int day1,month1,year1;
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        month1 = calendar.get(Calendar.MONTH);
        year1 = calendar.get(Calendar.YEAR);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), (view, year, month, dayOfMonth) -> {
            calendar.set(year,month,dayOfMonth);
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        },year1,month1,day1);
        datePickerDialog.show();
    }

    private void timePickerDialog(EditText editText){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            mHour = hourOfDay;
            mMinute = minute;
            editText.setText(String.format(Locale.getDefault(), "%02d:%02d",hourOfDay,minute));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.show();
    }
    private void search(EditText editText){
        editText.setOnClickListener(v -> {
            datePickerDialog(editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });
    }

}