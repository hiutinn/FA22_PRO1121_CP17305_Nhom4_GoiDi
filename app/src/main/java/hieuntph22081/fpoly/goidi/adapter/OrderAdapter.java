package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.OrderDish;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    List<Order> orders;
//    List<OrderDish> list = new ArrayList<>();
    OrderDishAdapter adapter;
    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderTable.setText("Bàn " + order.getTable().getId());
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
        }

        holder.tvOrderUser.setText("Khách hàng: " + order.getUser().getName());
        holder.tvOrderTotal.setText("Tổng tiền: " + order.getTotal());

        holder.recyclerViewDishes.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new OrderDishAdapter(context, order.getDishes());
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
        holder.imgDelete.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTable, tvOrderDate, tvOrderTime, tvOrderStatus, tvOrderUser, tvOrderTotal;
        ImageView imgDelete, imgDropDown;
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
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDropDown = itemView.findViewById(R.id.imgDropDown);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            recyclerViewDishes = itemView.findViewById(R.id.recyclerViewDishes);
        }
    }
}
