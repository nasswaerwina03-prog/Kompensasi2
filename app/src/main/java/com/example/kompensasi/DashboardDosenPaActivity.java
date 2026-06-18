package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.card.MaterialCardView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DashboardDosenPaActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalMahasiswa;

    TextView tvNamaNavbar,
            tvNamaDosen,
            tvNip,
            tvKelasDosen;

    RecyclerView recyclerMahasiswaPA;
    ArrayList<MahasiswaBimbinganModel> listMahasiswa;
    MahasiswaBimbinganAdapter adapterMahasiswa;

    LinearLayout navHome,
            navVerifikasi,
            navLogout;

    de.hdodenhof.circleimageview.CircleImageView ivProfile;
    android.widget.ImageView imgProfileDosen;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(
                R.layout.activity_dashboard_dosen_pa
        );

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        initView();

        SharedPreferences preferences =
                getSharedPreferences(
                        "SESSION",
                        MODE_PRIVATE
                );

        String namaDosen =
                preferences.getString(
                        "nama_dosen",
                        "-"
                );
        String kelas =
                preferences.getString(
                        "kelas_dosen",
                        "-"
                );
        String nip =
                preferences.getString(
                        "username_dosen",
                        "-"
                );

        tvNamaNavbar.setText(
                namaDosen
        );

        tvNamaDosen.setText(
                namaDosen
        );
        tvKelasDosen.setText(
                "Kelas : " + kelas
        );
        tvNip.setText(
                "NIP : " + nip
        );

        Cursor userCursor = databaseHelper.getUserByUsername(nip);
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
                    imgProfileDosen.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userCursor.close();
        }

        // ================= LOAD DATA =================

        loadDashboard();

        // ================= CLICK =================

        initMenu();

        initBottomNavbar();
    }

    // ================= INIT VIEW =================

    private void initView() {

        tvTotalMahasiswa =
                findViewById(
                        R.id.tvTotalMahasiswa
                );
        tvNamaNavbar =
                findViewById(R.id.tvNamaNavbar);

        tvNamaDosen =
                findViewById(R.id.tvNamaDosen);
        tvKelasDosen =
                findViewById(R.id.tvKelasDosen);
        tvNip =
                findViewById(R.id.tvNip);



        recyclerMahasiswaPA =
                findViewById(R.id.recyclerMahasiswaPA);

        ivProfile = findViewById(R.id.ivProfile);
        imgProfileDosen = findViewById(R.id.imgProfileDosen);

        MaterialCardView btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(DashboardDosenPaActivity.this, ProfileDosenActivity.class));
            overridePendingTransition(0, 0);
        });

        listMahasiswa = new ArrayList<>();
        adapterMahasiswa = new MahasiswaBimbinganAdapter(this, listMahasiswa);
        recyclerMahasiswaPA.setLayoutManager(new LinearLayoutManager(this));
        recyclerMahasiswaPA.setAdapter(adapterMahasiswa);

        navHome =
                findViewById(
                        R.id.navHome
                );

        navVerifikasi =
                findViewById(R.id.navVerifikasi);

        navLogout =
                findViewById(R.id.navLogout);
    }

    // ================= LOAD DASHBOARD =================

    private void loadDashboard() {

        try {

            SharedPreferences preferences =
                    getSharedPreferences(
                            "SESSION",
                            MODE_PRIVATE
                        );

            String kelas =
                    preferences.getString(
                            "kelas_dosen",
                            ""
                    );

            Cursor mahasiswaCursor =
                    databaseHelper.getMahasiswaBimbinganPa(
                            kelas
                    );

            if (mahasiswaCursor != null) {

                int totalMahasiswa =
                        mahasiswaCursor.getCount();

                tvTotalMahasiswa.setText(
                        String.valueOf(
                                totalMahasiswa
                        )
                );

                listMahasiswa.clear();
                if (mahasiswaCursor.moveToFirst()) {
                    do {
                        listMahasiswa.add(new MahasiswaBimbinganModel(
                                mahasiswaCursor.getInt(0),
                                mahasiswaCursor.getString(1),
                                mahasiswaCursor.getString(2),
                                mahasiswaCursor.getString(3),
                                mahasiswaCursor.getInt(4),
                                mahasiswaCursor.getString(5)
                        ));
                    } while (mahasiswaCursor.moveToNext());
                }
                adapterMahasiswa.notifyDataSetChanged();

                mahasiswaCursor.close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            Snackbar.make(
                    findViewById(android.R.id.content),
                    e.getMessage(),
                    Snackbar.LENGTH_LONG
            ).show();
        }
    }

    // ================= MENU =================
    private void initMenu() {
        // Menu removed
    }

    // ================= NAVBAR =================

    private void initBottomNavbar() {


        navHome.setOnClickListener(v -> {
            // Already in Home
        });



        navVerifikasi.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardDosenPaActivity.this, KelolaVerifikasiActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // ================= LOGOUT =================

        navLogout.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);

            builder.setTitle(
                    "Logout"
            );

            builder.setMessage(
                    "Yakin ingin keluar?"
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

                        snackbar.show();

                        Intent intent =
                                new Intent(
                                        DashboardDosenPaActivity.this,
                                        MainActivity.class
                                );

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);

                        finish();
                    }
            );

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

    private void showSnackbar(
            String message
    ) {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(
                                android.R.id.content
                        ),
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
