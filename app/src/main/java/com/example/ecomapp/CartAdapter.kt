package com.example.ecomapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(private val cartItems: MutableList<Map<*, *>>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.txtCartItemName.text = "Name: " + cartItem["productname"].toString()
        holder.txtCartItemPrice.text = "Amount: " + cartItem["productamount"].toString() + ".Rs"
        // You can add more fields if needed

        Glide.with(holder.itemView.context)
            .load(cartItem["downloadUri"].toString())
            .into(holder.imageViewProduct)

        holder.delete.setOnClickListener {
            deleteCartItem(position)
        }
      
    }

    private fun deleteCartItem(position: Int) {

        val productId = cartItems[position]["key"].toString()
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId).child("like")
        database.setValue(0)

                cartItems.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewCartItem)
//        var txtProductName: TextView = itemView.findViewById(R.id.txtCartItemName)
//        var txtProductPrice: TextView = itemView.findViewById(R.id.txtCartItemPrice)
        var txtCartItemName: TextView = itemView.findViewById(R.id.txtCartItemName)
        var txtCartItemPrice: TextView = itemView.findViewById(R.id.txtCartItemPrice)
         var delete: ImageView = itemView.findViewById(R.id.delete)

    }
}


