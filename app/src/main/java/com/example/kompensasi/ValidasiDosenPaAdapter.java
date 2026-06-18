package com.example.kompensasi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

// ================= KELAS ADAPTER =================
// Posisi kelas ini berfungsi sebagai jembatan untuk menampilkan list data ke dalam RecyclerView (Daftar).
public class ValidasiDosenPaAdapter extends RecyclerView.Adapter<ValidasiDosenPaAdapter.ViewHolder> {

    Context context;
    ArrayList<PengajuanModel> list;
    DatabaseHelper databaseHelper;
    Runnable onRefreshData;

    public ValidasiDosenPaAdapter(Context context, ArrayList<PengajuanModel> list, Runnable onRefreshData) {
        this.context = context;
        this.list = list;
        this.onRefreshData = onRefreshData;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reuse item_validasi_sekjur layout since it is identical to what Dosen PA needs
        View view = LayoutInflater.from(context).inflate(R.layout.item_validasi_sekjur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // ================= MENGISI DATA KE BARIS =================
    // Posisi ini memasukkan data dari database/model ke komponen UI (Teks, Gambar) pada tiap baris list.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PengajuanModel model = list.get(position);

        holder.tvTugas.setText(model.getNamaMahasiswa() + " - " + model.getKelas());
        holder.tvTanggal.setText(model.getTanggalPengajuan());
        holder.tvStatus.setText(model.getStatusDosenPa());

        // ================= STATUS =================

        if (model.getStatusDosenPa().equals("Disetujui")) {
            holder.tvStatus.setTextColor(Color.WHITE);
            holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.btnApprove.setVisibility(View.GONE); // Hilangkan tombol setelah disetujui
        } else {
            holder.tvStatus.setTextColor(Color.WHITE);
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FB8C00"));
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnApprove.setText("Approve");
        }

        // ================= APPROVE =================

        holder.btnApprove.setOnClickListener(v -> {
            if (model.getStatusDosenPa().equals("Pending")) {
                databaseHelper.updateStatusDosenPa(model.getIdPengajuan(), "Disetujui");
                
                Toast.makeText(context, "Persetujuan berhasil disimpan", Toast.LENGTH_SHORT).show();
                
                // Refresh list using callback
                if (onRefreshData != null) {
                    onRefreshData.run();
                }
            }
        });

        // ================= DETAIL =================

        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailKompensasiActivity.class);
            intent.putExtra("id_kompensasi", model.getIdKompensasi());
            intent.putExtra("from", "dosen_pa");
            context.startActivity(intent);
        });
    }

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTugas, tvTanggal, tvStatus;
        MaterialButton btnApprove, btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTugas = itemView.findViewById(R.id.tvTugas);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
