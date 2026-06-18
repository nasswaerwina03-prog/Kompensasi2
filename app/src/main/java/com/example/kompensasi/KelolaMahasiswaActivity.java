package com.example.kompensasi;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import org.apache.poi.ss.usermodel.DataFormatter;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class KelolaMahasiswaActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalMahasiswa;

    LinearLayout navRumah,
            navMahasiswa,
            navUser,
            navKompensasi,
            navLogout;

    MaterialButton btnTambahMahasiswa;
    MaterialButton btnImportExcel;

    EditText etSearchMahasiswa;

    RecyclerView recyclerMahasiswa;

    // ================= LIST =================

    ArrayList<MahasiswaModel> mahasiswaList;
    ActivityResultLauncher<Intent> excelLauncher;

    MahasiswaAdapter adapter;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_kelola_mahasiswa
        );
        excelLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            if (result.getResultCode() == RESULT_OK &&
                                    result.getData() != null) {

                                try {

                                    Uri uri =
                                            result.getData().getData();

                                    InputStream inputStream =
                                            getContentResolver()
                                                    .openInputStream(uri);

                                    boolean success =
                                            databaseHelper
                                                    .importMahasiswaFromExcel(
                                                            inputStream
                                                    );

                                    if (success) {

                                        loadMahasiswaData("");

                                        loadTotalMahasiswa();

                                        Toast.makeText(
                                                this,
                                                "Import berhasil",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    } else {

                                        Toast.makeText(
                                                this,
                                                "Import gagal",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();

                                    Toast.makeText(
                                            this,
                                            "Terjadi kesalahan",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                );
        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        tvTotalMahasiswa =
                findViewById(R.id.tvTotalMahasiswa);
        btnImportExcel =
                findViewById(R.id.btnImportExcel);
        btnTambahMahasiswa =
                findViewById(R.id.btnTambahMahasiswa);

        etSearchMahasiswa =
                findViewById(R.id.etSearchMahasiswa);

        recyclerMahasiswa =
                findViewById(R.id.recyclerMahasiswa);

        navRumah =
                findViewById(R.id.navRumah);

        navMahasiswa =
                findViewById(R.id.navMahasiswa);

        navUser =
                findViewById(R.id.navUser);

        navKompensasi =
                findViewById(R.id.navKompensasi);

        navLogout =
                findViewById(R.id.navLogout);

        // ================= RECYCLER =================

        mahasiswaList =
                new ArrayList<>();

        android.content.SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String role = preferences.getString("role", "admin");

        adapter =
                new MahasiswaAdapter(
                        this,
                        mahasiswaList,
                        role
                );

        recyclerMahasiswa.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerMahasiswa.setAdapter(adapter);

        // ================= LOAD =================

        loadTotalMahasiswa();

        loadMahasiswaData("");

        // ================= BUTTON TAMBAH =================

        btnTambahMahasiswa.setOnClickListener(v -> {

            showTambahMahasiswaDialog();
        });
        btnImportExcel.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_GET_CONTENT
                    );

            intent.setType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            excelLauncher.launch(intent);
        });

        // ================= SEARCH =================

        etSearchMahasiswa.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {

                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        loadMahasiswaData(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {

                    }
                }
        );

        // ================= NAVIGATION =================

        navRumah.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            KelolaMahasiswaActivity.this,
                            DashboardAdminActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );


            finish();
        });

        navUser.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            KelolaMahasiswaActivity.this,
                            UserActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );


            finish();
        });

        navMahasiswa.setOnClickListener(v -> {

        });

        navKompensasi.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            KelolaMahasiswaActivity.this,
                            KelolaKompensasiActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            finish();
        });

        // ================= LOGOUT =================

        navLogout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            KelolaMahasiswaActivity.this,
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }

    // ================= TOTAL MAHASISWA =================

    private void loadTotalMahasiswa() {

        Cursor cursor =
                databaseHelper.getAllMahasiswa();

        tvTotalMahasiswa.setText(
                String.valueOf(
                        cursor.getCount()
                )
        );

        cursor.close();
    }

    // ================= LOAD DATA =================

    private void loadMahasiswaData(
            String keyword
    ) {

        mahasiswaList.clear();

        Cursor cursor =
                databaseHelper.searchMahasiswa(
                        keyword
                );

        if (cursor.moveToFirst()) {

            int fotoIndex = -1;
            try {
                fotoIndex = cursor.getColumnIndexOrThrow("foto_profil");
            } catch (Exception e) {}

            do {

                String foto = fotoIndex != -1 ? cursor.getString(fotoIndex) : null;

                mahasiswaList.add(
                        new MahasiswaModel(
                                cursor.getInt(0),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                foto
                        )
                );

            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();

        cursor.close();
    }

    // ================= TAMBAH MAHASISWA =================

    private void showTambahMahasiswaDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Tambah Mahasiswa"
        );

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                40,
                30,
                40,
                10
        );

        // ================= NIM =================

        EditText etNim =
                new EditText(this);

        etNim.setHint("NIM");

        layout.addView(etNim);

        // ================= NAMA =================

        EditText etNama =
                new EditText(this);

        etNama.setHint("Nama Mahasiswa");

        layout.addView(etNama);

        // ================= SPINNER KELAS =================

        Spinner spKelas =
                new Spinner(this);

        ArrayList<String> kelasList =
                new ArrayList<>();

        Cursor kelasCursor =
                databaseHelper.getAllKelasDosenPa();

        while (kelasCursor.moveToNext()) {

            kelasList.add(
                    kelasCursor.getString(0)
            );
        }

        kelasCursor.close();

        ArrayAdapter<String> kelasAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        kelasList
                );

        spKelas.setAdapter(kelasAdapter);

        layout.addView(spKelas);

// ================= PASSWORD =================

        EditText etPassword =
                new EditText(this);

        etPassword.setHint("Password");

        layout.addView(etPassword);

        builder.setView(layout);
        // ================= BUTTON SIMPAN =================

        builder.setPositiveButton(
                "Simpan",
                (dialog, which) -> {

                    String nim =
                            etNim.getText().toString().trim();

                    String nama =
                            etNama.getText().toString().trim();

                    String kelas =
                            spKelas.getSelectedItem() != null ? spKelas.getSelectedItem().toString() : "";

                    String password =
                            etPassword.getText().toString().trim();
                    // ================= VALIDASI =================

                    if (
                            nim.isEmpty() ||
                                    nama.isEmpty() ||
                                    kelas.isEmpty() ||
                                    password.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "Semua field wajib diisi",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    // ================= INSERT =================

                    databaseHelper.insertMahasiswa(
                            nim,
                            nama,
                            kelas,
                            password
                    );
                    // ================= REFRESH =================

                    loadTotalMahasiswa();

                    loadMahasiswaData("");

                    Toast.makeText(
                            this,
                            "Mahasiswa berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        builder.setNegativeButton(
                "Batal",
                null
        );

        builder.show();
    }
}