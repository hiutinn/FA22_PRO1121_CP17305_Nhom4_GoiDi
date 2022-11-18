package hieuntph22081.fpoly.goidi.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hieuntph22081.fpoly.goidi.R;
import hieuntph22081.fpoly.goidi.model.Order;

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
        timePickerDialog(calendar,ed_tuNgay);
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
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double doanhThu = 0;
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Order order = s.getValue(Order.class);
                        doanhThu += getTungDoanhThu(order);
                    }

                    tv_doanhThu.setText(formatCurrency(doanhThu));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private double getTungDoanhThu(Order order) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        double total = 0;
        //13/11 so sanh 17/11 =-1
        //17/11 so sanh 13/11 = 1
        try {
            Date fromDate = format.parse(ed_tuNgay.getText().toString());
            Date toDate = format.parse(ed_denNgay.getText().toString());
            Log.e("TAG2", ""+fromDate.toString() + "  " + toDate.toString());
            Date orderDate = format.parse(order.getDate());
            if (orderDate.compareTo(fromDate) >=0 && orderDate.compareTo(toDate) <= 0 && order.getStatus() == 2) {
                total += order.getTotal();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage() );
        }
        return total;
    }

    private void datePickerDialog(Calendar calendar, EditText editText){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), (view, year, month, dayOfMonth) -> {
            calendar.set(year,month,dayOfMonth);
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        },year,month,day);
        datePickerDialog.show();
    }

    private void timePickerDialog(Calendar mcurrentTime, EditText editText){
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute,true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public String formatCurrency(double money) {
        String pattern="###,###.### VNĐ";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(money);
    }
}