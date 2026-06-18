package com.example.kompensasi;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import android.widget.AdapterView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class UserAdapter
        extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    // ================= VARIABLE =================

    Context context;

    ArrayList<UserModel> userList;

    DatabaseHelper databaseHelper;

    // ================= CONSTRUCTOR =================

    public UserAdapter(
            Context context,
            ArrayList<UserModel> userList
    ) {

        this.context = context;

        this.userList = userList;

        databaseHelper =
                new DatabaseHelper(context);
    }

    // ================= VIEW HOLDER =================

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNama,
                tvUsername,
                tvPassword,
                tvRole;

        ImageView ivProfil;

        ImageButton btnEdit,
                btnDelete;

        MaterialCardView cardRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfil =
                    itemView.findViewById(R.id.ivProfil);
            tvNama =
                    itemView.findViewById(R.id.tvNama);
            tvUsername =
                    itemView.findViewById(R.id.tvUsername);

            tvPassword =
                    itemView.findViewById(R.id.tvPassword);

            tvRole =
                    itemView.findViewById(R.id.tvRole);

            btnEdit =
                    itemView.findViewById(R.id.btnEdit);

            btnDelete =
                    itemView.findViewById(R.id.btnDelete);

            cardRole =
                    itemView.findViewById(R.id.cardRole);
        }
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
                                R.layout.item_user,
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

        UserModel user =
                userList.get(position);

        // ================= DATA =================

        holder.tvNama.setText(
                user.getNama()
        );

        holder.tvUsername.setText(
                user.getUsername()
        );

        holder.tvPassword.setText(
                "Password : " + user.getPassword()
        );

        holder.tvRole.setText(
                user.getRole()
        );

        // ================= AVATAR =================

        String fotoBase64 = user.getFotoProfil();
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

        // ================= ROLE COLOR =================

        String role =
                user.getRole();

        switch (role) {

            case "admin":

                holder.cardRole.setCardBackgroundColor(
                        Color.parseColor("#FFF0F5")
                );

                holder.tvRole.setTextColor(
                        Color.parseColor("#B1456E")
                );

                break;

            case "mahasiswa":

                holder.cardRole.setCardBackgroundColor(
                        Color.parseColor("#EEF7FF")
                );

                holder.tvRole.setTextColor(
                        Color.parseColor("#1976D2")
                );

                break;

            case "dosenpa":

                holder.cardRole.setCardBackgroundColor(
                        Color.parseColor("#F4F1FF")
                );

                holder.tvRole.setTextColor(
                        Color.parseColor("#7B61FF")
                );

                break;

            case "kajur":

                holder.cardRole.setCardBackgroundColor(
                        Color.parseColor("#FFF7E8")
                );

                holder.tvRole.setTextColor(
                        Color.parseColor("#F9A825")
                );

                break;

            case "sekjur":

                holder.cardRole.setCardBackgroundColor(
                        Color.parseColor("#E8FFF3")
                );

                holder.tvRole.setTextColor(
                        Color.parseColor("#00A86B")
                );

                break;
        }

        // ================= DELETE =================

        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle("Hapus User");

            builder.setMessage(
                    "Yakin ingin menghapus user ini?"
            );

            builder.setPositiveButton(
                    "Hapus",
                    (dialog, which) -> {

                        databaseHelper.deleteUser(
                                user.getId()
                        );

                        int currentPosition =
                                holder.getAdapterPosition();

                        if (
                                currentPosition !=
                                        RecyclerView.NO_POSITION
                        ) {

                            userList.remove(currentPosition);

                            notifyItemRemoved(currentPosition);

                            notifyItemRangeChanged(
                                    currentPosition,
                                    userList.size()
                            );
                        }

                        Toast.makeText(
                                context,
                                "User berhasil dihapus",
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

// ================= EDIT =================

        holder.btnEdit.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle("Edit User");

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

            EditText etNama =
                    new EditText(context);

            etNama.setHint("Nama");
            etNama.setText(user.getNama());

            layout.addView(etNama);

// ================= USERNAME =================

            EditText etUsername =
                    new EditText(context);

            etUsername.setHint("Username");
            etUsername.setText(user.getUsername());

            layout.addView(etUsername);

            // ================= PASSWORD =================

            EditText etPassword =
                    new EditText(context);

            etPassword.setHint("Password");

            etPassword.setText(
                    user.getPassword()
            );

            layout.addView(etPassword);

            // ================= KELAS =================

            EditText etKelas =
                    new EditText(context);

            etKelas.setHint("Kelas Dosen PA");

            etKelas.setText(
                    user.getKelas()
            );

            layout.addView(etKelas);

            // ================= ROLE =================

            Spinner spRole =
                    new Spinner(context);

            String[] roleList = {
                    "admin",
                    "dosenpa",
                    "kajur",
                    "sekjur"
            };

            ArrayAdapter<String> adapterRole =
                    new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            roleList
                    );

            spRole.setAdapter(adapterRole);
            // ================= ROLE LISTENER =================

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

            // ================= SELECT ROLE =================

            for (int i = 0; i < roleList.length; i++) {

                if (
                        roleList[i].equals(
                                user.getRole()
                        )
                ) {

                    spRole.setSelection(i);

                    break;
                }
            }

            layout.addView(spRole);


            builder.setView(scrollView);

            // ================= BUTTON UPDATE =================

            builder.setPositiveButton(
                    "Update",
                    (dialog, which) -> {

                        String newUsername =
                                etUsername.getText()
                                        .toString()
                                        .trim();
                        String newNama =
                                etNama.getText()
                                        .toString()
                                        .trim();
                        String newPassword =
                                etPassword.getText()
                                        .toString()
                                        .trim();

                        String newRole =
                                spRole.getSelectedItem()
                                        .toString();
                        String newKelas =
                                etKelas.getText()
                                        .toString()
                                        .trim();
                        // ================= VALIDASI =================

                        if (
                                newUsername.isEmpty() ||
                                        newPassword.isEmpty()
                        ) {

                            Toast.makeText(
                                    context,
                                    "Data tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }
                        if (
                                newRole.equals("dosenpa") &&
                                        newKelas.isEmpty()
                        ) {

                            Toast.makeText(
                                    context,
                                    "Kelas dosen PA wajib diisi",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // ================= UPDATE DATABASE =================

                        databaseHelper.updateUser(
                                user.getId(),
                                newNama,
                                newUsername,
                                newPassword,
                                newRole,
                                newKelas
                        );

                        // ================= UPDATE LIST =================

                        user.setNama(newNama);
                        user.setUsername(newUsername);

                        user.setPassword(newPassword);

                        user.setRole(newRole);
                        user.setKelas(newKelas);

                        notifyDataSetChanged();

                        Toast.makeText(
                                context,
                                "User berhasil diupdate",
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

    // ================= TOTAL DATA =================

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {

        return userList.size();
    }
}