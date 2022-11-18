package hieuntph22081.fpoly.goidi.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Table;

public class FragmentTable extends Fragment {
    FloatingActionButton button;
    RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    View view;
    DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table,container,false);
        return view;}
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            button= view.findViewById(R.id.floatAdd1);
            recyclerView= view.findViewById(R.id.recyclerTable);
            mDatabase = FirebaseDatabase.getInstance();
            myRef = mDatabase.getReference("Table");

            RecyclerView.LayoutManager manager = new GridLayoutManager(requireActivity(),1);
            recyclerView.setLayoutManager(manager);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){diaLogTable();}
            });
        }
        void diaLogTable(){
            Dialog dialog = new Dialog(requireActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_table);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.58);
            dialog.getWindow().setLayout(width, height);

            EditText edt_idtable;
            edt_idtable= dialog.findViewById(R.id.edt_idTable);
            Button btn_show = dialog.findViewById(R.id.btnShow);
            Button btn_no1 = dialog.findViewById(R.id.btn_No);
            btn_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idtable= Integer.parseInt(edt_idtable.getText().toString());
//                   Table table= new Table(idtable);

//                    myRef.setValue(table).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(requireActivity(), "table Successfuldsssxly", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            });

            btn_no1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


