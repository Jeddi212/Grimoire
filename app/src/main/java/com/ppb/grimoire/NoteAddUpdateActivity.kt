package com.ppb.grimoire

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textview.MaterialTextView
import com.ppb.grimoire.MainActivity.Companion.NtHelp
import com.ppb.grimoire.db.DatabaseContract
import com.ppb.grimoire.db.DatabaseContract.NoteColumns.Companion.DATE
import com.ppb.grimoire.db.NoteHelper
import com.ppb.grimoire.model.Language
import com.ppb.grimoire.model.Note
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0

    val SELECT_PHOTO = 1
    private lateinit var uri: Uri
    private lateinit var addImage: LinearLayout
    private lateinit var addText: LinearLayout
    private lateinit var submit: LinearLayout
    private lateinit var show: LinearLayout

    private lateinit var noteHelper: NoteHelper
    private lateinit var edt_title: EditText
    private lateinit var edt_description: EditText
    private lateinit var btn_submit: ImageView

    private var languageList = ArrayList<Language>()
    private lateinit var parent: LinearLayout

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        edt_title = findViewById(R.id.edt_title)
        edt_description = findViewById(R.id.edt_description)
        btn_submit = findViewById(R.id.btn_submit)

        noteHelper = NtHelp

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            note?.let {
                edt_title.setText(it.title)
                edt_description.setText(it.description)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //btn_submit.text = btnTitle
        btn_submit.setOnClickListener(this)

        initMiscellaneous();
        initElement()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (title.isEmpty()) {
                edt_title.error = "Field can not be blank"
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

            if (isEdit) {
                val result = noteHelper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@NoteAddUpdateActivity,
                        "Gagal mengupdate data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())
                val result = noteHelper.insert(values)

                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@NoteAddUpdateActivity,
                        "Gagal menambah data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        else if (view.id == R.id.layoutAddImage) {
            pickImage()
//            addImageView()
            Log.i("JEDDI", "Add Image")
        }
        else if (view.id == R.id.layoutAddText) {
            addTextView()
            Log.i("JEDDI", "Add Text")
        }
        else if (view.id == R.id.layoutSubmit) {
            saveData()
            Log.i("JEDDI", "Save Data")
        }
        else if (view.id == R.id.layoutShowData) {
            showData()
            Log.i("JEDDI", "Show Data")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data?.data != null) {
            uri = data.data!!
            try {
                // Update Image View w/ selected image from device storage
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                addImageView(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Note"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage).setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@NoteAddUpdateActivity,
                            "Gagal menghapus data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_PHOTO)
    }

    private fun addImageView(bitmap: Bitmap) {
        val inflater = LayoutInflater.from(this).inflate(R.layout.element_note_image, null)
        parent.addView(inflater)
        parent.getChildAt(parent.childCount - 1)
            .findViewById<ImageView>(R.id.extraImageNote)
            .setImageBitmap(bitmap)
    }

    private fun addTextView() {
        val inflater = LayoutInflater.from(this).inflate(R.layout.element_note_text, null)
        parent.addView(inflater)
    }

    private fun saveData() {
        languageList.clear()
        // this counts the no of child layout
        // inside the parent Linear layout
        val count = parent.childCount
        var v: View?

        for (i in 0 until count) {
            v = parent.getChildAt(i)

            val languageName: EditText = v.findViewById(R.id.et_name)

            // create an object of Language class
            val language = Language()
            language.name = languageName.text.toString()

            // add the data to arraylist
            languageList.add(language)
        }

        Log.i("JEDDI", "$languageList")
    }

    private fun showData() {
        val count = parent.childCount
        for (i in 0 until count) {
            Toast.makeText(this,
                "Language at $i is ${languageList[i].name}.", Toast.LENGTH_SHORT).show()
        }
        Log.i("JEDDI", "FROM SHOW ::: \n $languageList")
    }

    private fun initElement() {
        parent = findViewById(R.id.parent_linear_layout)

        addImage = findViewById(R.id.layoutAddImage)
        addText = findViewById(R.id.layoutAddText)
        submit = findViewById(R.id.layoutSubmit)
        show = findViewById(R.id.layoutShowData)

        addImage.setOnClickListener(this)
        addText.setOnClickListener(this)
        submit.setOnClickListener(this)
        show.setOnClickListener(this)
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)
        layoutMiscellaneous.findViewById<MaterialTextView>(R.id.textMiscellaneous).setOnClickListener(View.OnClickListener {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
            });
    }

}