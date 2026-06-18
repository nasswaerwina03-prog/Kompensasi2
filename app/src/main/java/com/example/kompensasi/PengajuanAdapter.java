package com.example.kompensasi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PengajuanAdapter
        extends RecyclerView.Adapter<PengajuanAdapter.ViewHolder> {

    // ================= ATTRIBUTE =================

    Context context;

    ArrayList<PengajuanModel> list;

    // ================= CONSTRUCTOR =================

    public PengajuanAdapter(
            Context context,
            ArrayList<PengajuanModel> list
    ) {

        this.context = context;

        this.list = list;
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
                                R.layout.item_pengajuan,
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

        PengajuanModel model =
                list.get(position);

        // ================= SET DATA =================

        holder.tvNamaMahasiswa.setText(
                model.getNamaMahasiswa()
        );

        holder.tvTanggal.setText(
                model.getTanggalPengajuan()
        );

        holder.tvTugas.setText(
                model.getTugasKompensasi()
        );

        holder.tvVideo.setText(
                "Video Bukti"
        );

        holder.tvStatusPa.setText(
                model.getStatusDosenPa()
        );

        holder.tvStatusSekjur.setText(
                model.getStatusSekjur()
        );

        holder.tvStatusKajur.setText(
                model.getStatusKajur()
        );

        // ================= STATUS FINAL =================

        String statusFinal;

        if (
                model.getStatusDosenPa()
                        .equalsIgnoreCase("Ditolak")
                        ||

                        model.getStatusSekjur()
                                .equalsIgnoreCase("Ditolak")
                        ||

                        model.getStatusKajur()
                                .equalsIgnoreCase("Ditolak")
        ) {

            statusFinal = "Ditolak";

        } else if (

                model.getStatusDosenPa()
                        .equalsIgnoreCase("Disetujui")
                        &&

                        model.getStatusSekjur()
                                .equalsIgnoreCase("Disetujui")
                        &&

                        model.getStatusKajur()
                                .equalsIgnoreCase("Disetujui")

        ) {

            statusFinal = "Disetujui";

        } else {

            statusFinal = "Pending";
        }

        holder.tvStatus.setText(
                statusFinal
        );

        // ================= PLAY VIDEO =================

        holder.btnPlay.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW
                    );

            intent.setDataAndType(
                    Uri.parse(
                            model.getBuktiVideo()
                    ),
                    "video/*"
            );

            context.startActivity(intent);
        });
    }

    // ================= TOTAL DATA =================

    @Override
    // ================= JUMLAH DATA =================
    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.
    public int getItemCount() {

        return list.size();
    }

    // ================= VIEW HOLDER =================

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNamaMahasiswa,
                tvTanggal,
                tvStatus,
                tvTugas,
                tvVideo,
                tvStatusPa,
                tvStatusSekjur,
                tvStatusKajur;

        ImageView btnPlay;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvNamaMahasiswa =
                    itemView.findViewById(
                            R.id.tvNamaMahasiswa
                    );

            tvTanggal =
                    itemView.findViewById(
                            R.id.tvTanggal
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvStatus
                    );

            tvTugas =
                    itemView.findViewById(
                            R.id.tvTugas
                    );

            tvVideo =
                    itemView.findViewById(
                            R.id.tvVideo
                    );

            tvStatusPa =
                    itemView.findViewById(
                            R.id.tvStatusPa
                    );

            tvStatusSekjur =
                    itemView.findViewById(
                            R.id.tvStatusSekjur
                    );

            tvStatusKajur =
                    itemView.findViewById(
                            R.id.tvStatusKajur
                    );

            btnPlay =
                    itemView.findViewById(
                            R.id.btnPlay
                    );
        }
    }
}