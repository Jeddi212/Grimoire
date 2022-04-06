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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tvHello: TextView
    private lateinit var btnEditProfile: Button
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var signout : Button
    private lateinit var binding: FragmentProfileBinding
    lateinit var user : User

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
    ): View {
        // Inflate the layout for this fragment
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
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
        Log.d("name",user.personPhoto.toString())
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        if(user.personPhoto!=null) {
            with(binding) {
                Glide.with(requireContext())
                    .load(user.personPhoto)
                    .into(userImageView)
            }
        }
        signout = binding.signOutButton
        signout.setOnClickListener(this)
        return binding.root
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

    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
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