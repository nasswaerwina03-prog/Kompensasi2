package com.example.kompensasi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ValidasiKajurAdapter
        extends RecyclerView.Adapter<
        ValidasiKajurAdapter.ViewHolder> {

    Context context;

    ArrayList<PengajuanModel> list;

    DatabaseHelper databaseHelper;
    Runnable onDataChanged;

    public ValidasiKajurAdapter(
            Context context,
            ArrayList<PengajuanModel> list,
            Runnable onDataChanged
    ) {

        this.context = context;

        this.list = list;
        
        this.onDataChanged = onDataChanged;

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
                                R.layout.item_validasi_kajur,
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

        PengajuanModel model =
                list.get(position);

        holder.tvTugas.setText(
                model.getNamaMahasiswa() +
                        " - " +
                        model.getKelas()
        );

        holder.tvTanggal.setText(
                model.getTanggalPengajuan()
        );

        holder.tvStatus.setText(
                model.getStatusKajur()
        );

        // ================= STATUS =================

        if ("Disetujui".equals(model.getStatusKajur())) {

            holder.tvStatus.setTextColor(
                    Color.WHITE
            );

            holder.tvStatus.setBackgroundColor(
                    Color.parseColor("#4CAF50")
            );

            holder.btnApprove.setVisibility(View.GONE);

        } else {

            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnApprove.setText(
                    "Approve"
            );
        }

        // ================= APPROVE =================

        holder.btnApprove.setOnClickListener(v -> {

            if ("Pending".equals(model.getStatusKajur())) {
                // ================= APPROVE KAJUR =================
                databaseHelper.verifikasiKajur(model.getIdPengajuan(), "Disetujui");

                // ================= AUTO SELESAI =================
                databaseHelper.updateStatusFinal(model.getIdKompensasi(), "Selesai");

                // ================= UPDATE SISA MENIT =================
                databaseHelper.updateSisamenit(model.getIdKompensasi(), 0);

                model.setStatusKajur("Disetujui");

                notifyDataSetChanged();
                if (onDataChanged != null) onDataChanged.run();

                Snackbar snackbar = Snackbar.make(v, "Berhasil disetujui!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });


        // ================= DETAIL =================

        holder.btnDetail.setOnClickListener(v -> {

            android.content.Intent intent =
                    new android.content.Intent(
                            context,
                            DetailKompensasiActivity.class
                    );

            intent.putExtra(
                    "id_kompensasi",
                    model.getIdKompensasi()
            );

            // ================= MODE KAJUR =================

            intent.putExtra(
                    "from",
                    "kajur"
            );

            context.startActivity(intent);
        });

        // ================= TOLAK Dihapus (Hanya Approve) =================
    }


    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTugas,
                tvTanggal,
                tvStatus;

        MaterialButton btnApprove,
                btnDetail;

        public ViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvTugas =
                    itemView.findViewById(R.id.tvTugas);

            tvTanggal =
                    itemView.findViewById(R.id.tvTanggal);

            tvStatus =
                    itemView.findViewById(R.id.tvStatus);

            btnApprove =
                    itemView.findViewById(R.id.btnApprove);

            btnDetail =
                    itemView.findViewById(R.id.btnDetail);
        }
    }
}