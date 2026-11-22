package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.example.model.Season;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit test untuk {@link SeasonCalculator}.
 *
 * <p>{@code @ParameterizedTest} + {@code @CsvSource} menjalankan satu test
 * berkali-kali dengan data berbeda. Setiap baris {@code "tanggal, musim"} di
 * bawah adalah satu kasus uji. Yang paling penting diuji adalah tanggal
 * <i>perbatasan</i> (sehari sebelum dan saat musim berganti), karena di situlah
 * kesalahan paling sering terjadi.</p>
 */
class SeasonCalculatorTest {

    private final SeasonCalculator calculator = new SeasonCalculator();

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
        // Musim dingin → semi (20 Maret)
        "2026-03-19, WINTER",
        "2026-03-20, SPRING",
        // Musim semi → panas (21 Juni)
        "2026-06-20, SPRING",
        "2026-06-21, SUMMER",
        // Musim panas → gugur (22 September)
        "2026-09-21, SUMMER",
        "2026-09-22, AUTUMN",
        // Musim gugur → dingin (21 Desember)
        "2026-12-20, AUTUMN",
        "2026-12-21, WINTER",
        // Tanggal di tengah musim
        "2026-01-15, WINTER",
        "2026-05-01, SPRING",
        "2026-08-17, SUMMER",
        "2026-11-22, AUTUMN",
    })
    void returnsCorrectSeason(LocalDate date, Season expected) {
        assertEquals(expected, calculator.seasonOf(date));
    }
}
