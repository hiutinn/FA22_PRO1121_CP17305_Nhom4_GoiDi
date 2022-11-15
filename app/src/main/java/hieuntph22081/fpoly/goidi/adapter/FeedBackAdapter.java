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

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHodler>{
    private List<FeedBack> feedBackList;
    private Context context;

    public FeedBackAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FeedBack> feedBackList){
        this.feedBackList = feedBackList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_feedback,parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
            FeedBack feedBack = feedBackList.get(position);
            if(feedBackList==null)
                return;
            holder.tv_id.setText(String.valueOf(feedBack.getId()));
            holder.tv_content.setText(String.valueOf(feedBack.getContent()));
            holder.tv_date.setText(String.valueOf(feedBack.getDate()));
            holder.tv_userId.setText(String.valueOf(feedBack.getUserid()));
    }

    @Override
    public int getItemCount() {

        return (feedBackList == null) ? 0: feedBackList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder{
        TextView tv_id,tv_content,tv_userId,tv_date;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtID);
            tv_content = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtContent);
            tv_userId = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtUserID);
            tv_date = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtDate);
        }
    }
}
