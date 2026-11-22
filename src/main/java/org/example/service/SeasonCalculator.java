package org.example.service;

import java.time.LocalDate;
import java.time.Month;

import org.example.model.Season;

/**
 * Menentukan musim (season) dari sebuah tanggal.
 *
 * <p><b>Kenapa dipisah ke kelas sendiri?</b><br>
 * Aturan "tanggal sekian masuk musim apa" adalah <i>logika murni</i>: tidak ada
 * hubungannya dengan tombol, tabel, atau warna. Dengan memisahkannya dari
 * tampilan (GUI), aturan ini bisa diuji otomatis lewat unit test
 * ({@code SeasonCalculatorTest}) dan bisa dipakai ulang di tempat lain.</p>
 *
 * <p><b>Aturan yang dipakai:</b> pembagian musim <i>astronomis</i> untuk
 * belahan bumi utara (Northern Hemisphere). Tanggal pergantian musim
 * sebenarnya bisa bergeser ±1 hari tiap tahun, tetapi di sini dibulatkan
 * menjadi tanggal tetap agar sederhana:</p>
 * <ul>
 *   <li>Spring : 20 Maret – 20 Juni</li>
 *   <li>Summer : 21 Juni – 21 September</li>
 *   <li>Autumn : 22 September – 20 Desember</li>
 *   <li>Winter : 21 Desember – 19 Maret</li>
 * </ul>
 */
public class SeasonCalculator {

    // Tanggal (hari dalam bulan) dimulainya setiap musim.
    // Disimpan sebagai konstanta (static final) supaya mudah diubah di satu tempat
    // dan tidak ada "angka ajaib" (magic number) yang tersebar di dalam kode.
    private static final int SPRING_START_DAY = 20; // 20 Maret
    private static final int SUMMER_START_DAY = 21; // 21 Juni
    private static final int AUTUMN_START_DAY = 22; // 22 September
    private static final int WINTER_START_DAY = 21; // 21 Desember

    /**
     * Mencari musim untuk tanggal tertentu.
     *
     * <p>Cara kerjanya: kita lihat bulannya dulu. Bulan yang seluruhnya berada
     * di dalam satu musim langsung dikembalikan. Bulan "perbatasan" (Maret,
     * Juni, September, Desember) dicek lagi tanggalnya, apakah sudah melewati
     * tanggal pergantian musim atau belum.</p>
     *
     * @param date tanggal yang ingin dicek, contoh {@code LocalDate.of(2026, 7, 1)}
     * @return musim pada tanggal tersebut, contoh {@link Season#SUMMER}
     */
    public Season seasonOf(LocalDate date) {
        Month month = date.getMonth();   // Bulan dari tanggal, contoh: JULY
        int day = date.getDayOfMonth();  // Tanggal dalam bulan, contoh: 1

        // "switch" memilih satu cabang berdasarkan nilai bulan.
        // Bentuk "case X -> ..." (switch expression) langsung menghasilkan nilai.
        return switch (month) {
            // Bulan yang seluruh harinya berada di satu musim:
            case APRIL, MAY -> Season.SPRING;
            case JULY, AUGUST -> Season.SUMMER;
            case OCTOBER, NOVEMBER -> Season.AUTUMN;
            case JANUARY, FEBRUARY -> Season.WINTER;

            // Bulan perbatasan: tergantung tanggalnya.
            // Contoh: 19 Maret masih WINTER, 20 Maret sudah SPRING.
            case MARCH -> day >= SPRING_START_DAY ? Season.SPRING : Season.WINTER;
            case JUNE -> day >= SUMMER_START_DAY ? Season.SUMMER : Season.SPRING;
            case SEPTEMBER -> day >= AUTUMN_START_DAY ? Season.AUTUMN : Season.SUMMER;
            case DECEMBER -> day >= WINTER_START_DAY ? Season.WINTER : Season.AUTUMN;
        };
    }
}
