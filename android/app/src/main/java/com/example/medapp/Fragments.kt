package com.example.medapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.medapp.Page1Activity
import com.example.medapp.R
import com.example.medapp.SecondActivity
import com.example.medapp.databinding.*
import com.example.medapp.databinding.ActivityMainBinding.bind
import java.util.ArrayList


//class MyDialogFragment : DialogFragment() {
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(requireContext()).apply{
 //           setTitle("Dialog Title")
 //           setPositiveButton("Ok"){ dialog, id->println("Ok")}
//        }.create()
//    }
//}
class HomeFragment : Fragment(R.layout.fragment_one){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentOneBinding.bind(view)

        binding.button1.setOnClickListener() {
            val intent = Intent(getActivity(), SecondActivity::class.java)
            startActivity(intent)
        }

    }
}

class Page1Fragment : Fragment(R.layout.fragment_three){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val binding = FragmentThreeBinding.bind(view)

            val intent = Intent(getActivity(), Page1Activity::class.java)
            startActivity(intent)


    }


}

class Page2Fragment : Fragment(R.layout.fragment_three){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentThreeBinding.bind(view)

            val intent = Intent(getActivity(), AlarmActivity::class.java)
            startActivity(intent)
    }
}

class EndFragment : Fragment(R.layout.fragment_four){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentFourBinding.bind(view)

    }
}





