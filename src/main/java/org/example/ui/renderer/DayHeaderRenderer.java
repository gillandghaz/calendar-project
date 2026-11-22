package org.example.ui.renderer;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.example.ui.theme.AppTheme;

/**
 * Mengatur tampilan baris judul tabel (nama hari: Sunday, Monday, ...).
 *
 * <p><b>Apa itu "renderer"?</b><br>
 * Di Swing, {@code JTable} tidak menggambar sendiri tiap kotaknya. Ia meminta
 * bantuan objek <i>renderer</i> untuk menentukan seperti apa sebuah kotak
 * terlihat (warna, posisi teks, font). Dengan membuat renderer sendiri, kita
 * bisa mengubah gaya tabel tanpa mengubah datanya.</p>
 *
 * <p>Renderer ini cukup sederhana: teks dibuat rata tengah, dan warna
 * latar/tulisan diambil dari {@link AppTheme}.</p>
 */
public class DayHeaderRenderer extends DefaultTableCellRenderer {

    /** Mengatur gaya header sekali saja saat renderer dibuat. */
    public DayHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER); // Nama hari berada di tengah kotak
        setBackground(AppTheme.HEADER_BACKGROUND);
        setForeground(AppTheme.HEADER_FOREGROUND);
    }
}
