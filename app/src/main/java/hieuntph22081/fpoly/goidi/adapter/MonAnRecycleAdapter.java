package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.MonAn;

public class MonAnRecycleAdapter extends RecyclerView.Adapter<MonAnRecycleAdapter.userViewHolder> {
    private List<MonAn> list;
    private Context context;


    public MonAnRecycleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MonAn> list){
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
        MonAn monAn = list.get(position);
        holder.img_monAn.setImageResource(monAn.getImg());
        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        holder.img_monAn.setScaleType(scaleType);
        holder.tv_tenMonAn.setText(monAn.getTen());
        holder.tv_gia.setText(String.valueOf(monAn.getGia()));
        AtomicInteger soLuong = new AtomicInteger();
        soLuong.set(monAn.getSoLuong());
//        holder.btn_tang.setOnClickListener(v -> {
//            soLuong.getAndIncrement();
//            holder.tv_soLuong.setText(String.valueOf(soLuong.get()));
//        });
//        holder.btn_giam.setOnClickListener(v -> {
//            soLuong.getAndDecrement();
//            holder.tv_soLuong.setText(String.valueOf(soLuong.get()));
//        });
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
        TextView tv_tenMonAn,tv_gia,tv_soLuong;
        Button btn_tang,btn_giam;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            img_monAn = itemView.findViewById(R.id.img_monAn);
            tv_tenMonAn = itemView.findViewById(R.id.tv_ten_mon);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            img_xoaMonAn = itemView.findViewById(R.id.img_xoaMonAn);
//            tv_soLuong = itemView.findViewById(R.id.tv_soLuong);
//            btn_tang = itemView.findViewById(R.id.btn_tang);
//            btn_giam = itemView.findViewById(R.id.btn_giam);
        }
    }
}
