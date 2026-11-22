package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test untuk {@link CalendarService}.
 *
 * <p><b>Apa itu unit test?</b><br>
 * Kode kecil yang memeriksa apakah kode utama menghasilkan jawaban yang benar.
 * Jalankan dengan klik kanan file ini lalu pilih <i>Test File</i> (NetBeans),
 * <i>Run</i> (IntelliJ), atau ikon ▶ di sebelah method (VS Code).
 * Bisa juga lewat terminal: {@code mvn test}.</p>
 *
 * <p>Setiap method bertanda {@code @Test} adalah satu skenario uji.
 * {@code assertEquals(harapan, hasil)} akan gagal jika keduanya berbeda.</p>
 */
class CalendarServiceTest {

    private final CalendarService service = new CalendarService();

    @Test
    @DisplayName("1 Juli 2026 jatuh pada hari Rabu (kolom 3)")
    void firstDayIsPlacedInCorrectColumn() {
        Integer[][] grid = service.buildMonthGrid(YearMonth.of(2026, 7));

        // Minggu, Senin, Selasa (kolom 0–2) harus kosong.
        assertNull(grid[0][0]);
        assertNull(grid[0][2]);
        assertEquals(1, grid[0][3]);  // Rabu
        assertEquals(4, grid[0][6]);  // Sabtu
        assertEquals(5, grid[1][0]);  // Minggu berikutnya pindah ke baris baru
    }

    @Test
    @DisplayName("Bulan yang dimulai hari Minggu diletakkan di kolom 0")
    void monthStartingOnSundayStartsAtColumnZero() {
        // 1 Februari 2026 adalah hari Minggu.
        assertEquals(0, service.firstColumnOf(YearMonth.of(2026, 2)));
    }

    @Test
    @DisplayName("Tahun kabisat: Februari 2024 punya 29 hari")
    void leapYearFebruaryHas29Days() {
        Integer[][] grid = service.buildMonthGrid(YearMonth.of(2024, 2));
        assertEquals(29, findLastDay(grid));
    }

    @Test
    @DisplayName("Bulan terpanjang tetap muat dalam 6 baris")
    void longestMonthFitsInSixRows() {
        // Agustus 2026: dimulai hari Sabtu dan punya 31 hari, sehingga butuh 6 baris penuh.
        Integer[][] grid = service.buildMonthGrid(YearMonth.of(2026, 8));
        assertEquals(1, grid[0][6]);
        assertEquals(31, grid[5][1]);
    }

    @Test
    @DisplayName("Tanggal bawaan = hari ini jika bulan yang ditampilkan adalah bulan ini")
    void defaultDayIsTodayForCurrentMonth() {
        LocalDate today = LocalDate.of(2026, 11, 22);
        assertEquals(22, service.defaultDay(YearMonth.of(2026, 11), today));
    }

    @Test
    @DisplayName("Tanggal bawaan = 1 untuk bulan selain bulan ini")
    void defaultDayIsFirstForOtherMonths() {
        LocalDate today = LocalDate.of(2026, 11, 22);
        assertEquals(1, service.defaultDay(YearMonth.of(2026, 10), today));
        assertEquals(1, service.defaultDay(YearMonth.of(2025, 11), today));
    }

    /** Helper: mencari angka tanggal terbesar di dalam grid. */
    private static int findLastDay(Integer[][] grid) {
        int max = 0;
        for (Integer[] row : grid) {
            for (Integer day : row) {
                if (day != null && day > max) {
                    max = day;
                }
            }
        }
        return max;
    }
}
