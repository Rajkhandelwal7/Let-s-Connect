package com.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsappclone.Fragments.ChatsFragment
import com.example.whatsappclone.Fragments.SearchFragment
import com.example.whatsappclone.Fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.widget.Toolbar
import com.example.whatsappclone.ModelClasses.Chat
import com.example.whatsappclone.ModelClasses.ChatList
import com.example.whatsappclone.ModelClasses.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    var refusers:DatabaseReference?=null
    var firebaseUser:FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarmain)

        firebaseUser=FirebaseAuth.getInstance().currentUser
        refusers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)


        val toolbar:Toolbar=findViewById(R.id.toolbarmain)
        setSupportActionBar(toolbar)
        supportActionBar!!.title=""

//        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
//        viewPagerAdapter.addfragments(ChatsFragment(),"Chats")
//        viewPagerAdapter.addfragments(SearchFragment(),"Search")
//        viewPagerAdapter.addfragments(SettingsFragment(),"Settings")
//
//
//        viewpager.adapter=viewPagerAdapter
//        tab_layout.setupWithViewPager(viewpager)

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
                var countunreadmessages=0
                for(datasnapshot in p0.children)
                {
                    val chat=datasnapshot.getValue(Chat::class.java)
                    if(chat!!.getReceiver().equals(firebaseUser!!.uid)&& !chat.isIsSeen())
                    {
                        countunreadmessages=countunreadmessages+1
                    }
                }
                if(countunreadmessages==0)
                {
                    viewPagerAdapter.addfragments(ChatsFragment(),"Chats")
                }
                else{
                    viewPagerAdapter.addfragments(ChatsFragment(),"($countunreadmessages)Chats")

                }
                viewPagerAdapter.addfragments(SearchFragment(),"Search")
                viewPagerAdapter.addfragments(SettingsFragment(),"Settings")
                viewpager.adapter=viewPagerAdapter
                tab_layout.setupWithViewPager(viewpager)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        //display the username and profile prixture
        refusers!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val user: Users?=p0.getValue(Users::class.java)
                    user_name.text=user!!.getusername()

                    Picasso.get().load(user.getprofile()).placeholder(R.drawable.profile).into(profile_image)

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }


        }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_xml,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_logut->
            {
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this@MainActivity,WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
                startActivity(intent)
                finish()
                return  true
            }

        }
        return false
    }
    internal  class ViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager){
        private val fragments:ArrayList<Fragment>
        private val titles:ArrayList<String>
        init {
            fragments=ArrayList<Fragment>()
            titles=ArrayList<String>()



        }

        override fun getCount(): Int {
            return fragments.size

        }

        override fun getItem(position: Int): Fragment {

            return fragments[position]
        }
        fun addfragments(fragment: Fragment,title:String)
        {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

    }
    private fun updateStatus(status:String)
    {
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        val hashmap=HashMap<String,Any>()
        hashmap["status"]=status
        ref.updateChildren(hashmap)
    }

    override fun onResume() {
        super.onResume()
        updateStatus("online")
    }

    override fun onPause() {
        super.onPause()
        updateStatus("offline")
    }
}