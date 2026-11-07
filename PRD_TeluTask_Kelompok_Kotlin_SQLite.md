# PRD TeluTask v1.0

**Product Requirements Document**  
**Aplikasi Manajemen Tugas Mahasiswa Berbasis Android**

Aplikasi task management offline untuk mencatat, mengelola, dan memantau tugas kuliah menggunakan Kotlin dan SQLite.

| **Informasi**      | **Detail**                          |
|--------------------|-------------------------------------|
| Nama aplikasi      | TeluTask                            |
| Dokumen            | PRD v1.0                            |
| Platform           | Android native                      |
| Bahasa & UI        | Kotlin + XML Layout                 |
| Database           | SQLite via SQLiteOpenHelper         |
| Pengguna utama     | Mahasiswa (penggunaan personal)     |
| Tim                | Faiz, Shafa, Rico, Adan, Rey, Marta |
| Tanggal penyusunan | 7 November 2026                     |

**Status:** Siap dijadikan acuan implementasi dan prompt AI per anggota.

# 1. Ringkasan Produk

TeluTask adalah aplikasi pengelolaan tugas mahasiswa yang berjalan secara offline pada perangkat Android. Pengguna dapat mencatat tugas, mengatur tenggat, menentukan tingkat prioritas, memperbarui status pengerjaan, dan melihat ringkasan progres tanpa akun atau server.

## 1.1 Masalah yang Diselesaikan

- Tugas dari berbagai mata kuliah sering tersebar pada catatan dan percakapan.
- Mahasiswa membutuhkan daftar tugas dengan deadline, prioritas, dan status yang mudah dipantau.
- Untuk tugas praktik Android, tim memerlukan implementasi CRUD SQLite dengan pembagian pekerjaan yang terukur.

## 1.2 Tujuan Produk

- Menyediakan pengelolaan tugas harian melalui empat operasi CRUD.
- Memberikan visibilitas tugas yang belum selesai, sudah selesai, dan prioritasnya.
- Menjaga data tetap tersedia setelah aplikasi ditutup dan dibuka kembali.

## 1.3 Target Pengguna dan Platform

Target utama: mahasiswa Telkom University yang ingin mengelola tugas secara personal. Platform: aplikasi Android native, Kotlin, XML Layout, RecyclerView, dan SQLiteOpenHelper. Rekomendasi minimum Android 7.0 (API 24). Tidak ada akun atau sinkronisasi.

# 2. Batasan Ruang Lingkup

| **Termasuk dalam versi 1.0**            | **Tidak termasuk**                                |
|-----------------------------------------|---------------------------------------------------|
| Tambah, lihat, edit, hapus task         | Login, registrasi, manajemen pengguna             |
| Judul, deskripsi, mata kuliah, deadline | Cloud sync, backend, REST API                     |
| Prioritas Low / Medium / High           | Kolaborasi real-time / pembagian tugas antar akun |
| Status Pending / Completed              | Pengingat notifikasi / alarm deadline             |
| Filter status dan prioritas             | Upload lampiran / foto / file                     |
| Ringkasan total, pending, completed     | Pencarian teks, kalender bulanan                  |

# 3. User Flow Utama

Buka aplikasi → Dashboard menampilkan semua task → Pengguna memilih tambah task / detail task / filter → Pengguna menyimpan perubahan atau membatalkan → Dashboard memuat ulang data dan statistik terbaru.

Alur tambah: Dashboard → Form tambah → Validasi → Simpan ke SQLite → Kembali ke Dashboard. Alur edit: Dashboard → Detail → Mode edit → Simpan. Alur hapus: Detail → Dialog konfirmasi → Hapus → Dashboard. Alur selesai: Daftar/Detail → Ubah status → Simpan → Perbarui daftar dan statistik.

# 4. Kebutuhan Fungsional

| **ID** | **Fitur & persyaratan**                                                                                                                                                           | **Penanggung jawab** |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|
| FR-01  | Dashboard menampilkan daftar task dari SQLite. Urutan awal: deadline terdekat terlebih dahulu. Tampilkan empty state jika belum ada task.                                         | Shafa 048            |
| FR-02  | Tambah task. Wajib: judul 1–80 karakter, deadline. Opsional: deskripsi maksimal 500 karakter, mata kuliah maksimal 60 karakter. Prioritas default Medium. Status default Pending. | Rico 075             |
| FR-03  | Pengguna bisa membuka detail task berdasarkan ID dan melihat seluruh field.                                                                                                       | Adan 077             |
| FR-04  | Pengguna bisa mengubah judul, deskripsi, mata kuliah, deadline, dan prioritas lalu menyimpan hasilnya.                                                                            | Adan 077             |
| FR-05  | Sebelum hapus, tampilkan konfirmasi. Batal tidak menghapus data. Hapus sukses kembali ke daftar.                                                                                  | Marta 069            |
| FR-06  | Status task dapat berpindah dari Pending ke Completed dan sebaliknya. Perubahan permanen dalam SQLite.                                                                            | Rey 068              |
| FR-07  | Filter status: All, Pending, Completed. Filter bekerja terhadap daftar task yang sedang ditampilkan.                                                                              | Rey 068              |
| FR-08  | Filter prioritas: All, Low, Medium, High. Dapat digabung dengan filter status.                                                                                                    | Rey 068              |
| FR-09  | Statistik menampilkan total task, Pending, dan Completed; angka otomatis diperbarui setelah CRUD/status berubah.                                                                  | Marta 069            |
| FR-10  | Database menyediakan CRUD konsisten dan data tetap tersimpan setelah relaunch.                                                                                                    | Faiz 072             |
| FR-11  | Tampilkan validasi dan pesan error yang jelas pada input salah, task tidak ditemukan, dan kegagalan penyimpanan.                                                                  | Semua pemilik fitur  |

# 5. Aturan Bisnis dan Validasi

- Judul tidak boleh kosong atau hanya spasi. Panjang maksimum 80 karakter.
- Deadline wajib dipilih dan disimpan dalam format YYYY-MM-DD untuk memudahkan pengurutan.
- Deskripsi dan nama mata kuliah opsional. Jika tidak diisi, simpan null atau string kosong secara konsisten.
- Nilai prioritas hanya Low, Medium, High. Nilai status hanya Pending dan Completed.
- Task baru berstatus Pending. Timestamp created_at diisi otomatis oleh database.
- Penghapusan harus melalui dialog konfirmasi. Pembatalan tidak mengubah database.
- Setelah tambah, edit, hapus, atau ubah status, daftar dan statistik harus diperbarui.

# 6. Struktur Halaman / UI

| **Layar**     | **Komponen minimum**                                                                                                         | **Pemilik**                                                      |
|---------------|------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| Dashboard     | Nama aplikasi; kartu Total, Pending, Completed; filter status & prioritas; RecyclerView task; Floating Action Button tambah. | Shafa 048 (layout); Rey 068 (filter); Marta 069 (data statistik) |
| Tambah Task   | Input judul, deskripsi, mata kuliah; DatePicker deadline; pilihan prioritas; tombol Simpan dan Batal.                        | Rico 075                                                         |
| Detail & Edit | Detail semua field; tombol Edit; mode edit berisi form; aksi Selesai/Belum selesai dan Hapus.                                | Adan 077 (layar); Rey 068 (status); Marta 069 (dialog hapus)     |

Arah visual: desain minimalis dengan warna merah, putih, dan navy; warna prioritas Low = netral/hijau, Medium = oranye, High = merah. Gunakan kontras teks yang jelas, komponen Material, dan jarak antar elemen yang konsisten.

# 7. Rancangan Data SQLite

Satu tabel utama cukup untuk versi 1.0. Kolom database tidak memakai suffix NIM karena merupakan kontrak bersama, bukan variabel Kotlin.

| **Kolom**   | **Tipe & batasan**                      | **Kegunaan**           |
|-------------|-----------------------------------------|------------------------|
| id          | INTEGER PRIMARY KEY AUTOINCREMENT       | Identitas unik task    |
| title       | TEXT NOT NULL                           | Judul task             |
| description | TEXT NULL                               | Deskripsi opsional     |
| course      | TEXT NULL                               | Mata kuliah opsional   |
| deadline    | TEXT NOT NULL                           | Tanggal ISO YYYY-MM-DD |
| priority    | TEXT NOT NULL DEFAULT 'Medium'          | Low / Medium / High    |
| status      | TEXT NOT NULL DEFAULT 'Pending'         | Pending / Completed    |
| created_at  | TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP | Waktu dibuat           |

```sql
CREATE TABLE tasks (
id INTEGER PRIMARY KEY AUTOINCREMENT,
title TEXT NOT NULL,
description TEXT,
course TEXT,
deadline TEXT NOT NULL,
priority TEXT NOT NULL DEFAULT 'Medium' CHECK(priority IN ('Low','Medium','High')),
status TEXT NOT NULL DEFAULT 'Pending' CHECK(status IN ('Pending','Completed')),
created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## 7.1 Kontrak Data Bersama

Model data dibentuk oleh Faiz dengan nama Task072. Semua anggota memakai model dan helper yang sama, sehingga tidak membuat schema atau kelas database duplikat.

```kotlin
data class Task072(
val id072: Long = 0L,
val title072: String,
val description072: String? = null,
val course072: String? = null,
val deadline072: String,
val priority072: String = "Medium",
val status072: String = "Pending",
val createdAt072: String = ""
)
```

## 7.2 API Helper Database

- `insertTask072(task072: Task072): Long` - menambah satu task; hasil ID row baru.
- `getAllTasks072(): List<Task072>` - membaca seluruh task urut deadline ASC.
- `getTaskById072(taskId072: Long): Task072?` - membaca satu task.
- `updateTask072(task072: Task072): Int` - memperbarui data; hasil jumlah baris.
- `updateStatus072(taskId072: Long, status072: String): Int` - mengubah status.
- `deleteTask072(taskId072: Long): Int` - menghapus task; hasil jumlah baris.

Kontrak navigasi: task ID dikirim melalui Intent extra dengan key tetap EXTRA_TASK_ID072. Setiap fitur memanggil helper yang tersedia tanpa menulis SQL ulang di Activity.

# 8. Pembagian Pekerjaan dan File Ownership

| **Anggota** | **Modul + pekerjaan utama**                                                                                     | **File milik anggota**                                                  |
|-------------|-----------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| Faiz (072)  | Menyusun model, schema, SQLiteOpenHelper, CRUD, query sorted, contract ID. Menyediakan mock/sample test.        | Task072.kt; TaskDatabaseHelper072.kt; TaskContract072.kt                |
| Shafa (048) | Dashboard, adapter RecyclerView, item card, tombol tambah, navigasi ke detail, refresh onResume.                | MainActivity048.kt; TaskAdapter048.kt; activity_main.xml; item_task.xml |
| Rico (075)  | Form tambah, DatePicker, pilihan prioritas, validasi, insert via helper, kembali ke daftar.                     | AddTaskActivity075.kt; activity_add_task.xml                            |
| Adan (077)  | Detail task, mode edit dan update, penanganan ID invalid, refresh data saat kembali.                            | TaskDetailActivity077.kt; activity_task_detail.xml                      |
| Rey (068)   | Helper filter status+prioritas, toggle completion, callbacks terhubung ke adapter/detail bersama pemilik layar. | TaskFilter068.kt; TaskStatusManager068.kt                               |
| Marta (069) | Komponen dialog hapus, penghitung statistik, warna/theme, matriks QA, dokumentasi demo.                         | DeleteConfirm069.kt; TaskStats069.kt; colors.xml; themes.xml; README.md |

## 8.1 Definition of Done per Anggota

- Kode modul ter-commit ke branch pribadi dan dapat di-build tanpa error.
- Semua variabel milik pembuat modul memakai suffix 3 digit NIM.
- Setidaknya satu alur sukses, satu kasus invalid, dan satu kasus relaunch diuji jika relevan.
- Perubahan pada file anggota lain melalui koordinasi / pull request, bukan pengeditan langsung tanpa izin.
- Ada demo singkat fitur dan catatan dependensi/callback yang perlu dihubungkan.

# 9. Aturan Penamaan Kode Wajib

Setiap deklarasi variabel Kotlin custom harus memakai tiga digit NIM pemilik variabel sebagai suffix. Termasuk val/var lokal, properti class, parameter fungsi, parameter lambda, dan const val. Gunakan lowerCamelCase + suffix, misalnya selectedStatus068.

| **Nama** | **Suffix** | **Contoh deklarasi yang benar**                            |
|----------|------------|------------------------------------------------------------|
| Faiz     | 072        | val database072 = TaskDatabaseHelper072(this)              |
| Shafa    | 048        | val taskAdapter048 = TaskAdapter048(...)                   |
| Rico     | 075        | val title075 = inputTitle075.text.toString()               |
| Adan     | 077        | val taskId077 = intent.getLongExtra(...)                   |
| Rey      | 068        | val selectedStatus068 = "Pending"                          |
| Marta    | 069        | val completedCount069 = tasks069.count { task069 -> ... } |

- View ID XML disarankan memakai suffix pemilik UI, misalnya rvTasks048, etTitle075, btnSave077.
- Nama class dan helper buatan sendiri sebaiknya juga memakai suffix pemilik, misalnya TaskStats069.
- Nama metode framework yang di-override wajib mengikuti API Android, misalnya onCreate dan onResume; metode tersebut dikecualikan dari aturan suffix.
- Nama table dan column SQL, nama atribut AndroidManifest, resource Android standar, dan package identifier tidak dianggap variabel Kotlin dan tidak wajib disuffix.
- Jangan memakai implicit lambda it; gunakan nama eksplisit dengan suffix, misalnya tasks069.count { task069 -> task069.status072 == "Completed" }.

# 10. Integrasi antar Modul

- Faiz menyerahkan Task072 dan kontrak DatabaseHelper lebih dulu kepada seluruh tim.
- Shafa mengatur MainActivity dan adapter; Rey hanya menyerahkan filter/status helper dan callback contract agar file Dashboard tidak bentrok.
- Rico menggunakan insertTask072 dan tidak membuat database helper sendiri.
- Adan menggunakan getTaskById072 dan updateTask072, memanggil komponen status milik Rey serta dialog hapus milik Marta.
- Marta menghitung statistik dari List<Task072>; Shafa menampilkan hasilnya pada kartu dashboard.
- Seluruh Activity didaftarkan dalam AndroidManifest dan navigasi menggunakan Intent; jangan menimpa manifest secara paralel.

## 10.1 Branch Git yang Direkomendasikan

feature/faiz-072-database; feature/shafa-048-dashboard; feature/rico-075-create; feature/adan-077-detail-edit; feature/rey-068-status-filter; feature/marta-069-stats-delete-qa. Satu branch utama main. Gabungkan perubahan melalui pull request setelah build berhasil.

# 11. Rencana Implementasi 7 Hari

| **Hari** | **Fokus pekerjaan**                                                                          | **Output**                                          |
|----------|----------------------------------------------------------------------------------------------|-----------------------------------------------------|
| 1        | Kickoff: setup project Android, layout kasar, kontrak Task072, pembagian ownership.          | Project bisa dibuka dan dibuild; kontrak disetujui. |
| 2        | Faiz selesaikan database/model; yang lain buat UI dan helper tanpa menunggu integrasi penuh. | Schema dan CRUD dasar berfungsi.                    |
| 3        | Rico form tambah; Adan detail-edit; Shafa dashboard; Rey status/filter; Marta stats/delete.  | Fitur berjalan secara mandiri.                      |
| 4        | Sambungkan semua modul dan callback; pastikan navigasi berjalan.                             | End-to-end CRUD dan filter.                         |
| 5        | Uji validasi, edge cases, lifecycle dan persistence; perbaiki konflik merge.                 | Bug utama ditutup.                                  |
| 6        | Perapihan visual, empty state, statistik dan dokumentasi.                                    | Release candidate / APK debug.                      |
| 7        | Regression test, demo kelompok, finalisasi PRD/README dan pembagian presentasi.              | APK final + source + dokumentasi.                   |

# 12. Persyaratan Nonfungsional

- Offline-first: seluruh fitur inti dapat digunakan tanpa internet; tidak meminta izin jaringan untuk fitur dasar.
- Persistensi: data tidak hilang setelah keluar dari aplikasi, kembali, atau restart perangkat.
- Keandalan: tidak crash saat daftar kosong, input invalid, tanggal belum dipilih, atau task ID tidak ditemukan.
- Usability: tombol utama jelas, form bisa dipakai pada layar ponsel standar, dan status berhasil/gagal terlihat.
- Performa: list ratusan task tetap dapat digulir dengan wajar melalui RecyclerView.
- Privasi: data tetap tersimpan lokal di ruang aplikasi Android, tanpa akun dan pengiriman data ke server.

# 13. Skenario Pengujian dan Acceptance Criteria

| **ID** | **Langkah uji**                               | **Hasil yang diharapkan**                            |
|--------|-----------------------------------------------|------------------------------------------------------|
| AT-01  | Tambah task valid dengan deadline + priority. | Task muncul dalam daftar dan SQLite.                 |
| AT-02  | Tambah task tanpa judul atau deadline.        | Ada error; data tidak tersimpan.                     |
| AT-03  | Buka detail dan edit judul/deadline.          | Nilai baru tampil pada detail dan daftar.            |
| AT-04  | Ubah Pending → Completed → Pending.           | Status dan statistik mengikuti perubahan.            |
| AT-05  | Filter Completed + High.                      | Hanya task yang memenuhi keduanya terlihat.          |
| AT-06  | Klik hapus lalu pilih Batal.                  | Task tetap ada.                                      |
| AT-07  | Klik hapus lalu konfirmasi.                   | Task hilang dari daftar dan DB.                      |
| AT-08  | Tutup paksa dan buka lagi aplikasi.           | Semua perubahan sebelumnya tetap ada.                |
| AT-09  | Hapus task terakhir.                          | Dashboard menunjukkan empty state dan statistik nol. |
| AT-10  | Akses detail ID yang tidak ditemukan.         | Tampilkan pesan wajar, tidak crash.                  |
| AT-11  | Periksa deklarasi variabel semua file.        | Tidak ada variabel custom tanpa suffix pemilik.      |

# 14. Kriteria Selesai / Serah Terima

- Seluruh FR-01 s.d. FR-11 telah diimplementasikan dan diuji.
- APK debug dapat dipasang di perangkat/emulator, seluruh operasi berjalan tanpa internet.
- Database tersimpan lokal dan lolos AT-01 s.d. AT-11.
- Seluruh anggota memiliki kontribusi kode pada modul sesuai ownership.
- Source code rapi, README memuat cara menjalankan aplikasi, versi tool yang dipakai, serta pembagian tugas.
- Tidak ada nama variabel custom yang melanggar aturan suffix NIM.


# 15. Petunjuk Memakai PRD untuk Prompt AI

## 15.1 Instruksi Global untuk Semua Anggota

Salin bagian instruksi ini ke awal prompt setiap anggota:

> Kamu adalah Android developer yang membantu mengembangkan **TeluTask v1.0** berdasarkan PRD ini. Gunakan Kotlin, Android Studio, XML Layout, RecyclerView, dan SQLiteOpenHelper. Aplikasi harus offline, tanpa login, server, Firebase, Room, atau Jetpack Compose. Jangan menambahkan fitur di luar PRD. Pertahankan tabel `tasks`, model `Task072`, nama method database Faiz, dan key navigasi `TaskContract072.EXTRA_TASK_ID072`. Setiap **variabel Kotlin yang kamu deklarasikan** (lokal, properti, parameter, dan lambda) wajib diakhiri tiga digit NIM yang ditentukan untuk modulmu. Kecualikan signature method Android yang harus di-override, nama field SQL, serta nama API/library dari luar project. Jangan mengganti nama file yang sudah menjadi milik anggota lain. Berikan source code lengkap beserta nama/path file, resource yang dibutuhkan, bagian integrasi, dan cara menguji fitur. Bila membutuhkan modul yang belum ada, buat asumsi interface sesuai PRD, bukan membuat database atau model alternatif.

**Langkah kerja kelompok:**

1. Semua orang menggunakan satu project Android Studio dan package name yang sama, disepakati di awal, misalnya `com.example.telutask`.
2. Faiz menyiapkan kelas data, helper, dan kontrak navigasi lebih dulu. Anggota lain bisa mengerjakan layout secara paralel.
3. Setiap anggota menyalin isi PRD yang relevan beserta instruksi global dan prompt modulnya saat meminta AI menulis kode.
4. Tiap anggota mengerjakan file miliknya sendiri. Pengubahan `AndroidManifest.xml`, `build.gradle.kts`, dan file Activity utama harus melalui integrator yang disepakati, yaitu Shafa, dibantu Marta untuk testing.
5. Sebelum merge, jalankan Build Project, uji fitur, dan periksa suffix variabel dengan peninjauan kode.

## 15.2 Template Prompt Spesifik per Anggota

### Prompt Faiz (072) | Database

> Kerjakan modul **Faiz 072** sesuai PRD. Buat `Task072.kt`, `TaskDatabaseHelper072.kt`, dan `TaskContract072.kt`. Terapkan struktur SQLite, constraint priority/status, seluruh API pada bagian 7.2, query deadline ASC, pengelolaan Cursor yang aman, dan penanganan kasus ID tidak ditemukan. Seluruh variabel serta parameter yang kamu deklarasikan memakai suffix `072`. Sertakan contoh cara Rico/Shafa/Adan/Rey/Marta memanggil API, tanpa mengimplementasikan Activity mereka. Jangan membuat database ganda.

### Prompt Shafa (048) | Dashboard dan RecyclerView

> Kerjakan modul **Shafa 048** sesuai PRD. Buat `MainActivity048.kt`, `TaskAdapter048.kt`, `activity_main.xml`, `item_task.xml`. Gunakan `Task072` dan API `TaskDatabaseHelper072` milik Faiz, tombol tambah menuju `AddTaskActivity075`, dan klik task menuju `TaskDetailActivity077` dengan Intent extra `TaskContract072.EXTRA_TASK_ID072`. Dashboard menampilkan RecyclerView, empty state, kartu statistik, dan tempat UI filter. Buat titik integrasi ke `TaskFilter068` dan `TaskStats069`. Seluruh variabel Kotlin milikmu diakhiri `048`. Jelaskan manifest dan callback yang perlu dihubungkan tanpa mengambil alih implementasi Rey/Marta.

### Prompt Rico (075) | Tambah Task

> Kerjakan modul **Rico 075** sesuai PRD. Buat `AddTaskActivity075.kt` dan `activity_add_task.xml`. Form terdiri dari judul, deskripsi, mata kuliah, DatePicker deadline, priority Low/Medium/High, tombol Simpan dan Batal. Terapkan validasi bagian 5 dan panggil `insertTask072` dengan model `Task072`. Nilai awal status Pending. Seluruh variabel, properti, dan parameter yang kamu deklarasikan harus diakhiri `075`. Setelah berhasil, tutup Activity agar dashboard menyegarkan data pada `onResume`.

### Prompt Adan (077) | Detail dan Edit Task

> Kerjakan modul **Adan 077** sesuai PRD. Buat `TaskDetailActivity077.kt` dan `activity_task_detail.xml`. Ambil ID dari `TaskContract072.EXTRA_TASK_ID072`, tampilkan semua informasi task menggunakan `getTaskById072`, sediakan mode edit yang memvalidasi input lalu memanggil `updateTask072`. Tambahkan titik integrasi untuk `TaskStatusManager068` dan `DeleteConfirm069`, tetapi jangan menduplikasi implementasi kedua modul itu. Tangani ID yang tidak ditemukan. Semua variabel Kotlin milikmu harus diakhiri `077`.

### Prompt Rey (068) | Status dan Filter

> Kerjakan modul **Rey 068** sesuai PRD. Buat `TaskFilter068.kt` dan `TaskStatusManager068.kt`. Filter harus dapat menggabungkan status All/Pending/Completed dan prioritas All/Low/Medium/High dari `List<Task072>` tanpa mengubah sumber data. Status manager melakukan toggle Pending/Completed dengan `updateStatus072` dari helper Faiz dan mengembalikan hasil untuk refresh UI. Tuliskan interface/callback yang dibutuhkan Shafa serta Adan. Jangan membuat Activity baru atau mengedit file anggota lain. Seluruh variabel Kotlin yang kamu deklarasikan memakai suffix `068`.

### Prompt Marta (069) | Statistik, Delete, dan QA

> Kerjakan modul **Marta 069** sesuai PRD. Buat `TaskStats069.kt`, `DeleteConfirm069.kt`, resource `colors.xml` dan `themes.xml`, serta `README.md`. Statistik menghitung total/Pending/Completed dari `List<Task072>`; penghapusan harus memakai dialog konfirmasi, memanggil `deleteTask072`, lalu meminta layar pemanggil melakukan refresh. Buat matriks pengujian AT-01 sampai AT-11 dan langkah menjalankan aplikasi. Hindari mengedit Activity milik anggota lain, cukup jelaskan cara memanggil helper dari layar. Seluruh variabel Kotlin milikmu harus diakhiri `069`.

## 15.3 Format Keluaran yang Harus Diminta dari AI

Setiap anggota meminta AI menjawab dengan urutan berikut:

1. **Daftar file:** path dan nama file yang dibuat/diubah.
2. **Kode lengkap:** bukan cuplikan saja, dan bisa disalin ke project.
3. **Dependency:** hanya dependency yang memang digunakan.
4. **Kontrak integrasi:** constructor, method publik, ID layout, dan callback yang harus dipakai anggota lain.
5. **Instruksi pemasangan:** file ditempatkan di folder mana dan perubahan manifest/Gradle apa yang perlu diserahkan ke integrator.
6. **Pengujian:** langkah uji mandiri dan expected result sesuai PRD.
7. **Checklist suffix NIM:** konfirmasi semua deklarasi variabel Kotlin buatan anggota menggunakan tiga digit yang benar.

## 15.4 Larangan Umum Saat Vibe Coding

- Jangan mengganti `Task072` dengan model data lain atau membuat database terpisah di tiap Activity.
- Jangan mengubah nama kolom atau tipe field SQL tanpa kesepakatan semua anggota.
- Jangan berpindah ke Firebase, Supabase, Room, Jetpack Compose, atau backend API.
- Jangan menggunakan storage sementara sebagai pengganti SQLite untuk data utama.
- Jangan langsung menimpa file anggota lain saat mengintegrasikan modul.
- Jangan membuat nama variable campuran suffix orang lain untuk deklarasi milik sendiri. Properti `task072.title072` tetap dipanggil dengan nama aslinya karena merupakan API model Faiz.
- Jangan menyatakan modul selesai sebelum dapat di-build dan fitur sudah diuji.
