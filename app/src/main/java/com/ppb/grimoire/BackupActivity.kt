package com.ppb.grimoire

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class BackupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var backupBtn: Button
    private lateinit var restoreBtn: Button

    private fun init() {
        backupBtn = findViewById(R.id.backup_btn)
        restoreBtn = findViewById(R.id.restore_btn)

        backupBtn.setOnClickListener(this)
        restoreBtn.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        init()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.backup_btn -> {
                Log.i("JEDDI", "Masuk Backup Button ::: 18")
            }
            R.id.restore_btn -> {
                Log.i("JEDDI", "Masuk Restore Button ::: 22")
            }
        }
    }
}