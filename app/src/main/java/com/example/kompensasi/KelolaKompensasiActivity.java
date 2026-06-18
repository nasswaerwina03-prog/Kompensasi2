package com.example.kompensasi;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;


import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class KelolaKompensasiActivity
        extends AppCompatActivity {
    private static final int PICK_EXCEL_KOMPENSASI = 99;

    MaterialButton btnImportExcelKompensasi;
    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalKompensasi;

    LinearLayout navRumah,
            navUser,
            navMahasiswa,
            navKompensasi,
            navLogout;

    MaterialButton btnTambahKompensasi;

    EditText etSearchKompensasi;

    RecyclerView recyclerKompensasi;

    // ================= LIST =================

    ArrayList<KompensasiModel> kompensasiList;

    KompensasiAdapter adapter;

    ArrayList<Integer> mahasiswaIdList;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_kelola_kompensasi
        );

        // ================= DATABASE =================

        databaseHelper =
                new DatabaseHelper(this);

        // ================= INIT =================

        tvTotalKompensasi =
                findViewById(R.id.tvTotalKompensasi);

        btnTambahKompensasi =
                findViewById(R.id.btnTambahKompensasi);

        etSearchKompensasi =
                findViewById(R.id.etSearchKompensasi);

        recyclerKompensasi =
                findViewById(R.id.recyclerKompensasi);

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

        btnImportExcelKompensasi =
                findViewById(R.id.btnImportExcelKompensasi);
        // ================= RECYCLER =================

        kompensasiList =
                new ArrayList<>();

        adapter =
                new KompensasiAdapter(
                        this,
                        kompensasiList
                );

        recyclerKompensasi.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerKompensasi.setAdapter(adapter);

        // ================= LOAD =================

        loadTotalKompensasi();

        loadKompensasiData("");

        // ================= TAMBAH =================

        btnTambahKompensasi.setOnClickListener(v -> {

            showTambahKompensasiDialog();
        });

        // ================= SEARCH =================

        etSearchKompensasi.addTextChangedListener(
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

                        loadKompensasiData(
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

            startActivity(
                    new Intent(
                            this,
                            DashboardAdminActivity.class
                    )
            );

            finish();
        });

        navUser.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            this,
                            UserActivity.class
                    )
            );

            finish();
        });

        navMahasiswa.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            this,
                            KelolaMahasiswaActivity.class
                    )
            );

            finish();
        });

        navKompensasi.setOnClickListener(v -> {
            // Active state
        });
        btnImportExcelKompensasi.setOnClickListener(v -> {

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
                    PICK_EXCEL_KOMPENSASI
            );
        });
        // ================= LOGOUT =================

        navLogout.setOnClickListener(v -> {

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
                requestCode == PICK_EXCEL_KOMPENSASI &&
                        resultCode == RESULT_OK &&
                        data != null
        ) {

            importExcelKompensasi(data);
        }
    }

    // ================= TOTAL =================

    private void loadTotalKompensasi() {

        Cursor cursor =
                databaseHelper.getAllKompensasi();

        tvTotalKompensasi.setText(
                String.valueOf(
                        cursor.getCount()
                )
        );

        cursor.close();
    }

    // ================= LOAD DATA =================

    private void loadKompensasiData(
            String keyword
    ) {

        kompensasiList.clear();

        Cursor cursor =
                databaseHelper.searchKompensasi(
                        keyword
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(0);

                String namaMahasiswa =
                        cursor.getString(1);

                String nim =
                        cursor.getString(2);

                String kelas =
                        cursor.getString(3);

                int jumlahMenit =
                        cursor.getInt(4);

                int sisaMenit =
                        cursor.getInt(5);

                String tugas =
                        cursor.getString(6);

                String status =
                        cursor.getString(7);

                String tanggalMulai =
                        cursor.getString(8);

                String tanggal =
                        cursor.getString(9);

                String tanggalSelesai =
                        cursor.getString(10);

                String jamMulai = cursor.getString(11);
                String jamSelesai = cursor.getString(12);
                String fotoProfil = null;
                try {
                    fotoProfil = cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"));
                } catch (Exception e) {}

                kompensasiList.add(
                        new KompensasiModel(
                                id,
                                namaMahasiswa,
                                nim,
                                kelas,
                                jumlahMenit,
                                sisaMenit,
                                tugas,
                                status,
                                tanggalMulai,
                                tanggal,
                                tanggalSelesai,
                                jamMulai,
                                jamSelesai,
                                fotoProfil
                        )
                );

            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();

        cursor.close();
    }

    // ================= TAMBAH =================

    private void showTambahKompensasiDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Tambah Kompensasi"
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

        // ================= PILIH MAHASISWA =================

        Spinner spMahasiswa =
                new Spinner(this);

        ArrayList<String> mahasiswaNamaList =
                new ArrayList<>();

        mahasiswaIdList =
                new ArrayList<>();

        Cursor cursor =
                databaseHelper.getAllMahasiswa();

        if (cursor.moveToFirst()) {

            do {

                mahasiswaIdList.add(
                        cursor.getInt(0)
                );

                mahasiswaNamaList.add(
                        cursor.getString(3)
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapterMahasiswa =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        mahasiswaNamaList
                );

        spMahasiswa.setAdapter(
                adapterMahasiswa
        );

        layout.addView(spMahasiswa);

        // ================= JUMLAH KOMPENSASI =================

        EditText etJumlah =
                new EditText(this);

        etJumlah.setHint(
                "Jumlah Kompensasi / Menit"
        );

        layout.addView(etJumlah);

        builder.setView(layout);

        // ================= TANGGAL MULAI =================

        TextView etTanggalMulai =
                new TextView(this);

        etTanggalMulai.setText(
                "Pilih Tanggal Mulai"
        );

        etTanggalMulai.setTextSize(16f);

        etTanggalMulai.setPadding(
                30,
                30,
                30,
                30
        );

        etTanggalMulai.setBackgroundColor(
                android.graphics.Color.parseColor(
                        "#F5F5F5"
                )
        );

        layout.addView(etTanggalMulai);

// ================= DATE PICKER =================

        etTanggalMulai.setOnClickListener(v -> {

            Calendar calendar =
                    Calendar.getInstance();

            DatePickerDialog dialog =
                    new DatePickerDialog(
                            this,
                            (view,
                             year,
                             month,
                             dayOfMonth) -> {

                                String tanggal =
                                        dayOfMonth + "/" +
                                                (month + 1) + "/" +
                                                year;

                                etTanggalMulai.setText(
                                        tanggal
                                );
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            dialog.show();
        });
        // ================= BUTTON SIMPAN =================

        builder.setPositiveButton(
                "Simpan",
                (dialog, which) -> {

                    String jumlahString =
                            etJumlah.getText()
                                    .toString()
                                    .trim();
                    String tanggalMulai =
                            etTanggalMulai.getText()
                                    .toString()
                                    .trim();
                    if (tanggalMulai.equals(
                            "Pilih Tanggal Mulai"
                    )) {

                        Toast.makeText(
                                this,
                                "Tanggal mulai wajib dipilih",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }
                    if (
                            jumlahString.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "Jumlah kompensasi wajib diisi",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    int idMahasiswa =
                            mahasiswaIdList.get(
                                    spMahasiswa.getSelectedItemPosition()
                            );

                    int jumlah =
                            Integer.parseInt(
                                    jumlahString
                            );

                    databaseHelper.insertKompensasi(
                            idMahasiswa,
                            jumlah,
                            tanggalMulai
                    );

                    if (jumlah == 0) {
                        // Ambil ID kompensasi terakhir untuk auto-approve
                        Cursor c = databaseHelper.getReadableDatabase().rawQuery("SELECT MAX(id_kompensasi) FROM tb_kompensasi", null);
                        if (c.moveToFirst()) {
                            int lastId = c.getInt(0);
                            databaseHelper.autoApproveKompensasi(idMahasiswa, lastId);
                        }
                        c.close();
                    }

                    loadTotalKompensasi();

                    loadKompensasiData("");

                    Toast.makeText(
                            this,
                            "Kompensasi berhasil ditambahkan",
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
    private void importExcelKompensasi(Intent data) {

        new Thread(() -> {

            try {

                InputStream inputStream =
                        getContentResolver()
                                .openInputStream(
                                        data.getData()
                                );

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

                    if (
                            row == null ||
                                    row.getCell(0) == null
                    ) {
                        continue;
                    }

                    try {

                        String nim =
                                formatter.formatCellValue(
                                                row.getCell(0)
                                        )
                                        .replace("'", "")
                                        .replace(".0", "")
                                        .trim();

                        String menitString =
                                formatter.formatCellValue(
                                                row.getCell(1)
                                        )
                                        .replace("'", "")
                                        .replace(".0", "")
                                        .trim();

                        if (
                                nim.isEmpty() ||
                                        menitString.isEmpty()
                        ) {

                            gagal++;
                            continue;
                        }

                        int jumlahMenit =
                                Integer.parseInt(
                                        menitString
                                );

                        Cursor cursor =
                                databaseHelper.getAllMahasiswa();

                        int idMahasiswa = -1;

                        if (cursor.moveToFirst()) {

                            do {

                                String dbNim =
                                        cursor.getString(2);

                                if (dbNim.equals(nim)) {

                                    idMahasiswa =
                                            cursor.getInt(0);

                                    break;
                                }

                            } while (cursor.moveToNext());
                        }

                        cursor.close();

                        if (idMahasiswa != -1) {

                            String tanggalMulai =
                                    java.text.DateFormat
                                            .getDateInstance()
                                            .format(
                                                    new java.util.Date()
                                            );
//                            buat import
                            databaseHelper.insertKompensasi(
                                    idMahasiswa,
                                    jumlahMenit,
                                    tanggalMulai
                            );

                            berhasil++;

                        } else {

                            gagal++;
                        }
                        cursor.close();

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

                    loadTotalKompensasi();

                    loadKompensasiData("");

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
}