
package com.example.whatsappclone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whatsappclone.Models.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profile.*

const val  KEY : String = "Users"
class ProfileActivity : AppCompatActivity() {

    val storage by lazy{
        FirebaseStorage.getInstance()
    }


    val auth by lazy{
        FirebaseAuth.getInstance()
    }


    val database by lazy{
        FirebaseFirestore.getInstance()
    }

    lateinit var downloadUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileiv.setOnClickListener {
            checkPermissionForImage()
        }

        profileNxtBtn.setOnClickListener {

            profileNxtBtn.isEnabled = false
            val name = profileName.text.toString()

            if(name.isEmpty()){
                Toast.makeText(this , "Please give your profile name" , Toast.LENGTH_SHORT).show()
            }
            else if(!::downloadUrl.isInitialized){
                Toast.makeText(this , "Please add a dp" , Toast.LENGTH_SHORT).show()
            }
            else{

                val user = User(name , downloadUrl , downloadUrl , auth.uid!!)
                database.collection(KEY)
                    .document(auth.uid!!).set(user).addOnSuccessListener {
                        startActivity(Intent(this , MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        profileNxtBtn.isEnabled = true 
                        Toast.makeText(this , it.toString() , Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


    private fun checkPermissionForImage() {

        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

             val permissionRead = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
             val permissionWrite = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            requestPermissions(
                permissionRead ,
                1001
            )


            requestPermissions(
                permissionWrite ,
                1002
            )
        }
        else{

            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(
            intent ,
            1000
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            data?.data?.let{
                profileiv.setImageURI(it)
                uploadImage(it)
            }
        }
        else{
            Toast.makeText(this , "Could not load the image" , Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(it: Uri) {
          profileNxtBtn.isEnabled =  false

          val ref = storage.reference.child("uploads/" + auth.uid.toString())
          val uploadTask = ref.putFile(it)

         uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot , Task<Uri>>{ task ->
             if(!task.isSuccessful){
                 task.exception?.let{
                     throw it
                 }
             }
             return@Continuation ref.downloadUrl
         })
             .addOnCompleteListener { task2 ->
                 profileNxtBtn.isEnabled = true
                 if(task2.isSuccessful){
                     downloadUrl =task2.result.toString()
                 }
                 else{
                     Toast.makeText(this , "Could not download the image" ,Toast.LENGTH_SHORT).show()
                 }
             }
             .addOnFailureListener{
                     Log.e("SHIVAM" , it.toString())
             }
    }
}