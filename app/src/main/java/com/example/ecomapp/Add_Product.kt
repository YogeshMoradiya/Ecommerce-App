package com.example.ecomapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream

class Add_Product : AppCompatActivity() {

    private var resultUri1 : Uri?=null
    lateinit var productimage: ImageView
    lateinit var productname: TextInputEditText
    lateinit var productamount:TextInputEditText
    lateinit var description:TextInputEditText
    lateinit var productsave: Button
    var storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)


        productimage = findViewById(R.id.productimage)
        productname = findViewById(R.id.productname)
        productamount = findViewById(R.id.productamount)
        description = findViewById(R.id.adddescription)
        productsave = findViewById(R.id.productsave)


        productimage.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }


        productsave.setOnClickListener {
            productimage.isDrawingCacheEnabled = true
            productimage.buildDrawingCache()
            val bitmap = (productimage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val storageRef = storage.reference
            val mountainImagesRef = storageRef.child("product/img${Math.random()}.jpg")
// While the file names are the same, the references point to different files
            mountainImagesRef.name == mountainImagesRef.name // true
            mountainImagesRef.path == mountainImagesRef.path // false
            var uploadTask =mountainImagesRef .putBytes(data)


            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                mountainImagesRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    productimage.setImageURI(downloadUri)

                    val database = Firebase.database
                    val myRef = database.getReference("products").push()
                    var daata=Mydata(downloadUri.toString(),productname.text.toString(),productamount.text.toString(),description.text.toString(),myRef.key)
                    myRef.setValue(daata)
                    Toast.makeText(this@Add_Product, "Product added successfully", Toast.LENGTH_SHORT).show()

                    // Redirect to homepage
                    val intent = Intent(this@Add_Product, MainActivity::class.java)
                    startActivity(intent)
                    finish()

//                    val des = Intent(this@Add_Product, ProductDetailsActivity::class.java)
//                    intent.putExtra("description",description.toString())
//                    startActivity(des)
//                    finish()
                } else {
                    // Handle failures
                    // ...
                }
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                resultUri1 = result.uri
                productimage.setImageURI(resultUri1)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}