package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class DownloadPdfActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        int idKompensasi = getIntent().getIntExtra("id_kompensasi", 0);
        generatePdf(idKompensasi);
    }

    private void generatePdf(int idKompensasi) {
        try {
            Cursor cursor = databaseHelper.getDetailKompensasi(idKompensasi);
            if (cursor.moveToFirst()) {
                String nama = cursor.getString(0);
                String nim = cursor.getString(1);
                String kelas = cursor.getString(2);
                int jumlah = cursor.getInt(3);
                int sisa = cursor.getInt(4);
                String tugas = cursor.getString(5);
                String status = cursor.getString(6);
                String tanggal = cursor.getString(7);

                // DATA DOSEN PA
                String namaDosenPa = "-";
                Cursor dosenCursor = databaseHelper.getNamaDosenPa(idKompensasi);
                if (dosenCursor != null) {
                    if (dosenCursor.moveToFirst()) {
                        namaDosenPa = dosenCursor.getString(0);
                    }
                    dosenCursor.close();
                }

                // DATA SEKJUR
                String namaSekjur = "-";
                Cursor sekjurCursor = databaseHelper.getSekjur();
                if (sekjurCursor != null) {
                    if (sekjurCursor.moveToFirst()) {
                        namaSekjur = sekjurCursor.getString(0);
                    }
                    sekjurCursor.close();
                }

                // DATA KAJUR
                String namaKajurData = "-";
                String nipKajurData = "-";
                Cursor kajurCursor = databaseHelper.getKajur();
                if (kajurCursor != null) {
                    if (kajurCursor.moveToFirst()) {
                        namaKajurData = kajurCursor.getString(0);
                        nipKajurData = kajurCursor.getString(1);
                    }
                    kajurCursor.close();
                }

                // FOLDER
                File folder = new File(getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS), "Kompensasi");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // FILE
                File file = new File(folder, "Surat_Kompensasi_" + nim + ".pdf");

                // DOCUMENT
                Document document = new Document(PageSize.A4, 30, 30, 30, 30);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // FONT
                Font headerFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                Font headerFontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
                Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

                // LOGO HEADER
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_polsri);
                ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
                logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, logoStream);
                Image logoImage = Image.getInstance(logoStream.toByteArray());
                logoImage.scaleAbsolute(70, 70);

                // WATERMARK
                Image watermark = Image.getInstance(logoStream.toByteArray());
                watermark.setAbsolutePosition(145, 270);
                watermark.scaleAbsolute(300, 300);
                PdfContentByte canvas = writer.getDirectContentUnder();
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.15f);
                canvas.setGState(gs);
                canvas.addImage(watermark);

                // HEADER TABLE
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{18, 82});

                PdfPCell logoCell = new PdfPCell(logoImage);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(logoCell);

                Paragraph headerText = new Paragraph();
                headerText.setAlignment(Element.ALIGN_CENTER);
                headerText.add(new Chunk("KEMENTERIAN PENDIDIKAN TINGGI, SAINS,\nDAN TEKNOLOGI\n", headerFontNormal));
                headerText.add(new Chunk("POLITEKNIK NEGERI SRIWIJAYA\n", headerFontBold));
                headerText.add(new Chunk("JURUSAN MANAJEMEN INFORMATIKA\n", headerFontBold));
                headerText.add(new Chunk("Jalan Srijaya Negara Bukit Besar - Palembang 30139 Telepon (0711) 353414\n", headerFontNormal));
                headerText.add(new Chunk("Laman: http://polsri.ac.id, Pos El : info@polsri.ac.id", headerFontNormal));

                PdfPCell textCell = new PdfPCell(headerText);
                textCell.setBorder(PdfPCell.NO_BORDER);
                textCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(textCell);

                document.add(headerTable);
                document.add(new Paragraph("\n"));

                // LINE SEPARATOR
                LineSeparator ls = new LineSeparator();
                ls.setLineWidth(2f);
                document.add(new Chunk(ls));
                document.add(new Paragraph("\n"));

                // TITLE
                Paragraph title = new Paragraph();
                title.setAlignment(Element.ALIGN_CENTER);
                title.add(new Chunk("SURAT TUGAS KOMPENSASI KETIDAKHADIRAN\n", titleFont));
                title.add(new Chunk("SEMESTER GANJIL TAHUN AKADEMIK 2025/2026\n", titleFont));
                title.add(new Chunk("NOMOR : 0001/PL6.1.25/AK/2026", titleFont));
                document.add(title);
                document.add(new Paragraph("\n"));

                // OPENING
                document.add(new Paragraph("Politeknik Negeri Sriwijaya memberikan tugas khusus sebagai kompensasi kepada mahasiswa :", normalFont));

                // IDENTITAS
                PdfPTable infoTable = new PdfPTable(3);
                infoTable.setWidthPercentage(80);
                infoTable.setWidths(new float[]{30, 5, 65});
                infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                addInfoRow(infoTable, "Nama", nama, normalFont);
                addInfoRow(infoTable, "NPM", nim, normalFont);
                addInfoRow(infoTable, "Semester / Kelas", kelas, normalFont);
                addInfoRow(infoTable, "Jurusan", "Manajemen Informatika", normalFont);
                addInfoRow(infoTable, "Prodi", "DIII/Manajemen Informatika", normalFont);
                document.add(infoTable);

                // KETERANGAN WAKTU
                document.add(new Paragraph("Yang bersangkutan tidak hadir " + jumlah + " menit selama semester ganjil tahun akademik 2025/2026", normalFont));

                double jamSekolah = jumlah / 50.0;
                document.add(new Paragraph("Lama Kompensasi : " + jumlah + " menit, atau " + String.format(Locale.US, "%.1f", jamSekolah) + " jam sekolah", normalFont));

                int menitDikompensasi = jumlah - sisa;
                double jamDikompensasi = menitDikompensasi / 50.0;
                document.add(new Paragraph("Sekarang dikompensasi : " + menitDikompensasi + " menit, atau " + String.format(Locale.US, "%.1f", jamDikompensasi) + " jam sekolah", normalFont));

                double jamSisa = sisa / 50.0;
                document.add(new Paragraph("Sisa : " + sisa + " menit, atau " + String.format(Locale.US, "%.1f", jamSisa) + " jam sekolah", normalFont));

                String tempat = (tugas != null && !tugas.isEmpty() && !tugas.equals("-")) ? tugas : "......................................................................";
                document.add(new Paragraph("Saudara melaksanakan kompensasi di bengkel/laboratorium/ " + tempat + " pada", normalFont));
                document.add(new Paragraph("Hari/Tanggal : " + tanggal, normalFont));
                document.add(new Paragraph("Pukul              : .....................", normalFont));
                document.add(new Paragraph("\n"));

                // TABEL SESI
                PdfPTable sesiTable = new PdfPTable(5);
                sesiTable.setWidthPercentage(100);
                sesiTable.setWidths(new float[]{8, 20, 15, 37, 20});

                addTableCell(sesiTable, "No", boldFont, Element.ALIGN_CENTER);
                addTableCell(sesiTable, "Tanggal", boldFont, Element.ALIGN_CENTER);
                addTableCell(sesiTable, "Jumlah", boldFont, Element.ALIGN_CENTER);
                addTableCell(sesiTable, "Uraian Pelaksanaan Kompensasi", boldFont, Element.ALIGN_CENTER);
                addTableCell(sesiTable, "Nama & Tanda Tangan", boldFont, Element.ALIGN_CENTER);

                Cursor sesiCursor = databaseHelper.getSesiByKompensasi(idKompensasi);
                int no = 1;
                if (sesiCursor != null && sesiCursor.moveToFirst()) {
                    do {
                        String tglSesi = sesiCursor.getString(6);
                        String menitSesi = String.valueOf(sesiCursor.getInt(5));
                        String tugasSesi = sesiCursor.getString(2);

                        addTableCell(sesiTable, String.valueOf(no++), normalFont, Element.ALIGN_CENTER);
                        addTableCell(sesiTable, tglSesi, normalFont, Element.ALIGN_CENTER);
                        addTableCell(sesiTable, menitSesi, normalFont, Element.ALIGN_CENTER);
                        addTableCell(sesiTable, tugasSesi, normalFont, Element.ALIGN_LEFT);
                        addTableCell(sesiTable, "Disetujui", normalFont, Element.ALIGN_CENTER);
                    } while (sesiCursor.moveToNext());
                } else {
                    addTableCell(sesiTable, " ", normalFont, Element.ALIGN_CENTER);
                    addTableCell(sesiTable, " ", normalFont, Element.ALIGN_CENTER);
                    addTableCell(sesiTable, " ", normalFont, Element.ALIGN_CENTER);
                    addTableCell(sesiTable, " ", normalFont, Element.ALIGN_CENTER);
                    addTableCell(sesiTable, " ", normalFont, Element.ALIGN_CENTER);
                }
                if (sesiCursor != null) sesiCursor.close();

                document.add(sesiTable);
                document.add(new Paragraph("\n"));

                // TTD KAJUR
                Paragraph date = new Paragraph("Palembang, " + tanggal, normalFont);
                date.setAlignment(Element.ALIGN_RIGHT);
                document.add(date);

                Paragraph titleKajur = new Paragraph("Ketua Jurusan,\nManajemen Informatika", normalFont);
                titleKajur.setAlignment(Element.ALIGN_RIGHT);
                document.add(titleKajur);

                Bitmap qrBitmap = generateQrCode("KAJUR-APPROVED-" + nim);
                ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, qrStream);
                Image qrImage = Image.getInstance(qrStream.toByteArray());
                qrImage.scaleAbsolute(70, 70);
                qrImage.setAlignment(Element.ALIGN_RIGHT);
                document.add(qrImage);

                Paragraph nameKajur = new Paragraph(namaKajurData + "\nNIP. " + nipKajurData, normalFont);
                nameKajur.setAlignment(Element.ALIGN_RIGHT);
                document.add(nameKajur);
                document.add(new Paragraph("\n"));

                // TABEL PENGESAHAN
                PdfPTable pengesahanTable = new PdfPTable(4);
                pengesahanTable.setWidthPercentage(100);
                pengesahanTable.setWidths(new float[]{30, 25, 25, 20});

                addTableCell(pengesahanTable, "Pengesahan Setelah Kompensasi", boldFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, "Mahasiswa", boldFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, "Penanggung Jawab", boldFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, "Sekjur / KPS", boldFont, Element.ALIGN_CENTER);

                addTableCell(pengesahanTable, "Nama", normalFont, Element.ALIGN_LEFT);
                addTableCell(pengesahanTable, nama, normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, namaDosenPa, normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, namaSekjur, normalFont, Element.ALIGN_CENTER);

                addTableCell(pengesahanTable, "Tanggal", normalFont, Element.ALIGN_LEFT);
                addTableCell(pengesahanTable, tanggal, normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, tanggal, normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, tanggal, normalFont, Element.ALIGN_CENTER);

                addTableCell(pengesahanTable, "Tanda Tangan", normalFont, Element.ALIGN_LEFT);
                addTableCell(pengesahanTable, "Mengetahui", normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, "Menyetujui", normalFont, Element.ALIGN_CENTER);
                addTableCell(pengesahanTable, "Menyetujui", normalFont, Element.ALIGN_CENTER);

                document.add(pengesahanTable);

                document.close();

                Toast.makeText(this, "PDF berhasil dibuat", Toast.LENGTH_LONG).show();

                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        finish();
    }

    private Bitmap generateQrCode(String text) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void addInfoRow(PdfPTable table, String key, String value, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(key, font));
        cell1.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cell2 = new PdfPCell(new Phrase(":", font));
        cell2.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cell3 = new PdfPCell(new Phrase(value, font));
        cell3.setBorder(PdfPCell.NO_BORDER);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
    }

    private void addTableCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);
        table.addCell(cell);
    }
}