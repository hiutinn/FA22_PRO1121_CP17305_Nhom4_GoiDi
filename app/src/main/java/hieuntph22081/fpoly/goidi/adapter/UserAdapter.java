package hieuntph22081.fpoly.goidi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.fragment.UserFragment;
import hieuntph22081.fpoly.goidi.model.Order;
import hieuntph22081.fpoly.goidi.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    private Context context;
    private List<User> list;
    private List<User> userList;
    EditText edtTenTv, edtPhone;
    Button btnSave, btnCancel;
    RadioButton rdoRoleAdmin, rdoRoleClient;
    RadioGroup rdoRole;
    int layout;

    public UserAdapter(Context context, int layout) {
        this.context=context;
        this.layout = layout;
    }
    public void setData(List<User> list1) {
        this.list=list1;
        this.userList = list1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = list.get(position);
        holder.tvTenTV.setText(user.getName());
        if (user.getRole() == 0) {
            holder.tvRole.setText("Admin");
            holder.tvRole.setTextColor(Color.RED);
        } else {
            holder.tvRole.setText("Client");
            holder.tvRole.setTextColor(Color.BLUE);
        }
        //holder.tvSolan.setText("S??? l???n ?????t b??n: "+ user.getSoLan());

        holder.tvPhone.setText(user.getPhone());
        holder.imgDelete.setOnClickListener(v -> {
            deleteUser(user.getId());
        });

        holder.imgEdit.setOnClickListener(v -> {
            openDialog(user);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                List<User> users = new ArrayList<>();

                if(search.isEmpty()){
                    list = userList;
                }else{
                    for(User user : userList){
                        if(user.getPhone().toLowerCase().contains(search)){
                            users.add(user);
                        }
                    }
                    list = users;
                }
                FilterResults results = new FilterResults();
                results.values = list;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTV, tvRole, tvPhone;
        ImageView imgDelete, imgEdit;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTV = itemView.findViewById(R.id.tvTenTV);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }

    public void deleteUser(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("B???n c?? mu???n x??a kh??ng ?");
        builder.setCancelable(true);

        builder.setPositiveButton("yes", (dialog, which) -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            myRef.child(id).removeValue((error, ref)
                    -> openSuccessDialog("X??a th??nh c??ng"));
            dialog.cancel();
            notifyDataSetChanged();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    protected void openDialog(User user) {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.58);
        dialog.getWindow().setLayout(width, height);

        TextView dialogUserTitle = dialog.findViewById(R.id.dialogUserTitle);
        dialogUserTitle.setText("S???a User");

        edtTenTv = dialog.findViewById(R.id.edtTenTV);
        edtPhone = dialog.findViewById(R.id.edtPhone);
        rdoRoleAdmin = dialog.findViewById(R.id.rdoRoleAdmin);
        rdoRoleClient = dialog.findViewById(R.id.rdoRoleClient);
        rdoRole = dialog.findViewById(R.id.rdoRole);
        btnCancel = dialog.findViewById(R.id.btnCancelTV);
        btnSave = dialog.findViewById(R.id.btnSaveTV);

        edtTenTv.setText(String.valueOf(user.getName()));
        edtPhone.setText(String.valueOf(user.getPhone()));
        if (user.getRole() == 0) {
            rdoRoleAdmin.setChecked(true);
        } else {
            rdoRoleClient.setChecked(true);
        }


        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            if (validate() > 0) {
                user.setName(edtTenTv.getText().toString());
                user.setPhone(edtPhone.getText().toString());
                if (rdoRoleAdmin.isChecked()) {
                    user.setRole(0);
                } else if (rdoRoleClient.isChecked()) {
                    user.setRole(1);
                }

                myRef.child(String.valueOf(user.getId())).updateChildren(user.toMap(), (error, ref) -> {
                    openSuccessDialog("C???p nh???p th??nh c??ng");
                    dialog.dismiss();
                });
                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    public int validate() {
        if (edtTenTv.getText().length() == 0 || edtPhone.getText().length() == 0) {
            openFailDialog("B???n ph???i nh???p ????? th??ng tin");
            return  -1;
        }

        if (!rdoRoleClient.isChecked() && !rdoRoleAdmin.isChecked()) {
            openFailDialog("H??y ch???n vai tr?? c???a ng?????i d??ng");
            return  -1;
        }
        return 1;
    }

    public void openSuccessDialog (String text) {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvNotifyContent = dialog.findViewById(R.id.tvNotifyContent);
        tvNotifyContent.setText(text);
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void openFailDialog (String text) {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fail_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvNotifyContent = dialog.findViewById(R.id.tvNotifyContent);
        tvNotifyContent.setText(text);
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();
    }
}
