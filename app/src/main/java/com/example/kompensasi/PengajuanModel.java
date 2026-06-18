package com.example.kompensasi;

public class PengajuanModel {

    // ================= ATTRIBUTE =================

    int idPengajuan,
            idMahasiswa,
            idKompensasi;

    String namaMahasiswa,
            tugasKompensasi,
            kelas,
            tanggalPengajuan,
            buktiVideo,
            statusDosenPa,
            statusSekjur,
            statusKajur,
            catatanPenolakan,
            keterangan;

    // ================= CONSTRUCTOR =================

    public PengajuanModel(
            int idPengajuan,
            int idMahasiswa,
            int idKompensasi,
            String namaMahasiswa,
            String tugasKompensasi,
            String kelas,
            String tanggalPengajuan,
            String buktiVideo,
            String statusDosenPa,
            String statusSekjur,
            String statusKajur,
            String catatanPenolakan,
            String keterangan
    ) {

        this.idPengajuan = idPengajuan;

        this.idMahasiswa = idMahasiswa;

        this.idKompensasi = idKompensasi;

        this.namaMahasiswa = namaMahasiswa;

        this.tugasKompensasi = tugasKompensasi;

        this.kelas = kelas;

        this.tanggalPengajuan = tanggalPengajuan;

        this.buktiVideo = buktiVideo;

        this.statusDosenPa = statusDosenPa;

        this.statusSekjur = statusSekjur;

        this.statusKajur = statusKajur;

        this.catatanPenolakan = catatanPenolakan;

        this.keterangan = keterangan;
    }

    // ================= GETTER =================

    public int getIdPengajuan() {

        return idPengajuan;
    }

    public int getIdMahasiswa() {

        return idMahasiswa;
    }

    public int getIdKompensasi() {

        return idKompensasi;
    }

    public String getNamaMahasiswa() {

        return namaMahasiswa;
    }

    public String getTugasKompensasi() {

        return tugasKompensasi;
    }

    public String getKelas() {

        return kelas;
    }

    public String getTanggalPengajuan() {

        return tanggalPengajuan;
    }

    public String getBuktiVideo() {

        return buktiVideo;
    }

    public String getStatusDosenPa() {

        return statusDosenPa;
    }

    public String getStatusSekjur() {

        return statusSekjur;
    }

    public String getStatusKajur() {

        return statusKajur;
    }

    // ================= SETTER =================

    public void setIdPengajuan(
            int idPengajuan
    ) {

        this.idPengajuan = idPengajuan;
    }

    public void setIdMahasiswa(
            int idMahasiswa
    ) {

        this.idMahasiswa = idMahasiswa;
    }

    public void setIdKompensasi(
            int idKompensasi
    ) {

        this.idKompensasi = idKompensasi;
    }

    public void setNamaMahasiswa(
            String namaMahasiswa
    ) {

        this.namaMahasiswa = namaMahasiswa;
    }

    public void setTugasKompensasi(
            String tugasKompensasi
    ) {

        this.tugasKompensasi = tugasKompensasi;
    }

    public void setKelas(
            String kelas
    ) {

        this.kelas = kelas;
    }

    public void setTanggalPengajuan(
            String tanggalPengajuan
    ) {

        this.tanggalPengajuan = tanggalPengajuan;
    }

    public void setBuktiVideo(
            String buktiVideo
    ) {

        this.buktiVideo = buktiVideo;
    }

    public void setStatusDosenPa(
            String statusDosenPa
    ) {

        this.statusDosenPa = statusDosenPa;
    }

    public void setStatusSekjur(
            String statusSekjur
    ) {

        this.statusSekjur = statusSekjur;
    }

    public void setStatusKajur(
            String statusKajur
    ) {

        this.statusKajur = statusKajur;
    }

    public String getCatatanPenolakan() {
        return catatanPenolakan;
    }

    public void setCatatanPenolakan(String catatanPenolakan) {
        this.catatanPenolakan = catatanPenolakan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}