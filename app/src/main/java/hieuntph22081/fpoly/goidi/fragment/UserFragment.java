package hieuntph22081.fpoly.goidi.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.UserAdapter;
import hieuntph22081.fpoly.goidi.model.User;

public class UserFragment extends Fragment {
    ListView lvThanhVien;
    ArrayList<User> list;
    UserAdapter adapter;
    User item;
    FloatingActionButton fab;
    Dialog dialog;
    EditText edMaTV, edTenTV, ednamSinh;
    Button btnsave, btncancel, btnUpdate;
    RadioButton rdoKhach, rdoAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        lvThanhVien = v.findViewById(R.id.lvUser);
        fab = v.findViewById(R.id.fabTV);
        capNhatLv();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getActivity(), 0);//bang = thi insert
            }
        });
        lvThanhVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                item = list.get(position);
                openDialog(getActivity(), 1);//=1 thi update

                return false;
            }
        });
        return v;
    }
    void capNhatLv() {
        list = new ArrayList<>();
        adapter = new UserAdapter(getActivity(), this, list);
        lvThanhVien.setAdapter(adapter);
        getListUser();
    }

    public void xoa(final String id) {
        User user = new User();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("bạn có muốn xóa không ?");
        builder.setCancelable(true);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_user");
                myRef.child(String.valueOf(user.getMaTV())).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.cancel();
                capNhatLv();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        builder.show();

    }

    protected void openDialog(final Context context, final int type) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.user_dialog);

        edMaTV = dialog.findViewById(R.id.edMaTV);
        edTenTV = dialog.findViewById(R.id.edTenTV);
        ednamSinh = dialog.findViewById(R.id.edNamSinh);
        btncancel = dialog.findViewById(R.id.btnCancelTV);
        btnsave = dialog.findViewById(R.id.btnSaveTV);
        btnUpdate = dialog.findViewById(R.id.btnUpdateTV);

        edMaTV.setEnabled(false);
        if (type != 0) {
            //cap nhat
            edMaTV.setText(String.valueOf(item.getMaTV()));
            edTenTV.setText(String.valueOf(item.getHoTen()));
            ednamSinh.setText(String.valueOf(item.getNamSinh()));
        }

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_user");
                item = new User();
                item.setHoTen(edTenTV.getText().toString());
                item.setNamSinh(ednamSinh.getText().toString());

                if (validate() > 0) {
                        //==- thi them nguoi dung
                        String pathObject = String.valueOf(item.getMaTV());
                        myRef.child(pathObject).setValue(item, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    capNhatLv();
                    dialog.dismiss();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_user");
                String newName = edTenTV.getText().toString().trim();
                user.setHoTen(newName);
                myRef.child(String.valueOf(user.getMaTV())).updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Update thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                capNhatLv();
            }
        });

        dialog.show();
    }

    public int validate() {
        int check = 1;
        if (edTenTV.getText().length() == 0 || ednamSinh.getText().length() == 0) {
            Toast.makeText(getContext(), "ban phia nhap day du thong tin", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        return check = 1;
    }

    private void getListUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_user");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    list.add(user);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if(user == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0 ; i < list.size(); i++){
                    if(user.getMaTV() == list.get(i).getMaTV()){
                        list.set(i, user);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0 ; i < list.size(); i++){
                    if(user.getMaTV() == list.get(i).getMaTV()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}