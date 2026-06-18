import os
import re

java_dir = r"C:\Users\Nasswa Erwina\AndroidStudioProjects\Kompensasi neeww\Kompensasi\app\src\main\java\com\example\kompensasi"

patterns = [
    (
        re.compile(r'(?<!// ================= INISIALISASI HALAMAN \(ONCREATE\) =================\n    )(protected void onCreate\(Bundle savedInstanceState\) {)'),
        r'// ================= INISIALISASI HALAMAN (ONCREATE) =================\n    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.\n    \1'
    ),
    (
        re.compile(r'(?<!// ================= KELAS ADAPTER =================\n)(public class \w+ extends RecyclerView\.Adapter)'),
        r'// ================= KELAS ADAPTER =================\n// Posisi kelas ini berfungsi sebagai jembatan untuk menampilkan list data ke dalam RecyclerView (Daftar).\n\1'
    ),
    (
        re.compile(r'(?<!// ================= MENGISI DATA KE BARIS =================\n    )(public void onBindViewHolder\(.*\) {)'),
        r'// ================= MENGISI DATA KE BARIS =================\n    // Posisi ini memasukkan data dari database/model ke komponen UI (Teks, Gambar) pada tiap baris list.\n    \1'
    ),
    (
        re.compile(r'(?<!// ================= JUMLAH DATA =================\n    )(public int getItemCount\(\) {)'),
        r'// ================= JUMLAH DATA =================\n    // Posisi ini menentukan seberapa banyak baris data yang akan ditampilkan di daftar.\n    \1'
    ),
    (
        re.compile(r'(?<!// ================= DEKLARASI VARIABEL =================\n)(public class \w+Activity extends AppCompatActivity {)'),
        r'// ================= HALAMAN ACTIVITY =================\n// Ini adalah kelas halaman utama untuk antarmuka pengguna.\n\1'
    )
]

def add_comments(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original_content = content
    for pattern, replacement in patterns:
        content = pattern.sub(replacement, content)

    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated: {os.path.basename(filepath)}")

for root, _, files in os.walk(java_dir):
    for file in files:
        if file.endswith(".java"):
            add_comments(os.path.join(root, file))

print("Proses penambahan komentar selesai.")
