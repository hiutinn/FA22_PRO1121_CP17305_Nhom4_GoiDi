package hieuntph22081.fpoly.goidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.FeedBack;
import hieuntph22081.fpoly.goidi.model.Table;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHodler> {
    private  Context Context;
    private List<Table> TableList;

    public TableAdapter(Context context){this.Context = context;}

    public void setTableList(List<Table> tableList) {
        TableList = tableList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TableAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_table,parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHodler holder, int position) {
        Table table = TableList.get(position);
        if(TableList==null)
            return;
        holder.tv_idtable.setText(String.valueOf(table.getIdtable()));
    }

    @Override
    public int getItemCount() {
        return (TableList == null) ? 0: TableList.size();
    }
    public class ViewHodler extends RecyclerView.ViewHolder{
        TextView tv_idtable;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            tv_idtable = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtID);
}}}
