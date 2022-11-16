package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.LoaiMonAn;

public class LoaiMonAnRecycleAdapter extends RecyclerView.Adapter<LoaiMonAnRecycleAdapter.userViewHolder> {
    private List<LoaiMonAn> list;
    private Context context;

    public LoaiMonAnRecycleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<LoaiMonAn> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_monan,parent,false);
        return new userViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        LoaiMonAn loaiMonAn = list.get(position);
        holder.img.setImageResource(loaiMonAn.getImg());
        holder.ten.setText(loaiMonAn.getTen());
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView ten;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_type_dish);
            ten = itemView.findViewById(R.id.tv_name_type);
        }
    }
}
