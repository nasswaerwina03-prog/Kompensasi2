package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DownloadPdfActivity
        extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper =
                new DatabaseHelper(this);

        int idKompensasi =
                getIntent().getIntExtra(
                        "id_kompensasi",
                        0
                );

        generatePdf(idKompensasi);
    }

    // ================= GENERATE PDF =================

    private void generatePdf(
            int idKompensasi
    ) {

        try {

            Cursor cursor =
                    databaseHelper.getDetailKompensasi(
                            idKompensasi
                    );

            if (cursor.moveToFirst()) {

                // ================= DATA =================

                String nama =
                        cursor.getString(0);

                String nim =
                        cursor.getString(1);

                String kelas =
                        cursor.getString(2);

                int jumlah =
                        cursor.getInt(3);

                int sisa =
                        cursor.getInt(4);

                String tugas =
                        cursor.getString(5);

                String status =
                        cursor.getString(6);

                String tanggal =
                        cursor.getString(7);

                // ================= STATUS VERIFIKASI =================

                Cursor pengajuan =
                        databaseHelper.getStatusPengajuan(
                                idKompensasi
                        );

                String statusDosenPa =
                        "Pending";

                String statusSekjur =
                        "Pending";

                String statusKajur =
                        "Pending";

                if (pengajuan.moveToFirst()) {

                    statusDosenPa =
                            pengajuan.getString(0);

                    statusSekjur =
                            pengajuan.getString(1);

                    statusKajur =
                            pengajuan.getString(2);
                }

                pengajuan.close();

                // ================= FOLDER =================

                File folder =
                        new File(
                                getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS),
                                "Kompensasi"
                        );

                if (!folder.exists()) {

                    folder.mkdirs();
                }

                // ================= FILE =================

                File file =
                        new File(
                                folder,
                                "Surat_Kompensasi_" +
                                        nim +
                                        ".pdf"
                        );

                // ================= DOCUMENT =================

                Document document =
                        new Document(
                                PageSize.A4,
                                40,
                                40,
                                50,
                                50
                        );

                PdfWriter.getInstance(
                        document,
                        new FileOutputStream(file)
                );

                document.open();

                // ================= FONT =================

                Font title =
                        new Font(
                                Font.FontFamily.TIMES_ROMAN,
                                15,
                                Font.BOLD
                        );

                Font subtitle =
                        new Font(
                                Font.FontFamily.TIMES_ROMAN,
                                13,
                                Font.BOLD
                        );

                Font normal =
                        new Font(
                                Font.FontFamily.TIMES_ROMAN,
                                12,
                                Font.NORMAL
                        );

                Font bold =
                        new Font(
                                Font.FontFamily.TIMES_ROMAN,
                                12,
                                Font.BOLD
                        );

                // ================= HEADER =================

                Paragraph kampus =
                        new Paragraph(
                                "KEMENTERIAN PENDIDIKAN TINGGI, SAINS, DAN TEKNOLOGI\n" +
                                        "POLITEKNIK NEGERI SRIWIJAYA\n" +
                                        "JURUSAN MANAJEMEN INFORMATIKA",
                                title
                        );

                kampus.setAlignment(
                        Element.ALIGN_CENTER
                );

                document.add(kampus);

                document.add(
                        new Paragraph("\n")
                );

                // ================= JUDUL =================

                Paragraph judul =
                        new Paragraph(
                                "SURAT TUGAS KOMPENSASI KETIDAKHADIRAN\n" +
                                        "SEMESTER GANJIL TAHUN AKADEMIK 2025/2026",
                                subtitle
                        );

                judul.setAlignment(
                        Element.ALIGN_CENTER
                );

                document.add(judul);

                document.add(
                        new Paragraph("\n")
                );

                // ================= PEMBUKA =================

                document.add(
                        new Paragraph(
                                "Mahasiswa berikut telah menyelesaikan " +
                                        "kewajiban kompensasi sesuai ketentuan " +
                                        "Jurusan Manajemen Informatika.\n\n",
                                normal
                        )
                );

                // ================= IDENTITAS =================

                PdfPTable table =
                        new PdfPTable(2);

                table.setWidthPercentage(100);

                table.setWidths(
                        new float[]{
                                30,
                                70
                        }
                );

                addRow(
                        table,
                        "Nama",
                        nama,
                        normal
                );

                addRow(
                        table,
                        "NIM",
                        nim,
                        normal
                );

                addRow(
                        table,
                        "Kelas",
                        kelas,
                        normal
                );

                addRow(
                        table,
                        "Jumlah Kompen",
                        jumlah + " Menit",
                        normal
                );

                addRow(
                        table,
                        "Sisa Kompen",
                        sisa + " Menit",
                        normal
                );

                addRow(
                        table,
                        "Tugas Kompensasi",
                        tugas,
                        normal
                );

                addRow(
                        table,
                        "Status",
                        status,
                        normal
                );

                addRow(
                        table,
                        "Tanggal",
                        tanggal,
                        normal
                );

                document.add(table);

                document.add(
                        new Paragraph("\n\n")
                );

                // ================= VALIDASI =================

                Paragraph pengesahan =
                        new Paragraph(
                                "VALIDASI DIGITAL",
                                bold
                        );

                pengesahan.setAlignment(
                        Element.ALIGN_CENTER
                );

                document.add(pengesahan);

                document.add(
                        new Paragraph("\n")
                );

// ================= DATA DOSEN PA =================

                String namaDosenPa =
                        "-";

                String nipDosenPa =
                        "-";

                Cursor dosenCursor =
                        databaseHelper.getNamaDosenPa(
                                idKompensasi
                        );

                if (dosenCursor != null) {

                    if (dosenCursor.moveToFirst()) {

                        namaDosenPa =
                                dosenCursor.getString(0);

                        nipDosenPa =
                                dosenCursor.getString(1);
                    }

                    dosenCursor.close();
                }

// ================= DATA SEKJUR =================

                String namaSekjur =
                        "-";

                String nipSekjur =
                        "-";

                Cursor sekjurCursor =
                        databaseHelper.getSekjur();

                if (sekjurCursor != null) {

                    if (sekjurCursor.moveToFirst()) {

                        namaSekjur =
                                sekjurCursor.getString(0);

                        nipSekjur =
                                sekjurCursor.getString(1);
                    }

                    sekjurCursor.close();
                }

// ================= DATA KAJUR =================

                String namaKajurData =
                        "-";

                String nipKajurData =
                        "-";

                Cursor kajurCursor =
                        databaseHelper.getKajur();

                if (kajurCursor != null) {

                    if (kajurCursor.moveToFirst()) {

                        namaKajurData =
                                kajurCursor.getString(0);

                        nipKajurData =
                                kajurCursor.getString(1);
                    }

                    kajurCursor.close();
                }

// ================= TABLE VALIDASI =================

                PdfPTable tabelValidasi =
                        new PdfPTable(3);

                tabelValidasi.setWidthPercentage(100);

                tabelValidasi.setWidths(
                        new float[]{
                                40,
                                30,
                                30
                        }
                );

// ================= HEADER =================

                addHeader(
                        tabelValidasi,
                        "Nama"
                );

                addHeader(
                        tabelValidasi,
                        "NIP / Username"
                );

                addHeader(
                        tabelValidasi,
                        "Verifikasi"
                );

// ================= DOSEN PA =================

                tabelValidasi.addCell(
                        namaDosenPa
                );

                tabelValidasi.addCell(
                        nipDosenPa
                );

                tabelValidasi.addCell(
                        "✔ " + statusDosenPa
                );

// ================= SEKJUR =================

                tabelValidasi.addCell(
                        namaSekjur
                );

                tabelValidasi.addCell(
                        nipSekjur
                );

                tabelValidasi.addCell(
                        "✔ " + statusSekjur
                );

// ================= KAJUR =================

                tabelValidasi.addCell(
                        namaKajurData
                );

                tabelValidasi.addCell(
                        nipKajurData
                );

                tabelValidasi.addCell(
                        "✔ " + statusKajur
                );

                document.add(
                        tabelValidasi
                );

                document.add(
                        new Paragraph("\n\n")
                );

                // ================= TTD KAJUR =================

                Paragraph kajur =
                        new Paragraph(
                                "Ketua Jurusan",
                                bold
                        );

                kajur.setAlignment(
                        Element.ALIGN_RIGHT
                );

                document.add(kajur);

                // ================= QR =================

                Bitmap bitmap =
                        generateQrCode(
                                "KAJUR-APPROVED-" + nim
                        );

                ByteArrayOutputStream stream =
                        new ByteArrayOutputStream();

                bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        stream
                );

                Image qrImage =
                        Image.getInstance(
                                stream.toByteArray()
                        );

                qrImage.scaleAbsolute(
                        120,
                        120
                );

                qrImage.setAlignment(
                        Element.ALIGN_RIGHT
                );

                document.add(qrImage);

                Paragraph namaKajur =
                        new Paragraph(
                                namaKajurData,
                                normal
                        );

                namaKajur.setAlignment(
                        Element.ALIGN_RIGHT
                );

                document.add(namaKajur);

                // ================= FOOTER =================

                document.add(
                        new Paragraph("\n")
                );

                Paragraph footer =
                        new Paragraph(
                                "Dokumen ini telah diverifikasi secara digital oleh sistem kompensasi.",
                                normal
                        );

                footer.setAlignment(
                        Element.ALIGN_CENTER
                );

                document.add(footer);

                // ================= CLOSE =================

                document.close();

                Toast.makeText(
                        this,
                        "PDF berhasil dibuat",
                        Toast.LENGTH_LONG
                ).show();

                // ================= OPEN PDF =================

                Uri uri =
                        FileProvider.getUriForFile(
                                this,
                                getPackageName() + ".provider",
                                file
                        );

                Intent intent =
                        new Intent(
                                Intent.ACTION_VIEW
                        );

                intent.setDataAndType(
                        uri,
                        "application/pdf"
                );

                intent.setFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                startActivity(intent);
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            }

            cursor.close();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();
        }

        finish();
    }

    // ================= QR =================

    private Bitmap generateQrCode(
            String text
    ) throws Exception {

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        text,
                        BarcodeFormat.QR_CODE,
                        300,
                        300
                );

        int width =
                matrix.getWidth();

        int height =
                matrix.getHeight();

        Bitmap bitmap =
                Bitmap.createBitmap(
                        width,
                        height,
                        Bitmap.Config.RGB_565
                );

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                bitmap.setPixel(
                        x,
                        y,
                        matrix.get(
                                x,
                                y
                        ) ? Color.BLACK : Color.WHITE
                );
            }
        }

        return bitmap;
    }

    // ================= ROW =================

    private void addRow(
            PdfPTable table,
            String key,
            String value,
            Font font
    ) {

        PdfPCell cell1 =
                new PdfPCell(
                        new Phrase(
                                key,
                                font
                        )
                );

        cell1.setBorder(
                PdfPCell.NO_BORDER
        );

        PdfPCell cell2 =
                new PdfPCell(
                        new Phrase(
                                ": " + value,
                                font
                        )
                );

        cell2.setBorder(
                PdfPCell.NO_BORDER
        );

        table.addCell(cell1);

        table.addCell(cell2);
    }

    // ================= HEADER =================

    private void addHeader(
            PdfPTable table,
            String text
    ) {

        Font font =
                new Font(
                        Font.FontFamily.TIMES_ROMAN,
                        11,
                        Font.BOLD
                );

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        table.addCell(cell);
    }
}