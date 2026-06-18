package com.example.kompensasi;

public class KompensasiModel {

    int id,
            jumlahmenit,
            sisamenit;

    String namaMahasiswa,
            tugasKompensasi,
            statusFinal,
            tanggalMulai,
            tanggalKompensasi,
            tanggalSelesai,
            jamMulai,
            jamSelesai,
            fotoProfil;
    String nim,
            kelas;

    public KompensasiModel(
            int id,
            String namaMahasiswa,
            String nim,
            String kelas,
            int jumlahmenit,
            int sisamenit,
            String tugasKompensasi,
            String statusFinal,
            String tanggalMulai,
            String tanggalKompensasi,
            String tanggalSelesai,
            String jamMulai,
            String jamSelesai,
            String fotoProfil
    ) {

        this.id = id;

        this.namaMahasiswa = namaMahasiswa;

        this.nim = nim;

        this.kelas = kelas;

        this.jumlahmenit = jumlahmenit;

        this.sisamenit = sisamenit;

        this.tugasKompensasi = tugasKompensasi;

        this.statusFinal = statusFinal;

        this.tanggalMulai = tanggalMulai;

        this.tanggalKompensasi = tanggalKompensasi;

        this.tanggalSelesai = tanggalSelesai;

        this.jamMulai = jamMulai;

        this.jamSelesai = jamSelesai;

        this.fotoProfil = fotoProfil;
    }

    // ================= GETTER =================

    public int getId() {
        return id;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public int getJumlahmenit() {
        return jumlahmenit;
    }
    public String getNim() {
        return nim;
    }

    public String getKelas() {
        return kelas;
    }
    public int getSisamenit() {
        return sisamenit;
    }

    public String getTugasKompensasi() {
        return tugasKompensasi;
    }

    public String getStatusFinal() {
        return statusFinal;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public String getTanggalKompensasi() {
        return tanggalKompensasi;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    // ================= SETTER =================

    public void setNamaMahasiswa(
            String namaMahasiswa
    ) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public void setJumlahmenit(
            int jumlahmenit
    ) {
        this.jumlahmenit = jumlahmenit;
    }

    public void setSisamenit(
            int sisamenit
    ) {
        this.sisamenit = sisamenit;
    }
    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public void setTugasKompensasi(
            String tugasKompensasi
    ) {
        this.tugasKompensasi = tugasKompensasi;
    }

    public void setStatusFinal(
            String statusFinal
    ) {
        this.statusFinal = statusFinal;
    }

    public void setTanggalMulai(
            String tanggalMulai
    ) {
        this.tanggalMulai = tanggalMulai;
    }

    public void setTanggalKompensasi(
            String tanggalKompensasi
    ) {
        this.tanggalKompensasi = tanggalKompensasi;
    }

    public void setTanggalSelesai(
            String tanggalSelesai
    ) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }
}