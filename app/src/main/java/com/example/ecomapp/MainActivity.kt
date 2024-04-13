package com.example.ecomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    private lateinit var homerecyclerView: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        homerecyclerView = findViewById(R.id.homerecyclerView)
        database = FirebaseDatabase.getInstance().reference


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerlayout)
        navView = findViewById(R.id.nav_view)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // Get navigation header view
        val headerView = navView.getHeaderView(0)
// Find views in the navigation header view
        val imageView = headerView.findViewById<ImageView>(R.id.img)
        val nametextView = headerView.findViewById<TextView>(R.id.uname)
        val textView = headerView.findViewById<TextView>(R.id.uemail)
// Set user details
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            // When firebase user is not null, set image and name
            Glide.with(this).load(firebaseUser.photoUrl).into(imageView)
            nametextView.text = firebaseUser.displayName
            textView.text = firebaseUser.email
        }
        headerView.setOnClickListener {
            startActivity(Intent(this, Profilepage::class.java))
            drawerLayout.closeDrawers()
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        setupNavigation()

        var likellist= mutableListOf<Map<*,*>>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val products = dataSnapshot.child("products").children

                val mapfinal = mutableListOf<Map<*, *>>()
                products.forEach { productSnapshot ->
                    val product = productSnapshot.value as Map<*, *>
                    mapfinal.add(product)
                }
//                for(i in 0 until mapfinal.size) {
//                    val product = mapfinal[i]
//                    var a:Long=1
//                    if(product["like"] as Long==a){
//                        likellist.add(mapfinal[i])
//                    }
//
//                }
                val adapter = ProductAdapter(mapfinal)
                homerecyclerView.adapter = adapter

            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("====", "loadPost:onCancelled", databaseError.toException())
                finish()
            }
        }


        database.addValueEventListener(postListener)
    }

    private fun setupNavigation() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Handle Home click
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_product -> {
                    // Handle Profile click
                    val intent = Intent(this, Add_Product::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_cart -> {
                    // Handle Settings click
                    val intent = Intent(this, Cartpage::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Setting -> {
                    // Handle Settings click
                    val intent = Intent(this, Profilepage::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {
                    auth.signOut()
//                    val intent = Intent(this@MainActivity, Loginpage::class.java)
//                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}