package hieuntph22081.fpoly.goidi;

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

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText txtMaTT,txtPassword;
    Button btnLogin;
    CheckBox chkRemember;
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
        String ma = txtMaTT.getText().toString();
        String pw = txtPassword.getText().toString();
        boolean status = chkRemember.isChecked();

        if (ma.length() == 0 || pw.length() == 0) {
            Toast.makeText(LoginActivity.this, "Không để trống các ô", Toast.LENGTH_SHORT).show();
        } else {
            boolean check = false;
//            for (ThuThu u : thuThuDAO.getAllThuThu()) {
//                if (ma.equals(u.getMaTT()) && pw.equals(u.getMatKhau())) {
//                    check = true;
//
//                    break;
//                }
//            }
            if (ma.equals("admin") && pw.equals("admin")) {
                    check = true;
                }
            if (check) {
                savePreference(ma,pw,!status,status);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTT", ma);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Thông tin sai", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void savePreference(String ma, String pw, boolean isLogout, boolean status) {
        SharedPreferences s = getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        if (!status) { // Khong luu
            editor.clear();
        } else { // luu
            editor.putString("U",ma);
            editor.putString("P",pw);
            editor.putBoolean("isLogout", isLogout);
            editor.putBoolean("CHK",status);
        }
        editor.commit();
    }

    List<Object> readPreference() {
        List<Object> ls = new ArrayList<>();
        SharedPreferences s = getSharedPreferences("MY_FILE",MODE_PRIVATE);
        ls.add(s.getString("U",""));
        ls.add(s.getString("P",""));
        ls.add(s.getBoolean("isLogout",true));
        ls.add(s.getBoolean("CHK",false));
        return ls;
    }
}