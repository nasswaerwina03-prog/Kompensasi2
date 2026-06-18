package com.example.kompensasi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class KompensasiDosenAdapter
        extends RecyclerView.Adapter<KompensasiDosenAdapter.ViewHolder> {

    Context context;

    ArrayList<KompensasiModel> list;

    DatabaseHelper databaseHelper;

    public KompensasiDosenAdapter(
            Context context,
            ArrayList<KompensasiModel> list
    ) {

        this.context = context;

        this.list = list;

        databaseHelper =
                new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_kompensasi_dosen,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        KompensasiModel model =
                list.get(position);

        holder.tvNamaMahasiswa.setText(
                model.getNamaMahasiswa()
        );

        holder.tvJumlahmenit.setText(
                "Jumlah Kompensasi : "
                        + model.getJumlahmenit()
                        + " menit"
        );

        holder.tvSisamenit.setText(
                "Sisa menit : "
                        + model.getSisamenit()
                        + " menit"
        );

        holder.tvTugas.setText(
                "Tugas : "
                        + model.getTugasKompensasi()
        );

        holder.tvJamMulai.setText(
                "Jam Mulai : " + model.getJamMulai()
        );

        holder.tvJamSelesai.setText(
                "Jam Selesai : " + model.getJamSelesai()
        );

        // ================= STATUS LABEL =================

        String status = model.getStatusFinal();
        switch (status) {
            case "Selesai":
                holder.tvStatus.setText("✓ Selesai");
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case "Siap Diverifikasi":
                holder.tvStatus.setText("⏳ Siap Diverifikasi");
                holder.tvStatus.setTextColor(Color.parseColor("#2196F3"));
                break;
            case "Menunggu Persetujuan":
                holder.tvStatus.setText("⏳ Menunggu Persetujuan");
                holder.tvStatus.setTextColor(Color.parseColor("#FB8C00"));
                break;
            case "Ada Tugas Aktif":
                holder.tvStatus.setText("📝 Ada Tugas Aktif");
                holder.tvStatus.setTextColor(Color.parseColor("#2196F3"));
                break;
            case "Menunggu Tugas Tambahan":
                holder.tvStatus.setText("⚠️ Menunggu Tugas Tambahan");
                holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
                break;
            default:
                holder.tvStatus.setText("⬤ " + status);
                holder.tvStatus.setTextColor(Color.parseColor("#888888"));
        }

        // ================= SISA PROGRESS =================

        if (model.getJumlahmenit() > 0) {
            int done = model.getJumlahmenit() - model.getSisamenit();
            holder.tvProgress.setText(done + "/" + model.getJumlahmenit() + " menit selesai");
        } else {
            holder.tvProgress.setText("-");
        }

        // ================= OPEN DETAIL =================

        holder.itemView.setOnClickListener(v -> {
            // Find id_mahasiswa from kompensasi
            android.database.Cursor mhsId = new DatabaseHelper(context).getReadableDatabase().rawQuery(
                    "SELECT id_mahasiswa FROM tb_kompensasi WHERE id_kompensasi=?",
                    new String[]{String.valueOf(model.getId())}
            );
            int idMahasiswa = -1;
            if (mhsId.moveToFirst()) idMahasiswa = mhsId.getInt(0);
            mhsId.close();

            Intent intent = new Intent(context, PengajuanMasukActivity.class);
            intent.putExtra("id_kompensasi", model.getId());
            intent.putExtra("id_mahasiswa", idMahasiswa);
            context.startActivity(intent);
        });
    }

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {

        return list.size();
    }

    // ================= VIEW HOLDER =================

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamaMahasiswa, tvJumlahmenit, tvSisamenit,
                tvTugas, tvJamMulai, tvJamSelesai, tvStatus, tvProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMahasiswa = itemView.findViewById(R.id.tvNamaMahasiswa);
            tvJumlahmenit   = itemView.findViewById(R.id.tvJumlahmenit);
            tvSisamenit     = itemView.findViewById(R.id.tvSisamenit);
            tvTugas         = itemView.findViewById(R.id.tvTugas);
            tvJamMulai      = itemView.findViewById(R.id.tvJamMulai);
            tvJamSelesai    = itemView.findViewById(R.id.tvJamSelesai);
            tvStatus        = itemView.findViewById(R.id.tvStatus);
            tvProgress      = itemView.findViewById(R.id.tvProgress);
        }
    }
}