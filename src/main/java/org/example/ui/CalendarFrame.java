package org.example.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.example.model.Season;
import org.example.service.CalendarService;
import org.example.service.SeasonCalculator;
import org.example.ui.renderer.DayCellRenderer;
import org.example.ui.renderer.DayHeaderRenderer;
import org.example.ui.theme.AppTheme;
import org.example.ui.theme.SeasonTheme;

/**
 * Jendela utama aplikasi kalender.
 *
 * <p><b>Tanggung jawab kelas ini hanya urusan tampilan</b>, yaitu:</p>
 * <ul>
 *   <li>Menyusun komponen di layar (combo box bulan & tahun, label musim, tabel tanggal).</li>
 *   <li>Merespons aksi pengguna (ganti bulan/tahun, klik tanggal).</li>
 *   <li>Menampilkan hasil dari kelas logika ke layar.</li>
 * </ul>
 *
 * <p>Perhitungan tidak dilakukan di sini, tetapi diserahkan ke kelas lain:</p>
 * <ul>
 *   <li>{@link CalendarService}: menyusun tanggal ke kotak 6×7.</li>
 *   <li>{@link SeasonCalculator}: menentukan musim dari sebuah tanggal.</li>
 *   <li>{@link SeasonTheme} & {@link AppTheme}: menyimpan warna dan font.</li>
 *   <li>{@link DayCellRenderer} & {@link DayHeaderRenderer}: mengatur gaya kotak tabel.</li>
 * </ul>
 *
 * <p><b>Catatan untuk NetBeans:</b> file ini berpasangan dengan
 * {@code CalendarFrame.form}. Bagian bertanda "Generated Code" dibuat otomatis
 * oleh GUI Designer NetBeans (tab <i>Design</i>) dan <b>tidak boleh diedit manual</b>.
 * Ubah tata letak lewat tab Design, lalu NetBeans akan menulis ulang bagian itu.</p>
 */
public class CalendarFrame extends javax.swing.JFrame {

    /** Teks di depan nama musim pada label, contoh hasilnya: "Season : Summer". */
    private static final String SEASON_LABEL_PREFIX = "Season : ";

    /** Berapa baris pilihan yang terlihat saat combo box dibuka (12 = semua bulan tanpa scroll). */
    private static final int COMBO_VISIBLE_ROWS = 12;

    // Objek-objek logika. Dibuat sekali di sini, lalu dipakai berulang selama aplikasi berjalan.
    private final CalendarService calendarService = new CalendarService();
    private final SeasonCalculator seasonCalculator = new SeasonCalculator();

    /**
     * Constructor: dipanggil saat jendela dibuat dengan {@code new CalendarFrame()}.
     *
     * <p>Urutannya penting:</p>
     * <ol>
     *   <li>{@code initComponents()} membuat semua komponen dari GUI Designer.</li>
     *   <li>Komponen diberi gaya dan diisi data awal.</li>
     *   <li>Kalender digambar untuk bulan & tahun saat ini.</li>
     * </ol>
     */
    public CalendarFrame() {
        initComponents();          // 1. Buat komponen (kode otomatis NetBeans, lihat di bawah)
        setLocationRelativeTo(null); // Taruh jendela di tengah layar saat dibuka

        setupComboBoxes();         // 2. Isi pilihan tahun & atur combo box
        setupSeasonLabel();        //    Atur font & warna awal label musim
        setupCalendarTable();      //    Atur gaya tabel & pasang listener klik
        selectCurrentMonth();      //    Pilih bulan & tahun hari ini

        updateCalendar();          // 3. Gambar kalender pertama kali
    }

    // =====================================================================
    // Persiapan komponen (dipanggil sekali dari constructor)
    // =====================================================================

    /**
     * Mengatur combo box bulan dan mengisi combo box tahun.
     *
     * <p>Combo box tahun di GUI Designer masih berisi contoh ("Item 1", "Item 2", ...).
     * Isinya dihapus lalu diganti dengan tahun {@link CalendarService#MIN_YEAR}
     * sampai {@link CalendarService#MAX_YEAR} memakai perulangan, supaya kita
     * tidak perlu mengetik 200 tahun secara manual.</p>
     */
    private void setupComboBoxes() {
        comboMonth.setMaximumRowCount(COMBO_VISIBLE_ROWS);
        comboYear.setMaximumRowCount(COMBO_VISIBLE_ROWS);

        comboYear.removeAllItems(); // Buang isi contoh dari GUI Designer
        for (int year = CalendarService.MIN_YEAR; year <= CalendarService.MAX_YEAR; year++) {
            comboYear.addItem(String.valueOf(year)); // Combo box berisi String, jadi angka diubah ke teks
        }
    }

    /** Memberi font dan warna awal pada label musim. Warnanya nanti berubah sesuai musim. */
    private void setupSeasonLabel() {
        labelSeason.setFont(AppTheme.SEASON_LABEL_FONT);
        labelSeason.setForeground(java.awt.Color.WHITE);
    }

    /**
     * Mengatur gaya tabel kalender dan cara memilih tanggal.
     *
     * <ul>
     *   <li>Header nama hari memakai {@link DayHeaderRenderer}.</li>
     *   <li>Kotak tanggal memakai {@link DayCellRenderer}.</li>
     *   <li>Pengguna hanya bisa memilih satu kotak dalam satu waktu.</li>
     *   <li>Setiap kali tanggal diklik, label musim diperbarui.</li>
     * </ul>
     */
    private void setupCalendarTable() {
        tableCalendar.getTableHeader().setDefaultRenderer(new DayHeaderRenderer());
        tableCalendar.setDefaultRenderer(Object.class, new DayCellRenderer());

        tableCalendar.setShowGrid(true);
        tableCalendar.setGridColor(AppTheme.GRID_COLOR);

        // Mode pilih per kotak (bukan per baris), dan hanya satu kotak sekaligus.
        tableCalendar.setCellSelectionEnabled(true);
        tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // MouseAdapter = "pendengar" (listener) aksi mouse. Kita hanya butuh aksi klik,
        // jadi cukup menimpa (override) method mouseClicked saja.
        tableCalendar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                refreshSeasonLabel(); // Musim dihitung ulang dari tanggal yang baru diklik
            }
        });
    }

    /** Memilih bulan & tahun hari ini di combo box, supaya kalender langsung menampilkan bulan sekarang. */
    private void selectCurrentMonth() {
        LocalDate today = LocalDate.now();
        // getMonthValue() menghasilkan 1–12, sedangkan indeks combo box dimulai dari 0, jadi dikurangi 1.
        comboMonth.setSelectedIndex(today.getMonthValue() - 1);
        comboYear.setSelectedItem(String.valueOf(today.getYear()));
    }

    // =====================================================================
    // Kode otomatis dari GUI Designer NetBeans (JANGAN diedit manual)
    // =====================================================================

    /**
     * Membuat dan menyusun semua komponen jendela.
     * PERINGATAN: method ini selalu ditulis ulang oleh Form Editor NetBeans.
     * Semua perubahan manual di dalamnya akan hilang.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTop = new javax.swing.JPanel();
        comboMonth = new javax.swing.JComboBox<>();
        comboYear = new javax.swing.JComboBox<>();
        labelSeason = new javax.swing.JLabel();
        scrollPaneCalendar = new javax.swing.JScrollPane();
        tableCalendar = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTop.setBackground(new java.awt.Color(170, 205, 205));
        panelTop.setPreferredSize(new java.awt.Dimension(600, 90));

        comboMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
        comboMonth.addActionListener(this::comboMonthActionPerformed);

        comboYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboYear.addActionListener(this::comboYearActionPerformed);

        labelSeason.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        labelSeason.setForeground(new java.awt.Color(0, 0, 0));
        labelSeason.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSeason.setText("Season : Spring");

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelSeason)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(comboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(199, Short.MAX_VALUE))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSeason, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(panelTop, java.awt.BorderLayout.NORTH);

        tableCalendar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCalendar.setRowHeight(60);
        scrollPaneCalendar.setViewportView(tableCalendar);

        getContentPane().add(scrollPaneCalendar, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =====================================================================
    // Event handler (dihubungkan ke combo box lewat GUI Designer)
    // =====================================================================

    /**
     * Dipanggil otomatis setiap kali pengguna memilih bulan lain di combo box bulan.
     *
     * @param evt informasi tentang kejadian (tidak dipakai di sini)
     */
    private void comboMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMonthActionPerformed
        updateCalendar();
    }//GEN-LAST:event_comboMonthActionPerformed

    /**
     * Dipanggil otomatis setiap kali pengguna memilih tahun lain di combo box tahun.
     *
     * @param evt informasi tentang kejadian (tidak dipakai di sini)
     */
    private void comboYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboYearActionPerformed
        updateCalendar();
    }//GEN-LAST:event_comboYearActionPerformed

    // =====================================================================
    // Pembaruan tampilan
    // =====================================================================

    /**
     * Menggambar ulang isi kalender untuk bulan & tahun yang sedang dipilih.
     *
     * <p>Alurnya: baca pilihan combo box → minta {@link CalendarService} menyusun
     * tanggal ke kotak 6×7 → salin hasilnya ke tabel → perbarui label musim.</p>
     */
    private void updateCalendar() {
        YearMonth yearMonth = getSelectedYearMonth();

        // Saat aplikasi baru dibuka, combo box tahun sempat kosong sebentar
        // (sedang diisi), sehingga belum ada bulan yang bisa ditampilkan.
        if (yearMonth == null) {
            return;
        }

        Integer[][] grid = calendarService.buildMonthGrid(yearMonth);

        // TableModel adalah "wadah data" milik JTable. Mengubah model = mengubah isi tabel di layar.
        DefaultTableModel model = (DefaultTableModel) tableCalendar.getModel();
        for (int row = 0; row < CalendarService.ROWS; row++) {
            for (int col = 0; col < CalendarService.COLUMNS; col++) {
                model.setValueAt(grid[row][col], row, col); // null → kotak kosong
            }
        }

        refreshSeasonLabel();
    }

    /**
     * Memperbarui teks dan warna label musim berdasarkan tanggal yang dipilih.
     * Jika belum ada tanggal yang diklik, dipakai tanggal bawaan
     * (lihat {@link CalendarService#defaultDay}).
     */
    private void refreshSeasonLabel() {
        YearMonth yearMonth = getSelectedYearMonth();
        if (yearMonth == null) {
            return;
        }

        LocalDate date = yearMonth.atDay(getSelectedDayOrDefault(yearMonth));
        Season season = seasonCalculator.seasonOf(date);
        SeasonTheme theme = SeasonTheme.of(season);

        labelSeason.setText(SEASON_LABEL_PREFIX + season.getDisplayName());
        panelTop.setBackground(theme.background());
        labelSeason.setForeground(theme.foreground());
    }

    // =====================================================================
    // Method bantu (helper) untuk membaca pilihan pengguna
    // =====================================================================

    /**
     * Membaca bulan & tahun dari kedua combo box dan menggabungkannya menjadi {@link YearMonth}.
     *
     * @return bulan & tahun yang dipilih, atau {@code null} jika salah satunya belum dipilih
     */
    private YearMonth getSelectedYearMonth() {
        int monthIndex = comboMonth.getSelectedIndex();          // 0 = Januari ... 11 = Desember
        String yearText = (String) comboYear.getSelectedItem();  // Contoh: "2026" (masih berupa teks)

        if (monthIndex < 0 || yearText == null) {
            return null;
        }
        // Integer.parseInt mengubah teks "2026" menjadi angka 2026.
        // Indeks bulan ditambah 1 karena YearMonth memakai 1–12.
        return YearMonth.of(Integer.parseInt(yearText), monthIndex + 1);
    }

    /**
     * Mengambil tanggal yang sedang dipilih di tabel.
     *
     * @param yearMonth bulan yang sedang ditampilkan (dipakai jika tidak ada tanggal yang dipilih)
     * @return tanggal yang diklik; jika belum ada atau yang diklik kotak kosong,
     *         dipakai tanggal hari ini (bila bulan ini) atau tanggal 1
     */
    private int getSelectedDayOrDefault(YearMonth yearMonth) {
        int row = tableCalendar.getSelectedRow();    // -1 jika tidak ada yang dipilih
        int col = tableCalendar.getSelectedColumn();

        if (row >= 0 && col >= 0) {
            Object value = tableCalendar.getValueAt(row, col);
            // "instanceof Integer day" = cek apakah isinya angka, sekaligus simpan ke variabel "day".
            if (value instanceof Integer day) {
                return day;
            }
        }
        return calendarService.defaultDay(yearMonth, LocalDate.now());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboMonth;
    private javax.swing.JComboBox<String> comboYear;
    private javax.swing.JLabel labelSeason;
    private javax.swing.JPanel panelTop;
    private javax.swing.JScrollPane scrollPaneCalendar;
    private javax.swing.JTable tableCalendar;
    // End of variables declaration//GEN-END:variables
}
