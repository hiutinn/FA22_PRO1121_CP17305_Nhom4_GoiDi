package hieuntph22081.fpoly.goidi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import hieuntph22081.fpoly.goidi.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> list;

    EditText edtTenTv, edtPhone;
    Button btnSave, btnCancel;
    RadioButton rdoRoleAdmin, rdoRoleClient;
    RadioGroup rdoRole;

    public UserAdapter(Context context, List<User> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user,parent,false));
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

        holder.tvPhone.setText(user.getPhone());
        holder.imgDelete.setOnClickListener(v -> {
            deleteUser(user.getId());
        });

        holder.itemView.setOnLongClickListener(v -> {
            openDialog(user);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTV, tvRole, tvPhone;
        ImageView imgDelete;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTV = itemView.findViewById(R.id.tvTenTV);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public void deleteUser(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setCancelable(true);

        builder.setPositiveButton("yes", (dialog, which) -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            myRef.child(id).removeValue((error, ref)
                    -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
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
                    Toast.makeText(context, "Update thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    public int validate() {
        int check = 1;
        if (edtTenTv.getText().length() == 0 || edtPhone.getText().length() == 0) {
            Toast.makeText(context, "Ban phia nhap day du thong tin!", Toast.LENGTH_SHORT).show();
            check = -1;
        }

        if (!rdoRoleClient.isChecked() && !rdoRoleAdmin.isChecked()) {
            Toast.makeText(context, "Hãy chọn vai trò của người dùng!", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        return check;
    }
}
