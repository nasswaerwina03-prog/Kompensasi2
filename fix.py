import re

file_path = 'app/src/main/java/com/example/kompensasi/DashboardMahasiswaActivity.java'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

target = '''            if (total > 0) {
                        pendingPihak = "Kajur";'''

replacement = '''            if (total > 0) {
                progressKompen.setMax(total);
                progressKompen.setProgress(selesai);
            } else {
                progressKompen.setMax(100);
                progressKompen.setProgress(0);
            }

            // ================= NAV BAR VISIBILITY =================
            navLaporan.setVisibility(android.view.View.GONE);
            navAjukan.setVisibility(android.view.View.VISIBLE);

            if (total == 0) {
                navAjukan.setOnClickListener(v -> {});
            } else {
                navAjukan.setOnClickListener(v -> {
                    Intent intent = new Intent(DashboardMahasiswaActivity.this, PengajuanActivity.class);
                    intent.putExtra("nim", nimLogin);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });
            }

            // ================= AUTO APPROVE & UI =================
            if (sisa > 0) {
                tvStatusKompensasi.setText("Anda memiliki " + sisa + " menit kompensasi yang harus diselesaikan.");
                tvStatusKompensasi.setVisibility(android.view.View.VISIBLE);
                btnUnduhSurat.setVisibility(android.view.View.GONE);
            } else {
                tvStatusKompensasi.setVisibility(android.view.View.VISIBLE);
                if (statusDosenPa.equals("Disetujui") && statusSekjur.equals("Disetujui") && statusKajur.equals("Disetujui")) {
                    tvStatusKompensasi.setText("Anda tidak memiliki tanggungan kompensasi.");
                    btnUnduhSurat.setVisibility(android.view.View.VISIBLE);
                } else {
                    btnUnduhSurat.setVisibility(android.view.View.GONE);
                    String pendingPihak = "";
                    if (!statusDosenPa.equals("Disetujui")) {
                        pendingPihak = "Dosen PA";
                    } else if (!statusSekjur.equals("Disetujui")) {
                        pendingPihak = "Sekjur";
                    } else if (!statusKajur.equals("Disetujui")) {
                        pendingPihak = "Kajur";'''

target = target.replace('\r\n', '\n')
replacement = replacement.replace('\r\n', '\n')
content = content.replace('\r\n', '\n')

content = content.replace(target, replacement)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
