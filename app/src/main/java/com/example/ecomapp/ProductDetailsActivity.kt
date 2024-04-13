package com.example.ecomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var dimage:ImageView
    lateinit var dname:TextView
    lateinit var dprice:TextView
    lateinit var pdescription:TextView
    lateinit var dcart:Button
    lateinit var delete:Button
    lateinit var edit:Button

    lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        dimage=findViewById(R.id.dimage)
        dname=findViewById(R.id.dname)
        dprice=findViewById(R.id.dprice)
        pdescription=findViewById(R.id.pdescription)
        dcart=findViewById(R.id.dcart)
        delete=findViewById(R.id.delete)
        edit=findViewById(R.id.edit)

        val downloadUri = intent.getStringExtra("downloadUri")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val description = intent.getStringExtra("description")

        Glide.with(this)
            .load(downloadUri)
            .into(dimage)

        dname.text = "Product Name : $productName"
        dprice.text = "Amount : $productPrice"
        pdescription.text = "Description \n $description"

        dcart.setOnClickListener {
            var intent=Intent(this@ProductDetailsActivity,Cartpage::class.java)
            startActivity(intent)
            finish()
        }

        delete.setOnClickListener {
            deleteProductFromDatabase(productId)
        }
        edit.setOnClickListener {
           val intent=Intent(this,Editproduct::class.java)
            intent.putExtra("downloadUri", downloadUri)
            intent.putExtra("productName", productName)
            intent.putExtra("productPrice", productPrice)
            intent.putExtra("description", description)
            startActivity(intent)
        }
    }

    private fun deleteProductFromDatabase(productId: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("products").child(productId)
        ref.removeValue()
            .addOnSuccessListener {
                // Product deleted successfully
                // You can navigate back to the previous activity or update the UI here
                finish()
            }
            .addOnFailureListener {
                // Failed to delete product
                // Handle error if needed
            }
    }
}
