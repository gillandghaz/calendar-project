package org.example;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.example.ui.CalendarFrame;

/**
 * Titik awal (entry point) aplikasi kalender.
 *
 * <p>Saat tombol <b>Run</b> ditekan di NetBeans, IntelliJ, atau VS Code,
 * Java mencari method {@code main} di kelas ini dan menjalankannya pertama kali.</p>
 *
 * <p>Tugas kelas ini hanya dua:</p>
 * <ol>
 *   <li>Mengatur gaya tampilan (Look and Feel) aplikasi.</li>
 *   <li>Membuka jendela kalender ({@link CalendarFrame}).</li>
 * </ol>
 */
public class Main {

    /** Logger dipakai untuk mencatat pesan error ke console, lebih rapi daripada System.out.println. */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /** Nama Look and Feel yang ingin dipakai. Nimbus tersedia bawaan sejak Java 6. */
    private static final String PREFERRED_LOOK_AND_FEEL = "Nimbus";

    /**
     * Method yang pertama kali dijalankan oleh Java.
     *
     * @param args argumen dari command line (tidak dipakai di aplikasi ini)
     */
    public static void main(String[] args) {
        applyLookAndFeel();

        // Semua kode yang membuat/mengubah tampilan Swing harus berjalan di
        // "Event Dispatch Thread" (EDT), yaitu thread khusus untuk GUI.
        // invokeLater menitipkan pekerjaan ke EDT supaya tampilan aman dan tidak error acak.
        EventQueue.invokeLater(() -> new CalendarFrame().setVisible(true));
    }

    /**
     * Mencoba memakai tema Nimbus. Jika tidak tersedia, aplikasi tetap jalan
     * dengan tema bawaan Java (error hanya dicatat, tidak menghentikan program).
     */
    private static void applyLookAndFeel() {
        try {
            // Telusuri semua tema yang terpasang, lalu pakai yang namanya "Nimbus".
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (PREFERRED_LOOK_AND_FEEL.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break; // Sudah ketemu, hentikan perulangan
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.WARNING, "Tema " + PREFERRED_LOOK_AND_FEEL + " gagal dipakai, memakai tema bawaan.", ex);
        }
    }
}
