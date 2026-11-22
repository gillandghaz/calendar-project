# Panduan Kontribusi

Dokumen ini menjelaskan cara bekerja di repository ini: dari menyalin repo,
aturan branch, sampai cara mengambil pembaruan dari repository asal (*upstream*).

---

## 1. Konfigurasi Git

Jika Git belum tersambung dengan GitHub, ikuti panduan resmi
[Set Up Git](https://docs.github.com/en/get-started/git-basics/set-up-git).

## 2. Menyalin Repository

```bash
cd D:\Users\Nama_User\Documents
git clone https://github.com/gillandghaz/calendar-project.git
cd calendar-project
```

Tambahkan repository asal sebagai `upstream` (cukup sekali):

```bash
git remote add upstream https://github.com/CloudyPr/Calendar-project.git
```

| Remote     | Repository                 | Fungsi |
|------------|----------------------------|--------|
| `origin`   | `gillandghaz/calendar-project` (fork) | Tempat **push** pekerjaan kita |
| `upstream` | `CloudyPr/Calendar-project` (asal)    | Hanya untuk **mengambil** pembaruan |

## 3. Aturan Branch

```
upstream/master ──sync──▶ main ──▶ branch fitur (feature/..., fix/..., refactor/...)
                                       │
                                       └── Pull Request ──▶ main
```

- **`main`** adalah branch stabil. **Jangan commit langsung ke `main`.**
- Setiap pekerjaan dibuat di branch baru dari `main`, dengan awalan sesuai jenisnya:

| Awalan      | Dipakai untuk | Contoh |
|-------------|---------------|--------|
| `feature/`  | Fitur baru | `feature/holiday-marker` |
| `fix/`      | Perbaikan bug | `fix/season-boundary` |
| `refactor/` | Merapikan kode tanpa mengubah fitur | `refactor/modular-structure` |
| `docs/`     | Dokumentasi | `docs/update-readme` |

```bash
git switch main
git pull origin main
git switch -c feature/nama-fitur
```

## 4. Mengedit Kode

1. Buka proyek di IDE (NetBeans, IntelliJ, atau VS Code). Lihat [README](README.md#menjalankan-aplikasi).
2. Letakkan kode di package yang sesuai:
   - Perhitungan/logika → `service/` (jangan memakai kelas Swing di sini)
   - Data/tipe → `model/`
   - Tampilan → `ui/`, warna/font → `ui/theme/`
3. Tulis komentar yang menjelaskan **apa** fungsi kode dan **mengapa** ditulis begitu.
4. **Jangan edit manual** bagian `Generated Code` di `CalendarFrame.java`.
   Ubah tampilan lewat tab **Design** di NetBeans.
5. Pastikan test lolos sebelum commit: `mvn test`.

## 5. Commit dan Push

Gunakan format [Conventional Commits](https://www.conventionalcommits.org/):

```
<jenis>: <ringkasan singkat>
```

| Jenis      | Arti |
|------------|------|
| `feat`     | Fitur baru |
| `fix`      | Perbaikan bug |
| `refactor` | Merapikan struktur kode |
| `docs`     | Dokumentasi |
| `style`    | Format/tampilan kode tanpa mengubah logika |
| `test`     | Menambah atau mengubah test |
| `chore`    | Konfigurasi, build, dan sejenisnya |

```bash
git add .
git commit -m "feat: tandai hari libur nasional dengan warna merah"
git push -u origin feature/nama-fitur
```

Setelah itu buka **Pull Request** di GitHub dari branch tersebut ke `main`
**di fork ini** (`gillandghaz/calendar-project`).

> ⚠️ Saat membuat Pull Request, pastikan *base repository* adalah
> `gillandghaz/calendar-project`, bukan `CloudyPr/Calendar-project`.

## 6. Mengambil Pembaruan dari Upstream

Jika ada perubahan baru di repository asal:

```bash
git switch main
git fetch upstream
git merge upstream/master        # Gabungkan pembaruan dari repository asal
git push origin main
```

Lalu perbarui branch kerja yang sedang aktif:

```bash
git switch feature/nama-fitur
git merge main
```

Bisa juga lewat tombol **Sync fork** di halaman GitHub fork.

### Jika Terjadi Konflik

Karena struktur kode fork ini sudah dipisah ke beberapa file, perubahan upstream
pada `CalendarFrame.java` lama kemungkinan besar akan konflik. Cara menanganinya:

1. Baca perubahan dari upstream (`git diff main upstream/master`).
2. Pindahkan logikanya ke kelas yang sesuai (misalnya aturan musim → `SeasonCalculator`,
   warna → `SeasonTheme`).
3. Selesaikan konflik, jalankan `mvn test`, lalu commit:
   `git commit -m "chore: merge upstream changes"`.
