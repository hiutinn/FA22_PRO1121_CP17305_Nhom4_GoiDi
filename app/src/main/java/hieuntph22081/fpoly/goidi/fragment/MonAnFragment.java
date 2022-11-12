package hieuntph22081.fpoly.goidi.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.MainActivity;
import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.adapter.LoaiMonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.adapter.MonAnRecycleAdapter;
import hieuntph22081.fpoly.goidi.model.LoaiMonAn;
import hieuntph22081.fpoly.goidi.model.MonAn;


public class MonAnFragment extends Fragment implements View.OnClickListener {
    private MonAn monAn;
    private MonAnRecycleAdapter adapter;
    private List<MonAn> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_CAMERA = 2;
    private ImageView img;

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
        actionButton = view.findViewById(R.id.btn_float_themMonAn);
        actionButton.setOnClickListener(this);
        actionButton.setColorFilter(Color.WHITE);
        recyclerView = view.findViewById(R.id.recycle_monAn);
        loadData();
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new MonAnRecycleAdapter(getActivity());
        adapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_float_themMonAn:
                diaLogAddMonAn();
                break;
        }
    }
    public void loadData(){
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_thai,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_nhatban,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_haisan,0));
        list.add(new MonAn(1,"Lẩu riêu cua",100000,R.drawable.dish_nuong,0));
    }
    public void diaLogAddMonAn(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_monan,null);
        img = view.findViewById(R.id.img_diaLogMonAn_anh);
        EditText ed_ten = view.findViewById(R.id.tv_ten_mon);
        EditText ed_gia = view.findViewById(R.id.tv_gia);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        img.setOnClickListener(v -> {
            requestPermission();
        });
        view.findViewById(R.id.btn_diaLogMonAn_luu).setOnClickListener(v -> {
            Log.e("size", list.size()+"");
            Log.e("id",img.getResources()+"");
        });
        view.findViewById(R.id.btn_diaLogMonAn_huy).setOnClickListener(v -> {
            alertDialog.cancel();
        });
        alertDialog.show();

    }public void diaLogGalleryOrCamera(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_gallery_or_camera,null);
        LinearLayout linear_camera = view.findViewById(R.id.linear_camera);
        LinearLayout linear_gallery = view.findViewById(R.id.linear_gallery);
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        linear_camera.setOnClickListener(v -> {
            cameraIntent();
            alertDialog.cancel();
        });
        linear_gallery.setOnClickListener(v -> {
            galleryIntent();
            alertDialog.cancel();
        });
        alertDialog.show();
    }
    public void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                diaLogGalleryOrCamera();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    public void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher_galeery.launch(Intent.createChooser(intent,"Select Picture"));
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher_camera.launch(intent);
        //startActivityForResult(intent, REQUEST_CAMERA);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE){
//
//        }
//        if(requestCode == REQUEST_CAMERA){
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                img.setImageBitmap(photo);
//                ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
//                img.setScaleType(scaleType);
//        }
//    }


    private ActivityResultLauncher<Intent> activityResultLauncher_galeery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK){
                        if(data == null){
                            return;
                        }
                        Uri uri =data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                            img.setImageBitmap(bitmap);
                            ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
                            img.setScaleType(scaleType);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> activityResultLauncher_camera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        img.setImageBitmap(photo);
                        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
                        img.setScaleType(scaleType);
                    }
                }
            }
    );
}