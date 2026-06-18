package com.example.kompensasi;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MahasiswaAdapter extends
        RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    // ================= COMPONENT =================

    Context context;

    ArrayList<MahasiswaModel> list;

    DatabaseHelper databaseHelper;

    String role;

    // ================= CONSTRUCTOR =================

    public MahasiswaAdapter(
            Context context,
            ArrayList<MahasiswaModel> list,
            String role
    ) {

        this.context = context;

        this.list = list;

        this.role = role;

        databaseHelper =
                new DatabaseHelper(context);
    }

    // ================= CREATE VIEW =================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_mahasiswa,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    // ================= BIND VIEW =================

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        MahasiswaModel model =
                list.get(position);

        // ================= SET DATA =================

        holder.tvNama.setText(
                model.getNama()
        );

        holder.tvNim.setText(
                model.getNim()
        );

        holder.tvKelas.setText(
                model.getKelas()
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

        // ================= ROLE BASED VISIBILITY =================

        if (role != null && !role.equals("admin")) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        // ================= EDIT =================

        holder.btnEdit.setOnClickListener(v -> {

            showEditDialog(
                    model
            );
        });

        // ================= DELETE =================

        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle(
                    "Hapus Mahasiswa"
            );

            builder.setMessage(
                    "Yakin ingin menghapus mahasiswa ini?"
            );

            builder.setPositiveButton(
                    "Hapus",
                    (dialog, which) -> {

                        databaseHelper.deleteMahasiswa(
                                model.getId()
                        );

                        list.remove(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(
                                position,
                                list.size()
                        );

                        Toast.makeText(
                                context,
                                "Mahasiswa berhasil dihapus",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
            );

            builder.setNegativeButton(
                    "Batal",
                    null
            );

            builder.show();
        });
    }

    // ================= EDIT DIALOG =================

    private void showEditDialog(
            MahasiswaModel model
    ) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        builder.setTitle(
                "Edit Mahasiswa"
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

        // ================= NIM =================

        EditText etNim =
                new EditText(context);

        etNim.setHint("NIM");

        etNim.setText(
                model.getNim()
        );

        layout.addView(etNim);

        // ================= NAMA =================

        EditText etNama =
                new EditText(context);

        etNama.setHint("Nama Mahasiswa");

        etNama.setText(
                model.getNama()
        );

        layout.addView(etNama);

        // ================= KELAS =================

        EditText etKelas =
                new EditText(context);

        etKelas.setHint("Kelas");

        etKelas.setText(
                model.getKelas()
        );

        layout.addView(etKelas);

        // ================= PASSWORD =================

        EditText etPassword =
                new EditText(context);

        etPassword.setHint("Password");

        etPassword.setText(
                model.getPassword()
        );

        layout.addView(etPassword);

        // ================= SET VIEW =================

        builder.setView(scrollView);

        // ================= BUTTON UPDATE =================

        builder.setPositiveButton(
                "Update",
                (dialog, which) -> {

                    String nim =
                            etNim.getText()
                                    .toString()
                                    .trim();

                    String nama =
                            etNama.getText()
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

                    // ================= VALIDASI =================

                    if (
                            nim.isEmpty() ||
                                    nama.isEmpty() ||
                                    kelas.isEmpty() ||
                                    password.isEmpty()
                    ) {

                        Toast.makeText(
                                context,
                                "Semua field wajib diisi",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    // ================= UPDATE DATABASE =================

                    databaseHelper.updateMahasiswa(
                            model.getId(),
                            nim,
                            nama,
                            kelas,
                            password
                    );

                    // ================= UPDATE MODEL =================

                    model.setNim(nim);

                    model.setNama(nama);

                    model.setKelas(kelas);

                    model.setPassword(password);

                    // ================= REFRESH ADAPTER =================

                    notifyDataSetChanged();

                    Toast.makeText(
                            context,
                            "Mahasiswa berhasil diupdate",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        // ================= BUTTON BATAL =================

        builder.setNegativeButton(
                "Batal",
                null
        );

        // ================= SHOW =================

        builder.show();
    }

    // ================= TOTAL DATA =================

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {

        return list.size();
    }

    // ================= VIEWHOLDER =================

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNama,
                tvNim,
                tvKelas;

        ImageView btnEdit,
                btnDelete,
                ivProfil;

        public ViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvNama =
                    itemView.findViewById(
                            R.id.tvNama
                    );

            tvNim =
                    itemView.findViewById(
                            R.id.tvNim
                    );

            tvKelas =
                    itemView.findViewById(
                            R.id.tvKelas
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );

            ivProfil = 
                    itemView.findViewById(
                            R.id.ivProfil
                    );
        }
    }
}