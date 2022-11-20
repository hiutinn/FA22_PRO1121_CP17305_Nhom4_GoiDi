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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.MainActivity;
import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.FeedBackAdapter;
import hieuntph22081.fpoly.goidi.model.FeedBack;
import hieuntph22081.fpoly.goidi.model.FeedBack;

public class FeedBackFragment extends Fragment {
    FloatingActionButton button;
    RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    FeedBackAdapter adapter;
    View view;
    DatabaseReference myRef;
    List<FeedBack> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        button= view.findViewById(R.id.floatAdd);
        recyclerView= view.findViewById(R.id.recyclerFeedback);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("feedbacks");

        RecyclerView.LayoutManager manager = new GridLayoutManager(requireActivity(),1);
        recyclerView.setLayoutManager(manager);

        adapter = new FeedBackAdapter(getContext());
        getFeedBackFromDB();
        adapter.setData(list);
        recyclerView.setAdapter(adapter);

//        button.setOnClickListener(v -> diaLogFeedback());
    }

    public void getFeedBackFromDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("feedbacks");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if (feedBack != null){
                    list.add(feedBack);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if(feedBack == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0 ; i < list.size(); i++){
                    if(feedBack.getId().equals(list.get(i).getId())){
                        list.set(i, feedBack);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if(feedBack == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0 ; i < list.size(); i++){
                    if(feedBack.getId().equals(list.get(i).getId())){
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
