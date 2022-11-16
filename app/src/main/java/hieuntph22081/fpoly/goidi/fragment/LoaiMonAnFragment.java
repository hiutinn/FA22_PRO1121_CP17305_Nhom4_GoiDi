package hieuntph22081.fpoly.goidi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.LoaiMonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.model.LoaiMonAn;

public class LoaiMonAnFragment extends Fragment {
    private LoaiMonAnRecycleAdapter adapter;
    private LoaiMonAn loaiMonAn;
    private List<LoaiMonAn> list = new ArrayList<>();
    private RecyclerView recyclerView;
    public LoaiMonAnFragment() {
        // Required empty public constructor
    }


    public static LoaiMonAnFragment newInstance() {
        LoaiMonAnFragment fragment = new LoaiMonAnFragment();
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
        return inflater.inflate(R.layout.fragment_loai_mon_an, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        recyclerView = view.findViewById(R.id.recycle_loaiMonAn);
        adapter = new LoaiMonAnRecycleAdapter(getActivity());
        adapter.setData(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    public void loadData(){
        list.add(new LoaiMonAn(1,R.drawable.dish_haisan,"Hải Sản"));
        list.add(new LoaiMonAn(2,R.drawable.dish_nhatban,"Món Nhật"));
        list.add(new LoaiMonAn(3,R.drawable.dish_nuong,"Món Nướng"));
        list.add(new LoaiMonAn(4,R.drawable.dish_thai,"Món Thái"));
    }
}