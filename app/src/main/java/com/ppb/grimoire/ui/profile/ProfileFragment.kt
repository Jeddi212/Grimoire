package com.ppb.grimoire.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ppb.grimoire.R
import com.ppb.grimoire.User
import com.ppb.grimoire.databinding.FragmentNewsBinding
import com.ppb.grimoire.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tvHello: TextView
    private lateinit var btnEditProfile: Button
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var signout : Button
    private lateinit var binding: FragmentProfileBinding
    lateinit var user : User
//    var name: String = "your name..." // Nanti ambil nama user-nya dari sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            var personName = acct.displayName.toString()
            var personGivenName = acct.givenName.toString()
            var personFamilyName = acct.familyName.toString()
            var personEmail = acct.email.toString()
            var personId = acct.id.toString()
            var personPhoto = acct.photoUrl
            Log.d("name",personName)
            user = User(personName,personGivenName,personFamilyName,personEmail,personId, personPhoto)
        }
        Log.d("name",user.personName)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        signout = binding.signOutButton
        signout.setOnClickListener(this)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHello = view.findViewById(R.id.nameTextView)
        val name: String = user.personName
        tvHello.text = resources.getString(R.string.nameTextView, name)

        btnEditProfile = view.findViewById(R.id.sign_out_button)
        btnEditProfile.setOnClickListener(this)
    }

//    override fun onClick(v: View) {
//        if (v.id == R.id.sign_out_button) {
//            tvHello.text = "Welcome"
//            btnEditProfile.text = "Button Ciao!"
//            btnEditProfile.setBackgroundColor(resources.getColor(R.color.teal_200, null))
//        }
//    }
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener() {
                Toast.makeText(requireContext(),"Signed Out Successfully", Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_out_button-> signOut()
        }
    }
}