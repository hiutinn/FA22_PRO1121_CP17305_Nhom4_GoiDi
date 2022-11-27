package hieuntph22081.fpoly.goidi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidi.model.User;

public class LoginActivity extends AppCompatActivity {
    EditText txtMaTT,txtPassword;
    Button btnLogin;
    CheckBox chkRemember;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        txtMaTT = findViewById(R.id.loginTxtMaTT);
        txtPassword = findViewById(R.id.loginTxtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        chkRemember = findViewById(R.id.chkRemember);
        List<Object> chkList;
        chkList = readPreference();
        if (chkList.size()>0) {
            if (!Boolean.parseBoolean(chkList.get(2).toString())) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTT", chkList.get(0).toString());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                finish();
            } else {
                txtMaTT.setText(chkList.get(0).toString());
                txtPassword.setText(chkList.get(1).toString());
                chkRemember.setChecked(Boolean.parseBoolean(chkList.get(3).toString()));
            }
        }

        btnLogin.setOnClickListener(v -> login());
    }

    void login() {
        String phone = txtMaTT.getText().toString();
        String pw = txtPassword.getText().toString();
        boolean status = chkRemember.isChecked();

        if (phone.length() == 0 || pw.length() == 0) {
            Snackbar.make(findViewById(R.id.loginLayout), "Không để trống thông tin!", Snackbar.LENGTH_SHORT).show();
        } else {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean check = false;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                            if (phone.equals(user.getPhone()) && pw.equals(user.getPassword()) && user.getRole() == 1) {
                                check = true;
                                break;
                            }
                    }

                    if (check) {
                        savePreference(userId,phone,pw,!status,status);
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", userId);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    } else
                        Snackbar.make(findViewById(R.id.loginLayout), "Thông tin đăng nhập không chính xác!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    void savePreference(String userId, String phone, String pw, boolean isLogout, boolean status) {
        SharedPreferences s = getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        if (!status) { // Khong luu
            editor.clear();
        } else { // luu
            editor.putString("userId",userId);
            editor.putString("phone",phone);
            editor.putString("password",pw);
            editor.putBoolean("isLogout", isLogout);
            editor.putBoolean("CHK",status);
        }
        editor.commit();
    }

    List<Object> readPreference() {
        List<Object> ls = new ArrayList<>();
        SharedPreferences s = getSharedPreferences("MY_FILE",MODE_PRIVATE);
        ls.add(s.getString("userId",""));
        ls.add(s.getString("phone",""));
        ls.add(s.getString("password",""));
        ls.add(s.getBoolean("isLogout",true));
        ls.add(s.getBoolean("CHK",false));
        return ls;
    }
}