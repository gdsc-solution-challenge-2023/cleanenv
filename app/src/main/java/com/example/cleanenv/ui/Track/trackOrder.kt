package com.example.cleanenv.ui.Track

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cleanenv.databinding.FragmentTrackOrderBinding
import com.example.cleanenv.ui.slideshow.SlideshowViewModel

class trackOrder : Fragment() {

    private var _binding: FragmentTrackOrderBinding? = null

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

        _binding = FragmentTrackOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.ttext
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