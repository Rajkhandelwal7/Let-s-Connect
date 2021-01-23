package com.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_ragister.*


class RagisterActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var refUsers:DatabaseReference
    private  var firebaseUserID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ragister)
        val toolbar: Toolbar =findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent = Intent(this@RagisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth=FirebaseAuth.getInstance()

        btn_register.setOnClickListener{
            Toast.makeText(this,"Wait for 10 sec",Toast.LENGTH_LONG).show()

            registerUser()
        }
    }

    private fun registerUser() {
        val username:String=username_register.text.toString()
        val email:String=email_register.text.toString()
        val password:String=password_register.text.toString()

        if(username=="")
        {
            Toast.makeText(this@RagisterActivity,"Please Provide Username",Toast.LENGTH_SHORT).show()
        }
        else if(email==""){
            Toast.makeText(this@RagisterActivity,"Please Provide Email",Toast.LENGTH_SHORT).show()
        }
        else if(password=="")
        {
            Toast.makeText(this@RagisterActivity,"Please Provide Password",Toast.LENGTH_SHORT).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                if(task.isSuccessful)
                {


                    firebaseUserID= mAuth.currentUser!!.uid

                    refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                    val userhashmap=HashMap<String,Any>()
                    userhashmap["uid"]=firebaseUserID
                    userhashmap["username"]=username
                    userhashmap["profile"]="https://firebasestorage.googleapis.com/v0/b/whatsappclone-de21e.appspot.com/o/profile.png?alt=media&token=bbcf65f2-d2f2-4644-989d-497b3809df36"
                    userhashmap["cover"]="https://firebasestorage.googleapis.com/v0/b/whatsappclone-de21e.appspot.com/o/profile.png?alt=media&token=bbcf65f2-d2f2-4644-989d-497b3809df36"
                    userhashmap["status"]="offline"
                    userhashmap["search"]=username.toLowerCase()
                    userhashmap["facebook"]="https://m.facebook.com"
                    userhashmap["instagram"]="https://m.instagram.com"
                    userhashmap["website"]="https://www.google.com"
                    refUsers.updateChildren(userhashmap).addOnCompleteListener { task->
                        if(task.isSuccessful)
                        {
                            val intent=Intent(this@RagisterActivity,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
                            startActivity(intent)
                            finish()
                        }

                    }


                }
                else{
                    Toast.makeText(this@RagisterActivity,"Some Error Occured"+ task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                }

            }
        }

    }
}