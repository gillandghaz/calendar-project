package org.example.service;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Logika inti kalender: menyusun angka-angka tanggal ke dalam kotak 6 baris × 7 kolom.
 *
 * <p><b>Kenapa dipisah dari tampilan?</b><br>
 * Kelas ini tidak tahu apa-apa soal {@code JTable} atau Swing. Ia hanya
 * menerima bulan & tahun lalu mengembalikan array 2 dimensi berisi angka
 * tanggal. Tampilan ({@code CalendarFrame}) yang bertugas memindahkan isi
 * array ini ke tabel di layar. Dengan begitu logikanya mudah diuji dan tidak
 * tercampur dengan kode GUI.</p>
 *
 * <p><b>Gambaran hasilnya</b> (contoh: Juli 2026, tanggal 1 jatuh hari Rabu):</p>
 * <pre>
 *   Sun  Mon  Tue  Wed  Thu  Fri  Sat
 *  null null null   1    2    3    4     ← baris 0
 *    5    6    7    8    9   10   11     ← baris 1
 *   ...
 * </pre>
 * Kotak yang bukan bagian dari bulan tersebut diisi {@code null} (kosong).
 */
public class CalendarService {

    /** Jumlah baris kalender. 6 baris cukup untuk bulan terpanjang sekalipun. */
    public static final int ROWS = 6;

    /** Jumlah kolom kalender = jumlah hari dalam seminggu (Minggu s.d. Sabtu). */
    public static final int COLUMNS = 7;

    /** Tahun paling awal yang bisa dipilih di combo box tahun. */
    public static final int MIN_YEAR = 1900;

    /** Tahun paling akhir yang bisa dipilih di combo box tahun. */
    public static final int MAX_YEAR = 2100;

    /**
     * Membuat susunan tanggal untuk satu bulan.
     *
     * @param yearMonth bulan & tahun yang ingin ditampilkan,
     *                  contoh {@code YearMonth.of(2026, 7)} untuk Juli 2026
     * @return array {@code [6][7]} berisi angka tanggal, atau {@code null} untuk kotak kosong
     */
    public Integer[][] buildMonthGrid(YearMonth yearMonth) {
        // Array Integer (bukan int) dipakai supaya kotak kosong bisa bernilai null.
        // Saat dibuat, semua isinya otomatis null.
        Integer[][] grid = new Integer[ROWS][COLUMNS];

        int startColumn = firstColumnOf(yearMonth); // Kolom tempat tanggal 1 diletakkan
        int daysInMonth = yearMonth.lengthOfMonth(); // 28/29/30/31, tahun kabisat otomatis dihitung

        // Masukkan tanggal 1 sampai tanggal terakhir satu per satu.
        for (int day = 1; day <= daysInMonth; day++) {
            // "position" adalah nomor kotak jika semua kotak dijejer jadi satu garis
            // (kotak ke-0 sampai ke-41). Dikurangi 1 karena tanggal dimulai dari 1,
            // sedangkan nomor kotak dimulai dari 0.
            int position = startColumn + day - 1;

            // Ubah nomor kotak menjadi baris & kolom.
            // Pembagian bulat (/) → baris ke berapa; sisa bagi (%) → kolom ke berapa.
            // Contoh: position 10 → baris 10/7 = 1, kolom 10%7 = 3.
            int row = position / COLUMNS;
            int col = position % COLUMNS;

            grid[row][col] = day;
        }
        return grid;
    }

    /**
     * Mencari kolom (0–6) untuk tanggal 1 di bulan tersebut.
     * Kolom 0 = Minggu, 1 = Senin, ..., 6 = Sabtu.
     *
     * <p>Java memberi nomor hari dengan aturan ISO: Senin = 1 ... Minggu = 7.
     * Kalender kita dimulai dari Minggu, jadi nomornya digeser dengan sisa bagi 7:
     * Minggu 7 % 7 = 0, Senin 1 % 7 = 1, ..., Sabtu 6 % 7 = 6.</p>
     *
     * @param yearMonth bulan & tahun yang dicek
     * @return indeks kolom tanggal 1 (0 = Minggu)
     */
    public int firstColumnOf(YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1); // Contoh: 2026-07-01
        return firstDay.getDayOfWeek().getValue() % COLUMNS;
    }

    /**
     * Menentukan tanggal "bawaan" ketika pengguna belum memilih tanggal apa pun.
     *
     * <ul>
     *   <li>Kalau bulan yang ditampilkan adalah bulan ini → pakai tanggal hari ini.</li>
     *   <li>Kalau bulan lain → pakai tanggal 1.</li>
     * </ul>
     *
     * @param yearMonth bulan yang sedang ditampilkan
     * @param today     tanggal hari ini (dikirim dari luar supaya mudah diuji)
     * @return tanggal bawaan, antara 1 dan jumlah hari di bulan itu
     */
    public int defaultDay(YearMonth yearMonth, LocalDate today) {
        if (YearMonth.from(today).equals(yearMonth)) {
            return today.getDayOfMonth();
        }
        return 1;
    }
}
