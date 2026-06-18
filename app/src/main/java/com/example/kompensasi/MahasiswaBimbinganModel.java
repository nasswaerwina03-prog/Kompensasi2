package com.example.kompensasi;

public class MahasiswaBimbinganModel {
    int idMahasiswa;
    String nim;
    String nama;
    String kelas;
    int totalKompen;
    String fotoProfil;

    public MahasiswaBimbinganModel(int idMahasiswa, String nim, String nama, String kelas, int totalKompen, String fotoProfil) {
        this.idMahasiswa = idMahasiswa;
        this.nim = nim;
        this.nama = nama;
        this.kelas = kelas;
        this.totalKompen = totalKompen;
        this.fotoProfil = fotoProfil;
    }

    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getKelas() {
        return kelas;
    }

    public int getTotalKompen() {
        return totalKompen;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }
}
