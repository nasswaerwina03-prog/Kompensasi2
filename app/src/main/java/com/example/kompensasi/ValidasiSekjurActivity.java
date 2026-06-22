package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ValidasiSekjurActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotal,
            tvPending,
            tvApproved,
            tvRejected;

    LinearLayout btnFilterPending,
            btnFilterApproved,
            btnFilterAutoApprove;

    EditText etSearchValidasi;
    String currentFilter = "Semua";

    RecyclerView recyclerValidasi;

    LinearLayout navHome,
            navValidasi,
            navLogout;

    // ================= LIST =================

    ArrayList<PengajuanModel> list;

    ValidasiSekjurAdapter adapter;

    // ================= PAGINATION =================
    com.google.android.material.button.MaterialButton btnPrevPage, btnNextPage;
    TextView tvPageInfo;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 50;
    private int totalFilteredData = 0;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_validasi_sekjur
        );

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        tvTotal =
                findViewById(R.id.tvTotal);

        tvPending =
                findViewById(R.id.tvPending);

        tvApproved =
                findViewById(R.id.tvApproved);

        tvRejected =
                findViewById(R.id.tvRejected);

        btnFilterPending = findViewById(R.id.btnFilterPending);
        btnFilterApproved = findViewById(R.id.btnFilterApproved);
        btnFilterAutoApprove = findViewById(R.id.btnFilterAutoApprove);

        etSearchValidasi = findViewById(R.id.etSearchValidasi);

        recyclerValidasi =
                findViewById(R.id.recyclerValidasi);

        navHome =
                findViewById(R.id.navHome);

        navValidasi =
                findViewById(R.id.navValidasi);

        navLogout =
                findViewById(R.id.navLogout);

        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        tvPageInfo = findViewById(R.id.tvPageInfo);

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                applyFilter();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            int maxPage = (totalFilteredData - 1) / ITEMS_PER_PAGE;
            if (currentPage < maxPage) {
                currentPage++;
                applyFilter();
            }
        });

        // ================= RECYCLER =================

        list =
                new ArrayList<>();

        adapter =
                new ValidasiSekjurAdapter(
                        this,
                        list,
                        this::loadData
                );

        recyclerValidasi.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerValidasi.setAdapter(adapter);

        // ================= LOAD =================

        loadData();

        etSearchValidasi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentPage = 0;
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnFilterPending.setOnClickListener(v -> {
            currentFilter = "Pending";
            currentPage = 0;
            applyFilter();
        });

        btnFilterApproved.setOnClickListener(v -> {
            currentFilter = "Disetujui";
            currentPage = 0;
            applyFilter();
        });

        btnFilterAutoApprove.setOnClickListener(v -> {
            currentFilter = "AutoApprove";
            currentPage = 0;
            applyFilter();
        });

        tvTotal.setOnClickListener(v -> {
            currentFilter = "Semua";
            currentPage = 0;
            applyFilter();
        });

        // ================= HOME =================

        navHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            DashboardSekjurActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            overridePendingTransition(
                    0,
                    0
            );
        });

        // ================= VALIDASI =================

        navValidasi.setOnClickListener(v -> {

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

    // ================= LOAD =================

    private void loadData() {

        list.clear();
        int pending = 0;

        int approved = 0;
        Cursor cursor =
                databaseHelper
                        .getPengajuanSekjur();

        int rejected = 0;

        if (cursor.moveToFirst()) {

            do {
                int idPengajuan =
                        cursor.getInt(0);

                int idMahasiswa =
                        cursor.getInt(1);

                int idKompensasi =
                        cursor.getInt(2);

                String namaMahasiswa =
                        cursor.getString(3);

                String kelas =
                        cursor.getString(4);

                String tugas =
                        cursor.getString(5);

                String tanggal =
                        cursor.getString(6);

                String bukti =
                        cursor.getString(7);

                String statusDosenPa =
                        cursor.getString(8);

                String statusSekjur =
                        cursor.getString(9);

                String statusKajur =
                        cursor.getString(10);

                String catatanPenolakan =
                        cursor.getString(11);

                String keterangan =
                        cursor.getString(12);

                if (keterangan != null && keterangan.contains("Otomatis Disetujui")) {
                    rejected++; // count auto approve here
                } else if ("Pending".equals(statusSekjur)) {
                    pending++;
                } else if ("Disetujui".equals(statusSekjur)) {
                    approved++;
                }

                list.add(
                        new PengajuanModel(
                                idPengajuan,
                                idMahasiswa,
                                idKompensasi,
                                namaMahasiswa,
                                tugas,
                                kelas,
                                tanggal,
                                bukti,
                                statusDosenPa,
                                statusSekjur,
                                statusKajur,
                                catatanPenolakan,
                                keterangan
                        )
                );

            } while (cursor.moveToNext());
        }

        tvTotal.setText(
                String.valueOf(
                        approved + rejected
                )
        );
        tvPending.setText(
                String.valueOf(pending)
        );

        tvApproved.setText(
                String.valueOf(approved)
        );

        tvRejected.setText(
                String.valueOf(rejected)
        );

        cursor.close();
        
        // Memanggil applyFilter() agar list di-paginate dan adapter di-set dengan pagedList
        applyFilter();
    }

    private void applyFilter() {

        ArrayList<PengajuanModel> filtered =
                new ArrayList<>();
                
        String keyword = etSearchValidasi.getText().toString().toLowerCase();

        for (PengajuanModel model : list) {
            boolean isAutoApprove = model.getKeterangan() != null && model.getKeterangan().contains("Otomatis Disetujui");
            boolean matchKeyword = model.getNamaMahasiswa().toLowerCase().contains(keyword);
            
            if (!matchKeyword) continue;
            
            if ("Semua".equals(currentFilter)) {
                filtered.add(model);
            } else if ("AutoApprove".equals(currentFilter)) {
                if (isAutoApprove) {
                    filtered.add(model);
                }
            } else if ("Disetujui".equals(currentFilter)) {
                if ("Disetujui".equals(model.getStatusSekjur()) && !isAutoApprove) {
                    filtered.add(model);
                }
            } else if ("Pending".equals(currentFilter)) {
                if ("Pending".equals(model.getStatusSekjur()) && !isAutoApprove) {
                    filtered.add(model);
                }
            }
        }

        totalFilteredData = filtered.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalFilteredData / ITEMS_PER_PAGE));
        tvPageInfo.setText("Halaman " + (currentPage + 1) + " dari " + totalPages);

        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, totalFilteredData);
        ArrayList<PengajuanModel> pagedList = new ArrayList<>();
        if (start < totalFilteredData) {
            pagedList = new ArrayList<>(filtered.subList(start, end));
        }

        adapter =
                new ValidasiSekjurAdapter(
                        this,
                        pagedList,
                        this::loadData
                );

        recyclerValidasi.setAdapter(adapter);
    }
}