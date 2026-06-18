package com.example.kompensasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// ================= KELAS ADAPTER =================
// Posisi kelas ini berfungsi sebagai jembatan untuk menampilkan list data ke dalam RecyclerView (Daftar).
public class MahasiswaBimbinganAdapter extends RecyclerView.Adapter<MahasiswaBimbinganAdapter.ViewHolder> {

    Context context;
    ArrayList<MahasiswaBimbinganModel> list;

    public MahasiswaBimbinganAdapter(Context context, ArrayList<MahasiswaBimbinganModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // ================= MENGISI DATA KE BARIS =================
    // Posisi ini memasukkan data dari database/model ke komponen UI (Teks, Gambar) pada tiap baris list.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MahasiswaBimbinganModel model = list.get(position);

        holder.tvNama.setText(model.getNama());
        holder.tvNim.setText(model.getNim());
        holder.tvKelas.setText("Kelas: " + model.getKelas() + " | Total Kompensasi: " + model.getTotalKompen() + " menit");

        String fotoBase64 = model.getFotoProfil();
        if (fotoBase64 != null && !fotoBase64.isEmpty() && !fotoBase64.equals("-")) {
            try {
                byte[] decodedString = android.util.Base64.decode(fotoBase64, android.util.Base64.DEFAULT);
                android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ivProfil.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
                holder.ivProfil.setImageResource(R.drawable.ic_person);
            }
        } else {
            holder.ivProfil.setImageResource(R.drawable.ic_person);
        }

        // Hide action buttons
        holder.btnEdit.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);
    }

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNim, tvKelas;
        ImageView btnEdit, btnDelete, ivProfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvKelas = itemView.findViewById(R.id.tvKelas);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivProfil = itemView.findViewById(R.id.ivProfil);
        }
    }
}
