package com.example.kompensasi;

public class MahasiswaModel {

    // ================= ATTRIBUTE =================

    int id;

    String nim,
            nama,
            kelas,
            password,
            fotoProfil;

    // ================= CONSTRUCTOR =================

    public MahasiswaModel(
            int id,
            String nim,
            String nama,
            String kelas,
            String password,
            String fotoProfil
    ) {

        this.id = id;

        this.nim = nim;

        this.nama = nama;

        this.kelas = kelas;

        this.password = password;

        this.fotoProfil = fotoProfil;
    }

    // ================= GETTER =================

    public int getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    // ================= SETTER =================

    public void setId(int id) {
        this.id = id;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }
}