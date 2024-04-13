package com.example.ecomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class Editproduct : AppCompatActivity() {

    lateinit var editimage: ImageView
    lateinit var editname: EditText
    lateinit var editprice: EditText
    lateinit var editdes: EditText
    lateinit var editsave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editproduct)

        editimage = findViewById(R.id.editimage)
        editname = findViewById(R.id.editname)
        editprice = findViewById(R.id.editprice)
        editdes = findViewById(R.id.editdes)
        editsave = findViewById(R.id.editsave)


        val downloadUri = intent.getStringExtra("downloadUri")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val description = intent.getStringExtra("description")

        editname.setText(productName)
        editprice.setText(productPrice)
        editdes.setText(description)

        Glide.with(this)
            .load(downloadUri)
            .centerCrop()
            .into(editimage)

        editimage.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        editsave.setOnClickListener {

//            val updatedName = productName.toString()
//            val updatedPrice = productPrice.toString()
//            val updatedDescription = description.toString()
//
//            val productId = intent.getStringExtra("proucts")
//
//            val database = FirebaseDatabase.getInstance()
//            val productRef = productId?.let { database.getReference("products").child(it) }
////            val updatedProduct = Mydata(
////                downloadUri.toString(),
////                updatedName,
////                updatedPrice,
////                updatedDescription,
////                myRef.key
////            )
//           // productRef?.setValue(updatedProduct)
//                ?.addOnSuccessListener {
//                    Toast.makeText(this@Editproduct, "Product details updated", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//                ?.addOnFailureListener {
//                    Toast.makeText(this@Editproduct, "Failed to update product details", Toast.LENGTH_SHORT).show()
//                }
        }
    }
}
