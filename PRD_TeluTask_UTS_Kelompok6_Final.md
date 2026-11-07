# PRODUCT REQUIREMENTS DOCUMENT (PRD)

## TeluTask: Aplikasi Task Management Mahasiswa

**Versi:** 2.0 (revisi berdasarkan ketentuan UTS Pengembangan Aplikasi Bergerak, November 2026)  
**Mata kuliah:** Pengembangan Aplikasi Bergerak  
**Kelompok:** 6, Task Management  
**Platform:** Android  
**Bahasa:** Kotlin  
**UI:** Android XML Layout + RecyclerView  
**Database:** Room Persistence Library (di atas SQLite), **bukan** SQLiteOpenHelper  
**Penyimpanan sesi:** SharedPreferences  
**Koneksi:** Offline; tidak memerlukan internet, API PHP, atau backend  
**Deadline pengumpulan:** 9 November 2026  
**Status:** Acuan final untuk implementasi dan prompting AI per anggota.

---

## 1. Tujuan Produk

TeluTask adalah aplikasi Android untuk mengelola tugas pribadi. Pengguna membuat akun, masuk ke aplikasi, menambahkan tugas, melihat daftar, mengedit, menghapus, menandai tugas selesai, dan mengurutkan daftar berdasarkan prioritas. Setiap pengguna hanya dapat mengakses tugas yang memiliki `user_id` sesuai sesi akunnya.

**Prioritas utama:** lulus seluruh requirement UTS terlebih dahulu. Hindari fitur yang belum diminta seperti sinkronisasi cloud, push notification, kolaborasi tugas, atau login Google.

### 1.1 Ruang lingkup

**Wajib:** Register, Login, Session, Logout; Room Entity + DAO + Database; CRUD Task; RecyclerView; form tambah/edit; isolasi data per user; validasi input.

**Bonus yang ditargetkan:** Checklist interaktif untuk `is_done`, pengurutan prioritas (High > Medium > Low), dan ViewBinding jika integrasi tidak mengganggu penyelesaian fitur wajib.

**Di luar cakupan:** API, jaringan, Firebase, upload file, chatbot, reminder/notifikasi, mata kuliah/kategori, deskripsi tugas, dan statistik kompleks. Entity Task **hanya** mencakup field dari soal UTS.

---

## 2. Requirement UTS dan Kriteria Kelulusan

| ID | Requirement | Kriteria penerimaan |
|---|---|---|
| AUTH-01 | Register | Username unik, username tidak kosong, password minimal 6 karakter. Akun disimpan ke Room. |
| AUTH-02 | Login | Validasi username dan password dengan data User di Room. |
| AUTH-03 | Session | `user_id` hasil login tersimpan di SharedPreferences; login tidak perlu diulang selama sesi ada. |
| AUTH-04 | Logout | Hapus sesi dan kembali ke Login; tombol Back tidak membuka layar pengguna sebelumnya. |
| DATA-01 | Room | Ada `User068`, `Task068`, `UserDao068`, `TaskDao068`, `TeluTaskDatabase068`. |
| DATA-02 | Data privat | Semua pembacaan, perubahan, dan penghapusan task dibatasi oleh `user_id` pengguna yang login. |
| TASK-01 | Create | User dapat menambah tugas dengan nama, prioritas, deadline; `is_done` default `false`. |
| TASK-02 | Read | RecyclerView menampilkan tugas milik user yang login saja. |
| TASK-03 | Update | User dapat mengedit nama, prioritas, deadline tugas miliknya. |
| TASK-04 | Delete | User dapat menghapus tugas miliknya setelah konfirmasi. |
| TASK-05 | Checklist | User dapat menandai tugas selesai/belum selesai; perubahan tersimpan. |
| TASK-06 | Sort | User dapat mengurutkan tugas berdasarkan prioritas. |
| UI-01 | Navigasi | Login → Dashboard → List Data (RecyclerView) → Form Tambah → Edit/Hapus. Dashboard dan List Data boleh satu layar jika daftar RecyclerView jelas tersedia. |
| VAL-01 | Validasi task | Nama tugas wajib diisi, prioritas harus valid, deadline wajib dipilih. |
| NIM-01 | Penamaan | Semua variabel dan nama tabel hasil modifikasi menggunakan suffix 3 digit NIM sesuai pemiliknya. |

### 2.1 Rubrik penilaian

| Komponen | Bobot |
|---|---:|
| Autentikasi & Session | 30% |
| Room Database (CRUD) | 30% |
| UI & RecyclerView | 15% |
| Data Integrity & Validation | 15% |
| Bonus (maksimum) | 10% |

**Aturan prioritas kerja:** AUTH + Room + isolasi `user_id` + validasi didahulukan sebelum mempercantik UI atau mengerjakan bonus.

---

## 3. Anggota, Kepemilikan Modul, dan Pembagian Tugas

| Anggota | 3 Digit NIM | Kepemilikan modul | Deliverables utama |
|---|---|---|---|
| **Rey** | **068** | Database Room & kontrak data | `User068.kt`, `Task068.kt`, `UserDao068.kt`, `TaskDao068.kt`, `TeluTaskDatabase068.kt` |
| **Shafa** | **048** | Autentikasi & sesi | `RegisterActivity048.kt`, `LoginActivity048.kt`, `SessionManager048.kt`, `LauncherActivity048.kt`, XML terkait |
| **Rico** | **075** | Create task | `AddTaskActivity075.kt`, `activity_add_task_075.xml`, validasi form tambah |
| **Adan** | **077** | Detail & edit task | `TaskDetailActivity077.kt`, `EditTaskActivity077.kt`, XML terkait |
| **Faiz** | **072** | Dashboard, RecyclerView, checklist, sortir | `DashboardActivity072.kt`, `TaskAdapter072.kt`, XML dashboard dan item |
| **Marta** | **069** | Konfirmasi hapus, desain konsisten, integrasi & QA | `DeleteTaskDialog069.kt`, resource tema, daftar pengujian, validasi build dan integrasi |

### 3.1 Detail tugas Rey (068)

1. Menyiapkan dependency Room beserta KSP atau kapt sesuai konfigurasi project (pilih **satu**, jangan keduanya).
2. Membuat Entity `User068` dan `Task068` tepat sesuai skema Bab 5.
3. Membuat `UserDao068`, `TaskDao068` dengan fungsi yang disepakati pada Bab 6.
4. Membuat singleton `TeluTaskDatabase068` dan menyediakan contoh cara memanggil DAO.
5. Menambahkan indeks/foreign key untuk isolasi dan integritas data.
6. Menguji register lookup, tambah, baca, edit, hapus, dan checklist melalui DAO.
7. Tidak mengubah fungsi/signature DAO tanpa memberi tahu semua anggota.

### 3.2 Detail tugas Shafa (048)

1. Membuat form Register (username, password, konfirmasi password).
2. Membuat form Login (username, password) dan pesan kegagalan.
3. Validasi username unik (disarankan tidak peka huruf besar/kecil) dan password minimal 6 karakter.
4. Memakai `UserDao068` untuk verifikasi pengguna yang tersimpan di Room.
5. Menyimpan `user_id` hasil autentikasi di SharedPreferences; membuat kelas `SessionManager048`.
6. Membuat `LauncherActivity048` sebagai LAUNCHER untuk memilih Login atau Dashboard berdasarkan sesi.
7. Mengimplementasikan Logout dengan membersihkan sesi dan navigasi yang aman.

### 3.3 Detail tugas Rico (075)

1. Membuat halaman Tambah Task berisi `nama_tugas`, `prioritas`, `deadline`.
2. Pilihan prioritas hanya `High`, `Medium`, `Low`.
3. Menggunakan DatePicker; menyimpan deadline sebagai `Long` waktu epoch dalam milidetik dengan konvensi yang sama di seluruh modul.
4. Menolak `nama_tugas` kosong dan deadline yang belum dipilih.
5. Memasukkan task melalui `TaskDao068.insertTask068(...)` dengan `user_id` **dari sesi**, bukan input pengguna.
6. Default task baru adalah `is_done=false`.
7. Setelah berhasil menyimpan, kembali ke dashboard dan tampilkan data yang terbaru.

### 3.4 Detail tugas Adan (077)

1. Membuat halaman detail dengan data `nama_tugas`, `prioritas`, `deadline`, `is_done`.
2. Saat membuka detail, ambil task berdasarkan **task ID dan user ID**, bukan task ID saja.
3. Membuat form edit dan memuat nilai lama ke form.
4. Memvalidasi form seperti Create Task.
5. Memanggil DAO update terikat `user_id` agar tidak dapat mengubah task milik user lain.
6. Menambahkan tombol hapus yang memanggil komponen konfirmasi dari Marta.
7. Jika task tidak ditemukan atau bukan milik user aktif, tampilkan pesan dan kembali ke dashboard.

### 3.5 Detail tugas Faiz (072)

1. Membuat Dashboard dengan RecyclerView berisi `nama_tugas`, prioritas, deadline, dan checklist.
2. Menyediakan tombol `+` untuk membuka Add Task dan tombol Logout melalui SessionManager milik Shafa.
3. Mengambil task dari DAO dengan filter `user_id` milik sesi aktif.
4. Menangani klik task untuk membuka Detail Task.
5. Menangani checkbox untuk mengubah `is_done` secara permanen di Room.
6. Menyediakan sorting prioritas `High → Medium → Low` dan pilihan urutan default terbaru/berdasar deadline (opsional).
7. Memuat ulang daftar setelah tambah, edit, hapus, maupun perubahan status.
8. Menyediakan empty state saat daftar task kosong.

### 3.6 Detail tugas Marta (069)

1. Membuat konfirmasi AlertDialog sebelum menghapus tugas di halaman detail.
2. Menghubungkan aksi hapus dengan DAO yang memeriksa `user_id` aktif dan `task_id`.
3. Menetapkan warna, spacing, teks, ikon, dan tema UI agar konsisten di seluruh halaman.
4. Membantu penggabungan branch dan menyelesaikan konflik resource/manifest/Gradle.
5. Menjalankan seluruh test case pada Bab 10, terutama kebocoran data antarakun.
6. Memastikan aplikasi dapat dibuild serta dapat didemokan tanpa internet.
7. Mengumpulkan screenshot layar dan dokumentasi akhir.

---

## 4. Aturan Penamaan 3 Digit NIM

**Aturan wajib dari soal UTS:** setiap variabel yang dibuat dan nama tabel hasil modifikasi wajib memuat 3 digit NIM.

| Pemilik | Contoh variabel | Contoh kelas/fungsi |
|---|---|---|
| Rey (068) | `taskId068`, `userId068`, `database068` | `Task068`, `getTaskById068()` |
| Shafa (048) | `username048`, `sessionManager048` | `LoginActivity048`, `saveUserId048()` |
| Rico (075) | `namaTugas075`, `prioritas075` | `AddTaskActivity075`, `validateForm075()` |
| Adan (077) | `taskId077`, `newDeadline077` | `EditTaskActivity077`, `saveChanges077()` |
| Faiz (072) | `taskList072`, `adapter072` | `DashboardActivity072`, `loadTasks072()` |
| Marta (069) | `dialog069`, `taskId069` | `DeleteTaskDialog069`, `confirmDelete069()` |

**Konvensi lintas modul:** suffix variabel mengikuti **orang yang mendeklarasikan**, bukan pemilik kelas yang dipakai. Contoh: Faiz menulis `val taskDao072 = database072.taskDao068()` (variabel lokal `taskDao072`, method milik Rey `taskDao068`). Nama metode bawaan Android yang wajib di-override (`onCreate`, `onResume`, `onBindViewHolder`, dan sebagainya) **jangan diganti** karena kontrak framework. Kata kunci Kotlin, nama properti library, anotasi Room, dan nama manifest/Gradle tidak boleh dipaksa memiliki suffix bila akan merusak kompatibilitas.

**Nama tabel menggunakan suffix pembuat database (Rey):** `users068` dan `tasks068`. Jangan buat versi `users048`, `tasks075`, dan lain-lain karena semua modul harus memakai **satu database yang sama**.

**Nama kolom** tetap mengikuti soal UTS dan harus dipetakan via `@ColumnInfo`: `id`, `username`, `password`, `user_id`, `nama_tugas`, `prioritas`, `deadline`, `is_done`. Suffix dipakai pada **nama variabel Kotlin**, bukan mengubah nama kolom yang menjadi kontrak bersama.

**XML:** setiap ID view custom sebaiknya juga bersuffix sesuai pemilik, contohnya `@+id/etNamaTugas075`, agar tidak bentrok.

---

## 5. Skema Database Room

### 5.1 Entity User

**Class:** `User068`  
**Tabel:** `users068`

| Kolom SQLite | Tipe | Constraint |
|---|---|---|
| `id` | INTEGER | Primary key, auto-generate |
| `username` | TEXT | Wajib, unik, tidak kosong |
| `password` | TEXT | Wajib; simpan hash password jika memungkinkan, bukan plaintext |

### 5.2 Entity Task

**Class:** `Task068`  
**Tabel:** `tasks068`

| Kolom SQLite | Tipe | Constraint |
|---|---|---|
| `id` | INTEGER | Primary key, auto-generate |
| `user_id` | INTEGER | Foreign key ke `users068.id`, wajib |
| `nama_tugas` | TEXT | Wajib, tidak boleh kosong |
| `prioritas` | TEXT | Salah satu `High`, `Medium`, `Low` |
| `deadline` | INTEGER | Tanggal deadline berupa epoch milliseconds (`Long`) |
| `is_done` | INTEGER / BOOLEAN Room | Default `false` |

**Relasi:** `User068 (1) → (N) Task068`, FK `tasks068.user_id → users068.id`. Gunakan indeks pada `user_id`. Untuk menghindari data yatim, relasi dapat memakai `ON DELETE CASCADE`.

### 5.3 Contoh kerangka Entity untuk acuan bersama

```kotlin
@Entity(tableName = "users068", indices = [Index(value = ["username"], unique = true)])
data class User068(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id068: Int = 0,
    @ColumnInfo(name = "username") val username068: String,
    @ColumnInfo(name = "password") val password068: String
)

@Entity(
    tableName = "tasks068",
    foreignKeys = [ForeignKey(
        entity = User068::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]
)
data class Task068(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id068: Int = 0,
    @ColumnInfo(name = "user_id") val userId068: Int,
    @ColumnInfo(name = "nama_tugas") val namaTugas068: String,
    @ColumnInfo(name = "prioritas") val prioritas068: String,
    @ColumnInfo(name = "deadline") val deadline068: Long,
    @ColumnInfo(name = "is_done") val isDone068: Boolean = false
)
```

Kerangka di atas memerlukan import Room yang sesuai. Pengembang database harus menghindari mengubah nama class, field, maupun table tanpa koordinasi.

### 5.4 Aturan keamanan data

- Session menyimpan `user_id`, tidak menyimpan password.
- Password **jangan** disimpan ke SharedPreferences. Jika memungkinkan, gunakan hash dengan salt (misalnya PBKDF2) di Room; jangan menyimpan plaintext pada produk nyata.
- Username harus unik. Pada versi UTS, validasi kasus username sama dengan perbedaan kapital, serta tangani exception saat insert jika terjadi duplikasi.
- Semua query data task **harus** memakai `user_id` dari sesi aktif.
- Saat sesi tidak ada, arahkan user ke Login sebelum membuat query task.
- UI tidak menyediakan input `user_id`; user tidak boleh memilih atau mengubah pemilik tugas.

---

## 6. Kontrak DAO Wajib (Disepakati Sebelum Coding Paralel)

> **Penting:** Semua anggota mengikuti nama fungsi DAO di bawah. Suffix `068` ada karena DAO milik Rey. Pemanggil boleh menggunakan nama variabel lokal sesuai suffix miliknya.

### 6.1 `UserDao068`

```kotlin
@Dao
interface UserDao068 {
    @Insert
    suspend fun insertUser068(user068: User068): Long

    @Query("SELECT * FROM users068 WHERE username = :username068 COLLATE NOCASE LIMIT 1")
    suspend fun getUserByUsername068(username068: String): User068?

    @Query("SELECT * FROM users068 WHERE id = :userId068 LIMIT 1")
    suspend fun getUserById068(userId068: Int): User068?
}
```

### 6.2 `TaskDao068`

```kotlin
@Dao
interface TaskDao068 {
    @Insert
    suspend fun insertTask068(task068: Task068): Long

    @Query("SELECT * FROM tasks068 WHERE user_id = :userId068 ORDER BY id DESC")
    suspend fun getTasksByUser068(userId068: Int): List<Task068>

    @Query("SELECT * FROM tasks068 WHERE id = :taskId068 AND user_id = :userId068 LIMIT 1")
    suspend fun getTaskById068(taskId068: Int, userId068: Int): Task068?

    @Query("""UPDATE tasks068 SET nama_tugas = :namaTugas068,
        prioritas = :prioritas068, deadline = :deadline068
        WHERE id = :taskId068 AND user_id = :userId068""")
    suspend fun updateTask068(
        taskId068: Int, userId068: Int, namaTugas068: String,
        prioritas068: String, deadline068: Long
    ): Int

    @Query("UPDATE tasks068 SET is_done = :isDone068 WHERE id = :taskId068 AND user_id = :userId068")
    suspend fun updateDone068(taskId068: Int, userId068: Int, isDone068: Boolean): Int

    @Query("DELETE FROM tasks068 WHERE id = :taskId068 AND user_id = :userId068")
    suspend fun deleteTask068(taskId068: Int, userId068: Int): Int

    @Query("""SELECT * FROM tasks068 WHERE user_id = :userId068
        ORDER BY CASE prioritas WHEN 'High' THEN 1
            WHEN 'Medium' THEN 2 WHEN 'Low' THEN 3 ELSE 4 END ASC, deadline ASC""")
    suspend fun getTasksSortedByPriority068(userId068: Int): List<Task068>
}
```

**Contract `TeluTaskDatabase068`:**

```kotlin
@Database(entities = [User068::class, Task068::class], version = 1, exportSchema = false)
abstract class TeluTaskDatabase068 : RoomDatabase() {
    abstract fun userDao068(): UserDao068
    abstract fun taskDao068(): TaskDao068
    // Sediakan companion object singleton getInstance068(context068: Context).
}
```

**Teknis:** gunakan `lifecycleScope.launch { ... }` untuk memanggil `suspend` DAO, bukan menjalankan operasi Room di main thread. Pakai dependensi lifecycle coroutine yang kompatibel dengan versi project. Fungsi `update` / `delete` mengembalikan jumlah baris terdampak; `0` berarti task tidak ditemukan atau bukan milik user aktif.

---

## 7. Navigasi dan Alur Pengguna

```text
[LauncherActivity048]
    |
    |-- Tidak ada sesi -> [LoginActivity048] <-> [RegisterActivity048]
    |                         |
    |                         | Login sukses + save user_id
    |                         v
    |                    [DashboardActivity072]
    |                         |-- RecyclerView daftar tugas user aktif
    |                         |-- Checkbox selesai/belum selesai
    |                         |-- Sortir prioritas
    |                         |-- Tombol (+) -> [AddTaskActivity075]
    |                         |-- Klik item -> [TaskDetailActivity077]
    |                         |                     |-- Edit -> [EditTaskActivity077]
    |                         |                     '-- Hapus -> [DeleteTaskDialog069]
    |                         '-- Logout -> hapus sesi -> Login
    |
    '-- Ada sesi -> [DashboardActivity072]
```

### 7.1 Layar

1. **Login:** username, password, tombol Login, navigasi Register.
2. **Register:** username, password, konfirmasi password, tombol Register, navigasi Login.
3. **Dashboard / List Data:** RecyclerView dengan task milik user saat ini; tombol tambah; Logout; toggle sortir; checkbox; empty state.
4. **Tambah Tugas:** nama tugas, prioritas, deadline, tombol Simpan / Batal.
5. **Detail Tugas:** informasi tugas, tombol Edit dan Hapus.
6. **Edit Tugas:** field yang dapat diperbarui dan tombol Simpan.

### 7.2 Detail komponen kartu task

- Nama tugas (utama).
- Prioritas (`High`, `Medium`, `Low`).
- Deadline tanggal dalam format Indonesia, misalnya `09 Nov 2026`.
- Checkbox `is_done`.
- Klik kartu membuka halaman detail.
- Saat dicentang, teks bisa diberi strikethrough (opsional) dan tetap terlihat.

### 7.3 Ketentuan sorting

- Saat pilih **Prioritas**, urutan `High > Medium > Low`.
- Jika prioritas sama, gunakan deadline yang lebih dekat terlebih dahulu.
- Jangan tercampur antaruser setelah sorting.

---

## 8. Struktur Folder dan Kepemilikan File

```text
app/src/main/java/com/example/telutask/
  data/
    entity/
      User068.kt                        # Rey
      Task068.kt                        # Rey
    dao/
      UserDao068.kt                     # Rey
      TaskDao068.kt                     # Rey
    database/
      TeluTaskDatabase068.kt            # Rey
  auth/
    LauncherActivity048.kt              # Shafa
    LoginActivity048.kt                 # Shafa
    RegisterActivity048.kt              # Shafa
    SessionManager048.kt                # Shafa
  task/
    AddTaskActivity075.kt               # Rico
    TaskDetailActivity077.kt            # Adan
    EditTaskActivity077.kt              # Adan
    DeleteTaskDialog069.kt              # Marta
  dashboard/
    DashboardActivity072.kt             # Faiz
    TaskAdapter072.kt                   # Faiz

app/src/main/res/layout/
  activity_launcher_048.xml             # Shafa (opsional)
  activity_login_048.xml                # Shafa
  activity_register_048.xml             # Shafa
  activity_dashboard_072.xml            # Faiz
  item_task_072.xml                     # Faiz
  activity_add_task_075.xml             # Rico
  activity_task_detail_077.xml          # Adan
  activity_edit_task_077.xml            # Adan

app/src/main/res/values/
  colors.xml                            # Marta: satu file bersama
  themes.xml                            # Marta: satu file bersama
  strings.xml                           # Marta: konsolidasi string

app/src/main/AndroidManifest.xml         # Integrator: Marta & Shafa
app/build.gradle.kts                     # Integrator: Marta & Rey
```

**Aturan resource:** nama file XML layout diberi suffix pemilik untuk menghindari bentrokan; nama standar resource yang diwajibkan Android/Gradle tetap dipertahankan (`AndroidManifest.xml`, `themes.xml`, dll.). Semua Activity didaftarkan di manifest dan hanya Launcher mempunyai intent `MAIN` + `LAUNCHER`.

---

## 9. Integrasi & Git Workflow

1. Marta membuat repository Android Studio dasar, package `com.example.telutask`, min SDK dan versi Gradle kompatibel dengan lingkungan kelompok.
2. Rey membuat dan push model/DAO/Room Database **terlebih dahulu** ke branch `feature/068-room`.
3. Tim menyepakati nama class/fungsi di Bab 6. Sementara menunggu database, anggota lain membuat UI dengan kontrak yang sudah ditetapkan; jangan membuat class database duplikat.
4. Shafa mengembangkan `feature/048-auth`, Rico `feature/075-create`, Adan `feature/077-edit`, Faiz `feature/072-dashboard`, Marta `feature/069-integration`.
5. Setelah Rey merge, hubungkan UI dengan DAO dan SessionManager.
6. Setiap anggota mengerjakan file miliknya. Jangan mengubah gradle, manifest, file resource global, atau kontrak DAO tanpa koordinasi.
7. Semua merge ke `main` melalui pull request dan pemeriksaan build. Jika waktu sangat terbatas, integrasikan dalam urutan: Room → Auth → Dashboard → Create → Edit/Delete → Checklist/Sort → QA.
8. Semua anggota melakukan smoke test setelah merge.

**Aturan dependensi antaranggota:**

| Pembuat | Dipakai oleh | Objek/fungsi yang dipakai |
|---|---|---|
| Rey | Semua | `TeluTaskDatabase068`, `UserDao068`, `TaskDao068`, entities |
| Shafa | Rico, Adan, Faiz, Marta | `SessionManager048.getUserId048()` |
| Rico | Faiz | `AddTaskActivity075` |
| Adan | Faiz & Marta | `TaskDetailActivity077`, `EditTaskActivity077` |
| Faiz | Semua | `DashboardActivity072` |
| Marta | Adan | `DeleteTaskDialog069` |

Shafa harus mengekspor metode **`getUserId048(): Int?`** dan **`clearSession048()`**, serta `saveUserId048(userId048: Int)`. Pengembalian `null` berarti sesi tidak ada. Metode bisa berupa anggota kelas SessionManager, bukan top-level function.

**Passing parameter antarlayar:** Gunakan `Intent` extras key yang konsisten, misalnya `EXTRA_TASK_ID077 = "extra_task_id"` dideklarasikan oleh Adan. Faiz mengirim `taskId072` dengan key milik Adan (tidak membuat key versi lain). `user_id` **tidak** dipercaya dari Intent; selalu baca dari SessionManager.

---

## 10. Test Case / Acceptance Test

| ID | Skenario | Hasil yang diharapkan | Penguji utama |
|---|---|---|---|
| T01 | Register username baru, password 6+ karakter | Akun berhasil masuk Room | Shafa |
| T02 | Register username kosong / password <6 karakter | Ditolak dengan pesan validasi | Shafa |
| T03 | Register username yang sama | Ditolak (tidak duplikat) | Shafa |
| T04 | Login benar | Masuk Dashboard, user_id tersimpan | Shafa |
| T05 | Login salah | Tetap di Login, ada pesan error | Shafa |
| T06 | Tutup/buka aplikasi setelah login | Langsung Dashboard selama sesi aktif | Shafa |
| T07 | Logout | Sesi terhapus, kembali Login | Shafa |
| T08 | Tambah task valid | Data tampil dalam RecyclerView | Rico, Faiz |
| T09 | Tambah task tanpa nama/deadline | Ditolak | Rico |
| T10 | Edit nama/prioritas/deadline | Perubahan tampil di Dashboard | Adan |
| T11 | Hapus task lalu konfirmasi | Task hilang dari Room dan daftar | Marta, Adan |
| T12 | Batalkan dialog hapus | Task tetap ada | Marta |
| T13 | Centang checklist | `is_done=true`, UI berubah, tetap tersimpan setelah restart | Faiz |
| T14 | Hapus centang | `is_done=false` dan disimpan | Faiz |
| T15 | Sort prioritas | High → Medium → Low | Faiz |
| T16 | Login akun A, buat tugas, logout, login akun B | Akun B **tidak** melihat tugas akun A | Marta |
| T17 | User B mencoba membuka ID task user A | Detail tidak terbuka, update/delete berdampak 0 baris | Marta, Adan |
| T18 | Tidak ada tugas | Empty state muncul tanpa crash | Faiz |
| T19 | Putar layar atau kembali dari form | Tidak crash, daftar tetap benar | Marta |
| T20 | Jalankan tanpa internet | Semua fungsi utama tetap berjalan | Marta |

**Demo minimum:** register 2 akun berbeda → login akun A → tambah 2 tugas → edit → checklist → sortir → hapus 1 → logout → login akun B → pastikan daftar kosong → login ulang akun A → pastikan tugas A tersisa dan statusnya tidak hilang.

---

## 11. Jadwal Singkat Menuju Pengumpulan

**7 November 2026:** Setup repository, kontrak Room, database, dan layout awal.  
**8 November 2026:** Integrasi autentikasi, CRUD, RecyclerView, checklist, sortir, dan uji isolasi data.  
**9 November 2026:** Final QA, build APK, dokumentasi, dan pengumpulan sebelum tenggat.

Target minimal pada akhir setiap fase adalah **APK dapat dijalankan**, bukan hanya file source terpisah.

---

## 12. Prompt Induk (Dibaca Seluruh Anggota)

> Saya mengembangkan TeluTask untuk UTS Pengembangan Aplikasi Bergerak, Kelompok 6 (Task Management). Baca seluruh PRD ini terlebih dahulu sebagai satu-satunya spesifikasi. Gunakan Android Studio, Kotlin, XML Layout, Room Persistence Library (SQLite), RecyclerView, SharedPreferences, dan tanpa API internet. Entity wajib User(id, username, password) serta Task(id, user_id, nama_tugas, prioritas, deadline, is_done). Nama tabel bersama `users068` dan `tasks068`; metode DAO wajib mengikuti Bab 6. Semua variabel, parameter, properti Kotlin, dan ID view custom yang saya deklarasikan harus menggunakan suffix tiga digit NIM saya. Jangan mengubah nama metode override Android atau nama kolom SQLite yang disepakati. Kerjakan hanya modul kepemilikan saya agar tidak bentrok. Untuk setiap file, tuliskan path, source code lengkap, dependency yang diperlukan, dan bagaimana mengintegrasikan dengan modul anggota lain. Jangan membuat database/helper baru jika milik Rey sudah tersedia. Utamakan fitur sesuai rubrik dan pastikan semua query task dibatasi `user_id` sesi login.

### 12.1 Prompt Rey (068)

> Saya Rey, NIM 068. Kerjakan **hanya** modul Room database menurut Bab 3, 5, 6, dan 8. Buat Entity `User068`, `Task068`, DAO, singleton `TeluTaskDatabase068`, serta contoh pemakaian DAO. Semua variabel/parameter/properti buatan saya harus berakhiran `068`; tabel `users068` dan `tasks068`. Pertahankan kolom SQLite yang disepakati. Implementasikan query task dengan pembatasan `user_id` untuk read, edit, checklist, delete, dan sorting. Berikan kode Kotlin lengkap beserta import, dependency Room + KSP/kapt, dan contoh test.

### 12.2 Prompt Shafa (048)

> Saya Shafa, NIM 048. Kerjakan **hanya** autentikasi, register, login, session SharedPreferences, logout, dan Launcher menurut PRD. Gunakan `UserDao068` milik Rey. Seluruh variabel/parameter/properti yang saya deklarasikan wajib suffix `048`. Buat Activity, XML Layout, validasi username unik dan password min 6 karakter, dan `SessionManager048` dengan `saveUserId048`, `getUserId048`, `clearSession048`. Setelah login arahkan ke `DashboardActivity072`; jika sesi tidak ada arahkan ke Login. Jangan membuat database baru.

### 12.3 Prompt Rico (075)

> Saya Rico, NIM 075. Kerjakan **hanya** fitur Tambah Tugas menurut PRD. Buat `AddTaskActivity075.kt` dan layout XML lengkap. Semua variabel/parameter/properti/View ID saya suffix `075`. Field wajib `nama_tugas`, `prioritas` (High, Medium, Low), `deadline` menggunakan DatePicker, dan `is_done` default false. Ambil `user_id` dari `SessionManager048`, simpan melalui `TaskDao068.insertTask068`. Pastikan validasi dan refresh Dashboard setelah kembali. Jangan menambahkan field Task baru atau membuat SQLiteOpenHelper.

### 12.4 Prompt Adan (077)

> Saya Adan, NIM 077. Kerjakan **hanya** halaman Detail dan Edit Tugas menurut PRD. Buat `TaskDetailActivity077`, `EditTaskActivity077`, dan layout XML. Semua variabel/parameter/properti/View ID saya suffix `077`. Baca task dengan `TaskDao068.getTaskById068(taskId, userId)` menggunakan sesi. Edit nama tugas, prioritas, deadline melalui `updateTask068`. Tombol hapus memanggil `DeleteTaskDialog069` milik Marta; jangan menulis fitur hapus duplikat. Sertakan Intent extra task ID yang disepakati.

### 12.5 Prompt Faiz (072)

> Saya Faiz, NIM 072. Kerjakan **hanya** Dashboard, RecyclerView, adapter, checkbox `is_done`, dan sorting prioritas menurut PRD. Buat `DashboardActivity072`, `TaskAdapter072`, dan XML layout. Semua variabel/parameter/properti/View ID saya suffix `072`. Ambil `user_id` melalui `SessionManager048`, lalu panggil DAO milik Rey; tampilkan **hanya** task user aktif. Checkbox memanggil `updateDone068` dan sortir memanggil `getTasksSortedByPriority068`. Navigasi ke `AddTaskActivity075` dan `TaskDetailActivity077`; Logout melalui SessionManager milik Shafa. Hindari pembacaan seluruh task tanpa filter user.

### 12.6 Prompt Marta (069)

> Saya Marta, NIM 069. Kerjakan **hanya** dialog konfirmasi hapus, tema visual konsisten, dan integrasi/QA sesuai PRD. Buat `DeleteTaskDialog069.kt` yang dapat dipanggil dari `TaskDetailActivity077`, memanggil `TaskDao068.deleteTask068(taskId, userId)` setelah konfirmasi, serta menangani hasil 0 baris. Semua variabel/parameter/properti yang saya deklarasikan suffix `069`. Buat checklist pengujian seluruh requirement, langkah merge branch, pemeriksaan manifest/Gradle, serta panduan build APK. Jangan mengubah kontrak DAO atau membuat Entity duplikat.

---

## 13. Definition of Done

Project dinyatakan siap dikumpulkan jika:

- [ ] Aplikasi build dan dapat dijalankan di emulator/perangkat Android.
- [ ] Register, Login, Session, dan Logout berjalan dengan Room + SharedPreferences.
- [ ] Ada tabel `users068` dan `tasks068` dalam satu database Room.
- [ ] Setiap query Task menjamin data user tidak tercampur.
- [ ] Form tambah dan edit memvalidasi input.
- [ ] Create, Read, Update, Delete Task berhasil dan tetap tersimpan setelah restart.
- [ ] RecyclerView berjalan; checklist dan sorting prioritas berfungsi.
- [ ] Semua variabel custom menggunakan 3 digit NIM pemilik.
- [ ] Tidak ada koneksi internet/API PHP yang diperlukan.
- [ ] Semua test case wajib lulus, terutama T16 dan T17.
- [ ] APK, source code, dan bukti demo siap dikumpulkan sesuai arahan dosen.

---

**Sumber requirement utama:** `UTS Pengembangan Aplikasi Bergerak November 2026.pdf`, halaman 1–2. Jika ada perbedaan interpretasi, prioritaskan instruksi dosen pada PDF, lalu gunakan kontrak bersama dalam PRD ini untuk koordinasi implementasi.
