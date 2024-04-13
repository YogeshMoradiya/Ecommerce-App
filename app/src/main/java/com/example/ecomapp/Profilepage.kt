package com.example.ecomapp

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage

class Profilepage : AppCompatActivity() {

    lateinit var ivImage: ImageView
    lateinit var tvName: TextView
    lateinit var tv_email: TextView
    lateinit var btLogout: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)


        // Assign variable
        ivImage = findViewById(R.id.iv_image)
        tvName = findViewById(R.id.tv_name)
        tv_email = findViewById(R.id.tv_email)
        btLogout = findViewById(R.id.bt_logout)



        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize firebase user
        val firebaseUser = firebaseAuth.currentUser

        // Check condition
        if (firebaseUser != null) {
            // When firebase user is not equal to null set image on image view
            Glide.with(this@Profilepage).load(firebaseUser.photoUrl).into(ivImage)
            // set name on text view
            tvName.text = firebaseUser.displayName
            tv_email.text = firebaseUser.email
        }

        ivImage.setOnClickListener {
            // Start image cropping activity
            CropImage.activity()
                .start(this)
        }


        // Initialize sign in client
        googleSignInClient =
            GoogleSignIn.getClient(this@Profilepage, GoogleSignInOptions.DEFAULT_SIGN_IN)
        btLogout.setOnClickListener {
            // Sign out from google
            googleSignInClient.signOut().addOnCompleteListener { task ->
                // Check condition
                if (task.isSuccessful) {
                    // When task is successful sign out from firebase
                    firebaseAuth.signOut()
                    // Display Toast
                    Toast.makeText(applicationContext, "Logout successful", Toast.LENGTH_SHORT)
                        .show()
                    // Finish activity
                    finish()
                }
            }
        }
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImageUri = result.uri

                // Display the cropped image using Glide
                Glide.with(this@Profilepage).load(croppedImageUri).into(ivImage)
                // Upload the cropped image to Firebase Storage
                uploadImageToFirebase(croppedImageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                // Handle cropping error
                Toast.makeText(this@Profilepage, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to upload image to Firebase Storage
    private fun uploadImageToFirebase(imageUri: Uri) {
        val firebaseUser = firebaseAuth.currentUser
        val storageReference = FirebaseStorage.getInstance().reference
        val randomName = "${firebaseUser?.uid}_${System.currentTimeMillis()}.jpg"
        val imageRef = storageReference.child("users/${firebaseUser?.uid}/$randomName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                Toast.makeText(this@Profilepage, "Image uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle image upload failure
                Toast.makeText(this@Profilepage, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

