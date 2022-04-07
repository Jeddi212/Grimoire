package com.ppb.grimoire

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.ppb.grimoire.db.DatabaseContract
import com.ppb.grimoire.db.ScheduleHelper
import com.ppb.grimoire.model.Schedule
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var schedule: Schedule? = null
    private var position: Int = 0
    private var clickedDate: String? = null
    private var clickedDateLong: Long = 0
    private lateinit var scheduleHelper: ScheduleHelper
    private lateinit var edt_title : EditText
    private lateinit var btn_submit : Button
    private lateinit var cv : CalendarView

    companion object {
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_DATE_LONG = "extra_date_long"
        const val EXTRA_SCHEDULE = "extra_schedule"
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
        setContentView(R.layout.activity_schedule_add_update)

        edt_title = findViewById(R.id.edt_title)
        btn_submit = findViewById(R.id.btn_submit)
        cv = findViewById(R.id.calendarAU)

        cv.setOnDateChangeListener { view, year, month, dayOfMonth ->
            clickedDate = getClickedDate(year, month, dayOfMonth)
        }

        scheduleHelper = ScheduleHelper.getInstance(applicationContext)

        schedule = intent.getParcelableExtra(EXTRA_SCHEDULE)
        clickedDate = intent.getStringExtra(EXTRA_DATE)
        clickedDateLong = intent.getLongExtra(EXTRA_DATE_LONG, 0)

        // Set focused date from schedule fragment
//        cv.setDate(SimpleDateFormat("d/M/yyyy").parse(clickedDate).time, true, true)
        Log.i("JEDDI", "LONG DATE ::: $clickedDateLong")
        if (clickedDateLong > 0) cv.date = clickedDateLong

        // TODO error edit kemungkinan sebelum ini
        if (schedule != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            schedule = Schedule()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Reminder"
            btnTitle = "Update"

            schedule?.let {
                edt_title.setText(it.title)
            }
        } else {
            actionBarTitle = "Add Reminder"
            btnTitle = "Submit"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle

        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim()

            val account = GoogleSignIn.getLastSignedInAccount(this)
            val personId = account?.id.toString()

            if (title.isEmpty()) {
                edt_title.error = "Field can't be blank"
                return
            }

            schedule?.title = title
            schedule?.personId = personId

            // TODO Sampe sini masuk
//            edt_title.setText(title)

            Log.i("JEDDI", "SAUA 106 title ::: | $title")

            val intent = Intent()
            intent.putExtra(EXTRA_SCHEDULE, schedule)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.ScheduleColumns.PERSON_ID, personId)
            values.put(DatabaseContract.ScheduleColumns.TITLE, title)

            if (clickedDate != null) {
                schedule?.date = clickedDate
                values.put(DatabaseContract.ScheduleColumns.DATE, clickedDate)
            }

            Log.i("JEDDI", "HAMPIR IF isEdit SAUA 117 | $isEdit")
            Log.i("JEDDI", "Clicked Date SAUA 118 | $clickedDate")

            if (isEdit) {
                Log.i("JEDDI", "AFTER IF isEdit SAUA 118 | $isEdit")

                // TODO disini errornya bosque
                val result = scheduleHelper.update(schedule?.id.toString(), values).toLong()

                Log.i("JEDDI", "Result from UPDATE ::: | $position")

                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@ScheduleAddUpdateActivity,
                        "Update fail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
//                schedule?.date = getCurrentDate()
//                values.put(DatabaseContract.ScheduleColumns.DATE, getCurrentDate())


                val result = scheduleHelper.insert(values)

                // TODO sampe sini masuk
//                edt_title.setText(schedule?.date)
//                edt_title.setText(values.get(DATE).toString())

                // TODO return nya -1, error ga tau diamana ?
//                edt_title.setText(result.toString())

                if (result > 0) {
                    schedule?.id = result.toInt()
                    setResult(RESULT_ADD, intent)

                    // TODO ga masuk sini, tapi else
//                    edt_title.setText(schedule?.id.toString())

                    finish()
                } else {
                    Toast.makeText(
                        this@ScheduleAddUpdateActivity,
                        "Add reminder fail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Cancel Edit"
            dialogMessage = "Do you wish to exit and cancel?"
        } else {
            dialogTitle = "Delete Reminder"
            dialogMessage = "Do you really want to delete this reminder?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = scheduleHelper.deleteById(schedule?.id.toString()).toLong()

                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@ScheduleAddUpdateActivity,
                            "Delete reminder fail", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("d/M/yyyy",
        Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    private fun getClickedDate(year: Int, month: Int, dayOfMonth: Int): String {

        return dayOfMonth.toString() +
                "/" + (month + 1).toString() +
                "/" + year.toString()
    }
}
