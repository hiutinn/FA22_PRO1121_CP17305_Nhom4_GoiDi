package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.fragment.UserFragment;
import hieuntph22081.fpoly.goidi.model.User;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    UserFragment fragment;
    private ArrayList<User> list;
    TextView tvMaTV,tvTenTV,tvNamSinh;
    ImageView imgDel ;

    public UserAdapter(@NonNull Context context,UserFragment fragment, @NonNull ArrayList<User> list) {
        super(context, 0,list);
        this.context=context;
        this.fragment=fragment;
        this.list=list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        if (v ==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v= inflater.inflate(R.layout.user_item,null);
        }
        final User item = list.get(position);
        if (item !=null){
            tvMaTV=v.findViewById(R.id.tvMaTV);
            tvMaTV.setText("Mã thành viên:"+item.getMaTV());

            tvTenTV =v.findViewById(R.id.tvTenTV);
            tvTenTV.setText("Tên thành viên: "+item.getHoTen());

            tvNamSinh=v.findViewById(R.id.tvNamSinh);
            tvNamSinh.setText("Năm sinh: "+item.getNamSinh());

            imgDel=v.findViewById(R.id.imgDeleteTV);
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.xoa(String.valueOf(item.getMaTV()));
                }
            });
        }
        for (int i=0;i<=list.size();i++){
            if (item.getMaTV()%2==0){
                tvMaTV.setTextColor(Color.RED);
            }else {
                tvMaTV.setTextColor(Color.BLUE);
            }
        }
        return v;
    }
}
