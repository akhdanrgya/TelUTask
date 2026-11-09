# Modul Akhdan / Adan (077)

Implementasi Bab 3.4: detail dan edit memakai XML + ViewBinding, DAO Room Rey,
dan sesi Shafa. Tidak ada database/dependency baru. Kedua Activity sudah terdaftar
sebagai non-exported. ComponentActivity dipakai agar kompatibel dengan tema platform
proyek saat ini. Insets system bar dan keyboard ditangani pada root layout.

## Navigasi dari Dashboard (Faiz)

```kotlin
startActivity(Intent(this, TaskDetailActivity077::class.java)
    .putExtra(TaskDetailActivity077.EXTRA_TASK_ID077, taskId072))
```

Jangan kirim user ID lewat Intent. Refresh daftar pada `onResume` Dashboard.
Detail memuat ulang pada `onResume`, termasuk setelah edit. Edit berhasil memanggil
`setResult(RESULT_OK)` dan `finish()`. ID invalid, data hilang, atau akses lintas akun
kembali ke Dashboard menggunakan CLEAR_TOP. Sesi hilang/berubah diarahkan ke Login
menggunakan NEW_TASK | CLEAR_TASK. Selama kedua Activity milik anggota lain belum
tersedia, fallback menutup layar; sesi invalid menutup task aplikasi.

Deadline mengikuti Create: epoch milliseconds, tengah malam timezone perangkat;
format tampilan `dd MMM yyyy`, locale Indonesia. Nama di-trim; nama kosong,
prioritas selain High/Medium/Low, dan deadline null ditolak. Isian yang belum disimpan
bertahan saat recreation. Update hanya tiga field, sehingga status selesai tetap utuh.

## Integrasi hapus Marta — belum tersedia di repository

PRD belum menetapkan signature `DeleteTaskDialog069`. Modul menyediakan interface
`DeleteTaskHost077` di `TaskScreen077.kt`. Application integrator perlu mengimplementasikan
interface ini dan mendaftarkan class Application di manifest. Implementasi method
`showDeleteTaskDialog077` harus memanggil komponen `DeleteTaskDialog069` Marta.

Parameter: Activity pemilik, task ID, user ID sesi yang membuka detail, callback sukses.
Dialog harus memeriksa lagi sesi saat konfirmasi, memakai
`deleteTask068(taskId, userId)`, menangani hasil 0 dan exception, lalu memanggil callback
hanya sesudah hapus berhasil. Jangan simpan referensi Activity setelah dialog ditutup.
Tombol Hapus dinonaktifkan sampai Application menyediakan interface tersebut.
Tidak ada SQL/delete/dialog duplikat di modul 077. T11/T12 menunggu integrasi Marta.

## Dependensi yang sudah tersedia

`androidx.activity`, `androidx.core:core-ktx`, `androidx.lifecycle:lifecycle-runtime-ktx`,
Room, dan `buildFeatures.viewBinding = true`. Tidak ada perubahan Gradle, DAO, atau tema global.
Resource modul berada di `strings_077.xml` dan dua layout bersuffix 077.

## Verifikasi manual setelah integrasi

- T10: buka detail, edit ketiga field, simpan; detail dan Dashboard menampilkan nilai baru.
- Nama spasi saja ditolak; simpan berulang tidak membuat operasi bersamaan.
- T17: buka ID akun A dengan sesi B; detail/edit ditutup dan data A tidak berubah.
- Sesi hilang atau berganti ketika edit terbuka: penyimpanan ditolak sebelum query.
- Task dihapus ketika form terbuka: update 0 menampilkan pesan dan kembali Dashboard.
- T19: ubah nama/prioritas/tanggal, putar layar; draft tetap sama.
- Status selesai tidak berubah setelah edit; tanggal sama dengan form Tambah.
- Gagal baca/simpan: pesan terlihat, baca bisa diulang, draft tidak dihapus.
- T11/T12 setelah Marta terhubung: konfirmasi menghapus; batal mempertahankan data.

Pengujian instrumentasi `TaskOwnership077Test` mengecek update lintas akun ditolak
dan update valid mempertahankan pemilik serta status selesai. Jalankan dengan emulator:
`bash gradlew :app:connectedDebugAndroidTest`.

## Hasil pemeriksaan 7 November 2026

- `:app:assembleDebug`, `:app:testDebugUnitTest`, dan
  `:app:compileDebugAndroidTestKotlin`: **BUILD SUCCESSFUL**.
- XML manifest/layout/string valid; `git diff --check` bersih.
- Test instrumentasi baru berhasil dikompilasi, belum dieksekusi karena
  `adb devices` tidak menampilkan emulator/perangkat tersambung.
- Unit test yang berjalan adalah unit test bawaan proyek; bukan bukti alur UI lulus.
- APK debug: `app/build/outputs/apk/debug/app-debug.apk`.
- Build menggunakan Java bawaan Android Studio dan `--no-configuration-cache`.
