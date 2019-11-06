package com.efhem.mystaff.ui.welcome


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.efhem.mystaff.R
import com.efhem.mystaff.databinding.FragmentSignInBinding
import com.efhem.mystaff.databinding.FragmentSignUpBinding
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {


    val VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private val viewModel: WelcomeViewModel by lazy {
        ViewModelProvider(this.activity!!, WelcomeViewModel.Factory(requireActivity().application))
            .get(WelcomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind: FragmentSignInBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        val navController = Navigation.findNavController(this.requireActivity(), R.id.nav_host_fragment)

        bind.noAcct.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        bind.btnSignIn.setOnClickListener {
            signIn(bind)
        }

        viewModel.stateMsgSignIn.observe(this, Observer {
            if(it.code in 200..300){
                navController.navigate(R.id.action_signInFragment_to_mainActivity)
            }

            Toast.makeText(this.requireContext(), it.msg, Toast.LENGTH_LONG).show()
        })

        return bind.root
    }

    private fun signIn(bind: FragmentSignInBinding) {
        val email = bind.tvInputEmail.editText?.text.toString()
        val password = bind.textInputLayout.editText?.text.toString()

        val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email)

        if (!matcher.find()) {
            bind.tvInputEmail.error = "Invalid Email address"
            return
        }else{
            bind.tvInputEmail.error = ""
        }

        if (password.isEmpty()) {
            bind.textInputLayout.error = "Invalid"
            return
        }else{
            bind.textInputLayout.error = ""
        }

        viewModel.signIn(email, password)
    }


}
