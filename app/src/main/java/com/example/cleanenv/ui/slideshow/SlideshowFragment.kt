package com.example.cleanenv.ui.slideshow

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanenv.SplashScreenAndLogin.Login
import com.example.cleanenv.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {
    val prefname = "myPref"
    private var _binding: FragmentSlideshowBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        sharedPreferences = this.requireActivity().getSharedPreferences(prefname, Context.MODE_PRIVATE)
        val editer = sharedPreferences.edit()
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: Button = binding.PlacingOrder
//        textView.setOnClickListener{
//            editer.apply() {
//                putBoolean("loggedin", false)
//                putString("phone", "")
//                apply()
//            }
//            val intent = Intent(context,Login::class.java)
//            startActivity(intent)
//        }
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}