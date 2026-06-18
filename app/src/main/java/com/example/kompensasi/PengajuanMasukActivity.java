package com.example.kompensasi;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class PengajuanMasukActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    TextView tvNama, tvNim, tvKelas, tvJumlah, tvSisa, tvStatus;
    ProgressBar progressSesi;
    RecyclerView recyclerSesi;
    MaterialButton btnTambahSesi, btnApprove, btnTolak;
    ImageView btnBack;

    int idKompensasi, idMahasiswa, jumlahMenit;
    String namaMahasiswa;

    ArrayList<SesiModel> sesiList = new ArrayList<>();
    SesiAdapter sesiAdapter;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_masuk);

        databaseHelper = new DatabaseHelper(this);

        idKompensasi = getIntent().getIntExtra("id_kompensasi", -1);
        idMahasiswa = getIntent().getIntExtra("id_mahasiswa", -1);

        btnBack       = findViewById(R.id.btnBack);
        tvNama        = findViewById(R.id.tvNama);
        tvNim         = findViewById(R.id.tvNim);
        tvKelas       = findViewById(R.id.tvKelas);
        tvJumlah      = findViewById(R.id.tvJumlah);
        tvSisa        = findViewById(R.id.tvSisa);
        tvStatus      = findViewById(R.id.tvStatus);
        progressSesi  = findViewById(R.id.progressSesi);
        recyclerSesi  = findViewById(R.id.recyclerSesi);
        btnTambahSesi = findViewById(R.id.btnTambahSesi);
        btnApprove    = findViewById(R.id.btnApprove);
        btnTolak      = findViewById(R.id.btnTolak);

        sesiAdapter = new SesiAdapter(this, sesiList, idKompensasi, databaseHelper, this::loadData);
        recyclerSesi.setLayoutManager(new LinearLayoutManager(this));
        recyclerSesi.setAdapter(sesiAdapter);

        btnBack.setOnClickListener(v -> finish());

        loadData();

        btnTambahSesi.setOnClickListener(v -> showTambahSesiDialog());

        btnApprove.setOnClickListener(v -> {
            int sisa = databaseHelper.getSisaMenit(idKompensasi);
            if (sisa > 0) {
                Toast.makeText(this, "Sisa kompensasi masih " + sisa + " menit. Harap selesaikan semua sesi dulu.", Toast.LENGTH_LONG).show();
                return;
            }
            new AlertDialog.Builder(this)
                .setTitle("Approve Final")
                .setMessage("Yakin menyetujui kompensasi ini? Data akan diteruskan ke Sekjur.")
                .setPositiveButton("Approve", (d, w) -> {
                    databaseHelper.approveFinalDosenPa(idKompensasi, idMahasiswa);
                    showSnackbar("Kompensasi disetujui, diteruskan ke Sekjur");
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
        });

        btnTolak.setOnClickListener(v -> {
            EditText etAlasan = new EditText(this);
            etAlasan.setHint("Masukkan alasan penolakan");
            new AlertDialog.Builder(this)
                .setTitle("Tolak Kompensasi")
                .setView(etAlasan)
                .setPositiveButton("Tolak", (d, w) -> {
                    String alasan = etAlasan.getText().toString().trim();
                    if (alasan.isEmpty()) {
                        Toast.makeText(this, "Alasan wajib diisi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    databaseHelper.tolakFinalDosenPa(idKompensasi, idMahasiswa, alasan);
                    showSnackbar("Kompensasi ditolak");
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
        });
    }

    private void loadData() {
        Cursor k = databaseHelper.getDetailKompensasi(idKompensasi);
        if (k.moveToFirst()) {
            jumlahMenit = k.getInt(3);
            int sisaMenit = k.getInt(4);
            String status = k.getString(6);

            tvJumlah.setText(jumlahMenit + " Menit");
            tvSisa.setText(sisaMenit + " Menit");
            tvStatus.setText(status);

            int done = jumlahMenit - sisaMenit;
            progressSesi.setMax(jumlahMenit);
            progressSesi.setProgress(done);

            // Enable approve hanya jika sisa = 0
            if (sisaMenit == 0 && !status.equals("Selesai")) {
                btnApprove.setEnabled(true);
                btnApprove.setAlpha(1f);
                btnTolak.setVisibility(View.GONE);
            } else if (status.equals("Selesai")) {
                btnApprove.setEnabled(false);
                btnApprove.setAlpha(0.4f);
                btnTambahSesi.setEnabled(false);
                btnTambahSesi.setAlpha(0.4f);
                btnTolak.setVisibility(View.GONE);
            } else {
                btnApprove.setEnabled(false);
                btnApprove.setAlpha(0.4f);
                btnTolak.setVisibility(View.VISIBLE);
            }
        }
        k.close();

        // Load mahasiswa info
        Cursor mhs = databaseHelper.getMahasiswaById(idMahasiswa);
        if (mhs.moveToFirst()) {
            namaMahasiswa = mhs.getString(mhs.getColumnIndexOrThrow("nama"));
            tvNama.setText(namaMahasiswa);
            tvNim.setText(mhs.getString(mhs.getColumnIndexOrThrow("nim")));
            tvKelas.setText(mhs.getString(mhs.getColumnIndexOrThrow("kelas")));
        }
        mhs.close();

        // Load sesi
        sesiList.clear();
        boolean adaPengajuan = false;
        Cursor sesiCursor = databaseHelper.getSesiByKompensasi(idKompensasi);
        while (sesiCursor.moveToNext()) {
            SesiModel sesi = new SesiModel(
                    sesiCursor.getInt(0),
                    sesiCursor.getInt(1),
                    sesiCursor.getString(2),
                    sesiCursor.getString(3),
                    sesiCursor.getString(4),
                    sesiCursor.getInt(5),
                    sesiCursor.getString(6),
                    sesiCursor.getString(7),
                    sesiCursor.getString(8),
                    sesiCursor.getString(9)
            );
            sesiList.add(sesi);
            if ("Menunggu Persetujuan".equals(sesi.getStatus())) {
                adaPengajuan = true;
            }
        }
        sesiCursor.close();
        sesiAdapter.notifyDataSetChanged();

        if (btnTolak.getVisibility() == View.VISIBLE) {
            if (adaPengajuan) {
                btnTolak.setEnabled(true);
                btnTolak.setAlpha(1f);
            } else {
                btnTolak.setEnabled(false);
                btnTolak.setAlpha(0.4f);
            }
        }
    }

    private void showTambahSesiDialog() {
        int sisa = databaseHelper.getSisaMenit(idKompensasi);
        if (sisa <= 0) {
            Toast.makeText(this, "Semua menit kompensasi sudah terpenuhi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if there's any session not finished
        boolean hasActiveSession = false;
        for (SesiModel s : sesiList) {
            if (s.getStatus().equals("Dikerjakan Mahasiswa") || s.getStatus().equals("Menunggu Persetujuan")) {
                hasActiveSession = true;
                break;
            }
        }

        if (hasActiveSession) {
            Toast.makeText(this, "Selesaikan atau verifikasi sesi yang ada sebelum menambah sesi baru.", Toast.LENGTH_LONG).show();
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_sesi, null);
        EditText etTugas     = dialogView.findViewById(R.id.etTugas);
        EditText etJamMulai  = dialogView.findViewById(R.id.etJamMulai);
        EditText etJamSelesai= dialogView.findViewById(R.id.etJamSelesai);
        EditText etMenit     = dialogView.findViewById(R.id.etMenit);
        TextView tvSisaInfo  = dialogView.findViewById(R.id.tvSisaInfo);
        tvSisaInfo.setText("Sisa kompensasi: " + sisa + " menit (maks per hari: 360 menit)");

        etJamMulai.setOnClickListener(v -> showTimePicker(etJamMulai));
        etJamSelesai.setOnClickListener(v -> showTimePicker(etJamSelesai));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Tambah Sesi Kompensasi")
                .setView(dialogView)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal", null)
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String tugas     = etTugas.getText().toString().trim();
            String jamMulai  = etJamMulai.getText().toString().trim();
            String jamSelesai= etJamSelesai.getText().toString().trim();
            String menitStr  = etMenit.getText().toString().trim();

            if (tugas.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty() || menitStr.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int menit = Integer.parseInt(menitStr);
            if (menit <= 0) {
                Toast.makeText(this, "Menit harus lebih dari 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (menit > 360) {
                Toast.makeText(this, "Maksimal 360 menit (6 jam) per hari", Toast.LENGTH_SHORT).show();
                return;
            }
            if (menit > sisa) {
                Toast.makeText(this, "Menit melebihi sisa kompensasi (" + sisa + " menit)", Toast.LENGTH_SHORT).show();
                return;
            }

            String tanggal = java.text.DateFormat.getDateInstance().format(new java.util.Date());
            databaseHelper.tambahSesi(idKompensasi, tugas, jamMulai, jamSelesai, menit, tanggal);
            dialog.dismiss();
            showSnackbar("Sesi berhasil ditambahkan ✓");
            loadData();
        });
    }

    private void showTimePicker(EditText target) {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            target.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private void showSnackbar(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#B1456E"));
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
    }

    // ================= INNER ADAPTER =================

    public static class SesiAdapter extends RecyclerView.Adapter<SesiAdapter.VH> {
        Context ctx;
        ArrayList<SesiModel> list;
        int idKompensasi;
        DatabaseHelper db;
        Runnable onChanged;

        public SesiAdapter(Context ctx, ArrayList<SesiModel> list, int idKompensasi, DatabaseHelper db, Runnable onChanged) {
            this.ctx = ctx; this.list = list; this.idKompensasi = idKompensasi;
            this.db = db; this.onChanged = onChanged;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_sesi, parent, false);
            return new VH(v);
        }

        @Override
        // ================= MENGISI DATA KE BARIS =================
    // Posisi ini memasukkan data dari database/model ke komponen UI (Teks, Gambar) pada tiap baris list.
    public void onBindViewHolder(VH h, int pos) {
            SesiModel s = list.get(pos);
            h.tvTugas.setText(s.getTugas());
            h.tvJam.setText(s.getJamMulai() + " – " + s.getJamSelesai());
            h.tvMenit.setText(s.getMenit() + " menit");
            h.tvTanggal.setText(s.getTanggal());
            h.tvStatus.setText(s.getStatus());

            if (s.getStatus().equals("Menunggu Persetujuan")) {
                h.tvStatus.setTextColor(Color.parseColor("#FF9800"));
                h.layoutAction.setVisibility(View.VISIBLE);
                h.tvAlasan.setVisibility(View.GONE);
            } else if (s.getStatus().equals("Disetujui")) {
                h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                h.layoutAction.setVisibility(View.GONE);
                h.tvAlasan.setVisibility(View.GONE);
            } else if (s.getStatus().equals("Ditolak")) {
                h.tvStatus.setTextColor(Color.parseColor("#E53935"));
                h.layoutAction.setVisibility(View.GONE);
                h.tvAlasan.setVisibility(View.VISIBLE);
                h.tvAlasan.setText("Alasan Penolakan: " + s.getAlasan());
            } else {
                h.tvStatus.setTextColor(Color.parseColor("#2196F3"));
                h.layoutAction.setVisibility(View.GONE);
                h.tvAlasan.setVisibility(View.GONE);
            }

            h.btnApproveSesi.setOnClickListener(v -> {
                db.approveSesiDosen(s.getIdSesi(), idKompensasi, s.getMenit());
                if (onChanged != null) onChanged.run();
            });

            h.btnTolakSesi.setOnClickListener(v -> {
                EditText etAlasan = new EditText(ctx);
                etAlasan.setHint("Alasan penolakan sesi ini");
                new AlertDialog.Builder(ctx)
                    .setTitle("Tolak Sesi")
                    .setView(etAlasan)
                    .setPositiveButton("Tolak", (dialog, which) -> {
                        String alasan = etAlasan.getText().toString().trim();
                        if (alasan.isEmpty()) {
                            Toast.makeText(ctx, "Alasan wajib diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.tolakSesi(s.getIdSesi(), alasan);
                        if (onChanged != null) onChanged.run();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            });

            if (s.getBukti() != null && !s.getBukti().equals("-") && !s.getBukti().isEmpty()) {
                h.btnLihatBukti.setVisibility(View.VISIBLE);
                h.btnLihatBukti.setOnClickListener(v -> {
                    try {
                        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
                        String bukti = s.getBukti();
                        if (bukti.startsWith("/")) {
                            java.io.File file = new java.io.File(bukti);
                            if (!file.exists()) {
                                android.widget.Toast.makeText(ctx, "Video tidak ditemukan di penyimpanan (Mungkin sudah terhapus).", android.widget.Toast.LENGTH_LONG).show();
                                return;
                            }
                            android.net.Uri uri = androidx.core.content.FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", file);
                            intent.setDataAndType(uri, "video/*");
                        } else {
                            intent.setDataAndType(android.net.Uri.parse(bukti), "video/*");
                        }
                        intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        ctx.startActivity(android.content.Intent.createChooser(intent, "Putar Video..."));
                    } catch (Exception e) {
                        e.printStackTrace();
                        android.widget.Toast.makeText(ctx, "Gagal memutar video: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                h.btnLihatBukti.setVisibility(View.GONE);
            }
        }

        @Override
        // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() { return list.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvTugas, tvJam, tvMenit, tvTanggal, tvStatus, tvAlasan;
            MaterialButton btnApproveSesi, btnTolakSesi, btnLihatBukti;
            LinearLayout layoutAction;

            public VH(@NonNull View itemView) {
                super(itemView);
                tvTugas = itemView.findViewById(R.id.tvTugas);
                tvJam = itemView.findViewById(R.id.tvJam);
                tvMenit = itemView.findViewById(R.id.tvMenit);
                tvTanggal = itemView.findViewById(R.id.tvTanggal);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                tvAlasan = itemView.findViewById(R.id.tvAlasan);
                btnApproveSesi = itemView.findViewById(R.id.btnApproveSesi);
                btnTolakSesi = itemView.findViewById(R.id.btnTolakSesi);
                btnLihatBukti = itemView.findViewById(R.id.btnLihatBukti);
                layoutAction = itemView.findViewById(R.id.layoutAction);
            }
        }
    }
}

