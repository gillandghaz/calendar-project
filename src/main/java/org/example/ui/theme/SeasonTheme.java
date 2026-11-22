package org.example.ui.theme;

import java.awt.Color;

import org.example.model.Season;

/**
 * Pasangan warna (latar + tulisan) untuk bagian atas aplikasi sesuai musim.
 *
 * <p><b>Apa itu {@code record}?</b><br>
 * {@code record} adalah cara singkat di Java (sejak versi 16) untuk membuat
 * kelas yang tugasnya hanya membawa data. Baris
 * {@code record SeasonTheme(Color background, Color foreground)} otomatis
 * membuatkan constructor, method {@code background()} dan {@code foreground()},
 * serta {@code equals}/{@code hashCode}/{@code toString}, tanpa kita tulis manual.</p>
 *
 * @param background warna latar panel atas
 * @param foreground warna tulisan label musim
 */
public record SeasonTheme(Color background, Color foreground) {

    // Satu tema untuk tiap musim. Dibuat sekali saja (static final) lalu dipakai berulang.
    private static final SeasonTheme SPRING = new SeasonTheme(new Color(166, 188, 201), new Color(255, 244, 235));
    private static final SeasonTheme SUMMER = new SeasonTheme(new Color(200, 184, 147), new Color(61, 21, 52));
    private static final SeasonTheme AUTUMN = new SeasonTheme(new Color(111, 77, 56), new Color(246, 224, 182));
    private static final SeasonTheme WINTER = new SeasonTheme(new Color(62, 75, 142), new Color(166, 188, 201));

    /**
     * Mengambil tema warna untuk musim tertentu.
     *
     * @param season musim yang sedang aktif
     * @return pasangan warna latar & tulisan untuk musim itu
     */
    public static SeasonTheme of(Season season) {
        return switch (season) {
            case SPRING -> SPRING;
            case SUMMER -> SUMMER;
            case AUTUMN -> AUTUMN;
            case WINTER -> WINTER;
        };
    }
}
