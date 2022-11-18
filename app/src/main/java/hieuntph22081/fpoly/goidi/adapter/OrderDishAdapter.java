package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.OrderDish;

public class OrderDishAdapter extends RecyclerView.Adapter<OrderDishAdapter.OrderDishViewHolder> {
    Context context;
    List<OrderDish> list;
    public OrderDishAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OrderDish> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDishViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dish_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDishViewHolder holder, int position) {
        OrderDish orderDish = list.get(position);
        if (orderDish == null)
            return;
        holder.tvName.setText(orderDish.getDish().getTen());
        holder.tvPrice.setText(String.valueOf(orderDish.getDish().getGia()));
        holder.tvQuantity.setText(String.valueOf(orderDish.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderDishViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        public OrderDishViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
