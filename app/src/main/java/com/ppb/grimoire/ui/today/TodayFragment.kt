package com.ppb.grimoire.ui.today

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppb.grimoire.R
import com.ppb.grimoire.adapter.ListScheduleAdapter
import com.ppb.grimoire.databinding.FragmentScheduleBinding
import com.ppb.grimoire.databinding.FragmentTodayBinding
import com.ppb.grimoire.model.Schedule
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TodayFragment : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    private val listSchedule = ArrayList<Schedule>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Log.d("1","1")
        binding = FragmentTodayBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("2","2")
        binding.rvToday.setHasFixedSize(true)

        Log.d("time milli",System.currentTimeMillis().toString())
        listSchedule.addAll(getListSchedule(convertDate(System.currentTimeMillis())))
        Log.d("convert date",convertDate(System.currentTimeMillis()))
        showRecyclerList()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showRecyclerList() {
        binding.rvToday.layoutManager = LinearLayoutManager(context)
        val listScheduleAdapter = ListScheduleAdapter(listSchedule)
        binding.rvToday.adapter = listScheduleAdapter
    }


    private fun getClickedDate(year: Int, month: Int, dayOfMonth: Int) : String {
        val dateString = dayOfMonth.toString() +
                "/" + (month + 1).toString() +
                "/" + year.toString()

        return dateString
    }

    private fun convertDate(dateInMilliseconds: Long): String {
        val dateFormat = "d/M/yyyy"
        return DateFormat.format(dateFormat, dateInMilliseconds).toString()
    }

    private fun getListSchedule(date: String) : ArrayList<Schedule> {
        val dataSchedule = if (date == "6/4/2022") {
            resources.getStringArray(R.array.schedule_item)
        } else {
            resources.getStringArray(R.array.schedule_item2)
        }

        val lSch = ArrayList<Schedule>()

        for (i in dataSchedule.indices) {
            val sch = Schedule(dataSchedule[i])
            lSch.add(sch)
        }

        return lSch
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}