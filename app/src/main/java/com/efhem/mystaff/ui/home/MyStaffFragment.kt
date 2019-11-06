package com.efhem.mystaff.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

import com.efhem.mystaff.R
import com.efhem.mystaff.adapter.StaffsAdapter
import com.efhem.mystaff.ui.welcome.WelcomeViewModel

/**
 * A simple [Fragment] subclass.
 */
class MyStaffFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this.activity!!, MainViewModel.Factory(requireActivity().application))
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_my_staff, container, false)

        val rc: RecyclerView = view.findViewById(R.id.rc_staffs)
        val adapter= StaffsAdapter()
        rc.adapter = adapter

        viewModel.staffs.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return view.rootView
    }


}
