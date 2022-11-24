package hieuntph22081.fpoly.goidi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback,parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
            FeedBack feedBack = feedBackList.get(position);
            if(feedBack == null)
                return;
            holder.tv_content.setText(String.valueOf(feedBack.getContent()));
            holder.tv_date.setText(String.valueOf(feedBack.getDate()));
            holder.tv_userId.setText(String.valueOf(feedBack.getUser().getName()));
            holder.imgDropDown.setImageResource(R.drawable.ic_drop_down);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Bạn có muốn xóa không ?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("feedbacks");
                    myRef.child(feedBack.getId()).removeValue((error, ref)
                            -> Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show());
                    dialog.cancel();
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                builder.show();
            });
    }

    @Override
    public int getItemCount() {

        return (feedBackList == null) ? 0: feedBackList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder{
        TextView tv_content,tv_userId,tv_date;
        RelativeLayout contentLayout;
        ImageView imgDropDown, imgDelete;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
//            tv_id = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtID);
            tv_content = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtContent);
            tv_userId = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtUserID);
            tv_date = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.txtDate);
            contentLayout = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.contentLayout);
            imgDropDown = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.imgDropDown);
            imgDelete = itemView.findViewById(hieuntph22081.fpoly.goidi.R.id.imgDelete);
        }
    }
}
