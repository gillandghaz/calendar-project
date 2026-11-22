package org.example.ui.theme;

import java.awt.Color;
import java.awt.Font;

/**
 * Kumpulan warna dan font umum yang dipakai di seluruh tampilan aplikasi.
 *
 * <p><b>Kenapa dikumpulkan di sini?</b><br>
 * Kalau warna ditulis langsung di banyak tempat ({@code new Color(200, 200, 200)}),
 * mengganti tema berarti harus mencari dan mengubahnya satu per satu.
 * Dengan satu kelas tema, cukup ubah nilainya di sini dan seluruh aplikasi ikut berubah.</p>
 *
 * <p>Kelas ini {@code final} dan constructor-nya {@code private} karena kelas
 * ini hanya wadah konstanta: tidak perlu (dan tidak boleh) dibuat objeknya
 * dengan {@code new AppTheme()}. Cara pakainya langsung, misalnya
 * {@code AppTheme.GRID_COLOR}.</p>
 *
 * <p>Warna ditulis dalam format RGB: {@code new Color(merah, hijau, biru)},
 * masing-masing bernilai 0–255.</p>
 */
public final class AppTheme {

    // ===== Font =====

    /**
     * Font label musim. Jika font "Poppins" tidak terpasang di komputer,
     * Java otomatis memakai font bawaan sistem sebagai gantinya.
     */
    public static final Font SEASON_LABEL_FONT = new Font("Poppins", Font.BOLD, 16);

    /** Ukuran huruf angka tanggal di dalam kotak kalender (dalam point). */
    public static final float DAY_FONT_SIZE = 16f;

    // ===== Header nama hari (Sunday, Monday, ...) =====

    /** Warna latar header nama hari (abu-abu terang). */
    public static final Color HEADER_BACKGROUND = new Color(220, 220, 220);

    /** Warna tulisan header nama hari (hijau tua kebiruan). */
    public static final Color HEADER_FOREGROUND = new Color(30, 70, 70);

    // ===== Kotak tanggal =====

    /** Warna garis pembatas antar kotak tanggal. */
    public static final Color GRID_COLOR = new Color(200, 200, 200);

    /** Latar kotak yang kosong (bukan bagian dari bulan yang ditampilkan). */
    public static final Color EMPTY_CELL_BACKGROUND = new Color(245, 245, 245);

    /** Latar kotak tanggal biasa. */
    public static final Color DAY_CELL_BACKGROUND = Color.WHITE;

    /** Latar kotak tanggal yang sedang diklik/dipilih (biru muda). */
    public static final Color SELECTED_CELL_BACKGROUND = new Color(190, 220, 245);

    /** Warna angka tanggal di hari Minggu (merah, seperti kalender pada umumnya). */
    public static final Color SUNDAY_FOREGROUND = Color.RED;

    /** Warna angka tanggal untuk hari selain Minggu. */
    public static final Color DAY_FOREGROUND = Color.DARK_GRAY;

    /** Constructor private: mencegah kelas ini dibuat objeknya. */
    private AppTheme() {
    }
}
