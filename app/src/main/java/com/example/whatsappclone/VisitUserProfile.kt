package com.example.whatsappclone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatsappclone.ModelClasses.MessageChatActivity
import com.example.whatsappclone.ModelClasses.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_visit_user_profile.*

class VisitUserProfile : AppCompatActivity() {
    private var uservisitid:String=""
     var user:Users?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_profile)
        uservisitid= intent.getStringExtra("visit_id")!!
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(uservisitid)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists())
                {
                     user=p0.getValue(Users::class.java)
                    username_dispaly.text=user!!.getusername()
                    Picasso.get().load(user!!.getprofile()).into(profile_display)
                    Picasso.get().load(user!!.getcover()).into(cover_display)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        facebook_displayy.setOnClickListener {
            val uri= Uri.parse(user!!.getfacebook())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        instagram_display.setOnClickListener {
            val uri= Uri.parse(user!!.getinstagram())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        website_display.setOnClickListener {
            val uri= Uri.parse(user!!.getwebsite())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        send_msg_btn.setOnClickListener{
            val intent= Intent(this, MessageChatActivity::class.java)
            intent.putExtra("visit_id",user!!.getuid())
            startActivity(intent)
        }

    }

}