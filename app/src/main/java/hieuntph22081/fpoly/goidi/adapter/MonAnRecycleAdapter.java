package hieuntph22081.fpoly.goidi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Dish;

public class MonAnRecycleAdapter extends RecyclerView.Adapter<MonAnRecycleAdapter.userViewHolder> {
    private List<Dish> list;
    private Context context;
    private IClickListener iClickListener;

    public interface IClickListener{
        void OnClickUpdateItem(Dish dish);
    }

    public MonAnRecycleAdapter(Context context,IClickListener listener) {
        this.context = context;
        this.iClickListener = listener;
    }

    public void setData(List<Dish> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monan,parent,false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {

        Dish dish = list.get(position);
        Glide.with(context).load(dish.getImg()).into(holder.img_monAn);
        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        holder.img_monAn.setScaleType(scaleType);
        holder.tv_tenMonAn.setText(dish.getTen());
        holder.tv_gia.setText(formatCurrency(dish.getGia()));
        AtomicInteger soLuong = new AtomicInteger();
        soLuong.set(dish.getSoLuong());

        holder.img_xoaMonAn.setOnClickListener(v -> {
            diaLogDelete(dish);
            Log.e("id",dish.getId());

        });
        holder.itemView.setOnClickListener(v -> {
            iClickListener.OnClickUpdateItem(dish);
        });
    }

    public String formatCurrency(double money) {
        String pattern="###,###.### VNĐ";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(money);
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        ImageView img_monAn,img_xoaMonAn;
        TextView tv_tenMonAn,tv_gia;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            img_monAn = itemView.findViewById(R.id.img_monAn);
            tv_tenMonAn = itemView.findViewById(R.id.tv_ten_mon);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            img_xoaMonAn = itemView.findViewById(R.id.img_xoaMonAn);
        }
    }
    public void diaLogDelete(Dish dish){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa không");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Dish/"+dish.getId());
                databaseRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
