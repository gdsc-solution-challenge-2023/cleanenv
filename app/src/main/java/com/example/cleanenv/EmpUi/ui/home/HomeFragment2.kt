package com.example.cleanenv.EmpUi.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanenv.EmpUi.TrackOrder
import com.example.cleanenv.Utils.Registrationeed
import com.example.cleanenv.databinding.FragmentHome1Binding
import com.google.firebase.database.*

class HomeFragment2 : Fragment() {

    val checkPass = Registrationeed()
    val prefname = "Home"
    var select = ""
    var mail = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editer : SharedPreferences.Editor
    private lateinit var database: DatabaseReference
    private var _binding: FragmentHome1Binding? = null
    private lateinit var progrsDialog: ProgressDialog

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

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/").child("orders")
        sharedPreferences = this.requireContext().getSharedPreferences(prefname, Context.MODE_PRIVATE)
        mail = requireActivity().intent.getStringExtra("email")
            ?.let { checkPass.mailDotNotTakingProblemSolved(it) }.toString()
        editer = sharedPreferences.edit()

        _binding = FragmentHome1Binding.inflate(inflater, container, false)
        val root: View = binding.root
        progrsDialog = ProgressDialog(requireContext())
        progrsDialog.show()

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it  "unassigned"
//        }
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // TODO Auto-generated method stub
                for (ds in snapshot.children) {
                    val t = ds.child("emp").value.toString()
                    if(t=="unassigned" || t==requireActivity().intent.getStringExtra("email")) {
                        select = ds.key.toString()
                    }
                }
                if(select==""){
                    binding.haveOrder.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                    progrsDialog.dismiss()
                }
                else{
                    database.child(select).get().addOnSuccessListener {
                        binding.OrderId.text = select
                        binding.name.text = it.child("name").value.toString()
                        binding.address.text = it.child("address").value.toString()
                        binding.items.text = it.child("order").value.toString()
                        progrsDialog.dismiss()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "netprob", Toast.LENGTH_SHORT).show()
                        progrsDialog.dismiss()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                progrsDialog.dismiss()
                Toast.makeText(requireContext(), "netprob", Toast.LENGTH_SHORT).show()
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.click.setOnClickListener {
            if(select!=""){
                database.child(select).child("emp").setValue(requireActivity().intent.getStringExtra("email")).addOnSuccessListener {
                    Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "ddd", Toast.LENGTH_SHORT).show()
                }
            }else Toast.makeText(requireContext(), "no order", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(),TrackOrder::class.java)
            intent.putExtra("key",select)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}