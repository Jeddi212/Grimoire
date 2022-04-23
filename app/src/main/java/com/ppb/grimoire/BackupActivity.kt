package com.ppb.grimoire

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.nio.channels.FileChannel


class BackupActivity : AppCompatActivity(), View.OnClickListener {

    val SELECT_PHOTO = 1
    private lateinit var uri: Uri
    private lateinit var iv: ImageView

    private lateinit var backupBtn: Button
    private lateinit var restoreBtn: Button

    private fun init() {
        backupBtn = findViewById(R.id.backup_btn)
        restoreBtn = findViewById(R.id.restore_btn)

        iv = findViewById(R.id.imageee)

        backupBtn.setOnClickListener(this)
        restoreBtn.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        init()
    }

    override fun onClick(v: View) {
//        val account = GoogleSignIn.getLastSignedInAccount(this)

        when (v.id) {
            R.id.backup_btn -> {
//                exportDB()
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, SELECT_PHOTO)
            }
            R.id.restore_btn -> {
//                importDB()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data?.data != null) {
            uri = data.data!!
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                iv.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun checkStorage(): Boolean {
        val mExternalStorageAvailable: Boolean
        val mExternalStorageWriteable: Boolean
        val state = Environment.getExternalStorageState()
        when (state) {
            Environment.MEDIA_MOUNTED ->         // We can read and write the media
            {
                mExternalStorageWriteable = true
                mExternalStorageAvailable = mExternalStorageWriteable
            }
            Environment.MEDIA_MOUNTED_READ_ONLY -> {
                // We can only read the media
                mExternalStorageAvailable = true
                mExternalStorageWriteable = false
            }
            else ->         // Something else is wrong. It may be one of many other states, but
                // all we need
                // to know is we can neither read nor write
            {
                mExternalStorageWriteable = false
                mExternalStorageAvailable = mExternalStorageWriteable
            }
        }
        return mExternalStorageAvailable && mExternalStorageWriteable
    }

    private fun audiooo() {
        // Add a specific media item.
        val resolver = applicationContext.contentResolver

// Find all audio files on the primary external storage device.
        val audioCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        Log.i("JEDDI", "AUDIO ::: \n $audioCollection")

// Publish a new song.
        val newSongDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "My Song.mp3")
        }

        Log.i("JEDDI", "Song Details ::: \n $newSongDetails")

// Keeps a handle to the new song's URI in case we need to modify it
// later.
        val myFavoriteSongUri = resolver
            .insert(audioCollection, newSongDetails)

        Log.i("JEDDI", "Song Uri ::: \n $myFavoriteSongUri")
    }

    @SuppressLint("SdCardPath")
    private fun exportDrive() {
        val databaseName = "db_grimoire"
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath = "/data/com.ppb.grimoire/databases/$databaseName"
//        val backupDBPath = "db_grimoire.db"
        val currentDB = File(data, currentDBPath)

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(RetrofitMetadataPart::class.java)

        val metadataJSON = jsonAdapter.toJson(
            RetrofitMetadataPart(
                name = currentDBPath,
            )
        )

        val metadataPart = MultipartBody.Part.create(
            metadataJSON.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        )

        val multimediaPart = MultipartBody.Part.create(
            currentDBPath.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        )

//        googleDriveApi.uploadFileMultipart(
//            metadataPart,
//            multimediaPart
//        )

//        val backupDB = File(sd, backupDBPath)
//        Log.i("JEDDI", "DB ::: \n $currentDB")
//        try {
//            source = FileInputStream(currentDB).channel
//            Log.i("JEDDI", "Source ::: \n $source")
//            destination = FileOutputStream(backupDB).channel
//            destination.transferFrom(source, 0, source.size())
//            source.close()
//            destination.close()
//            Toast.makeText(this, "Your Database is Exported !!", Toast.LENGTH_LONG).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    @SuppressLint("SdCardPath")
    private fun exportDB() {
        val databaseName = "db_grimoire"
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath = "/data/com.ppb.grimoire/databases/$databaseName"
        val backupDBPath = "db_grimoire.db"
        val currentDB = File(data, currentDBPath)
        val backupDB = File(sd, backupDBPath)
        Log.i("JEDDI", "DB ::: \n $currentDB")
        try {
            source = FileInputStream(currentDB).channel
            Log.i("JEDDI", "Source ::: \n $source")
            destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            Toast.makeText(this, "Your Database is Exported !!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SdCardPath")
    private fun importDB() {
        val dir = Environment.getExternalStorageDirectory().absolutePath
        val sd = File(dir)
        val data = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val backupDBPath = "/data/com.ppb.grimoire/databases/db_grimoire"
        val currentDBPath = "db_grimoire.db"
        val currentDB = File(sd, currentDBPath)
        val backupDB = File(data, backupDBPath)
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            Toast.makeText(this, "Your Database is Imported !!", LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getDriveService(): Drive? {
        GoogleSignIn.getLastSignedInAccount(this)?.let { googleAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                this, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = googleAccount.account!!
            return Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName(getString(R.string.app_name))
                .build()
        }
        return null
    }

    private fun accessDriveFiles() {
        getDriveService()?.let { googleDriveService ->
            CoroutineScope(Dispatchers.IO).launch {
                var pageToken: String?
                do {
                    val result = googleDriveService.files().list().apply {
                        spaces = "drive"
                        fields = "nextPageToken, files(id, name)"
                        pageToken = this.pageToken
                    }.execute()

                    result.files.forEach { file ->
                        Log.d("JEDDI", ("name=${file.name} id=${file.id}"))
                    }
                } while (pageToken != null)
            }
        }
    }

    fun uploadFileToGDrive() {
        getDriveService()?.let { googleDriveService ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val localFileDirectory = File(getExternalFilesDir("backup")!!.toURI())
                    val actualFile = File("${localFileDirectory}/FILE_NAME_BACKUP")
                    val gFile = com.google.api.services.drive.model.File()
                    gFile.name = actualFile.name
                    val fileContent = FileContent("text/plain", actualFile)
                    googleDriveService.Files().create(gFile, fileContent).execute()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        } ?: Toast.makeText(this, "Please Log In first!", LENGTH_SHORT).show()
    }

    data class RetrofitMetadataPart(
//        val parents: List<String>, //directories
        val name: String //file name
    )
}