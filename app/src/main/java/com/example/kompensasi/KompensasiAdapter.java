package com.example.kompensasi;
import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.app.DatePickerDialog;

import java.util.Calendar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class KompensasiAdapter
        extends RecyclerView.Adapter<KompensasiAdapter.ViewHolder> {

    Context context;

    ArrayList<KompensasiModel> list;

    DatabaseHelper databaseHelper;

    public KompensasiAdapter(
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
                                R.layout.item_kompensasi,
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

        // ================= SET DATA =================

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
        holder.tvNim.setText(
                "NIM : " +
                        model.getNim()
        );

        holder.tvKelas.setText(
                "Kelas : " +
                        model.getKelas()
        );

        holder.tvStatus.setText(
                model.getStatusFinal()
        );
        if (model.getStatusFinal().equalsIgnoreCase(
                "Selesai"
        )) {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#4CAF50")
            );

        } else if (model.getStatusFinal().equalsIgnoreCase(
                "Pending"
        )) {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#FF9800")
            );

        } else {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#E57373")
            );
        }
        if (model.getStatusFinal().equalsIgnoreCase(
                "Selesai"
        )) {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#4CAF50")
            );

        } else if (model.getStatusFinal().equalsIgnoreCase(
                "Pending"
        )) {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#FF9800")
            );

        } else {

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#E57373")
            );
        }

        holder.tvTanggalMulai.setText(
                "Mulai : " +
                        model.getTanggalMulai()
        );

        holder.tvTanggalKompensasi.setText(
                "Kompensasi : " +
                        model.getTanggalKompensasi()
        );

        holder.tvTanggalSelesai.setText(
                "Selesai : " +
                        model.getTanggalSelesai()
        );

        // ================= FOTO PROFIL =================
        String fotoBase64 = model.getFotoProfil();
        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(fotoBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ivProfil.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
                holder.ivProfil.setImageResource(R.drawable.ic_person);
            }
        } else {
            holder.ivProfil.setImageResource(R.drawable.ic_person);
        }

        // ================= DELETE =================

        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle(
                    "Hapus Kompensasi"
            );

            builder.setMessage(
                    "Yakin ingin menghapus data ini?"
            );

            builder.setPositiveButton(
                    "Hapus",
                    (dialog, which) -> {

                        databaseHelper.deleteKompensasi(
                                model.getId()
                        );

                        list.remove(position);

                        notifyDataSetChanged();

                        Snackbar snackbar =
                                Snackbar.make(
                                        v,
                                        "Data berhasil dihapus",
                                        Snackbar.LENGTH_SHORT
                                );

                        snackbar.setBackgroundTint(
                                Color.parseColor("#B1456E")
                        );

                        snackbar.setTextColor(
                                Color.WHITE
                        );

                        snackbar.show();
                    }
            );

            builder.setNegativeButton(
                    "Batal",
                    null
            );

            builder.show();
        });

        // ================= DETAIL =================

        holder.btnDetail.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            DetailKompensasiActivity.class
                    );

            intent.putExtra(
                    "id_kompensasi",
                    model.getId()
            );

            context.startActivity(intent);

        });
        // ================= EDIT =================

        holder.btnEdit.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle(
                    "Edit Kompensasi"
            );

            android.widget.ScrollView scrollView = new android.widget.ScrollView(context);

            LinearLayout layout =
                    new LinearLayout(context);

            layout.setOrientation(
                    LinearLayout.VERTICAL
            );

            layout.setPadding(
                    40,
                    30,
                    40,
                    10
            );

            scrollView.addView(layout);

            // ================= NAMA =================

            TextView tvNama =
                    new TextView(context);

            tvNama.setText(
                    "Mahasiswa : "
                            + model.getNamaMahasiswa()
            );

            tvNama.setTextSize(16f);

            layout.addView(tvNama);

            // ================= JUMLAH menit =================

            EditText etmenit =
                    new EditText(context);

            etmenit.setHint(
                    "Jumlah Kompensasi"
            );

            etmenit.setText(
                    String.valueOf(
                            model.getJumlahmenit()
                    )
            );

            layout.addView(etmenit);
            // ================= TANGGAL MULAI =================

            TextView etTanggalMulai =
                    new TextView(context);

            etTanggalMulai.setText(
                    model.getTanggalMulai()
            );

            etTanggalMulai.setTextSize(16f);

            etTanggalMulai.setPadding(
                    30,
                    30,
                    30,
                    30
            );

            etTanggalMulai.setBackgroundColor(
                    Color.parseColor("#F5F5F5")
            );

            layout.addView(etTanggalMulai);

// ================= DATE PICKER =================

            etTanggalMulai.setOnClickListener(view -> {

                Calendar calendar =
                        Calendar.getInstance();

                DatePickerDialog dialog =
                        new DatePickerDialog(
                                context,
                                (datePicker,
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

            builder.setView(scrollView);
            // ================= UPDATE =================

            builder.setPositiveButton(
                    "Update",
                    (dialog, which) -> {

                        String menitString =
                                etmenit.getText()
                                        .toString()
                                        .trim();
                        String tanggalMulai =
                                etTanggalMulai.getText()
                                        .toString()
                                        .trim();

                        if (menitString.isEmpty()) {

                            Toast.makeText(
                                    context,
                                    "Jumlah menit wajib diisi",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        int menit =
                                Integer.parseInt(
                                        menitString
                                );

                        databaseHelper.updateKompensasi(
                                model.getId(),
                                menit,
                                tanggalMulai
                        );

                        model.setJumlahmenit(
                                menit
                        );

                        model.setTanggalMulai(
                                tanggalMulai
                        );

                        model.setSisamenit(
                                menit
                        );

                        notifyDataSetChanged();

                        Snackbar snackbar =
                                Snackbar.make(
                                        v,
                                        "Data berhasil diupdate",
                                        Snackbar.LENGTH_SHORT
                                );

                        snackbar.setBackgroundTint(
                                Color.parseColor("#B1456E")
                        );

                        snackbar.setTextColor(
                                Color.WHITE
                        );

                        snackbar.show();
                    }
            );

            builder.setNegativeButton(
                    "Batal",
                    null
            );

            builder.show();
        });
    }

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {

        return list.size();
    }

    // ================= VIEW HOLDER =================

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView
                tvNamaMahasiswa,
                tvJumlahmenit,
                tvSisamenit;
        TextView tvNim,
                tvKelas,
                tvStatus,
                tvTanggalMulai,
                tvTanggalKompensasi,
                tvTanggalSelesai;

        ImageView btnDelete,
                btnEdit,
                btnDetail,
                ivProfil;

        public ViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvNamaMahasiswa =
                    itemView.findViewById(
                            R.id.tvNamaMahasiswa
                    );
            tvSisamenit =
                    itemView.findViewById(
                            R.id.tvSisamenit
                    );
            tvJumlahmenit =
                    itemView.findViewById(
                            R.id.tvJumlahmenit
                    );
            tvNim =
                    itemView.findViewById(R.id.tvNim);

            tvKelas =
                    itemView.findViewById(R.id.tvKelas);

            tvStatus =
                    itemView.findViewById(R.id.tvStatus);

            tvTanggalMulai =
                    itemView.findViewById(R.id.tvTanggalMulai);

            tvTanggalKompensasi =
                    itemView.findViewById(R.id.tvTanggalKompensasi);

            tvTanggalSelesai =
                    itemView.findViewById(R.id.tvTanggalSelesai);

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDetail =
                    itemView.findViewById(
                            R.id.btnDetail
                    );

            ivProfil = 
                    itemView.findViewById(
                            R.id.ivProfil
                    );
        }
    }
}