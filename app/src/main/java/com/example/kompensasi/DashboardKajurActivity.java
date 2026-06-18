package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;

public class DashboardKajurActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalPengajuan,
            tvPending;

    LinearLayout navHome,
            navValidasi,
            navLogout;

    RecyclerView recyclerPengajuan;
    ValidasiKajurAdapter adapter;
    ArrayList<PengajuanModel> list;

    com.google.android.material.card.MaterialCardView btnProfile;
    TextView tvNamaNavbar;
    de.hdodenhof.circleimageview.CircleImageView ivProfile;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_dashboard_kajur
        );

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        tvTotalPengajuan =
                findViewById(R.id.tvTotalPengajuan);

        tvPending =
                findViewById(R.id.tvPending);

        navHome =
                findViewById(R.id.navHome);

        navValidasi =
                findViewById(R.id.navValidasi);

        navLogout =
                findViewById(R.id.navLogout);

        recyclerPengajuan = findViewById(R.id.recyclerPengajuan);

        list = new ArrayList<>();
        adapter = new ValidasiKajurAdapter(this, list);
        recyclerPengajuan.setLayoutManager(new LinearLayoutManager(this));
        recyclerPengajuan.setAdapter(adapter);

        btnProfile = findViewById(R.id.btnProfile);
        tvNamaNavbar = findViewById(R.id.tvNamaNavbar);

        // ================= SESSION INFO =================
        android.content.SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String nama = preferences.getString("nama", "Kajur");
        String username = preferences.getString("username", "");
        tvNamaNavbar.setText(nama);

        ivProfile = findViewById(R.id.ivProfile);
        Cursor userCursor = databaseHelper.getUserByUsername(username);
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

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileUserActivity.class));
            overridePendingTransition(0, 0);
        });

        // ================= LOAD DASHBOARD =================

        loadDashboard();



        navValidasi.setOnClickListener(v -> {
            Intent intent = new Intent(this, ValidasiKajurActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // ================= HOME =================

        navHome.setOnClickListener(v -> {

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
                    "Ya",
                    (dialog, which) -> {

                        Intent intent =
                                new Intent(
                                        this,
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

            builder.show();
        });
    }

    // ================= LOAD DASHBOARD =================

    private void loadDashboard() {

        Cursor cursor = databaseHelper.getAllPengajuan();
        int total = 0;
        int pending = 0;
        if (cursor.moveToFirst()) {
            do {
                total++;
                String statusKajur = cursor.getString(cursor.getColumnIndexOrThrow("status_kajur"));
                if ("Pending".equals(statusKajur)) {
                    pending++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvTotalPengajuan.setText(String.valueOf(total));
        tvPending.setText(String.valueOf(pending));

        // Load list pengajuan yang pending
        list.clear();
        Cursor cursorKajur = databaseHelper.getPengajuanKajur();
        if (cursorKajur.moveToFirst()) {
            do {
                int idPengajuan = cursorKajur.getInt(0);
                int idMahasiswa = cursorKajur.getInt(1);
                int idKompensasi = cursorKajur.getInt(2);
                String namaMahasiswa = cursorKajur.getString(3);
                String kelas = cursorKajur.getString(4);
                String tugas = cursorKajur.getString(5);
                String tanggal = cursorKajur.getString(6);
                String bukti = cursorKajur.getString(7);
                String statusDosenPa = cursorKajur.getString(8);
                String statusSekjur = cursorKajur.getString(9);
                String statusKajur = cursorKajur.getString(10);
                String catatanPenolakan = cursorKajur.getString(11);
                String keterangan = cursorKajur.getString(12);

                if ("Pending".equals(statusKajur)) {
                    list.add(new PengajuanModel(
                            idPengajuan, idMahasiswa, idKompensasi, namaMahasiswa,
                            tugas, kelas, tanggal, bukti, statusDosenPa, statusSekjur,
                            statusKajur, catatanPenolakan, keterangan
                    ));
                }
            } while (cursorKajur.moveToNext());
        }
        cursorKajur.close();
        adapter.notifyDataSetChanged();
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
