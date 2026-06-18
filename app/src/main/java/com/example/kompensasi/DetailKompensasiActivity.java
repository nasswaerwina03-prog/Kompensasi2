package com.example.kompensasi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class DetailKompensasiActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    TextView tvNama, tvNim, tvKelas, tvJumlahmenit, tvSisamenit, tvStatus,
             tvStatusDosenPa, tvStatusSekjur, tvStatusKajur;

    ImageView btnBack;
    de.hdodenhof.circleimageview.CircleImageView ivProfileMahasiswa;
    RecyclerView recyclerSesi;

    int idKompensasi;
    ArrayList<SesiModel> sesiList = new ArrayList<>();
    SesiDetailAdapter sesiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kompensasi);

        databaseHelper = new DatabaseHelper(this);

        tvNama = findViewById(R.id.tvNama);
        tvNim = findViewById(R.id.tvNim);
        tvKelas = findViewById(R.id.tvKelas);
        tvJumlahmenit = findViewById(R.id.tvJumlahmenit);
        tvSisamenit = findViewById(R.id.tvSisamenit);
        tvStatus = findViewById(R.id.tvStatus);
        tvStatusDosenPa = findViewById(R.id.tvStatusDosenPa);
        tvStatusSekjur = findViewById(R.id.tvStatusSekjur);
        tvStatusKajur = findViewById(R.id.tvStatusKajur);
        ivProfileMahasiswa = findViewById(R.id.ivProfileMahasiswa);
        recyclerSesi = findViewById(R.id.recyclerSesi);
        btnBack = findViewById(R.id.btnBack);

        idKompensasi = getIntent().getIntExtra("id_kompensasi", 0);

        sesiAdapter = new SesiDetailAdapter(this, sesiList);
        recyclerSesi.setLayoutManager(new LinearLayoutManager(this));
        recyclerSesi.setAdapter(sesiAdapter);

        loadDetailKompensasi();
        loadSesiList();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDetailKompensasi() {
        Cursor cursor = databaseHelper.getDetailKompensasi(idKompensasi);
        if (cursor != null && cursor.moveToFirst()) {
            String nama = cursor.getString(0);
            String nim = cursor.getString(1);
            String kelas = cursor.getString(2);
            int jumlahmenit = cursor.getInt(3);
            int sisamenit = cursor.getInt(4);
            String statusFinal = cursor.getString(6);

            String fotoProfil = null;
            try {
                fotoProfil = cursor.getString(12);
            } catch (Exception e) {}
            if (fotoProfil != null && !fotoProfil.isEmpty()) {
                try {
                    byte[] decodedString = android.util.Base64.decode(fotoProfil, android.util.Base64.DEFAULT);
                    android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (ivProfileMahasiswa != null) {
                        ivProfileMahasiswa.setImageBitmap(decodedByte);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            tvNama.setText(nama);
            tvNim.setText(nim);
            tvKelas.setText(kelas);
            tvJumlahmenit.setText(jumlahmenit + " menit");
            tvSisamenit.setText(sisamenit + " menit");
            tvStatus.setText(statusFinal);

            if (statusFinal.equalsIgnoreCase("Selesai") || statusFinal.equalsIgnoreCase("Disetujui")) {
                tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else if (statusFinal.equalsIgnoreCase("Pending")) {
                tvStatus.setBackgroundColor(Color.parseColor("#FF9800"));
            } else {
                tvStatus.setBackgroundColor(Color.parseColor("#E57373"));
            }

            Cursor statusCursor = databaseHelper.getDetailStatusPengajuan(idKompensasi);
            if (statusCursor != null && statusCursor.moveToFirst()) {
                String statusDosenPa = statusCursor.getString(0);
                String statusSekjur = statusCursor.getString(1);
                String statusKajur = statusCursor.getString(2);

                tvStatusDosenPa.setText(statusDosenPa);
                tvStatusSekjur.setText(statusSekjur);
                tvStatusKajur.setText(statusKajur);

                setStatusColor(tvStatusDosenPa, statusDosenPa);
                setStatusColor(tvStatusSekjur, statusSekjur);
                setStatusColor(tvStatusKajur, statusKajur);
            }
            if (statusCursor != null) {
                statusCursor.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void loadSesiList() {
        sesiList.clear();
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
        }
        sesiCursor.close();
        sesiAdapter.notifyDataSetChanged();
    }

    private void setStatusColor(TextView textView, String status) {
        if (status.equalsIgnoreCase("Disetujui")) {
            textView.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (status.equalsIgnoreCase("Pending")) {
            textView.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {
            textView.setBackgroundColor(Color.parseColor("#E57373"));
        }
    }

    public static class SesiDetailAdapter extends RecyclerView.Adapter<SesiDetailAdapter.VH> {
        Context ctx;
        ArrayList<SesiModel> list;

        public SesiDetailAdapter(Context ctx, ArrayList<SesiModel> list) {
            this.ctx = ctx;
            this.list = list;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_sesi, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            SesiModel s = list.get(pos);
            h.tvTugas.setText(s.getTugas());
            h.tvJam.setText(s.getJamMulai() + " – " + s.getJamSelesai());
            h.tvMenit.setText(s.getMenit() + " menit");
            h.tvTanggal.setText(s.getTanggal());
            h.tvStatus.setText(s.getStatus());

            if (s.getStatus().equals("Menunggu Persetujuan")) {
                h.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (s.getStatus().equals("Disetujui")) {
                h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            } else if (s.getStatus().equals("Ditolak")) {
                h.tvStatus.setTextColor(Color.parseColor("#E53935"));
                h.tvAlasan.setVisibility(View.VISIBLE);
                h.tvAlasan.setText("Alasan Penolakan: " + s.getAlasan());
            } else {
                h.tvStatus.setTextColor(Color.parseColor("#2196F3"));
            }

            // Hide action layout for Sekjur/Kajur
            h.layoutAction.setVisibility(View.GONE);

            if (s.getBukti() != null && !s.getBukti().equals("-") && !s.getBukti().isEmpty()) {
                h.btnLihatBukti.setVisibility(View.VISIBLE);
                h.btnLihatBukti.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String bukti = s.getBukti();
                        if (bukti.startsWith("/")) {
                            File file = new File(bukti);
                            Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", file);
                            intent.setDataAndType(uri, "video/*");
                        } else {
                            intent.setDataAndType(Uri.parse(bukti), "video/*");
                        }
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        ctx.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ctx, "Tidak ada aplikasi untuk memutar video atau akses ditolak", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                h.btnLihatBukti.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView tvTugas, tvJam, tvMenit, tvTanggal, tvStatus, tvAlasan;
            MaterialButton btnLihatBukti;
            LinearLayout layoutAction;

            public VH(@NonNull View itemView) {
                super(itemView);
                tvTugas = itemView.findViewById(R.id.tvTugas);
                tvJam = itemView.findViewById(R.id.tvJam);
                tvMenit = itemView.findViewById(R.id.tvMenit);
                tvTanggal = itemView.findViewById(R.id.tvTanggal);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                tvAlasan = itemView.findViewById(R.id.tvAlasan);
                btnLihatBukti = itemView.findViewById(R.id.btnLihatBukti);
                layoutAction = itemView.findViewById(R.id.layoutAction);
            }
        }
    }
}
