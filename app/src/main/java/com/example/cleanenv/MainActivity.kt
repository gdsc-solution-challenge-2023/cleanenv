package com.example.cleanenv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.cleanenv.SplashScreenAndLogin.both
import com.example.cleanenv.Utils.Registrationeed
import com.example.cleanenv.databinding.ActivityMainBinding
import com.example.cleanenv.ui.slideshow.SlideshowFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    val prefname = "myPref"
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var databasemailtoph: DatabaseReference
    private lateinit var auth : FirebaseAuth
    val help = Registrationeed()
    lateinit var email : String
    lateinit var name : String
    lateinit var header: View
    lateinit var userName : TextView
    lateinit var userEmail: TextView
    lateinit var phone : String
    lateinit var editer : SharedPreferences.Editor
    var pic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        editer = sharedPreferences.edit()
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        header = navView.getHeaderView(0)
        userName = header.findViewById<TextView>(R.id.UserName)
        userEmail = header.findViewById<TextView>(R.id.UserEmail)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/")
        databasemailtoph = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/").child("EmailToPhone")
        phone = intent.getStringExtra("phone").toString()
        val bundle = Bundle()
        bundle.putString("phone", phone)
        val mapFragment = SlideshowFragment()
        mapFragment.setArguments(bundle)






        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_aboutUs,R.id.nav_contactUs,R.id.nav_trackOrder
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun picLoading() {
        Glide.with(this)
            .asBitmap()
            .apply(RequestOptions().override(145, 145))
            .load(pic)
            .placeholder(R.drawable.profile_pic_dummy)
            .error(R.drawable.profile_pic_dummy)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(header.findViewById(R.id.imageView));
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val  mRefresh = menu.findItem(R.id.action_logout)
        mRefresh.setOnMenuItemClickListener {
            editer.apply() {
                putBoolean("loggedin", false)
                putString("phone", "")
                apply()
            }
            val intent = Intent(this, both::class.java)
            startActivity(intent)
            finish()
            return@setOnMenuItemClickListener true
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        database.child("users").child(phone.toString()).get().addOnSuccessListener(){
            email = it.child("email").value.toString()
            name = it.child("name").value.toString()
            pic = it.child("pic").value.toString()
            userEmail.text = email
            userName.text = name
            if(intent.getBooleanExtra("commingFromRegister",false)==true){
                email?.let { it2 ->
                    val temp = help.mailDotNotTakingProblemSolved(it2)
                    databasemailtoph.child(temp).setValue(phone)
                        .addOnSuccessListener {
                            Toast.makeText(this, "welcome "+ name, Toast.LENGTH_LONG).show()
                            Toast.makeText(
                                this,
                                "successfully Registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Network Problem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    intent.putExtra("commingFromRegister",false)
                }
            }
            picLoading()
            if(sharedPreferences.getBoolean("fst",false))Toast.makeText(this, "welcome "+ name, Toast.LENGTH_LONG).show()
            editer.apply() {
                putBoolean("fst", false)
                apply()
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
            Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT).show()
        }
    }
}
