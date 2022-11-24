package hieuntph22081.fpoly.goidi.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Dish;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.OrderDish;
import hieuntph22081.fpoly.goidi.model.Table;
import hieuntph22081.fpoly.goidi.model.User;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    List<Order> orders;
    FirebaseDatabase database;
    DatabaseReference myRef;
//    List<OrderDish> list = new ArrayList<>();
    OrderDishAdapter2 adapter;
    List<User> users = new ArrayList<>();
    List<Table> tables = new ArrayList<>();
    List<Dish> dishes = new ArrayList<>();
    List<OrderDish> orderDishes = new ArrayList<>();
    OrderDishAdapter dishAdapter = new OrderDishAdapter(context);
    RecyclerView recyclerView_orderDish;
    private SpinnerAdapter spinnerAdapter;
    int mHour, mMinute;
    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderTable.setText("Bàn " + order.getTable().getNumber());
        holder.tvOrderDate.setText("Ngày: " + order.getDate());
        holder.tvOrderTime.setText(order.getStartTime() + " - " + order.getEndTime());
        switch (order.getStatus()) {
            case 0:
                holder.tvOrderStatus.setText("Đang chờ");
                break;
            case 1:
                holder.tvOrderStatus.setText("Đang dùng");
                break;
            case 2:
                holder.tvOrderStatus.setText("Đã xong");
                break;
            case 3:
                diaLogDelete(order);
                break;
        }
        holder.tvOrderUser.setText("Khách hàng: " + order.getUser().getName());
        holder.tvOrderTotal.setText("Tổng tiền: " + formatCurrency(order.getTotal()) );
        holder.recyclerViewDishes.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new OrderDishAdapter2(context);
        adapter.setData(order.getDishes());
        holder.recyclerViewDishes.setAdapter(adapter);
        holder.itemView.setOnClickListener(v -> {
            if (holder.contentLayout.getVisibility() == View.GONE) {
                holder.contentLayout.setVisibility(View.VISIBLE);
                holder.imgDropDown.setImageResource(R.drawable.ic_drop_up);
            } else {
                holder.contentLayout.setVisibility(View.GONE);
                holder.imgDropDown.setImageResource(R.drawable.ic_drop_down);
            }
        });
        if(order.getStatus()==2){
            holder.imgEdit.setEnabled(false);
            holder.imgEdit.setVisibility(View.INVISIBLE);
        }else{
            holder.imgEdit.setVisibility(View.VISIBLE);
            holder.imgEdit.setEnabled(true);
            holder.imgEdit.setOnClickListener(v ->{
                openOrderDialog(order);
            });
        }

    }

    public void openOrderDialog(Order order) {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        TextView dialogUserTitle = dialog.findViewById(R.id.dialogUserTitle);
        dialogUserTitle.setText("Sửa Order");

        Spinner spnOrderUser = dialog.findViewById(R.id.spnOrderUser);
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    User user = s.getValue(User.class);
                    users.add(user);
                }
                List<String> userNames = new ArrayList<>();
                for (User u : users) {
                    userNames.add(u.getName());
                }
                spnOrderUser.setAdapter(new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, userNames));
                spnOrderUser.setSelection(userNames.indexOf(order.getUser().getName()));
                Log.e("TAG", ": "+  userNames.indexOf(order.getUser().getName()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        spnOrderUser.setSelection(users.indexOf(order.getUser()));

        Spinner spnOrderTable = dialog.findViewById(R.id.spnOrderTable);
        myRef.child("tables").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tables.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Table table = s.getValue(Table.class);
                    tables.add(table);
                }
                List<String> tableNames = new ArrayList<>();
                for (Table t : tables) {
                    tableNames.add("Bàn số " +  t.getNumber());
                }
                spnOrderTable.setAdapter(new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tableNames));
                spnOrderTable.setSelection(tableNames.indexOf("Bàn số " + order.getTable().getNumber()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        EditText edtOrderDate = dialog.findViewById(R.id.edtOrderDate);
        edtOrderDate.setText(order.getDate());
        edtOrderDate.setOnClickListener(v -> {datePickerDialog(edtOrderDate);});
        EditText edtOrderStartTime = dialog.findViewById(R.id.edtOrderStartTime);
        edtOrderStartTime.setText(order.getStartTime());
        edtOrderStartTime.setOnClickListener(v -> timePickerDialog(edtOrderStartTime));
        EditText edtOrderEndTime = dialog.findViewById(R.id.edtOrderEndTime);
        edtOrderEndTime.setText(order.getEndTime());
        edtOrderEndTime.setOnClickListener(v -> timePickerDialog(edtOrderEndTime));

        Spinner spnOrderStatus = dialog.findViewById(R.id.spnOrderStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Đang chờ");
        statuses.add("Đang dùng");
        statuses.add("Đã thanh toán");
        statuses.add("Hủy");
        spnOrderStatus.setAdapter(new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statuses));
        spnOrderStatus.setSelection(order.getStatus());

        Button btnOrderDish = dialog.findViewById(R.id.btnOrderDish);
        myRef.child("orders/"+order.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null){
                    Order order1 = snapshot.getValue(Order.class);
                    orderDishes = order1.getDishes();
                    dishAdapter.setData(orderDishes);
                    recyclerView_orderDish = dialog.findViewById(R.id.recycleView_orderDish);
                    recyclerView_orderDish.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView_orderDish.setAdapter(dishAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        orderDishes = order.getDishes();

        btnOrderDish.setOnClickListener(v -> {
            orderDishes = openDishDialog();
        });
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(v -> {
            order.setUser(users.get(spnOrderUser.getSelectedItemPosition()));
            order.setTable(tables.get(spnOrderTable.getSelectedItemPosition()));
            order.setDate(edtOrderDate.getText().toString());
            order.setStartTime(edtOrderStartTime.getText().toString());
            order.setEndTime(edtOrderEndTime.getText().toString());
            order.setStatus(spnOrderStatus.getSelectedItemPosition());
            order.setDishes(orderDishes);
            order.setTotal();
            myRef.child("orders").child(order.getId()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    protected List<OrderDish> openDishDialog() {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dish_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        Spinner spnDish = dialog.findViewById(R.id.spnDish);
        myRef.child("Dish").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishes.clear();
                List<String> dishNames = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Dish dish = s.getValue(Dish.class);
//                  dishNames.add(dish.getTen());
                    dishes.add(dish);
                }
                spinnerAdapter = new SpinnerAdapter(context, dishes);
                spnDish.setAdapter(spinnerAdapter);
//                spnDish.setAdapter(new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dishNames));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TextView edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);


        dishAdapter.setData(orderDishes);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(dishAdapter);


        btnAdd.setOnClickListener(v -> {
            OrderDish orderDish = new OrderDish();
            try {
                if (edtQuantity.getText().toString().trim().length() == 0) {
                    Toast.makeText(context, "Hãy nhập số lượng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderDish.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
            } catch (Exception e) {
                Toast.makeText(context, "Số lượng phải là số!", Toast.LENGTH_SHORT).show();
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
            orderDishes.add(orderDish);
            dishAdapter.setData(orderDishes);
        });
        Toast.makeText(context, ""+orderDishes.size(), Toast.LENGTH_SHORT).show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog( context, (view, year, month, dayOfMonth) -> {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.show();
    }
    
    public String formatCurrency(double money) {
        String pattern="###,###.### VNĐ";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(money);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTable, tvOrderDate, tvOrderTime, tvOrderStatus, tvOrderUser, tvOrderTotal;
        ImageView imgEdit, imgDropDown;
        RecyclerView recyclerViewDishes;
        RelativeLayout contentLayout;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTable = itemView.findViewById(R.id.tvOrderTable);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderUser = itemView.findViewById(R.id.tvOrderUser);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDropDown = itemView.findViewById(R.id.imgDropDown);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            recyclerViewDishes = itemView.findViewById(R.id.recyclerViewDishes);
        }
    }
    public void diaLogDelete(Order order){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("orders/"+order.getId());
        databaseRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
