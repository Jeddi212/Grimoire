package com.ppb.grimoire.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.ppb.grimoire.MainActivity.Companion.ScHelp
import com.ppb.grimoire.ScheduleAddUpdateActivity
import com.ppb.grimoire.adapter.ListScheduleAdapter
import com.ppb.grimoire.databinding.FragmentScheduleBinding
import com.ppb.grimoire.db.ScheduleHelper
import com.ppb.grimoire.helper.MappingHelper
import com.ppb.grimoire.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private var listSchedule = ArrayList<Schedule>()
    private lateinit var clickedDate: String

    private lateinit var personId: String

    private lateinit var scheduleHelper: ScheduleHelper
    private lateinit var listScheduleAdapter: ListScheduleAdapter

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        listScheduleAdapter = ListScheduleAdapter(this)

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        personId = account?.id.toString()

        clickedDate = getCurrentDate()

        binding = FragmentScheduleBinding.inflate(layoutInflater)

        scheduleHelper = ScHelp

        // Floating action Bar, move to new activity
        binding.fabAdd.setOnClickListener {
            val intent = Intent(context, ScheduleAddUpdateActivity::class.java)

            // Ngirim date yang di click ke activity insert schedule
            intent.putExtra(ScheduleAddUpdateActivity.EXTRA_DATE, clickedDate)
            intent.putExtra(ScheduleAddUpdateActivity.EXTRA_DATE_LONG, binding.calendar.date)

            startActivityForResult(intent, ScheduleAddUpdateActivity.REQUEST_ADD)
        }

        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->

            clickedDate = getClickedDate(year, month, dayOfMonth)

            listSchedule.clear()
            loadScheduleAsync()
            listSchedule.addAll(listScheduleAdapter.listSchedule)

            showRecyclerList()
        }

        if (savedInstanceState == null) {
            loadScheduleAsync()
        } else {
//            val list = savedInstanceState.getParcelableArrayList<Schedule>(EXTRA_STATE)
//            if (list != null) {
//                listScheduleAdapter.listSchedule = list
//            }
            loadScheduleAsync()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clickedDate = getCurrentDate()
        binding.rvSchedule.setHasFixedSize(true)

        showRecyclerList()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showRecyclerList() {
        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        listScheduleAdapter = ListScheduleAdapter(this)
        binding.rvSchedule.adapter = listScheduleAdapter
    }

    private fun getClickedDate(year: Int, month: Int, dayOfMonth: Int): String {

        return dayOfMonth.toString() +
                "/" + (month + 1).toString() +
                "/" + year.toString()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvSchedule, message, Snackbar.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                ScheduleAddUpdateActivity.REQUEST_ADD -> if (resultCode ==
                    ScheduleAddUpdateActivity.RESULT_ADD) {
                    val schedule = data.getParcelableExtra<Schedule>(ScheduleAddUpdateActivity.EXTRA_SCHEDULE)

                    // Not NULL?
                    listScheduleAdapter.addItem(schedule!!)
                    binding.rvSchedule.smoothScrollToPosition(listScheduleAdapter.itemCount - 1)

                    Toast.makeText(requireContext(),"One item recorded successfully", Toast.LENGTH_SHORT).show()
//                    showSnackbarMessage("One item recorded successfully")
                }
                ScheduleAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        ScheduleAddUpdateActivity.RESULT_UPDATE -> {
                            val schedule = data.getParcelableExtra<Schedule>(ScheduleAddUpdateActivity.EXTRA_SCHEDULE)

                            val position = data.getIntExtra(ScheduleAddUpdateActivity.EXTRA_POSITION, 0)

                            listScheduleAdapter.updateItem(position, schedule!!)
                            binding.rvSchedule.smoothScrollToPosition(position)
                            Toast.makeText(requireContext(),"One item updated successfully", Toast.LENGTH_SHORT).show()
//                            showSnackbarMessage("One item updated succesfully")
                        }
                        ScheduleAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(ScheduleAddUpdateActivity.EXTRA_POSITION, 0)

                            listScheduleAdapter.removeItem(position)
                            Toast.makeText(requireContext(),"One item deleted successfully", Toast.LENGTH_SHORT).show()
//                            showSnackbarMessage("One item deleted successfully")
                        }
                    }
            }
        }
    }

    private fun loadScheduleAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val deferredSchedule = async(Dispatchers.IO) {
                val cursor = scheduleHelper.queryByDate(clickedDate, personId)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressbar.visibility = View.INVISIBLE
            val schedule = deferredSchedule.await()

            if (schedule.size > 0) {
                listScheduleAdapter.listSchedule = schedule
            } else {
                listScheduleAdapter.listSchedule = ArrayList()
                Toast.makeText(requireContext(),"Seems to be empty here, enjoy your day", Toast.LENGTH_SHORT).show()
//                showSnackbarMessage("Seems to be empty here, enjoy your day")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listScheduleAdapter.listSchedule)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("d/M/yyyy",
            Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


        private const val EXTRA_STATE = "EXTRA_STATE"

    }

}
