package hieuntph22081.fpoly.goidi.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hieuntph22081.fpoly.goidi.R;

public class DoanhThuFragment extends Fragment {
    private TextInputEditText ed_tuNgay,ed_denNgay;
    private TextView tv_doanhThu;
    private int day,month,year;

    public DoanhThuFragment() {
        // Required empty public constructor
    }


    public static DoanhThuFragment newInstance() {
        DoanhThuFragment fragment = new DoanhThuFragment();
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
        return inflater.inflate(R.layout.fragment_doanh_thu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed_tuNgay = view.findViewById(R.id.ed_DT_tuNgay);
        ed_denNgay = view.findViewById(R.id.ed_DT_denNgay);
        tv_doanhThu = view.findViewById(R.id.tv_doanhThu);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        ed_tuNgay.setOnClickListener(v -> {
            datePickerDialog(calendar,ed_tuNgay);
        });
        ed_denNgay.setOnClickListener(v -> {
            datePickerDialog(calendar,ed_denNgay);
        });
        view.findViewById(R.id.btn_doanhThu).setOnClickListener(v -> {

        });
    }
    private void datePickerDialog(Calendar calendar, EditText editText){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }
}