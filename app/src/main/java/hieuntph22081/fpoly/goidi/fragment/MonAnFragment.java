package hieuntph22081.fpoly.goidi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.LoaiMonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.adapter.MonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.model.LoaiMonAn;
import hieuntph22081.fpoly.goidi.model.MonAn;


public class MonAnFragment extends Fragment {
    private MonAn monAn;
    private MonAnRecycleAdapter adapter;
    private List<MonAn> list = new ArrayList<>();
    private RecyclerView recyclerView;

    public MonAnFragment() {
        // Required empty public constructor
    }


    public static MonAnFragment newInstance() {
        MonAnFragment fragment = new MonAnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        adapter = new MonAnRecycleAdapter(getActivity());
        adapter.setData(list);
        recyclerView = view.findViewById(R.id.recycle_monAn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    public void loadData(){
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_thai,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_nhatban,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_haisan,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_nuong,0));
    }
}