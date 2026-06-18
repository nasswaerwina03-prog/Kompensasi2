package com.example.kompensasi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity
        extends AppCompatActivity {

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_edit_user
        );
    }
}