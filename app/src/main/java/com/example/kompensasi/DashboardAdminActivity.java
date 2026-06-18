package com.example.kompensasi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.database.Cursor;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.content.SharedPreferences;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class DashboardAdminActivity extends AppCompatActivity {

    // ================= CHART =================

    private BarChart barChart;

    // ================= LOGOUT =================

    private LinearLayout navLogout;

    // ================= NAVIGATION =================

    private LinearLayout navRumah,
            navUser,
            navMahasiswa,
            navKompensasi;

    // ================= MENU CARD =================

    private MaterialCardView cardUser,
            cardMahasiswa,
            cardKompensasi;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        // ================= INIT =================

        initView();
        loadDashboardData();


        // ================= CLICK =================

        initNavigation();

        initMenu();

        initLogout();
    }

    // ================= INIT VIEW =================

    private void initView() {

        tvTotalMahasiswa =
                findViewById(R.id.tvTotalMahasiswa);

        tvTotalKompensasi =
                findViewById(R.id.tvTotalKompensasi);

        navLogout =
                findViewById(R.id.navLogout);

        navRumah =
                findViewById(R.id.navRumah);

        navUser =
                findViewById(R.id.navUser);

        navMahasiswa =
                findViewById(R.id.navMahasiswa);

        navKompensasi =
                findViewById(R.id.navKompensasi);

        cardUser =
                findViewById(R.id.cardUser);

        cardMahasiswa =
                findViewById(R.id.cardMahasiswa);

        cardKompensasi =
                findViewById(R.id.cardKompensasi);

        de.hdodenhof.circleimageview.CircleImageView ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> {
            startActivity(new Intent(DashboardAdminActivity.this, ProfileAdminActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor userCursor = dbHelper.getUserByUsername(username);
        if (userCursor != null && userCursor.moveToFirst()) {
            String fotoProfil = null;
            try {
                fotoProfil = userCursor.getString(userCursor.getColumnIndexOrThrow("foto_profil"));
            } catch (Exception e) {}
            if (fotoProfil != null && !fotoProfil.isEmpty()) {
                try {
                    byte[] decodedString = Base64.decode(fotoProfil, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivProfile.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userCursor.close();
        }
    }

// ================= MENU =================

    private void initMenu() {

        cardUser.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            UserActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });

        cardMahasiswa.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            KelolaMahasiswaActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });

        // ================= KOMPENSASI =================

        cardKompensasi.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            KelolaKompensasiActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });
    }

// ================= NAVIGATION =================

    private void initNavigation() {

        navRumah.setOnClickListener(v -> {

            showSnackbar(
                    "Dashboard Admin"
            );
        });

        navUser.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            UserActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });

        navMahasiswa.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            KelolaMahasiswaActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });

        // ================= NAV KOMPENSASI =================

        navKompensasi.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardAdminActivity.this,
                            KelolaKompensasiActivity.class
                    )
            );

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });
    }
    // ================= LOGOUT =================

    private void initLogout() {

        navLogout.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);

            builder.setTitle("Logout");

            builder.setMessage(
                    "Yakin ingin keluar dari dashboard?"
            );

            builder.setPositiveButton(
                    "Logout",
                    (dialog, which) -> {

                        Snackbar snackbar =
                                Snackbar.make(
                                        findViewById(
                                                android.R.id.content
                                        ),
                                        "Berhasil logout 👋",
                                        Snackbar.LENGTH_LONG
                                );

                        snackbar.setBackgroundTint(
                                Color.parseColor("#B1456E")
                        );

                        snackbar.setTextColor(
                                Color.WHITE
                        );

                        snackbar.setAction(
                                "OK",
                                view -> {

                                }
                        );

                        snackbar.setActionTextColor(
                                Color.WHITE
                        );

                        snackbar.show();

                        new Handler().postDelayed(() -> {

                            Intent intent =
                                    new Intent(
                                            DashboardAdminActivity.this,
                                            MainActivity.class
                                    );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);

                            overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                            );

                            finish();

                        }, 1200);

                    });

            builder.setNegativeButton(
                    "Batal",
                    null
            );

            AlertDialog dialog =
                    builder.create();

            dialog.show();

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setTextColor(
                    Color.parseColor("#B1456E")
            );

            dialog.getButton(
                    AlertDialog.BUTTON_NEGATIVE
            ).setTextColor(
                    Color.GRAY
            );
        });
    }

    // ================= SNACKBAR =================

    private void showSnackbar(String message) {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_SHORT
                );

        snackbar.setBackgroundTint(
                Color.parseColor("#B1456E")
        );

        snackbar.setTextColor(
                Color.WHITE
        );

        snackbar.show();
    }
// ================= DASHBOARD DATA =================

    private void loadDashboardData() {

        DatabaseHelper dbHelper =
                new DatabaseHelper(this);

        // ================= TOTAL MAHASISWA =================

        Cursor mahasiswaCursor =
                dbHelper.getAllMahasiswa();

        int totalMahasiswa =
                mahasiswaCursor.getCount();

        tvTotalMahasiswa.setText(
                String.valueOf(totalMahasiswa)
        );

        mahasiswaCursor.close();

        // ================= TOTAL KOMPENSASI =================

        Cursor kompensasiCursor =
                dbHelper.getAllKompensasi();

        int totalKompensasi =
                kompensasiCursor.getCount();

        tvTotalKompensasi.setText(
                String.valueOf(totalKompensasi)
        );

        kompensasiCursor.close();
    }
    // ================= CHART =================

    private TextView tvTotalMahasiswa,
            tvTotalKompensasi;
    @Override
    protected void onResume() {
        super.onResume();
        android.content.SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String uname = preferences.getString("username", "");
        if (uname != null && !uname.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            android.database.Cursor userCursor = dbHelper.getUserByUsername(uname);
            if (userCursor != null && userCursor.moveToFirst()) {
                String fotoProfil = null;
                try {
                    fotoProfil = userCursor.getString(userCursor.getColumnIndexOrThrow("foto_profil"));
                } catch (Exception e) {}
                if (fotoProfil != null && !fotoProfil.isEmpty()) {
                    try {
                        byte[] decodedString = android.util.Base64.decode(fotoProfil, android.util.Base64.DEFAULT);
                        android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        de.hdodenhof.circleimageview.CircleImageView ivProfile = findViewById(R.id.ivProfile);
                        if (ivProfile != null) {
                            ivProfile.setImageBitmap(decodedByte);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(userCursor != null) userCursor.close();
        }
    }
}
