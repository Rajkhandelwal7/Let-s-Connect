package com.example.whatsappclone.Fragments

import android.os.Bundle
import android.os.UserHandle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.AdapterClasses.UserAdapter
import com.example.whatsappclone.ModelClasses.Users
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null
    private var recyclerView:RecyclerView?=null
    private var searchEditText:EditText?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView=view.findViewById(R.id.searchlist)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager=LinearLayoutManager(context)
        searchEditText=view.findViewById(R.id.search_users_et)




        mUsers=ArrayList()
        retrieveAllUsers()
        searchEditText!!.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                searchforUsers(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return view
    }

    private fun retrieveAllUsers() {
        val firebaseUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val refusers= FirebaseDatabase.getInstance().reference.child("Users")
        refusers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchEditText!!.text.toString() == "") {
                    for (snapshot in p0.children) {
                        val user: Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getuid()).equals(firebaseUserID)) {
                            (mUsers as ArrayList<Users>).add(user)

                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers!!, false)
                    recyclerView!!.adapter = userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun searchforUsers(str:String)
    {
        val firebaseUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val queryusers= FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(str)
            .endAt(str + "\uf8ff")
        queryusers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(snapshot in p0.children)
                {
                    val user: Users?=snapshot.getValue(Users::class.java)
                    if(!(user!!.getuid()).equals(firebaseUserID))
                    {
                        (mUsers as ArrayList<Users>).add(user)

                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter=userAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


}