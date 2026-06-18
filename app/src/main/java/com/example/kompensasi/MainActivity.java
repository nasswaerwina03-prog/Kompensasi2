package com.example.kompensasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    MaterialButton btnLogin;
    TextView tvLupaPassword;

    DatabaseHelper databaseHelper;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLupaPassword = findViewById(R.id.tvLupaPassword);

        tvLupaPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LupaPasswordActivity.class);
            startActivity(intent);
        });

        // ================= INISIALISASI DATABASE =================
        // Posisi ini digunakan untuk membuat objek DatabaseHelper
        // yang akan dipakai untuk mengecek data login ke SQLite.
        databaseHelper =
                new DatabaseHelper(this);

        // ================= AKSI TOMBOL LOGIN =================
        // Posisi ini menangkap event ketika tombol login diklik.
        btnLogin.setOnClickListener(v -> {

            String username =
                    etUsername.getText()
                            .toString()
                            .trim();

            String password =
                    etPassword.getText()
                            .toString()
                            .trim();

            // ================= VALIDASI INPUT =================
            // Posisi ini mengecek apakah form username dan password kosong.
            // Jika kosong, proses akan berhenti (return) dan menampilkan error.

            if (username.isEmpty()) {

                etUsername.setError(
                        "Username wajib diisi"
                );

                showErrorSnackbar(
                        v,
                        "Username belum diisi"
                );

                return;
            }

            if (password.isEmpty()) {

                etPassword.setError(
                        "Password wajib diisi"
                );

                showErrorSnackbar(
                        v,
                        "Password belum diisi"
                );

                return;
            }

            // ================= LOGIN USER (ADMIN, DOSEN, DLL) =================
            // Posisi ini menjalankan query ke database (tabel tb_user)
            // untuk mencari apakah username & password cocok.

            Cursor cursor =
                    databaseHelper.loginUser(
                            username,
                            password
                    );

            // ================= JIKA USER DITEMUKAN =================
            // Posisi ini mengecek apakah hasil query ada (moveToFirst).
            // Jika ada, berarti login sukses sebagai Admin/Dosen/Sekjur/Kajur.

            if (cursor.moveToFirst()) {

                String role =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "role"
                                )
                        );
                SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                if (role.equals("dosenpa")) {
                    editor.putString(
                            "kelas_dosen",
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "kelas"
                                    )
                            )
                    );

                    editor.putString(
                            "nama_dosen",
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "nama"
                                    )
                            )
                    );

                    editor.putString(
                            "username_dosen",
                            username
                    );
                }

                editor.putString(
                        "nama",
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "nama"
                                )
                        )
                );

                editor.putString(
                        "username",
                        username
                );

                editor.putString(
                        "role",
                        role
                );

                editor.apply();

                // ================= PENGALIHAN HALAMAN (ROUTING) =================
                // Posisi ini menentukan halaman mana yang akan dibuka
                // berdasarkan role (peran) user yang login.
                Intent intent = null;

                switch (role) {

                    case "admin":

                        intent = new Intent(
                                MainActivity.this,
                                DashboardAdminActivity.class
                        );

                        break;

                    case "dosenpa":

                        intent = new Intent(
                                MainActivity.this,
                                DashboardDosenPaActivity.class
                        );

                        break;

                    case "kajur":

                        intent = new Intent(
                                MainActivity.this,
                                DashboardKajurActivity.class
                        );

                        break;

                    case "sekjur":

                        intent = new Intent(
                                MainActivity.this,
                                DashboardSekjurActivity.class
                        );

                        break;
                }

                Snackbar snackbar =
                        Snackbar.make(
                                v,
                                "Login berhasil 👋",
                                Snackbar.LENGTH_LONG
                        );

                snackbar.setBackgroundTint(
                        Color.parseColor("#B1456E")
                );

                snackbar.setTextColor(
                        Color.WHITE
                );

                snackbar.show();

                Intent finalIntent = intent;

                v.postDelayed(() -> {

                    startActivity(finalIntent);

                    overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    );

                    finish();

                }, 1200);

            } else {

                // ================= CEK LOGIN MAHASISWA =================
                // Jika tidak ditemukan di tb_user, posisi ini akan mengecek
                // ke tabel tb_mahasiswa menggunakan nim dan password.

                cursor =
                        databaseHelper.loginMahasiswa(
                                username,
                                password
                        );

                if (cursor.moveToFirst()) {

                    String nimMahasiswa =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "nim"
                                    )
                            );

                    Snackbar snackbar =
                            Snackbar.make(
                                    v,
                                    "Login mahasiswa berhasil 👋",
                                    Snackbar.LENGTH_LONG
                            );

                    snackbar.setBackgroundTint(
                            Color.parseColor("#B1456E")
                    );

                    snackbar.setTextColor(
                            Color.WHITE
                    );

                    snackbar.show();

                    SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nim_mahasiswa", nimMahasiswa);
                    editor.apply();

                    v.postDelayed(() -> {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        DashboardMahasiswaActivity.class
                                );

                        // ================= KIRIM SESSION NIM =================

                        intent.putExtra(
                                "nim",
                                nimMahasiswa
                        );

                        startActivity(intent);

                        overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );

                        finish();

                    }, 1200);

                } else {

                    showErrorSnackbar(
                            v,
                            "Username / NIM atau Password Salah"
                    );
                }
            }

            cursor.close();

        });

    }

    // ================= ERROR SNACKBAR =================

    private void showErrorSnackbar(
            android.view.View view,
            String message
    ) {

        Snackbar snackbar =
                Snackbar.make(
                        view,
                        message,
                        Snackbar.LENGTH_LONG
                );

        snackbar.setBackgroundTint(
                Color.parseColor("#D32F2F")
        );

        snackbar.setTextColor(
                Color.WHITE
        );

        snackbar.show();
    }
}