package com.example.cleanenv.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanenv.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    val prefname = "Home"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editer : SharedPreferences.Editor
    private var _binding: FragmentHomeBinding? = null
    var paper = 0
    var bottle = 0
    var canMetal = 0
    var glassWine = 0
    var cloths = 0
    var OthersTrush = 0
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = this.requireContext().getSharedPreferences(prefname, Context.MODE_PRIVATE)
        editer = sharedPreferences.edit()

//        val crashButton = Button(this)
//        crashButton.text = "Test Crash"
//        crashButton.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }

        binding.paper.setOnClickListener{
            paper=1
            binding.ClickedPaper.visibility = View.VISIBLE
            binding.paper.visibility = View.GONE
        }
        binding.ClickedPaper.setOnClickListener{
            paper=0
            binding.ClickedPaper.visibility = View.GONE
            binding.paper.visibility = View.VISIBLE
        }
        binding.bottle.setOnClickListener{
            bottle=1
            binding.ClickedBottle.visibility = View.VISIBLE
            binding.bottle.visibility = View.GONE
        }
        binding.ClickedBottle.setOnClickListener{
            bottle=0
            binding.ClickedBottle.visibility = View.GONE
            binding.bottle.visibility = View.VISIBLE
        }
        binding.metleBotle.setOnClickListener{
            canMetal=1
            binding.ClickedMetalBottle.visibility = View.VISIBLE
            binding.metleBotle.visibility = View.GONE
        }
        binding.ClickedMetalBottle.setOnClickListener{
            canMetal=0
            binding.ClickedMetalBottle.visibility = View.GONE
            binding.metleBotle.visibility = View.VISIBLE
        }
        binding.Glass.setOnClickListener{
            glassWine=1
            binding.ClickedGlass.visibility = View.VISIBLE
            binding.Glass.visibility = View.GONE
        }
        binding.ClickedGlass.setOnClickListener{
            glassWine=0
            binding.ClickedGlass.visibility = View.GONE
            binding.Glass.visibility = View.VISIBLE
        }
        binding.cloths.setOnClickListener{
            cloths=1
            binding.ClikedCloths.visibility = View.VISIBLE
            binding.cloths.visibility = View.GONE
        }
        binding.ClikedCloths.setOnClickListener{
            cloths=0
            binding.ClikedCloths.visibility = View.GONE
            binding.cloths.visibility = View.VISIBLE
        }
        binding.Others.setOnClickListener{
            OthersTrush=1
            binding.ClickedOthers.visibility = View.VISIBLE
            binding.Others.visibility = View.GONE
        }
        binding.ClickedOthers.setOnClickListener{
            OthersTrush=0
            binding.ClickedOthers.visibility = View.GONE
            binding.Others.visibility = View.VISIBLE
        }
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding.button.setOnClickListener{
            val intent = Intent(context,addressDetails::class.java)
            intent.putExtra("paper",paper)
            intent.putExtra("bottle",bottle)
            intent.putExtra("canMetal",canMetal)
            intent.putExtra("glassWine",glassWine)
            intent.putExtra("cloths",cloths)
            intent.putExtra("OthersTrush",OthersTrush)
            startActivity(intent)
//            Toast.makeText(context, "${paper} + ${bottle} + ${canMetal} + ${glassWine} + ${cloths} + ${OthersTrush}", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        paper = 0
        bottle = 0
        canMetal = 0
        glassWine = 0
        cloths = 0
        OthersTrush = 0
    }


}