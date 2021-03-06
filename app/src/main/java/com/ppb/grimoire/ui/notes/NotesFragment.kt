package com.ppb.grimoire.ui.notes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.ppb.grimoire.MainActivity.Companion.NtHelp
import com.ppb.grimoire.NoteAddUpdateActivity
import com.ppb.grimoire.adapter.NoteAdapter
import com.ppb.grimoire.databinding.FragmentNotesBinding
import com.ppb.grimoire.db.NoteHelper
import com.ppb.grimoire.helper.MappingHelper
import com.ppb.grimoire.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentNotesBinding
    private lateinit var personId: String
    private lateinit var noteHelper: NoteHelper
    private lateinit var listNoteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding = FragmentNotesBinding.inflate(layoutInflater)
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.setHasFixedSize(true)

        listNoteAdapter = NoteAdapter(this)
        binding.rvNotes.adapter = listNoteAdapter

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        personId = account?.id.toString()

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(context, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        noteHelper = NtHelp

        // proses ambil data
        loadNotesAsync()

        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                listNoteAdapter.listNotes = list
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                NoteAddUpdateActivity.REQUEST_ADD -> if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                    listNoteAdapter.addItem(note!!)
                    binding.rvNotes.smoothScrollToPosition(listNoteAdapter.itemCount - 1)
                    Toast.makeText(
                        requireContext(),
                        "One item recorded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NoteAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        NoteAddUpdateActivity.RESULT_UPDATE -> {
                            val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                            listNoteAdapter.updateItem(position, note!!)
                            binding.rvNotes.smoothScrollToPosition(position)
                            Toast.makeText(
                                requireContext(),
                                "One item updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        NoteAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                            listNoteAdapter.removeItem(position)
                            Toast.makeText(
                                requireContext(),
                                "One item deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listNoteAdapter.listNotes)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll(personId)
                MappingHelper.mapCursorNoteToArrayList(cursor)
            }
            binding.progressbar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                listNoteAdapter.listNotes = notes
            } else {
                listNoteAdapter.listNotes = ArrayList()
                Toast.makeText(requireContext(), "Note is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}