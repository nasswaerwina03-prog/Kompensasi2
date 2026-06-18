package com.example.kompensasi;

public class SesiModel {

    int idSesi, idKompensasi, menit;
    String tugas, jamMulai, jamSelesai, tanggal, status, alasan, bukti;

    public SesiModel(int idSesi, int idKompensasi, String tugas, String jamMulai,
                     String jamSelesai, int menit, String tanggal, String status, String alasan, String bukti) {
        this.idSesi = idSesi;
        this.idKompensasi = idKompensasi;
        this.tugas = tugas;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.menit = menit;
        this.tanggal = tanggal;
        this.status = status;
        this.alasan = alasan;
        this.bukti = bukti;
    }

    public int getIdSesi() { return idSesi; }
    public int getIdKompensasi() { return idKompensasi; }
    public String getTugas() { return tugas; }
    public String getJamMulai() { return jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public int getMenit() { return menit; }
    public String getTanggal() { return tanggal; }
    public String getStatus() { return status; }
    public String getAlasan() { return alasan; }
    public String getBukti() { return bukti; }
}
