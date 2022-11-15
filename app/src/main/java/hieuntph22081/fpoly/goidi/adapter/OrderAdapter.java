package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import hieuntph22081.fpoly.goidi.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
//    List<Order> orders;
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTable, tvOrderDate, tvOrderTime, tvOrderStatus, tvOrderUser, tvOrderTotal;
        ImageView imgDelete, imgDropDown;
        FlowLayout dishesLayout;
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
            dishesLayout = itemView.findViewById(R.id.dishesLayout);


        }
    }
}
