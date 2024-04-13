package com.example.ecomapp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.util.Locale


class ProductAdapter(var mapfinal: MutableList<Map<*, *>>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private val cartItems = mutableListOf<Map<*, *>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = list[position]

        val product = mapfinal[position]

        // holder.txtProductName.text = product["productname"].toString()
        holder.txtProductName.text = "Name : " + product["productname"].toString()
        holder.txtProductPrice.text = "Amount : " + product["productamount"].toString() + ".Rs"
        holder.description.text = "Desription : " + product["description"].toString()

        Glide.with(holder.itemView.context)
            .load(product["downloadUri"].toString())
            .into(holder.imageViewProduct)


        //Log.d("---=====", "onBindViewHolder: ${mapfinal.get("productname").toString()} ${mapfinal.get("productamount").toString()}")
        holder.btnAddToCart.setOnClickListener {

//            var database:DatabaseReference=Firebase.database.reference.child("products${product["key"]}/like")
//            database.setValue(1)
            val productId = product["key"].toString() // Assuming "key" is the product ID
            val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("products/$productId/like")
            database.setValue(1)

            Toast.makeText(holder.itemView.context, "Product added to cart successfully", Toast.LENGTH_SHORT).show()
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("downloadUri", product["downloadUri"].toString())
            intent.putExtra("description", product["description"].toString())
            intent.putExtra("productName", product["productname"].toString())
            intent.putExtra("productPrice", product["productamount"].toString())
            // Add more extras if needed
            holder.itemView.context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return mapfinal.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        var txtProductName: TextView = itemView.findViewById(R.id.txtproductname)
        var txtProductPrice: TextView = itemView.findViewById(R.id.txtproductprice)
        var description: TextView = itemView.findViewById(R.id.description)
        var btnAddToCart: Button = itemView.findViewById(R.id.btnaddcart)

    }

}