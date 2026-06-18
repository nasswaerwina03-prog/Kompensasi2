package com.example.kompensasi;

public class UserModel {

    // ================= ATTRIBUTE =================

    int id;

    String nama,
            username,
            password,
            role,
            kelas,
            fotoProfil;

    // ================= CONSTRUCTOR =================

    public UserModel(
            int id,
            String nama,
            String username,
            String password,
            String role,
            String kelas,
            String fotoProfil
    ) {

        this.id = id;

        this.nama = nama;

        this.username = username;

        this.password = password;

        this.role = role;

        this.kelas = kelas;

        this.fotoProfil = fotoProfil;
    }

    // ================= GETTER =================

    public int getId() {

        return id;
    }

    public String getNama() {

        return nama;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public String getRole() {

        return role;
    }

    public String getKelas() {

        return kelas;
    }

    public String getFotoProfil() {

        return fotoProfil;
    }

    // ================= SETTER =================

    public void setId(int id) {

        this.id = id;
    }

    public void setNama(String nama) {

        this.nama = nama;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setRole(String role) {

        this.role = role;
    }

    public void setKelas(String kelas) {

        this.kelas = kelas;
    }

    public void setFotoProfil(String fotoProfil) {

        this.fotoProfil = fotoProfil;
    }
}