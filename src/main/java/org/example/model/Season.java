package org.example.model;

/**
 * Daftar musim (season) yang bisa ditampilkan oleh aplikasi kalender.
 *
 * <p><b>Apa itu {@code enum}?</b><br>
 * {@code enum} adalah tipe data khusus di Java yang nilainya sudah pasti dan
 * terbatas. Di sini hanya ada empat musim, jadi kita tidak perlu memakai
 * {@code String} biasa seperti {@code "Spring"} yang rawan salah ketik
 * (misalnya {@code "spring"} atau {@code "Sprng"}). Kalau salah tulis nama
 * enum, compiler langsung memberi error sebelum program dijalankan.</p>
 *
 * <p>Kelas ini hanya berisi <i>data</i> musim. Cara menentukan musim dari
 * sebuah tanggal ada di {@link org.example.service.SeasonCalculator}, dan
 * warna tiap musim ada di {@link org.example.ui.theme.SeasonTheme}.</p>
 */
public enum Season {

    /** Musim semi (± 20 Maret – 20 Juni). */
    SPRING("Spring"),

    /** Musim panas (± 21 Juni – 21 September). */
    SUMMER("Summer"),

    /** Musim gugur (± 22 September – 20 Desember). */
    AUTUMN("Autumn"),

    /** Musim dingin (± 21 Desember – 19 Maret). */
    WINTER("Winter");

    /**
     * Nama musim yang ditampilkan ke pengguna di layar.
     * Kata kunci {@code final} berarti nilainya tidak bisa diubah setelah diisi.
     */
    private final String displayName;

    /**
     * Constructor enum: dipanggil otomatis satu kali untuk setiap konstanta
     * di atas (SPRING, SUMMER, dst.) saat program pertama kali memakai enum ini.
     *
     * @param displayName nama musim yang akan tampil di layar
     */
    Season(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Mengambil nama musim yang ramah dibaca manusia.
     *
     * @return contoh: {@code "Spring"} untuk {@link #SPRING}
     */
    public String getDisplayName() {
        return displayName;
    }
}
