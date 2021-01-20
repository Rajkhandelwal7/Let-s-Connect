package com.example.whatsappclone.AdapterClasses

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.ModelClasses.Chat
import com.example.whatsappclone.ModelClasses.MessageChatActivity
import com.example.whatsappclone.ModelClasses.Users
import com.example.whatsappclone.R
import com.example.whatsappclone.VisitUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class UserAdapter(mcontext:Context,muserslist:List<Users>,ischatcheck:Boolean):RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    private val mcontext:Context
    private val muserslist:List<Users>
    private val ischatcheck:Boolean
    var lastmsg:String=""
    init {
        this.mcontext=mcontext
        this.muserslist=muserslist
        this.ischatcheck=ischatcheck
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        var userNameTxt:TextView
        var profileimageView:CircleImageView
        var onlinestatus:CircleImageView
        var offlinestatus:CircleImageView
        var lastmessagetxt:TextView
        init {
            userNameTxt=itemView.findViewById(R.id.username)
            profileimageView=itemView.findViewById(R.id.profile_image)
            onlinestatus=itemView.findViewById(R.id.image_online)
            offlinestatus=itemView.findViewById(R.id.image_offline)
            lastmessagetxt=itemView.findViewById(R.id.msg_last)

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_search_item_layout,parent,false)
        return UserAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users=muserslist[position]
        holder.userNameTxt.text=user!!.getusername()
        Picasso.get().load(user.getprofile()).placeholder(R.drawable.profile).into(holder.profileimageView)
        if(ischatcheck==true)
        {
            retrievelastMessage(user.getuid(),holder.lastmessagetxt)
        }
        else{
            holder.lastmessagetxt.visibility=View.GONE
        }
        if(ischatcheck==true)
        {
            if(user.getstatus()=="online")
            {
                holder.onlinestatus.visibility=View.VISIBLE
                holder.offlinestatus.visibility=View.GONE
            }
            else{
                holder.onlinestatus.visibility=View.GONE
                holder.offlinestatus.visibility=View.VISIBLE

            }
        }
        else{
            holder.onlinestatus.visibility=View.GONE
            holder.offlinestatus.visibility=View.GONE

        }
        holder.itemView.setOnClickListener{
            val options= arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder:AlertDialog.Builder=AlertDialog.Builder(mcontext)
            builder.setTitle("What do You want?")
            builder.setItems(options,DialogInterface.OnClickListener{dialog, position ->
                if(position==0)
                {
                    val intent= Intent(mcontext, MessageChatActivity::class.java)
                    intent.putExtra("visit_id",user.getuid())
                    mcontext.startActivity(intent)



                }
                else if(position==1){
                    val intent= Intent(mcontext, VisitUserProfile::class.java)
                    intent.putExtra("visit_id",user.getuid())
                    mcontext.startActivity(intent)

                }

            })
            builder.show()
        }

    }

    private fun retrievelastMessage(chatUserid: String?, lastmessagetxt: TextView) {
        lastmsg="DefaultMsg"
        val firebaseUser=FirebaseAuth.getInstance().currentUser
        val refrence=FirebaseDatabase.getInstance().reference.child("Chats")
        refrence.addValueEventListener(object:ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot in p0.children)
                {
                    val chat:Chat?=datasnapshot.getValue(Chat::class.java)
                    if(firebaseUser!=null && chat!=null)

                    {

                        if(chat.getReceiver()==firebaseUser!!.uid && chat .getSender()==chatUserid ||chat.getReceiver()==chatUserid
                            &&chat.getSender()==firebaseUser!!.uid)
                        {
                            lastmsg=chat.getMessage()!!
                        }
                    }
                }
                when(lastmsg)
                {
                    "DefaultMsg"->lastmessagetxt.text="No Messages"
                    "sent you an image."->lastmessagetxt.text="sent an Image "
                    else->lastmessagetxt.text=lastmsg


                }
                lastmsg="DefaultMsg"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }

    override fun getItemCount(): Int {
        return muserslist.size

    }
}