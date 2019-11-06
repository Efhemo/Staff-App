package com.efhem.mystaff.ui.welcome


import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.efhem.mystaff.databinding.FragmentSignUpBinding
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

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

        val bind: FragmentSignUpBinding =
            DataBindingUtil.inflate(inflater, com.efhem.mystaff.R.layout.fragment_sign_up, container, false)

        val navControlller = Navigation.findNavController(this.requireActivity(), com.efhem.mystaff.R.id.nav_host_fragment)

        val string = SpannableStringBuilder(resources.getString(com.efhem.mystaff.R.string.all_done))
        string.setSpan(
            ForegroundColorSpan(Color.RED),
            0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(UnderlineSpan(), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        bind.spanTv.text = string

        viewModel.stateMsg.observe(this, Observer {

            if(it.code  in 200..300){
                navControlller.navigate(com.efhem.mystaff.R.id.action_signUpFragment_to_signInFragment)
            }

            Toast.makeText(this.requireContext(), it.msg, Toast.LENGTH_LONG).show()

        })

        bind.btnSearch.setOnClickListener {
             signUp(bind)
        }

        return bind.root

    }

    private fun signUp(bind: FragmentSignUpBinding): Unit {
        val fisrtName = bind.wrapperFirstName.editText?.text.toString()
        val lastName = bind.wrapperLastName.editText?.text.toString()
        val pob = bind.wrapperPlaceOfBirth.editText?.text.toString()
        val dob = bind.edDob.text.toString()
        val email = bind.wrapperEmailAddress.editText?.text.toString()
        val password = bind.wrapperPassword.editText?.text.toString()

        if (fisrtName.isEmpty()) {
            bind.wrapperFirstName.error = "Invalid"
            return
        }else{
            bind.wrapperFirstName.error = ""
        }

        if (lastName.isEmpty()) {
            bind.wrapperLastName.error = "Invalid"
            return
        }else{
            bind.wrapperLastName.error = ""
        }

        if (pob.isEmpty()) {
            bind.wrapperPlaceOfBirth.error = "Invalid"
            return
        }else{
            bind.wrapperPlaceOfBirth.error = ""
        }

        if (dob.isEmpty()) {
            Toast.makeText(this.requireContext(), "Date of birth is required", Toast.LENGTH_LONG).show()
            return
        }

        val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email)

        if (!matcher.find()) {
            bind.wrapperEmailAddress.error = "Invalid Email address"
            return
        }else{
            bind.wrapperEmailAddress.error = ""
        }

        if (password.isEmpty()) {
            bind.wrapperPlaceOfBirth.error = "Invalid"
            return
        }else{
            bind.wrapperPlaceOfBirth.error = ""
        }

        viewModel.signUp("$fisrtName $lastName", pob, dob, email, password)
    }


}
