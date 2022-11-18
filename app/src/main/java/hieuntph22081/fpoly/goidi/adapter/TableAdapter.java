package hieuntph22081.fpoly.goidi.adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Table;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHodler> {
    private Context context;
    private List<Table> TableList;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    public TableAdapter(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("tables");
    }


    public void setTableList(List<Table> tableList) {
        TableList = tableList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TableAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHodler holder, int position) {
        Table table = TableList.get(position);

        if (table == null)
            return;
        holder.tvNumber.setText("Bàn số " + table.getNumber());
        holder.tvSeat.setText("Chỗ ngồi: " + table.getSeat());
        holder.itemView.setOnLongClickListener(v -> {
            diaLogTable(table);
            return true;
        });
    }
    void diaLogTable(Table table){
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_table);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.58);
        dialog.getWindow().setLayout(width, height);

        EditText edt_number = dialog.findViewById(R.id.edt_number);
        EditText edt_seat = dialog.findViewById(R.id.edt_seat);
        Button btn_show = dialog.findViewById(R.id.btnShow);
        Button btn_no1 = dialog.findViewById(R.id.btn_No);
        btn_show.setOnClickListener(v -> {
            table.setSeat(Integer.parseInt(edt_seat.getText().toString()));
            table.setNumber(Integer.parseInt(edt_number.getText().toString()));
            myRef.child(table.getId()).updateChildren(table.toMap()).
                    addOnCompleteListener(task ->
                            Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show());
            dialog.dismiss();
        });

        btn_no1.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    public int getItemCount() {

        return (TableList == null) ? 0 : TableList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        TextView tvNumber, tvSeat;
        ImageView imgDelete;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.tvNumber);
            tvSeat = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.tvSeat);
            imgDelete = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.imgDelete);
        }
    }
}

