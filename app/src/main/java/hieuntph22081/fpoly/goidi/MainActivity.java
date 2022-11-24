package hieuntph22081.fpoly.goidi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import hieuntph22081.fpoly.goidi.fragment.DoanhThuFragment;
import hieuntph22081.fpoly.goidi.fragment.FeedBackFragment;
import hieuntph22081.fpoly.goidi.fragment.MonAnFragment;
import hieuntph22081.fpoly.goidi.fragment.OrderFragment;
import hieuntph22081.fpoly.goidi.fragment.TableFragment;
import hieuntph22081.fpoly.goidi.fragment.Top10DishFragment;
import hieuntph22081.fpoly.goidi.fragment.Top10UserFragment;
import hieuntph22081.fpoly.goidi.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBar);
        frameLayout = findViewById(R.id.frameLayout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new OrderFragment());
        this.setTitle(R.string.nav_order);
        navigationView.getMenu().getItem(0).setChecked(true);

        SpannableString s = new SpannableString("Đăng xuất");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        navigationView.getMenu().getItem(6).getSubMenu().getItem(1).setTitle(s);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.dangXuat:
                savePreference("admin","admin",true,false);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.quanLyFeedback:
                replaceFragment(new FeedBackFragment());
                this.setTitle(R.string.nav_feedback);
                break;
            case R.id.quanLyUser:
                replaceFragment(new UserFragment());
                this.setTitle(R.string.nav_user);
                break;
            case R.id.quanLyOrder:
                replaceFragment(new OrderFragment());
                this.setTitle(R.string.nav_order);
                break;
            case R.id.quanLyDish:
                replaceFragment(new MonAnFragment());
                this.setTitle(R.string.nav_dish);
                break;
            case R.id.top10User:
                replaceFragment(new Top10UserFragment());
                this.setTitle("Khách hàng tiềm năng");
                break;
            case R.id.top10Dish:r:
                replaceFragment(new Top10DishFragment());
                this.setTitle("Top 10 món ăn");
                break;
            case R.id.doanhThu:
                replaceFragment(new DoanhThuFragment());
                this.setTitle("Quản lý doanh thu");
                break;
            case R.id.quanLyTable:
                replaceFragment(new TableFragment());
                this.setTitle("Quản lý bàn");
                break;
        }
        drawerLayout.closeDrawer(navigationView);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed(); // Thoát
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

}