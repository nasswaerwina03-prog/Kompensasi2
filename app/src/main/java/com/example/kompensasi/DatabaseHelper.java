package com.example.kompensasi;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.InputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ================= INISIALISASI DATABASE =================
    // Posisi ini adalah tempat kita mendefinisikan nama file database SQLite
    // dan versi database-nya.
    private static final String DATABASE_NAME =
            "kompensasi.db";

    private static final int DATABASE_VERSION = 16;

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ================= BUAT TABEL USER =================
        // Posisi ini berfungsi untuk menjalankan perintah SQL pembuatan tabel 'tb_user'
        // Tabel ini menyimpan data Admin, Dosen PA, Kajur, dan Sekjur.
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_user (" +
                        "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nama TEXT," +
                        "username TEXT UNIQUE," +
                        "password TEXT," +
                        "role TEXT," +
                        "kelas TEXT," +
                        "foto_profil TEXT," +
                        "email TEXT)"
        );

        // ================= MAHASISWA =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_mahasiswa (" +
                        "id_mahasiswa INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_user INTEGER," +
                        "nim TEXT UNIQUE," +
                        "nama TEXT," +
                        "kelas TEXT," +
                        "password TEXT," +
                        "foto_profil TEXT," +
                        "email TEXT)"
        );

        // ================= ADMIN =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_admin (" +
                        "id_admin INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_user INTEGER," +
                        "nama TEXT)"
        );

        // ================= DOSEN PA =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_dosen_pa (" +
                        "id_dosen_pa INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_user INTEGER," +
                        "nama TEXT)"
        );

        // ================= SEKJUR =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_sekjur (" +
                        "id_sekjur INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_user INTEGER," +
                        "nama TEXT)"
        );

        // ================= KAJUR =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_kajur (" +
                        "id_kajur INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_user INTEGER," +
                        "nama TEXT)"
        );

        // ================= BUAT TABEL KOMPENSASI =================
        // Posisi ini membuat tabel untuk merekam data tugas kompensasi mahasiswa,
        // seperti jumlah menit, sisa menit, status, dan tanggal.
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_kompensasi (" +
                        "id_kompensasi INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_mahasiswa INTEGER," +
                        "jenis_kegiatan TEXT," +
                        "jumlah_menit INTEGER," +
                        "sisa_menit INTEGER," +
                        "tugas_kompensasi TEXT," +
                        "status_final TEXT," +
                        "tanggal_mulai TEXT," +
                        "tanggal_kompensasi TEXT," +
                        "tanggal_selesai TEXT," +
                        "jam_mulai TEXT DEFAULT '-'," +
                        "jam_selesai TEXT DEFAULT '-')"
        );

        // ================= PENGAJUAN =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_pengajuan (" +
                        "id_pengajuan INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_mahasiswa INTEGER," +
                        "id_kompensasi INTEGER," +
                        "tanggal TEXT," +
                        "bukti TEXT," +
                        "status_dosen_pa TEXT," +
                        "status_sekjur TEXT," +
                        "status_kajur TEXT, catatan_penolakan TEXT," +
                        "keterangan TEXT DEFAULT '-')"
        );

        // ================= SESI KOMPENSASI =================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tb_sesi (" +
                        "id_sesi INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_kompensasi INTEGER," +
                        "tugas TEXT," +
                        "jam_mulai TEXT," +
                        "jam_selesai TEXT," +
                        "menit INTEGER," +
                        "tanggal TEXT," +
                        "status TEXT DEFAULT 'Disetujui'," +
                        "alasan TEXT DEFAULT '-'," +
                        "bukti TEXT DEFAULT '-')"
        );

// ================= DEFAULT MAHASISWA =================

        // ================= DEFAULT MAHASISWA =================

        insertDefaultMahasiswa(
                db,
                "511",
                "Ahmad Fauzi",
                "5IA",
                "123"
        );

        insertDefaultMahasiswa(
                db,
                "512",
                "Muhammad Rizki",
                "5IB",
                "123"
        );

        insertDefaultMahasiswa(
                db,
                "513",
                "Dinda Ayu Lestari",
                "5IC",
                "123"
        );
        // ================= DEFAULT DOSEN PA =================

        insertDefaultDosenPa(
                db,
                "Ravie Kurnia Laday, S.Kom., M.T.",
                "198904262019031000",
                "1IA"
        );

        insertDefaultDosenPa(
                db,
                "Nur Jumriatunnisah, S.Kom., M.Kom.",
                "199106092019032000",
                "1IB"
        );

        insertDefaultDosenPa(
                db,
                "Delta Khairunnisa, S.Pd., M.Pd.",
                "199112262022032000",
                "1IC"
        );

        insertDefaultDosenPa(
                db,
                "Dewi Irmawati Siregar, S.Kom., M.Kom.",
                "197709182001122000",
                "1ID"
        );

        insertDefaultDosenPa(
                db,
                "Nurul Ilma Hasana Kunio, S.Pd., M.Pd.",
                "199401212022032000",
                "1IE"
        );

        insertDefaultDosenPa(
                db,
                "M Zulkarnain, S.Kom., M.Kom.",
                "199003292022031000",
                "1IF"
        );

        insertDefaultDosenPa(
                db,
                "Indra Satriadi, S.T., M.Kom.",
                "197211162000031000",
                "1IG"
        );

        insertDefaultDosenPa(
                db,
                "Meivi Kusnandar, S.Kom., M.Kom.",
                "197405072002121000",
                "1IM"
        );

        insertDefaultDosenPa(
                db,
                "Ienda Meiriska, S.Kom., M.Kom.",
                "197905172002122000",
                "1IN"
        );
        // ================= DEFAULT USER =================

        insertDefaultUser(
                db,
                "Administrator",
                "admin",
                "123",
                "admin"
        );
        insertDefaultUser(
                db,
                "Administrator",
                "admin",
                "123",
                "admin"
        );
        insertDefaultUser(
                db,
                "Sony Oktapriandi, S.Kom., M.Kom.",

                "197510272008121000",
                "123",
                "kajur"
        );
        insertDefaultUser(
                db,
                "Sulistiyanto, S.Kom., M.T.I.",
                "196705111992031003",
                "123",
                "sekjur"
        );


    }
    public boolean isSudahMengajukan(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tb_pengajuan " +
                                "WHERE id_kompensasi=?",
                        new String[]{
                                String.valueOf(idKompensasi)
                        }
                );

        boolean ada =
                cursor.getCount() > 0;

        cursor.close();

        return ada;
    }
// ================= GET ID PENGAJUAN =================

    public int getIdPengajuanByKompensasi(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id_pengajuan " +
                                "FROM tb_pengajuan " +
                                "WHERE id_kompensasi=?",
                        new String[]{
                                String.valueOf(idKompensasi)
                        }
                );

        int idPengajuan = -1;

        if (cursor.moveToFirst()) {

            idPengajuan =
                    cursor.getInt(0);
        }

        cursor.close();

        return idPengajuan;
    }
    // ================= INSERT DEFAULT USER =================

    private void insertDefaultUser(
            SQLiteDatabase db,
            String nama,
            String username,
            String password,
            String role
    ){

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tb_user WHERE username=?",
                new String[]{username}
        );

        if (cursor.getCount() == 0) {

            db.execSQL(
                    "INSERT INTO tb_user " +
                            "(nama,username,password,role) VALUES " +
                            "('" + nama + "','" +
                            username + "','" +
                            password + "','" +
                            role + "')"
            );
        }

        cursor.close();
    }
    // ================= DEFAULT DOSEN PA =================

    private void insertDefaultDosenPa(
            SQLiteDatabase db,
            String nama,
            String username,
            String kelas
    ) {

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tb_user WHERE username=?",
                        new String[]{
                                username
                        }
                );

        if (cursor.getCount() == 0) {

            ContentValues values =
                    new ContentValues();

            values.put(
                    "nama",
                    nama
            );

            values.put(
                    "username",
                    username
            );

            values.put(
                    "password",
                    "123"
            );

            values.put(
                    "role",
                    "dosenpa"
            );

            values.put(
                    "kelas",
                    kelas
            );

            db.insert(
                    "tb_user",
                    null,
                    values
            );
        }

        cursor.close();
    }
    // ================= DEFAULT MAHASISWA =================

    private void insertDefaultMahasiswa(
            SQLiteDatabase db,
            String nim,
            String nama,
            String kelas,
            String password
    ) {

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tb_mahasiswa WHERE nim=?",
                        new String[]{nim}
                );

        if (cursor.getCount() == 0) {

            ContentValues values =
                    new ContentValues();

            values.put("nim", nim);

            values.put("nama", nama);

            values.put("kelas", kelas);

            values.put("password", password);

            db.insert(
                    "tb_mahasiswa",
                    null,
                    values
            );
        }

        cursor.close();
    }

    // ================= UPDATE DATABASE =================


    // ================= UPGRADE DATABASE =================
    // Posisi (fungsi) ini akan otomatis dipanggil jika DATABASE_VERSION dinaikkan angkanya.
    // Berfungsi untuk menambah kolom baru ke tabel yang sudah ada tanpa menghapus data lama.
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 5) {

            db.execSQL(
                    "ALTER TABLE tb_kompensasi " +
                            "ADD COLUMN tugas_kompensasi TEXT DEFAULT '-'"
            );

            db.execSQL(
                    "ALTER TABLE tb_kompensasi " +
                            "ADD COLUMN status_final TEXT DEFAULT 'Pending'"
            );

            db.execSQL(
                    "ALTER TABLE tb_kompensasi " +
                            "ADD COLUMN tanggal_kompensasi TEXT DEFAULT '-'"
            );

            db.execSQL(
                    "ALTER TABLE tb_kompensasi " +
                            "ADD COLUMN tanggal_selesai TEXT DEFAULT '-'"
            );
        }
        if (oldVersion < 6) {

            db.execSQL(
                    "ALTER TABLE tb_mahasiswa " +
                            "ADD COLUMN password TEXT DEFAULT '123'"
            );
        }

        if (oldVersion < 7) {

            db.execSQL(
                    "ALTER TABLE tb_user " +
                            "ADD COLUMN kelas TEXT"
            );
        }
        if (oldVersion < 10) {

            db.execSQL(
                    "ALTER TABLE tb_kompensasi " +
                            "ADD COLUMN tanggal_mulai TEXT DEFAULT '-'"

            );
        }

        if (oldVersion < 11) {

            db.execSQL(
                    "ALTER TABLE tb_pengajuan " +
                            "ADD COLUMN catatan_penolakan TEXT"
            );
        }
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE tb_kompensasi ADD COLUMN jam_mulai TEXT DEFAULT '-'");
            db.execSQL("ALTER TABLE tb_kompensasi ADD COLUMN jam_selesai TEXT DEFAULT '-'");
            db.execSQL("ALTER TABLE tb_pengajuan ADD COLUMN keterangan TEXT DEFAULT '-'");
        }
        if (oldVersion < 13) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS tb_sesi (" +
                            "id_sesi INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_kompensasi INTEGER," +
                            "tugas TEXT," +
                            "jam_mulai TEXT," +
                            "jam_selesai TEXT," +
                            "menit INTEGER," +
                            "tanggal TEXT," +
                            "status TEXT DEFAULT 'Disetujui'," +
                            "alasan TEXT DEFAULT '-'," +
                            "bukti TEXT DEFAULT '-')"
            );
        }
        if (oldVersion < 14) {
            try {
                db.execSQL("ALTER TABLE tb_sesi ADD COLUMN bukti TEXT DEFAULT '-'");
            } catch (Exception e) {
                // Column might already exist if table was created in v13 with v14 structure
            }
        }
        if (oldVersion < 15) {
            try {
                db.execSQL("ALTER TABLE tb_user ADD COLUMN foto_profil TEXT");
                db.execSQL("ALTER TABLE tb_mahasiswa ADD COLUMN foto_profil TEXT");
            } catch (Exception e) {
            }
        }
        if (oldVersion < 16) {
            try {
                db.execSQL("ALTER TABLE tb_user ADD COLUMN email TEXT");
                db.execSQL("ALTER TABLE tb_mahasiswa ADD COLUMN email TEXT");
            } catch (Exception e) {
            }
        }
    }


    // ================= FUNGSI LOGIN USER =================
    // Posisi ini digunakan oleh MainActivity untuk mengecek
    // apakah username dan password cocok dengan data di tb_user.
    public Cursor loginUser(
            String username,
            String password
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_user " +
                        "WHERE username=? AND password=?",
                new String[]{
                        username,
                        password
                }
        );
    }
    public Cursor loginMahasiswa(
            String nim,
            String password
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_mahasiswa " +
                        "WHERE nim=? AND password=?",
                new String[]{
                        nim,
                        password
                }
        );
    }

    // ================= USER =================

    public Cursor getAllUsers() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_user",
                null
        );
    }

    public Cursor getUserByRole(
            String role
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_user WHERE role=?",
                new String[]{role}
        );
    }

    public Cursor getUserByUsername(
            String username
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_user WHERE username=?",
                new String[]{username}
        );
    }

    public Cursor searchUser(
            String keyword
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_user " +
                        "WHERE username LIKE ? " +
                        "OR role LIKE ?",
                new String[]{
                        "%" + keyword + "%",
                        "%" + keyword + "%"
                }
        );
    }

    public Cursor getAllRole() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT DISTINCT role FROM tb_user",
                null
        );
    }

    public boolean insertUser(
            String nama,
            String username,
            String password,
            String role,
            String kelas
    ){

        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tb_user WHERE username=?",
                        new String[]{username}
                );

        if (cursor.getCount() > 0) {

            cursor.close();

            db.close();

            return false;
        }

        cursor.close();

        ContentValues values =
                new ContentValues();

        values.put("nama", nama);

        values.put("username", username);

        values.put("password", password);

        values.put("role", role);

        values.put("kelas", kelas);

        long result =
                db.insert(
                        "tb_user",
                        null,
                        values
                );

        db.close();

        return result != -1;
    }

    public void updateUser(
            int id,
            String nama,
            String username,
            String password,
            String role,
            String kelas
    ){

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("nama", nama);

        values.put("username", username);

        values.put("password", password);

        values.put("role", role);

        values.put("kelas", kelas);

        db.update(
                "tb_user",
                values,
                "id_user=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        db.close();

    }

    public void deleteUser(
            int id
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "tb_user",
                "id_user=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        db.close();
    }
// ================= INSERT KOMPENSASI =================

    public long insertKompensasi(
            int idMahasiswa,
            int jumlahmenit,
            String tanggalMulai
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "id_mahasiswa",
                idMahasiswa
        );
        values.put(
                "tanggal_mulai",
                tanggalMulai
        );

        values.put(
                "jumlah_menit",
                jumlahmenit
        );

        // otomatis sisa menit = jumlah menit awal

        values.put(
                "sisa_menit",
                jumlahmenit
        );

        values.put(
                "jenis_kegiatan",
                "-"
        );

        values.put(
                "tugas_kompensasi",
                "-"
        );

        // ================= STATUS FINAL =================
        // ================= UPDATE STATUS =================
        if (jumlahmenit == 0) {
            values.put("status_final", "Selesai");
        } else {
            values.put("status_final", "Belum Kompen");
        }

        values.put(
                "tanggal_kompensasi",
                java.text.DateFormat
                        .getDateInstance()
                        .format(new java.util.Date())
        );

        values.put(
                "tanggal_selesai",
                "-"
        );

        long newId = db.insert(
                "tb_kompensasi",
                null,
                values
        );

        if (jumlahmenit == 0 && newId != -1) {
            // Update kompensasi status to Selesai
            ContentValues updateValues = new ContentValues();
            updateValues.put("status_final", "Selesai");
            db.update("tb_kompensasi", updateValues, "id_kompensasi=?", new String[]{String.valueOf(newId)});

            // Auto insert pengajuan
            ContentValues pengajuanValues = new ContentValues();
            pengajuanValues.put("id_mahasiswa", idMahasiswa);
            pengajuanValues.put("id_kompensasi", (int) newId);
            pengajuanValues.put("tanggal", tanggalMulai);
            pengajuanValues.put("bukti", "-");
            pengajuanValues.put("status_dosen_pa", "Disetujui");
            pengajuanValues.put("status_sekjur", "Disetujui");
            pengajuanValues.put("status_kajur", "Disetujui");
            pengajuanValues.put("keterangan", "Otomatis Disetujui (0 Menit)");
            db.insert("tb_pengajuan", null, pengajuanValues);
        }

        db.close();
        return newId;
    }
// ================= INSERT PENGAJUAN =================

    public void insertPengajuan(
            int idMahasiswa,
            int idKompensasi,
            String tanggal,
            String bukti
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "id_mahasiswa",
                idMahasiswa
        );

        values.put(
                "id_kompensasi",
                idKompensasi
        );

        values.put(
                "tanggal",
                tanggal
        );

        values.put(
                "bukti",
                bukti
        );

        // ================= DEFAULT VERIFIKASI =================

        values.put(
                "status_dosen_pa",
                "Pending"
        );

        values.put(
                "status_sekjur",
                "Pending"
        );

        values.put(
                "status_kajur",
                "Pending"
        );

        db.insert(
                "tb_pengajuan",
                null,
                values
        );

        db.close();
    }

    // ================= INSERT PENGAJUAN LENGKAP (dengan jam & keterangan) =================

    public void insertPengajuanLengkap(
            int idMahasiswa,
            int idKompensasi,
            String tanggal,
            String bukti,
            String jamMulai,
            String jamSelesai,
            String keterangan
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_mahasiswa", idMahasiswa);
        values.put("id_kompensasi", idKompensasi);
        values.put("tanggal", tanggal);
        values.put("bukti", bukti);
        values.put("jam_mulai", jamMulai);
        values.put("jam_selesai", jamSelesai);
        values.put("keterangan", keterangan);
        values.put("status_dosen_pa", "Pending");
        values.put("status_sekjur", "Pending");
        values.put("status_kajur", "Pending");

        db.insert("tb_pengajuan", null, values);
        db.close();
    }

    // ================= VERIFIKASI DOSEN PA =================

    public void verifikasiDosenPa(
            int idPengajuan,
            String status
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_dosen_pa",
                status
        );

        db.update(
                "tb_pengajuan",
                values,
                "id_pengajuan=?",
                new String[]{
                        String.valueOf(idPengajuan)
                }
        );

        db.close();
    }
    // ================= GET BUKTI KOMPENSASI =================

    public String getBuktiKompensasi(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        String bukti = "-";

        Cursor cursor =
                db.rawQuery(
                        "SELECT bukti " +
                                "FROM tb_pengajuan " +
                                "WHERE id_kompensasi=? AND bukti IS NOT NULL AND bukti != '-' AND bukti != '' " +
                                "ORDER BY id_pengajuan DESC",
                        new String[]{
                                String.valueOf(idKompensasi)
                        }
                );

        if (cursor.moveToFirst()) {
            bukti = cursor.getString(0);
        }

        cursor.close();

        // Fallback to tb_sesi if tb_pengajuan doesn't have the video
        if (bukti == null || bukti.equals("-") || bukti.isEmpty()) {
            Cursor cursorSesi = db.rawQuery(
                    "SELECT bukti FROM tb_sesi " +
                    "WHERE id_kompensasi=? AND bukti IS NOT NULL AND bukti != '-' AND bukti != '' " +
                    "ORDER BY id_sesi DESC LIMIT 1",
                    new String[]{String.valueOf(idKompensasi)}
            );
            if (cursorSesi.moveToFirst()) {
                bukti = cursorSesi.getString(0);
            }
            cursorSesi.close();
        }

        if (bukti == null) bukti = "-";

        return bukti;
    }
    // ================= DETAIL STATUS PENGAJUAN =================

    public Cursor getDetailStatusPengajuan(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "status_dosen_pa, " +
                        "status_sekjur, " +
                        "status_kajur " +
                        "FROM tb_pengajuan " +
                        "WHERE id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );
    }
    // ================= CEK SEMUA VERIFIKASI =================

    public boolean isSemuaVerifikasiApproved(
            int idPengajuan
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT " +
                                "status_dosen_pa, " +
                                "status_sekjur, " +
                                "status_kajur " +
                                "FROM tb_pengajuan " +
                                "WHERE id_pengajuan=?",
                        new String[]{
                                String.valueOf(idPengajuan)
                        }
                );

        boolean approved = false;

        if (cursor.moveToFirst()) {

            String dosenPa =
                    cursor.getString(0);

            String sekjur =
                    cursor.getString(1);

            String kajur =
                    cursor.getString(2);

            approved =
                    dosenPa.equals("Disetujui") &&
                            sekjur.equals("Disetujui") &&
                            kajur.equals("Disetujui");
        }

        cursor.close();

        return approved;
    }

    // ================= CEK APPROVE DOSEN PA =================

    public boolean isDosenPaApproved(
            int idPengajuan
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT status_dosen_pa " +
                                "FROM tb_pengajuan " +
                                "WHERE id_pengajuan=?",
                        new String[]{
                                String.valueOf(idPengajuan)
                        }
                );

        boolean approved = false;

        if (cursor.moveToFirst()) {

            String status =
                    cursor.getString(0);

            approved =
                    status.equals("Disetujui");
        }

        cursor.close();

        return approved;
    }
    public Cursor getStatusPengajuan(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "status_dosen_pa, " +
                        "status_sekjur, " +
                        "status_kajur " +
                        "FROM tb_pengajuan " +
                        "WHERE id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );
    }
// ================= PENGAJUAN KAJUR =================

    public Cursor getPengajuanKajur() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_pengajuan.id_pengajuan, " +
                        "tb_pengajuan.id_mahasiswa, " +
                        "tb_pengajuan.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_pengajuan.tanggal, " +
                        "tb_pengajuan.bukti, " +
                        "tb_pengajuan.status_dosen_pa, " +
                        "tb_pengajuan.status_sekjur, " +
                        "tb_pengajuan.status_kajur, " +
                        "tb_pengajuan.catatan_penolakan, " +
                        "tb_pengajuan.keterangan " +
                        "FROM tb_pengajuan " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_pengajuan.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "INNER JOIN tb_kompensasi " +
                        "ON tb_pengajuan.id_kompensasi = tb_kompensasi.id_kompensasi " +
                        "WHERE tb_pengajuan.status_sekjur='Disetujui' " +
                        "ORDER BY tb_pengajuan.id_pengajuan ASC",
                null
        );
    }

    public void updateStatusDosenPa(int idPengajuan, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status_dosen_pa", status);
        db.update("tb_pengajuan", values, "id_pengajuan=?", new String[]{String.valueOf(idPengajuan)});
        db.close();
    }

    // ================= VERIFIKASI SEKJUR =================

    public void verifikasiSekjur(
            int idPengajuan,
            String status
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_sekjur",
                status
        );

        db.update(
                "tb_pengajuan",
                values,
                "id_pengajuan=?",
                new String[]{
                        String.valueOf(idPengajuan)
                }
        );

        db.close();
    }
    // ================= VERIFIKASI KAJUR =================

    public void verifikasiKajur(
            int idPengajuan,
            String status
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_kajur",
                status
        );

        db.update(
                "tb_pengajuan",
                values,
                "id_pengajuan=?",
                new String[]{
                        String.valueOf(idPengajuan)
                }
        );

        db.close();
    }
    // ================= UPDATE STATUS FINAL =================

    public void updateStatusFinal(
            int idKompensasi,
            String status
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_final",
                status
        );

        db.update(
                "tb_kompensasi",
                values,
                "id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );

        db.close();
    }
    // ================= GET ALL PENGAJUAN =================

    public Cursor getAllPengajuan() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_pengajuan",
                null
        );
    }
    // ================= PENGAJUAN DOSEN PA =================

    public Cursor getPengajuanDosenPa(String kelas) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_pengajuan.id_pengajuan, " +
                        "tb_pengajuan.id_mahasiswa, " +
                        "tb_pengajuan.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_pengajuan.tanggal, " +
                        "tb_pengajuan.bukti, " +
                        "tb_pengajuan.status_dosen_pa, " +
                        "tb_pengajuan.status_sekjur, " +
                        "tb_pengajuan.status_kajur, " +
                        "tb_pengajuan.catatan_penolakan, " +
                        "tb_pengajuan.keterangan " +
                        "FROM tb_pengajuan " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_pengajuan.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "INNER JOIN tb_kompensasi " +
                        "ON tb_pengajuan.id_kompensasi = tb_kompensasi.id_kompensasi " +
                        "WHERE tb_mahasiswa.kelas = ? " +
                        "ORDER BY tb_pengajuan.id_pengajuan DESC",
                new String[]{kelas}
        );
    }

    // ================= PENGAJUAN SEKJUR =================

    public Cursor getPengajuanSekjur() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_pengajuan.id_pengajuan, " +
                        "tb_pengajuan.id_mahasiswa, " +
                        "tb_pengajuan.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_pengajuan.tanggal, " +
                        "tb_pengajuan.bukti, " +
                        "tb_pengajuan.status_dosen_pa, " +
                        "tb_pengajuan.status_sekjur, " +
                        "tb_pengajuan.status_kajur, " +
                        "tb_pengajuan.catatan_penolakan, " +
                        "tb_pengajuan.keterangan " +
                        "FROM tb_pengajuan " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_pengajuan.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "INNER JOIN tb_kompensasi " +
                        "ON tb_pengajuan.id_kompensasi = tb_kompensasi.id_kompensasi " +
                        "WHERE tb_pengajuan.status_dosen_pa='Disetujui' " +
                        "ORDER BY tb_pengajuan.id_pengajuan ASC",
                null
        );
    }

    // ================= VALIDASI FINAL =================

    public void cekDanUpdateStatusFinal(
            int idPengajuan,
            int idKompensasi
    ) {

        if (isSemuaVerifikasiApproved(idPengajuan)) {

            updateStatusFinal(
                    idKompensasi,
                    "Disetujui"
            );

            updateSisamenit(
                    idKompensasi,
                    0
            );
        }
    }
// ================= UPDATE KOMPENSASI =================

    public void updateKompensasi(
            int idKompensasi,
            int jumlahMenit,
            String tanggalMulai
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "jumlah_menit",
                jumlahMenit
        );

        values.put(
                "sisa_menit",
                jumlahMenit
        );

        values.put(
                "tanggal_mulai",
                tanggalMulai
        );

        if (jumlahMenit == 0) {
            values.put("status_final", "Selesai");
        } else {
            values.put("status_final", "Belum Kompen");
        }

        db.update(
                "tb_kompensasi",
                values,
                "id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );

        db.close();
    }
    // ================= UPDATE TUGAS =================

    public void updateTugasKompensasi(
            int idKompensasi,
            String tugas
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "tugas_kompensasi",
                tugas
        );

        db.update(
                "tb_kompensasi",
                values,
                "id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );

        db.close();
    }
// ================= DELETE KOMPENSASI =================

    public void deleteKompensasi(
            int id
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "tb_kompensasi",
                "id_kompensasi=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        db.close();
    }

// ================= GET ALL KOMPENSASI =================

    public Cursor getAllKompensasi() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_kompensasi.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.jumlah_menit, " +
                        "tb_kompensasi.sisa_menit, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_kompensasi.status_final, " +
                        "tb_kompensasi.tanggal_mulai, " +
                        "tb_kompensasi.tanggal_kompensasi, " +
                        "tb_kompensasi.tanggal_selesai, " +
                        "tb_kompensasi.jam_mulai, " +
                        "tb_kompensasi.jam_selesai, " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_kompensasi " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_kompensasi.id_mahasiswa = tb_mahasiswa.id_mahasiswa",
                null
        );
    }

    // ================= GET USER PASSWORD =================

    public String getUserPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM tb_user WHERE username=?", new String[]{username});
        String pass = "-";
        if (cursor.moveToFirst()) {
            pass = cursor.getString(0);
        }
        cursor.close();
        return pass;
    }

    public String getMahasiswaPassword(String nim) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM tb_mahasiswa WHERE nim=?", new String[]{nim});
        String pass = "-";
        if (cursor.moveToFirst()) {
            pass = cursor.getString(0);
        }
        cursor.close();
        return pass;
    }

    // ================= CEK USERNAME (MAHASISWA) =================

    public Cursor getKompensasiMahasiswa(
            int idMahasiswa
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_kompensasi.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.jumlah_menit, " +
                        "tb_kompensasi.sisa_menit, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_kompensasi.status_final, " +
                        "tb_kompensasi.tanggal_mulai, " +
                        "tb_kompensasi.tanggal_kompensasi, " +
                        "tb_kompensasi.tanggal_selesai, " +
                        "tb_kompensasi.jam_mulai, " +
                        "tb_kompensasi.jam_selesai, " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_kompensasi " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_kompensasi.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "WHERE tb_kompensasi.id_mahasiswa=?",
                new String[]{
                        String.valueOf(idMahasiswa)
                }
        );
    }

// ================= SEARCH KOMPENSASI =================

    public Cursor searchKompensasi(
            String keyword
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_kompensasi.id_kompensasi, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.jumlah_menit, " +
                        "tb_kompensasi.sisa_menit, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_kompensasi.status_final, " +
                        "tb_kompensasi.tanggal_mulai, " +
                        "tb_kompensasi.tanggal_kompensasi, " +
                        "tb_kompensasi.tanggal_selesai, " +
                        "tb_kompensasi.jam_mulai, " +
                        "tb_kompensasi.jam_selesai, " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_kompensasi " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_kompensasi.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "WHERE tb_mahasiswa.nama LIKE ?",
                new String[]{
                        "%" + keyword + "%"
                }
        );
    }
    public Cursor searchKompensasiByKelas(
            String keyword,
            String kelas
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "COALESCE(tb_kompensasi.id_kompensasi, 0), " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.kelas, " +
                        "COALESCE(tb_kompensasi.jumlah_menit, 0), " +
                        "COALESCE(tb_kompensasi.sisa_menit, 0), " +
                        "COALESCE(tb_kompensasi.tugas_kompensasi, '-'), " +
                        "COALESCE(tb_kompensasi.status_final, 'Selesai'), " +
                        "COALESCE(tb_kompensasi.tanggal_mulai, '-'), " +
                        "COALESCE(tb_kompensasi.tanggal_kompensasi, '-'), " +
                        "COALESCE(tb_kompensasi.tanggal_selesai, '-'), " +
                        "COALESCE(tb_kompensasi.jam_mulai, '-'), " +
                        "COALESCE(tb_kompensasi.jam_selesai, '-'), " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_mahasiswa " +
                        "LEFT JOIN tb_kompensasi " +
                        "ON tb_kompensasi.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "WHERE tb_mahasiswa.nama LIKE ? " +
                        "AND tb_mahasiswa.kelas = ?",
                new String[]{
                        "%" + keyword + "%",
                        kelas
                }
        );
    }
    
    public Cursor getMahasiswaBimbinganPa(String kelas) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " +
                        "tb_mahasiswa.id_mahasiswa, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.kelas, " +
                        "COALESCE(SUM(tb_kompensasi.jumlah_menit), 0) as total_kompen, " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_mahasiswa " +
                        "LEFT JOIN tb_kompensasi " +
                        "ON tb_mahasiswa.id_mahasiswa = tb_kompensasi.id_mahasiswa " +
                        "WHERE tb_mahasiswa.kelas = ? " +
                        "GROUP BY tb_mahasiswa.id_mahasiswa",
                new String[]{kelas}
        );
    }
    // ================= MAHASISWA =================

    public void insertMahasiswa(
            String nim,
            String nama,
            String kelas,
            String password
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("nim", nim);

        values.put("nama", nama);

        values.put("kelas", kelas);

        values.put("password", password);

        db.insert(
                "tb_mahasiswa",
                null,
                values
        );

        db.close();
    }

    public void updateMahasiswa(
            int id,
            String nim,
            String nama,
            String kelas,
            String password
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("nim", nim);

        values.put("nama", nama);

        values.put("kelas", kelas);

        values.put("password", password);

        db.update(
                "tb_mahasiswa",
                values,
                "id_mahasiswa=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        db.close();
    }

    public void deleteMahasiswa(
            int id
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "tb_mahasiswa",
                "id_mahasiswa=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        db.close();
    }

    public Cursor getAllMahasiswa() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_mahasiswa",
                null
        );
    }

    public Cursor searchMahasiswa(
            String keyword
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_mahasiswa " +
                        "WHERE nim LIKE ? " +
                        "OR nama LIKE ? " +
                        "OR kelas LIKE ?",
                new String[]{
                        "%" + keyword + "%",
                        "%" + keyword + "%",
                        "%" + keyword + "%"
                }
        );
    }
    // ================= GET PENGAJUAN MAHASISWA =================

    public Cursor getPengajuanMahasiswa(
            int idMahasiswa
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_pengajuan.id_pengajuan, " +
                        "tb_pengajuan.id_kompensasi, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_pengajuan.tanggal, " +
                        "tb_pengajuan.bukti, " +
                        "tb_pengajuan.status_dosen_pa, " +
                        "tb_pengajuan.status_sekjur, " +
                        "tb_pengajuan.status_kajur " +
                        "FROM tb_pengajuan " +
                        "INNER JOIN tb_kompensasi " +
                        "ON tb_pengajuan.id_kompensasi = tb_kompensasi.id_kompensasi " +
                        "WHERE tb_pengajuan.id_mahasiswa=?",
                new String[]{
                        String.valueOf(idMahasiswa)
                }
        );
    }
    // ================= GET MAHASISWA BY KELAS =================

    public Cursor getMahasiswaByKelas(
            String kelas
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tb_mahasiswa " +
                        "WHERE kelas=?",
                new String[]{
                        kelas
                }
        );
    }
    // ================= GET KELAS DOSEN PA =================

    public Cursor getAllKelasDosenPa() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT DISTINCT kelas FROM tb_user " +
                        "WHERE role='dosenpa' " +
                        "AND kelas IS NOT NULL " +
                        "AND kelas != ''",
                null
        );
    }
    public void updateSisamenit(
            int idKompensasi,
            int sisamenit
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "sisa_menit",
                sisamenit
        );

        // ================= JIKA SELESAI =================

        if (sisamenit == 0) {

            values.put(
                    "status_final",
                    "Selesai"
            );

            values.put(
                    "tanggal_selesai",
                    java.text.DateFormat
                            .getDateInstance()
                            .format(new java.util.Date())
            );
        }

        db.update(
                "tb_kompensasi",
                values,
                "id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );

        db.close();
    }
    public Cursor getDetailKompensasi(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "tb_mahasiswa.nama, " +
                        "tb_mahasiswa.nim, " +
                        "tb_mahasiswa.kelas, " +
                        "tb_kompensasi.jumlah_menit, " +
                        "tb_kompensasi.sisa_menit, " +
                        "tb_kompensasi.tugas_kompensasi, " +
                        "tb_kompensasi.status_final, " +
                        "tb_kompensasi.tanggal_mulai, " +
                        "tb_kompensasi.tanggal_kompensasi, " +
                        "tb_kompensasi.tanggal_selesai, " +
                        "tb_kompensasi.jam_mulai, " +
                        "tb_kompensasi.jam_selesai, " +
                        "tb_mahasiswa.foto_profil " +
                        "FROM tb_kompensasi " +
                        "INNER JOIN tb_mahasiswa " +
                        "ON tb_kompensasi.id_mahasiswa = tb_mahasiswa.id_mahasiswa " +
                        "WHERE tb_kompensasi.id_kompensasi=?",
                new String[]{
                        String.valueOf(idKompensasi)
                }
        );
    }

// ================= IMPORT EXCEL MAHASISWA =================

    public boolean importMahasiswaFromExcel(
            InputStream inputStream
    ) {

        try {

            SQLiteDatabase db =
                    this.getWritableDatabase();

            Workbook workbook =
                    new XSSFWorkbook(inputStream);

            Sheet sheet =
                    workbook.getSheetAt(0);

            DataFormatter formatter =
                    new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row =
                        sheet.getRow(i);

                if (
                        row == null ||
                                row.getCell(0) == null
                ) {
                    continue;
                }

                String nim =
                        formatter.formatCellValue(
                                        row.getCell(0)
                                )
                                .replace("'", "")
                                .replace(".0", "")
                                .trim();

                String nama =
                        formatter.formatCellValue(
                                        row.getCell(1)
                                )
                                .replace("'", "")
                                .trim();

                String kelas =
                        formatter.formatCellValue(
                                        row.getCell(2)
                                )
                                .replace("'", "")
                                .trim();

                String password =
                        formatter.formatCellValue(
                                        row.getCell(3)
                                )
                                .replace("'", "")
                                .trim();

                Cursor cursor =
                        db.rawQuery(
                                "SELECT * FROM tb_mahasiswa WHERE nim=?",
                                new String[]{nim}
                        );

                boolean exists =
                        cursor.getCount() > 0;

                cursor.close();

                if (!exists) {

                    ContentValues values =
                            new ContentValues();

                    values.put("nim", nim);

                    values.put("nama", nama);

                    values.put("kelas", kelas);

                    values.put("password", password);

                    db.insert(
                            "tb_mahasiswa",
                            null,
                            values
                    );
                }
            }

            workbook.close();

            inputStream.close();

            db.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

// ================= GET DOSEN PA SESUAI KELAS =================

    public Cursor getNamaDosenPa(
            int idKompensasi
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT u.nama, u.username " +
                        "FROM tb_kompensasi k " +

                        "INNER JOIN tb_mahasiswa m " +
                        "ON k.id_mahasiswa = m.id_mahasiswa " +

                        "INNER JOIN tb_user u " +
                        "ON u.kelas = m.kelas " +

                        "WHERE k.id_kompensasi = ? " +
                        "AND u.role = 'dosenpa' " +
                        "LIMIT 1",

                new String[]{
                        String.valueOf(idKompensasi)
                }
        );
    }

// ================= GET SEKJUR =================

    public Cursor getSekjur() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT nama, username " +
                        "FROM tb_user " +
                        "WHERE role = 'sekjur' " +
                        "LIMIT 1",

                null
        );
    }

// ================= GET KAJUR =================

    public Cursor getKajur() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT nama, username " +
                        "FROM tb_user " +
                        "WHERE role = 'kajur' " +
                        "LIMIT 1",

                null
        );
    }


    // ================= CLOSE =================

    @Override
    public synchronized void close() {

        super.close();
    }

    public void updateStatusSekjurDitolak(
            int idPengajuan,
            String catatan
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_sekjur",
                "Ditolak"
        );

        values.put(
                "catatan_penolakan",
                catatan
        );

        db.update(
                "tb_pengajuan",
                values,
                "id_pengajuan=?",
                new String[]{
                        String.valueOf(idPengajuan)
                }
        );
    }

    public void updateStatusKajurDitolak(
            int idPengajuan,
            String catatan
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status_kajur",
                "Ditolak"
        );

        values.put(
                "catatan_penolakan",
                catatan
        );

        db.update(
                "tb_pengajuan",
                values,
                "id_pengajuan=?",
                new String[]{
                        String.valueOf(idPengajuan)
                }
        );
}

    // ================= TAMBAH SESI KOMPENSASI =================

    public void tambahSesi(
            int idKompensasi,
            String tugas,
            String jamMulai,
            String jamSelesai,
            int menit,
            String tanggal
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_kompensasi", idKompensasi);
        values.put("tugas", tugas);
        values.put("jam_mulai", jamMulai);
        values.put("jam_selesai", jamSelesai);
        values.put("menit", menit);
        values.put("tanggal", tanggal);
        values.put("status", "Dikerjakan Mahasiswa");
        values.put("alasan", "-");
        db.insert("tb_sesi", null, values);

        // Update status_final kompensasi to reflect that there's an active task
        ContentValues kv = new ContentValues();
        kv.put("status_final", "Ada Tugas Aktif");
        db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});

        db.close();
    }

    // ================= AJUKAN SESI (MAHASISWA) =================

    public void ajukanSesiMahasiswa(int idSesi, String bukti) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Menunggu Persetujuan");
        values.put("bukti", bukti);
        db.update("tb_sesi", values, "id_sesi=?", new String[]{String.valueOf(idSesi)});

        // Also update status_final and tanggal_kompensasi in tb_kompensasi
        Cursor cursor = db.rawQuery(
                "SELECT id_kompensasi FROM tb_sesi WHERE id_sesi=?",
                new String[]{String.valueOf(idSesi)}
        );
        if (cursor.moveToFirst()) {
            int idKompensasi = cursor.getInt(0);
            ContentValues kv = new ContentValues();
            kv.put("status_final", "Menunggu Persetujuan");
            kv.put("tanggal_kompensasi", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
            db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});
        }
        cursor.close();
        db.close();
    }

    // ================= APPROVE SESI (DOSEN PA) =================

    public void approveSesiDosen(int idSesi, int idKompensasi, int menit) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("status", "Disetujui");
        db.update("tb_sesi", values, "id_sesi=?", new String[]{String.valueOf(idSesi)});

        // Kurangi sisa_menit
        db.execSQL(
                "UPDATE tb_kompensasi SET sisa_menit = sisa_menit - ? WHERE id_kompensasi = ?",
                new Object[]{menit, idKompensasi}
        );

        // Jika sisa_menit sudah 0, update status_final
        Cursor cursor = db.rawQuery(
                "SELECT sisa_menit FROM tb_kompensasi WHERE id_kompensasi = ?",
                new String[]{String.valueOf(idKompensasi)}
        );
        if (cursor.moveToFirst()) {
            int sisa = cursor.getInt(0);
            ContentValues sv = new ContentValues();
            if (sisa <= 0) {
                sv.put("sisa_menit", 0);
                sv.put("status_final", "Siap Diverifikasi");
            } else {
                sv.put("status_final", "Menunggu Tugas Tambahan");
            }
            db.update("tb_kompensasi", sv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});
        }
        cursor.close();
        db.close();
    }

    // ================= TOLAK SESI =================

    public void tolakSesi(int idSesi, String alasan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", "Ditolak");
        values.put("alasan", alasan);
        db.update("tb_sesi", values, "id_sesi=?", new String[]{String.valueOf(idSesi)});

        // Reset status_final so dosen can add replacement task
        Cursor cursor = db.rawQuery(
                "SELECT id_kompensasi FROM tb_sesi WHERE id_sesi=?",
                new String[]{String.valueOf(idSesi)}
        );
        if (cursor.moveToFirst()) {
            int idKompensasi = cursor.getInt(0);
            ContentValues kv = new ContentValues();
            kv.put("status_final", "Ada Tugas Aktif");
            db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});
        }
        cursor.close();
        db.close();
    }

    // ================= GET SESI BY KOMPENSASI =================

    public Cursor getSesiByKompensasi(int idKompensasi) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT id_sesi, id_kompensasi, tugas, jam_mulai, jam_selesai, menit, tanggal, status, alasan, bukti " +
                        "FROM tb_sesi WHERE id_kompensasi = ? ORDER BY id_sesi DESC",
                new String[]{String.valueOf(idKompensasi)}
        );
    }

    // ================= GET SESI BELUM SELESAI BY MAHASISWA =================

    public Cursor getSesiBelumSelesaiByMahasiswa(int idMahasiswa) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT s.id_sesi, s.id_kompensasi, s.tugas, s.jam_mulai, s.jam_selesai, s.menit, s.tanggal, s.status " +
                        "FROM tb_sesi s " +
                        "JOIN tb_kompensasi k ON s.id_kompensasi = k.id_kompensasi " +
                        "WHERE k.id_mahasiswa = ? AND (s.status = 'Dikerjakan Mahasiswa' OR s.status = 'Ditolak')",
                new String[]{String.valueOf(idMahasiswa)}
        );
    }

    // ================= APPROVE FINAL DOSEN PA =================

    public void approveFinalDosenPa(int idKompensasi, int idMahasiswa) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Update status_final kompensasi (jangan Selesai dulu karena masih butuh Sekjur & Kajur)
        ContentValues kv = new ContentValues();
        kv.put("status_final", "Menunggu Persetujuan Sekjur");
        db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});

        // Cek apakah sudah ada pengajuan
        Cursor existing = db.rawQuery(
                "SELECT id_pengajuan FROM tb_pengajuan WHERE id_kompensasi=?",
                new String[]{String.valueOf(idKompensasi)}
        );

        if (existing.moveToFirst()) {
            int idPengajuan = existing.getInt(0);
            ContentValues pv = new ContentValues();
            pv.put("status_dosen_pa", "Disetujui");
            db.update("tb_pengajuan", pv, "id_pengajuan=?", new String[]{String.valueOf(idPengajuan)});
        } else {
            // Buat pengajuan baru
            ContentValues pv = new ContentValues();
            pv.put("id_mahasiswa", idMahasiswa);
            pv.put("id_kompensasi", idKompensasi);
            pv.put("tanggal", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
            pv.put("bukti", "-");
            pv.put("status_dosen_pa", "Disetujui");
            pv.put("status_sekjur", "Pending");
            pv.put("status_kajur", "Pending");
            pv.put("keterangan", "Disetujui oleh Dosen PA");
            db.insert("tb_pengajuan", null, pv);
        }
        existing.close();
        db.close();
    }

    // ================= TOLAK FINAL DOSEN PA =================

    public void tolakFinalDosenPa(int idKompensasi, int idMahasiswa, String alasan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues kv = new ContentValues();
        kv.put("status_final", "Menunggu Persetujuan");
        db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});

        Cursor existing = db.rawQuery(
                "SELECT id_pengajuan FROM tb_pengajuan WHERE id_kompensasi=?",
                new String[]{String.valueOf(idKompensasi)}
        );
        if (existing.moveToFirst()) {
            int idPengajuan = existing.getInt(0);
            ContentValues pv = new ContentValues();
            pv.put("status_dosen_pa", "Ditolak");
            pv.put("catatan_penolakan", alasan);
            db.update("tb_pengajuan", pv, "id_pengajuan=?", new String[]{String.valueOf(idPengajuan)});
        }
        existing.close();
        db.close();
    }

    // ================= AUTO APPROVE KOMPENSASI =================

    public void autoApproveKompensasi(int idMahasiswa, int idKompensasi) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Update tb_kompensasi
        ContentValues kv = new ContentValues();
        kv.put("status_final", "Selesai");
        db.update("tb_kompensasi", kv, "id_kompensasi=?", new String[]{String.valueOf(idKompensasi)});

        // Cek apakah sudah ada pengajuan
        Cursor existing = db.rawQuery(
                "SELECT id_pengajuan FROM tb_pengajuan WHERE id_kompensasi=?",
                new String[]{String.valueOf(idKompensasi)}
        );

        if (existing.moveToFirst()) {
            int idPengajuan = existing.getInt(0);
            ContentValues pv = new ContentValues();
            pv.put("status_dosen_pa", "Disetujui");
            pv.put("status_sekjur", "Disetujui");
            pv.put("status_kajur", "Disetujui");
            pv.put("keterangan", "Otomatis Disetujui (Auto-Approve)");
            db.update("tb_pengajuan", pv, "id_pengajuan=?", new String[]{String.valueOf(idPengajuan)});
        } else {
            // Buat pengajuan baru
            ContentValues pv = new ContentValues();
            pv.put("id_mahasiswa", idMahasiswa);
            pv.put("id_kompensasi", idKompensasi);
            pv.put("tanggal", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
            pv.put("bukti", "-");
            pv.put("status_dosen_pa", "Disetujui");
            pv.put("status_sekjur", "Disetujui");
            pv.put("status_kajur", "Disetujui");
            pv.put("keterangan", "Otomatis Disetujui (Auto-Approve)");
            db.insert("tb_pengajuan", null, pv);
        }
        existing.close();
        db.close();
    }

    // ================= GET MAHASISWA BY ID =================

    public Cursor getMahasiswaById(int idMahasiswa) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM tb_mahasiswa WHERE id_mahasiswa=?",
                new String[]{String.valueOf(idMahasiswa)}
        );
    }

    public boolean isMahasiswa(String nim) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_mahasiswa WHERE nim=?", new String[]{nim});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // ================= GET SISA MENIT KOMPENSASI =================

    public int getSisaMenit(int idKompensasi) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT sisa_menit FROM tb_kompensasi WHERE id_kompensasi=?",
                new String[]{String.valueOf(idKompensasi)}
        );
        int sisa = 0;
        if (cursor.moveToFirst()) {
            sisa = cursor.getInt(0);
        }
        cursor.close();
        return sisa;
    }
    // ================= UPDATE PROFIL =================

    public void updateMahasiswaProfile(String nim, String password, String fotoProfil, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        values.put("foto_profil", fotoProfil);
        values.put("email", email);

        db.update("tb_mahasiswa", values, "nim=?", new String[]{nim});
        db.close();
    }

    public void updateUserProfile(String username, String password, String fotoProfil, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        values.put("foto_profil", fotoProfil);
        values.put("email", email);

        db.update("tb_user", values, "username=?", new String[]{username});
        db.close();
    }

    public String getFotoProfilMahasiswa(String nim) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT foto_profil FROM tb_mahasiswa WHERE nim=?", new String[]{nim});
        String foto = null;
        if (cursor.moveToFirst()) {
            foto = cursor.getString(0);
        }
        cursor.close();
        return foto;
    }

    public String getFotoProfilUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT foto_profil FROM tb_user WHERE username=?", new String[]{username});
        String foto = null;
        if (cursor.moveToFirst()) {
            foto = cursor.getString(0);
        }
        cursor.close();
        return foto;
    }

    public String getEmailMahasiswa(String nim) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM tb_mahasiswa WHERE nim=?", new String[]{nim});
        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
            if (email == null) email = "";
        }
        cursor.close();
        return email;
    }

    public String getEmailUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM tb_user WHERE username=?", new String[]{username});
        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
            if (email == null) email = "";
        }
        cursor.close();
        return email;
    }
}



