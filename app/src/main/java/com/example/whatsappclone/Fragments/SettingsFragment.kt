package com.example.whatsappclone.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.whatsappclone.ModelClasses.Users
import com.example.whatsappclone.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.user_search_item_layout.*


class SettingsFragment : Fragment() {
    var usersref:DatabaseReference?=null
    var firebaseUser:FirebaseUser?=null
    private var Requestcode=438
    private var imageuri:Uri?=null
    private var storageReference:StorageReference?=null
    private var coverchecker:String?=null
    private var socialchecker:String?=null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view= inflater.inflate(R.layout.fragment_settings, container, false)
        firebaseUser=FirebaseAuth.getInstance().currentUser
        usersref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageReference=FirebaseStorage.getInstance().reference.child("User Images")
        usersref!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    if (context != null) {
                        val user: Users? = p0.getValue(Users::class.java)
                        view.username_settings.text = user!!.getusername()
                        Picasso.get().load(user.getprofile()).into(view.profile_image_settings)
                        Picasso.get().load(user.getcover()).into(view.cover_image_settings)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        view.profile_image_settings.setOnClickListener{
            pickimage()
        }
        view.cover_image_settings.setOnClickListener{
            coverchecker="cover"
            pickimage()
        }
        view.set_facebook.setOnClickListener{
            socialchecker="facebook"
            setsocialLinks()
        }
        view.set_instagram.setOnClickListener{
            socialchecker="instagram"
            setsocialLinks()
        }
        view.set_website.setOnClickListener{
            socialchecker="website"
            setsocialLinks()
        }


        return view

    }

    private fun setsocialLinks() {
        val builder:AlertDialog.Builder=AlertDialog.Builder(context!!,R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        if(socialchecker=="website")
        {

            builder.setTitle("Write Url")


        }
        else{
            builder.setTitle("Write username")

        }
        val edittext=EditText(context)
        if(socialchecker=="website")
        {
            edittext.hint="e.g www.goole.com"
            edittext.setHintTextColor(resources.getColor(R.color.black))
            edittext.setTextColor(resources.getColor(R.color.black))


        }
        else{
            edittext.hint="e.g RajKhandelwal7"
            edittext.setHintTextColor(resources.getColor(R.color.black))
            edittext.setTextColor(resources.getColor(R.color.black))


        }
        builder.setView(edittext)
        builder.setPositiveButton("Create",DialogInterface.OnClickListener{
            dialog, which ->
            val str=edittext.text.toString()
            if(str=="")
            {
                Toast.makeText(context,"Please Write Something...",Toast.LENGTH_SHORT).show()
            }
            else{
                savesocialLinks(str)
            }
        })
        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{
            dialog, which ->
            dialog.cancel()
            
        })
        builder.show()


    }

    private fun savesocialLinks(str: String) {
        val mapsocial=HashMap<String,Any>()
//        mapsocial["cover"]=url
//        usersref!!.updateChildren(mapsocial)

        when(socialchecker)
        {
            "facebook"->{
                mapsocial["facebook"]="https://m.facebook.com/$str"
            }
            "instagram"->{
                mapsocial["instagram"]="https://m.instagram.com/$str"

            }
            "website"->{
                mapsocial["website"]="https://$str"
            }
        }
               usersref!!.updateChildren(mapsocial).addOnCompleteListener { task->
                   if(task.isSuccessful)
                   {
                       Toast.makeText(context,"Updates Sucessfully", Toast.LENGTH_SHORT).show()
                   }
               }

    }

    private fun pickimage() {
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,Requestcode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==Requestcode && resultCode == Activity.RESULT_OK && data!!.data!=null)
        {
            imageuri=data.data
            Toast.makeText(context,"Uploading",Toast.LENGTH_SHORT).show()
            uploadimagetodatabase()
        }
    }

    private fun uploadimagetodatabase() {
        val progressbar=ProgressDialog(context)
        progressbar.setMessage("image is uploading please wait...")
        progressbar.show()
        if(imageuri!=null)
        {
            val fileref=storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadtask:StorageTask<*>
             uploadtask=fileref.putFile(imageuri!!)
            uploadtask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>> { task->
                if(!task.isSuccessful)
                {
                    task.exception.let {
                        throw it!!

                    }

                }
                return@Continuation fileref.downloadUrl

            }).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    val downloadurl=task.result
                    val url=downloadurl.toString()
                    if(coverchecker=="cover")
                    {
                        val mapcoverimg=HashMap<String,Any>()
                        mapcoverimg["cover"]=url
                        usersref!!.updateChildren(mapcoverimg)
                        coverchecker=""
                    }
                    else{
                        val mapprofileimg=HashMap<String,Any>()
                        mapprofileimg["profile"]=url
                        usersref!!.updateChildren(mapprofileimg)
                        coverchecker=""

                    }
                    progressbar.dismiss()

                }
            }

        }
    }


}