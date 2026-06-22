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

public class DashboardSekjurActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalPengajuan,
            tvPending,
            tvDisetujui;

    LinearLayout navHome,
            navValidasi,
            navLogout;

    RecyclerView recyclerPengajuan;
    ValidasiSekjurAdapter adapter;
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
                R.layout.activity_dashboard_sekjur
        );

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        tvTotalPengajuan =
                findViewById(R.id.tvTotalPengajuan);

        tvPending =
                findViewById(R.id.tvPending);

        tvDisetujui =
                findViewById(R.id.tvDisetujui);

        navHome =
                findViewById(R.id.navHome);

        navValidasi =
                findViewById(R.id.navValidasi);

        navLogout =
                findViewById(R.id.navLogout);

        recyclerPengajuan = findViewById(R.id.recyclerPengajuan);

        list = new ArrayList<>();
        adapter = new ValidasiSekjurAdapter(this, list, this::loadDashboard);
        recyclerPengajuan.setLayoutManager(new LinearLayoutManager(this));
        recyclerPengajuan.setAdapter(adapter);

        btnProfile = findViewById(R.id.btnProfile);
        tvNamaNavbar = findViewById(R.id.tvNamaNavbar);

        // ================= BOTTOM NAVBAR =================
        navHome = findViewById(R.id.navHome);
        navValidasi = findViewById(R.id.navValidasi);
        navLogout = findViewById(R.id.navLogout);

        // ================= SESSION INFO =================
        android.content.SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String nama = preferences.getString("nama", "Sekjur");
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
            Intent intent = new Intent(this, ValidasiSekjurActivity.class);
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

        Cursor cursor = databaseHelper.getPengajuanSekjur();
        int total = 0;
        int pending = 0;
        int disetujui = 0;

        if (cursor.moveToFirst()) {
            do {
                total++;
                String statusSekjur = cursor.getString(cursor.getColumnIndexOrThrow("status_sekjur"));
                if ("Pending".equals(statusSekjur)) {
                    pending++;
                } else if ("Disetujui".equals(statusSekjur)) {
                    disetujui++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvTotalPengajuan.setText(String.valueOf(total));
        tvPending.setText(String.valueOf(pending));
        tvDisetujui.setText(String.valueOf(disetujui));

        // Load list pengajuan yang pending
        list.clear();
        Cursor cursorSekjur = databaseHelper.getPengajuanSekjur();
        if (cursorSekjur.moveToFirst()) {
            do {
                int idPengajuan = cursorSekjur.getInt(0);
                int idMahasiswa = cursorSekjur.getInt(1);
                int idKompensasi = cursorSekjur.getInt(2);
                String namaMahasiswa = cursorSekjur.getString(3);
                String kelas = cursorSekjur.getString(4);
                String tugas = cursorSekjur.getString(5);
                String tanggal = cursorSekjur.getString(6);
                String bukti = cursorSekjur.getString(7);
                String statusDosenPa = cursorSekjur.getString(8);
                String statusSekjur = cursorSekjur.getString(9);
                String statusKajur = cursorSekjur.getString(10);
                String catatanPenolakan = cursorSekjur.getString(11);
                String keterangan = cursorSekjur.getString(12);

                if ("Pending".equals(statusSekjur)) {
                    list.add(new PengajuanModel(
                            idPengajuan, idMahasiswa, idKompensasi, namaMahasiswa,
                            tugas, kelas, tanggal, bukti, statusDosenPa, statusSekjur,
                            statusKajur, catatanPenolakan, keterangan
                    ));
                }
            } while (cursorSekjur.moveToNext());
        }
        cursorSekjur.close();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        
        // Refresh data
        loadDashboard();
        
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
