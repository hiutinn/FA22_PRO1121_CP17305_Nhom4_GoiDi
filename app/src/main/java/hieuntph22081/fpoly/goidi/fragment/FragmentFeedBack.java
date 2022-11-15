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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.FeedBack;

public class FragmentFeedBack extends Fragment {
    FloatingActionButton button;
    RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    View view;
    DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button= view.findViewById(R.id.floatAdd);
        recyclerView= view.findViewById(R.id.recyclerFeedback);
        mDatabase = FirebaseDatabase.getInstance();
         myRef = mDatabase.getReference("FeedBack");

        RecyclerView.LayoutManager manager = new GridLayoutManager(requireActivity(),1);
        recyclerView.setLayoutManager(manager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaLogFeedback();
            }
        });
    }
    void diaLogFeedback(){
        Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_feedback);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.58);
        dialog.getWindow().setLayout(width, height);

        EditText edt_id,edt_content,edt_userId,edt_date;
        edt_id = dialog.findViewById(R.id.edt_idFeedback);
        edt_content = dialog.findViewById(R.id.edt_content);
        edt_userId = dialog.findViewById(R.id.edt_idUser);
        edt_date = dialog.findViewById(R.id.edt_date);

        Button btn_add = dialog.findViewById(R.id.btnSend);
        Button btn_noTks = dialog.findViewById(R.id.btn_noThank);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(edt_id.getText().toString());
                String content = edt_content.getText().toString();
                int userId = Integer.parseInt(edt_userId.getText().toString());
                String date = edt_date.getText().toString();

                FeedBack feedBack = new FeedBack(id,content,date,userId);
                myRef.setValue(feedBack).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireActivity(), "Send Feedback Successfuldsssxly", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        btn_noTks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
