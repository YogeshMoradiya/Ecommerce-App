package com.example.ecomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Cartpage : AppCompatActivity() {

       lateinit var cartrecyclerView: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartpage)

        cartrecyclerView = findViewById(R.id.cartrecyclerView)

//        val cartItems: MutableList<Map<*, *>> = mutableListOf()
//
//        val adapter = CartAdapter(cartItems)
//        cartrecyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference

        var likellist= mutableListOf<Map<*,*>>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartItems: MutableList<Map<*, *>> = mutableListOf()
                val products = dataSnapshot.child("products").children
                val mapfinal = mutableListOf<Map<*, *>>()
                products.forEach { productSnapshot ->
                    val product = productSnapshot.value as Map<*, *>
                    mapfinal.add(product)
                }

                for(i in 0 until mapfinal.size) {
                    val product = mapfinal[i]
                    var a:Long=1
                    if(product["like"] as Long==a){
                        cartItems.add(mapfinal[i])
                    }

                }
                val adapter = CartAdapter(cartItems)
                cartrecyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("====", "loadPost:onCancelled", databaseError.toException())
                finish()
            }
        }
        database.addValueEventListener(postListener)
    }
}

