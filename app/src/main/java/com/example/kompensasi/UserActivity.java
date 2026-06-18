package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Spinner;
import org.apache.poi.ss.usermodel.DataFormatter;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;

import java.io.InputStream;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class UserActivity extends AppCompatActivity {
    private static final int PICK_EXCEL_FILE = 1;

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalUser;

    LinearLayout navRumah,
            navUser,
            navLogout;
    LinearLayout navMahasiswa,
            navKompensasi;
    MaterialButton btnTambah;
    EditText etSearch;

    RecyclerView recyclerUser;

    ChipGroup chipGroupRole;
    MaterialButton btnImportExcel;

    // ================= LIST =================

    ArrayList<UserModel> userList;

    UserAdapter adapter;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================
        btnImportExcel =
                findViewById(R.id.btnImportExcel);
        tvTotalUser =
                findViewById(R.id.tvTotalUser);
        btnTambah =
                findViewById(R.id.btnTambah);
        navRumah =
                findViewById(R.id.navRumah);

        navUser =
                findViewById(R.id.navUser);
        navMahasiswa =
                findViewById(R.id.navMahasiswa);

        navKompensasi =
                findViewById(R.id.navKompensasi);

        navLogout =
                findViewById(R.id.navLogout);

        etSearch =
                findViewById(R.id.etSearch);

        recyclerUser =
                findViewById(R.id.recyclerUser);

        chipGroupRole =
                findViewById(R.id.chipGroupRole);

        // ================= RECYCLER =================

        userList = new ArrayList<>();

        adapter = new UserAdapter(this, userList);

        recyclerUser.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerUser.setAdapter(adapter);

        // ================= LOAD =================

        loadTotalUser();

        loadUserData("");

        loadRoleFilter();
        btnTambah.setOnClickListener(v -> {

            showTambahUserDialog();
        });
        btnImportExcel.setOnClickListener(v -> {

            Intent intent =
                    new Intent(Intent.ACTION_GET_CONTENT);

            intent.setType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Pilih File Excel"
                    ),
                    PICK_EXCEL_FILE
            );
        });



        // ================= SEARCH =================

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                loadUserData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // ================= NAVIGATION =================

        navRumah.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserActivity.this,
                            DashboardAdminActivity.class
                    );

            startActivity(intent);

            finish();
        });

        navUser.setOnClickListener(v -> {
            // Active state, do nothing or refresh
        });

        navMahasiswa.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserActivity.this,
                            KelolaMahasiswaActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            finish();
        });

        navKompensasi.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserActivity.this,
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
                            UserActivity.this,
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
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                requestCode == PICK_EXCEL_FILE &&
                        resultCode == RESULT_OK &&
                        data != null
        ) {

            importExcel(data);
        }
    }
    // ================= TOTAL USER =================

    private void loadTotalUser() {

        Cursor cursor =
                databaseHelper.getAllUsers();

        tvTotalUser.setText(
                String.valueOf(cursor.getCount())
        );

        cursor.close();
    }

    // ================= LOAD USER =================

    private void loadUserData(String keyword) {

        userList.clear();

        Cursor cursor =
                databaseHelper.searchUser(keyword);

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(0);

                String nama =
                        cursor.getString(1);

                String username =
                        cursor.getString(2);

                String password =
                        cursor.getString(3);

                String role =
                        cursor.getString(4);

                String kelas =
                        cursor.getString(5);
                
                int fotoIndex = -1;
                try {
                    fotoIndex = cursor.getColumnIndexOrThrow("foto_profil");
                } catch (Exception e) {}
                String fotoProfil = fotoIndex != -1 ? cursor.getString(fotoIndex) : null;

                userList.add(
                        new UserModel(
                                id,
                                nama,
                                username,
                                password,
                                role,
                                kelas,
                                fotoProfil
                        )
                );


            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();

        cursor.close();
    }

    // ================= ROLE FILTER =================

    private void loadRoleFilter() {

        Cursor cursor =
                databaseHelper.getAllRole();

        while (cursor.moveToNext()) {

            String role =
                    cursor.getString(0);


            Chip chip = new Chip(this);

            chip.setText(role);

            chip.setCheckable(true);

            chip.setClickable(true);

            chip.setChipBackgroundColorResource(
                    R.color.purple_500
            );
            chip.setChipStrokeColorResource(
                    R.color.purple_200
            );

            chip.setOnClickListener(v -> {

                filterRole(role);
            });

            chipGroupRole.addView(chip);
        }

        cursor.close();
    }


    // ================= FILTER ROLE =================

    private void filterRole(String role) {

        userList.clear();

        Cursor cursor =
                databaseHelper.getUserByRole(role);

        if (cursor.moveToFirst()) {

            int fotoIndex = -1;
            try {
                fotoIndex = cursor.getColumnIndexOrThrow("foto_profil");
            } catch (Exception e) {}

            do {

                String foto = fotoIndex != -1 ? cursor.getString(fotoIndex) : null;

                userList.add(
                        new UserModel(
                                cursor.getInt(0),
                                cursor.getString(1),
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
    private void importExcel(Intent data) {

        new Thread(() -> {

            try {

                if (
                        data == null ||
                                data.getData() == null
                ) {

                    runOnUiThread(() -> {

                        Toast.makeText(
                                this,
                                "File tidak ditemukan",
                                Toast.LENGTH_LONG
                        ).show();
                    });

                    return;
                }

                InputStream inputStream =
                        getContentResolver()
                                .openInputStream(
                                        data.getData()
                                );

                if (inputStream == null) {

                    runOnUiThread(() -> {

                        Toast.makeText(
                                this,
                                "Gagal membaca file",
                                Toast.LENGTH_LONG
                        ).show();
                    });

                    return;
                }

                XSSFWorkbook workbook =
                        new XSSFWorkbook(inputStream);

                Sheet sheet =
                        workbook.getSheetAt(0);

                DataFormatter formatter =
                        new DataFormatter();

                int berhasil = 0;

                int gagal = 0;

                for (
                        int i = 1;
                        i <= sheet.getLastRowNum();
                        i++
                ) {

                    Row row =
                            sheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    try {

                        String nama = "";
                        String username = "";
                        String password = "";
                        String role = "";
                        String kelas = "";

                        // ================= NAMA =================

                        if (row.getCell(0) != null) {

                            nama =
                                    formatter.formatCellValue(
                                                    row.getCell(0)
                                            )
                                            .replace("'", "")
                                            .trim();
                        }

                        // ================= USERNAME =================

                        if (row.getCell(1) != null) {

                            username =
                                    formatter.formatCellValue(
                                                    row.getCell(1)
                                            )
                                            .replace("'", "")
                                            .replace(".0", "")
                                            .trim();
                        }

                        // ================= PASSWORD =================

                        if (row.getCell(2) != null) {

                            password =
                                    formatter.formatCellValue(
                                                    row.getCell(2)
                                            )
                                            .replace("'", "")
                                            .trim();
                        }

                        // ================= ROLE =================

                        if (row.getCell(3) != null) {

                            role =
                                    formatter.formatCellValue(
                                                    row.getCell(3)
                                            )
                                            .replace("'", "")
                                            .trim()
                                            .toLowerCase();
                        }

                        // ================= KELAS =================

                        if (row.getCell(4) != null) {

                            kelas =
                                    formatter.formatCellValue(
                                                    row.getCell(4)
                                            )
                                            .replace("'", "")
                                            .trim();
                        }

                        // ================= VALIDASI =================

                        if (
                                nama.isEmpty() ||
                                        username.isEmpty() ||
                                        password.isEmpty() ||
                                        role.isEmpty()
                        ) {

                            gagal++;
                            continue;
                        }

                        boolean insert =
                                databaseHelper.insertUser(
                                        nama,
                                        username,
                                        password,
                                        role,
                                        kelas
                                );

                        if (insert) {

                            berhasil++;

                        } else {

                            gagal++;
                        }

                    } catch (Exception e) {

                        gagal++;

                        e.printStackTrace();
                    }
                }

                workbook.close();

                inputStream.close();

                int finalBerhasil = berhasil;

                int finalGagal = gagal;

                runOnUiThread(() -> {

                    loadUserData("");

                    loadTotalUser();

                    chipGroupRole.removeAllViews();

                    loadRoleFilter();

                    Toast.makeText(
                            this,
                            "Import selesai\n" +
                                    "Berhasil : " + finalBerhasil +
                                    "\nGagal : " + finalGagal,
                            Toast.LENGTH_LONG
                    ).show();
                });

            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(() -> {

                    Toast.makeText(
                            this,
                            "ERROR : " + e.toString(),
                            Toast.LENGTH_LONG
                    ).show();
                });
            }

        }).start();
    }
    // ================= TAMBAH USER =================

    private void showTambahUserDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Tambah User");

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(40, 30, 40, 10);

        // ================= USERNAME =================
        EditText etNama =
                new EditText(this);

        etNama.setHint("Nama");

        layout.addView(etNama);
        EditText etUsername =
                new EditText(this);

        etUsername.setHint("Username");

        layout.addView(etUsername);

        // ================= PASSWORD =================

        EditText etPassword =
                new EditText(this);

        etPassword.setHint("Password");

        layout.addView(etPassword);

        // ================= ROLE =================

        Spinner spRole =
                new Spinner(this);

        String[] roleList = {
                "admin",
                "dosenpa",
                "kajur",
                "sekjur"
        };
        // ================= KELAS =================

        EditText etKelas =
                new EditText(this);

        etKelas.setHint("Kelas Dosen PA");

        etKelas.setEnabled(false);

        layout.addView(etKelas);

        ArrayAdapter<String> adapterRole =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roleList
                );

        spRole.setAdapter(adapterRole);
        spRole.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        String selectedRole =
                                spRole.getSelectedItem()
                                        .toString();

                        if (selectedRole.equals("dosenpa")) {

                            etKelas.setEnabled(true);

                        } else {

                            etKelas.setText("");

                            etKelas.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                }
        );

        layout.addView(spRole);
        builder.setView(layout);
        // ================= BUTTON SIMPAN =================

        builder.setPositiveButton(
                "Simpan",
                (dialog, which) -> {
                    String nama =
                            etNama.getText()
                                    .toString()
                                    .trim();
                    String username =
                            etUsername.getText()
                                    .toString()
                                    .trim();
                    String kelas =
                            etKelas.getText()
                                    .toString()
                                    .trim();

                    String password =
                            etPassword.getText()
                                    .toString()
                                    .trim();

                    String role =
                            spRole.getSelectedItem()
                                    .toString();

                    // ================= VALIDASI =================

                    // ================= VALIDASI =================

                    if (
                            username.isEmpty() ||
                                    password.isEmpty() ||
                                    role.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "Semua field wajib diisi",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

// ================= VALIDASI KELAS DOSEN PA =================

                    if (
                            role.equals("dosenpa") &&
                                    kelas.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "Kelas dosen PA wajib diisi",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    // ================= INSERT DATABASE =================

                    databaseHelper.insertUser(
                            nama,
                            username,
                            password,
                            role,
                            kelas
                    );

                    // ================= REFRESH =================

                    loadTotalUser();

                    loadUserData("");

                    chipGroupRole.removeAllViews();

                    loadRoleFilter();

                    Toast.makeText(
                            this,
                            "User berhasil ditambahkan",
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
