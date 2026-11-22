package org.example.ui.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.example.ui.theme.AppTheme;

/**
 * Mengatur tampilan setiap kotak tanggal di dalam kalender.
 *
 * <p>Aturan tampilannya:</p>
 * <ul>
 *   <li>Angka tanggal rata tengah dengan ukuran huruf {@link AppTheme#DAY_FONT_SIZE}.</li>
 *   <li>Kotak kosong (bukan bagian bulan ini) berwarna abu-abu sangat terang.</li>
 *   <li>Kotak yang sedang dipilih berwarna biru muda.</li>
 *   <li>Kolom Minggu (kolom pertama) angkanya berwarna merah.</li>
 * </ul>
 *
 * <p>Lihat penjelasan "renderer" di {@link DayHeaderRenderer}.</p>
 */
public class DayCellRenderer extends DefaultTableCellRenderer {

    /** Indeks kolom hari Minggu. Kolom dihitung dari 0, dan Minggu ada di paling kiri. */
    private static final int SUNDAY_COLUMN = 0;

    /**
     * Dipanggil otomatis oleh {@code JTable} setiap kali sebuah kotak perlu digambar
     * (misalnya saat aplikasi dibuka, bulan diganti, atau kotak diklik).
     *
     * @param table      tabel yang sedang digambar
     * @param value      isi kotak: angka tanggal, atau {@code null} jika kosong
     * @param isSelected {@code true} jika kotak ini sedang dipilih pengguna
     * @param hasFocus   {@code true} jika kotak ini sedang fokus keyboard
     * @param row        nomor baris kotak (mulai dari 0)
     * @param column     nomor kolom kotak (mulai dari 0, 0 = Minggu)
     * @return komponen (label) yang sudah diatur gayanya untuk digambar
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // Panggil versi bawaan dulu supaya teks kotak terisi dengan benar.
        // hasFocus sengaja dikirim "false" agar tidak muncul garis fokus di sekitar kotak.
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(getFont().deriveFont(AppTheme.DAY_FONT_SIZE)); // deriveFont = salin font dengan ukuran baru
        setBackground(backgroundFor(value, isSelected));
        setForeground(column == SUNDAY_COLUMN ? AppTheme.SUNDAY_FOREGROUND : AppTheme.DAY_FOREGROUND);

        // DefaultTableCellRenderer sendiri adalah sebuah JLabel, jadi "this" bisa langsung dikembalikan.
        return this;
    }

    /**
     * Memilih warna latar kotak.
     *
     * @param value      isi kotak ({@code null} berarti kotak kosong)
     * @param isSelected apakah kotak sedang dipilih
     * @return warna latar yang sesuai
     */
    private Color backgroundFor(Object value, boolean isSelected) {
        if (value == null) {
            return AppTheme.EMPTY_CELL_BACKGROUND;
        }
        if (isSelected) {
            return AppTheme.SELECTED_CELL_BACKGROUND;
        }
        return AppTheme.DAY_CELL_BACKGROUND;
    }
}
