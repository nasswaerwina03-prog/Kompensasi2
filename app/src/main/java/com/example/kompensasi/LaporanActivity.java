package com.example.kompensasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class LaporanActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ImageView btnBack;
    MaterialButton btnUnduh;
    TextView tvPesan;
    String nimLogin;
    int idKompensasi = -1;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        databaseHelper = new DatabaseHelper(this);
        btnBack = findViewById(R.id.btnBack);
        btnUnduh = findViewById(R.id.btnUnduh);
        tvPesan = findViewById(R.id.tvPesan);

        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        nimLogin = prefs.getString("nim_mahasiswa", "");

        btnBack.setOnClickListener(v -> finish());

        loadKompensasi();

        btnUnduh.setOnClickListener(v -> {
            if (idKompensasi != -1) {
                Intent intent = new Intent(this, DownloadPdfActivity.class);
                intent.putExtra("id_kompensasi", idKompensasi);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Data kompensasi tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKompensasi() {
        Cursor mhs = databaseHelper.searchMahasiswa(nimLogin);
        if (mhs.moveToFirst()) {
            int idMhs = mhs.getInt(mhs.getColumnIndexOrThrow("id_mahasiswa"));
            Cursor kmp = databaseHelper.getKompensasiMahasiswa(idMhs);
            
            boolean foundVerified = false;
            while (kmp.moveToNext()) {
                int idK = kmp.getInt(kmp.getColumnIndexOrThrow("id_kompensasi"));
                Cursor pengajuan = databaseHelper.getStatusPengajuan(idK);
                if (pengajuan.moveToFirst()) {
                    String stPA = pengajuan.getString(0);
                    String stSJ = pengajuan.getString(1);
                    String stKJ = pengajuan.getString(2);
                    
                    if ("Disetujui".equals(stPA) && "Disetujui".equals(stSJ) && "Disetujui".equals(stKJ)) {
                        idKompensasi = idK;
                        tvPesan.setText("Anda dapat mengunduh laporan kompensasi yang sudah terverifikasi lengkap.");
                        btnUnduh.setEnabled(true);
                        foundVerified = true;
                        pengajuan.close();
                        break;
                    }
                }
                pengajuan.close();
            }

            if (!foundVerified) {
                // Check if there's any compensation at all to show a different message
                if (kmp.getCount() > 0) {
                    tvPesan.setText("Laporan belum tersedia. Tunggu hingga semua pihak (Dosen PA, Sekjur, Kajur) menyetujui pengajuan Anda.");
                } else {
                    tvPesan.setText("Tidak ada data laporan kompensasi.");
                }
                btnUnduh.setEnabled(false);
            }
            kmp.close();
        }
        mhs.close();
    }
}
