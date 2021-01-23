package com.example.whatsappclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.AdapterClasses.UserAdapter
import com.example.whatsappclone.ModelClasses.ChatList
import com.example.whatsappclone.ModelClasses.Users
import com.example.whatsappclone.Notifications.Token
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

class ChatsFragment : Fragment()
{


    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var usersChatList: List<ChatList>? = null
    lateinit var recycler_view_chatlist: RecyclerView
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        recycler_view_chatlist = view.findViewById(R.id.recycler_view_chat_list)
        recycler_view_chatlist.setHasFixedSize(true)
        recycler_view_chatlist.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        usersChatList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                (usersChatList as ArrayList).clear()

                for (dataSnapshot in p0.children)
                {
                    val chatList = dataSnapshot.getValue(ChatList::class.java)

                    (usersChatList as ArrayList).add(chatList!!)
                }
                retrieveChatList()
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }

        })
        updateToken(FirebaseInstanceId.getInstance().token)


        return view
    }

    private fun updateToken(token: String?) {
            val ref=FirebaseDatabase.getInstance().reference.child("Tokens")
            val token1=Token(token!!)
        ref.child(firebaseUser!!.uid).setValue(token1)

    }

    private  fun retrieveChatList()
    {
        mUsers = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList).clear()

                for (datasnopshot in p0.children)
                {
                    val user = datasnopshot.getValue(Users::class.java)

                    for (eachChatList in usersChatList!!)
                    {
                        if (user!!.getuid().equals(eachChatList.getid()))
                        {
                            (mUsers as ArrayList).add(user!!)
                        }
                    }
                }

                userAdapter = UserAdapter(context!!, (mUsers as ArrayList<Users>), true)
                recycler_view_chatlist.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })
    }



}


/*
class ChatsFragment : Fragment() {
    private var userAdapter: UserAdapter?=null
    private var mUsers:List<Users>?=null

    private var UserschatList:List<ChatList>?=null
    lateinit var recycler_view_chat_list:RecyclerView
    private var firebaseUsers:FirebaseUser?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_chats, container, false)
        recycler_view_chat_list=view.findViewById(R.id.recycler_view_chat_list)
        recycler_view_chat_list.setHasFixedSize(true)
        recycler_view_chat_list.layoutManager=LinearLayoutManager(context)
        firebaseUsers=FirebaseAuth.getInstance().currentUser
        UserschatList=ArrayList()
        val ref=FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseUsers!!.uid)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (UserschatList as ArrayList).clear()
                for(datasnapshot in p0.children)
                {
                    val chatlist=datasnapshot.getValue(ChatList::class.java)
                    (UserschatList as ArrayList).add(chatlist!!)
                }
                retrievechatList()


            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        return view
    }


    private fun retrievechatList(){
        mUsers=ArrayList()
        val ref=FirebaseDatabase.getInstance().reference.child("Users")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList).clear()
                for(datasnapshot in p0.children)
                {
                    val user=datasnapshot.getValue(Users::class.java)
                    for(eachchatList in UserschatList!!)
                    {
                        if(user!!.getuid().equals(eachchatList.getid()))
                        {
                            (mUsers as ArrayList).add(user!!)
                        }
                    }
                }
                userAdapter= UserAdapter(context!!,(mUsers as ArrayList<Users>),true)
                recycler_view_chat_list.adapter=userAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}*/
