package com.example.whatsappclone.AdapterClasses
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.ModelClasses.Chat
import com.example.whatsappclone.R
import com.example.whatsappclone.ViewFullImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class   ChatsAdapter(
    mContext: Context,
    mChatList: List<Chat>,
    imageUrl: String
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>()
{

    private val mContext: Context
    private val mChatList: List<Chat>
    private var imageUrl: String = ""
    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mContext = mContext
        this.mChatList = mChatList
        this.imageUrl = imageUrl
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder
    {
        return if (position == 1)
        {
            val view: View = LayoutInflater.from(mContext).inflate(
                R.layout.message_item_right,
                parent,
                false
            )
            ViewHolder(view)
        }
        else
        {
            val view: View = LayoutInflater.from(mContext).inflate(
                R.layout.message_item_left,
                parent,
                false
            )
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat: Chat = mChatList[position]

        Picasso.get().load(imageUrl).into(holder.profile_image)

        if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals(""))
        {
            // image message --- right side
            if (chat.getSender().equals(firebaseUser!!.uid))
            {
                holder.show_text_message!!.visibility = View.GONE
                holder.right_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.right_image_view)
                holder.right_image_view!!.setOnClickListener {
                    val options= arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image",
                        "Cancel"

                    )
                    val builder:AlertDialog.Builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want ?")
                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(mContext, ViewFullImage::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext.startActivity(intent)

                        } else if (which == 1) {
                            deletesentmessage(position, holder)
                        }

                    })
                    builder.show()
                }

            }

            // image message --- left side
            else if (!chat.getSender().equals(firebaseUser!!.uid))
            {
                holder.show_text_message!!.visibility = View.GONE
                holder.left_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.left_image_view)
                holder.left_image_view!!.setOnClickListener {
                    val options= arrayOf<CharSequence>(
                        "View Full Image",
                        //"Delete Image",
                        "Cancel"

                    )
                    val builder:AlertDialog.Builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want ?")
                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(mContext, ViewFullImage::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext.startActivity(intent)

                        }

                    })
                    builder.show()
                }
            }
        }
        // text messages
        else
        {
            holder.show_text_message!!.text = chat.getMessage()
            if(firebaseUser!!.uid==chat.getSender())
            {
                holder.show_text_message!!.setOnClickListener {
                    val options= arrayOf<CharSequence>(

                        "Delete Message",
                        "Copy Text",
                        "Cancel"

                    )
                    val builder:AlertDialog.Builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want ?")
                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->

                        if (which == 0) {
                            deletesentmessage(position, holder)
                        } else if (which == 2) {
                            val textToCopy = holder.show_text_message!!.text
                            val clipboardManager =
                                mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("text", textToCopy)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(
                                holder.itemView.context,
                                "Text copied to clipboard",
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    })
                    builder.show()
                }
            }
            else if(firebaseUser!!.uid==chat.getReceiver())
            {
                holder.show_text_message!!.setOnClickListener {
                    val options= arrayOf<CharSequence>(
                        "Copy Text",
                        "Cancel"

                    )
                    val builder:AlertDialog.Builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want ?")
                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->

                        if (which == 0) {
                            val textToCopy = holder.show_text_message!!.text
                            val clipboardManager =
                                mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("text", textToCopy)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(
                                holder.itemView.context,
                                "Text copied to clipboard",
                                Toast.LENGTH_LONG
                            ).show()
                           // deletesentmessage(position, holder)
                        } else  {



                        }
                    })
                    builder.show()
                }
            }

        }

        // sent and seen message
        if (position == mChatList.size-1)
        {
            if (chat.isIsSeen())
            {
                holder.text_seen!!.text = "seen"

                if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals(""))
                {
                    val lp: RelativeLayout.LayoutParams? = holder.text_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.text_seen!!.layoutParams = lp
                }
            }
            else
            {
                holder.text_seen!!.text = "sent"

                if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals(""))
                {
                    val lp: RelativeLayout.LayoutParams? = holder.text_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.text_seen!!.layoutParams = lp
                }
            }
        }
        else
        {
            holder.text_seen!!.visibility = View.GONE
        }

    }




    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var profile_image: CircleImageView? = null
        var show_text_message: TextView? = null
        var left_image_view: ImageView? = null
        var text_seen: TextView? = null
        var right_image_view: ImageView? = null


        init {
            profile_image = itemView.findViewById(R.id.profile_image)
            show_text_message = itemView.findViewById(R.id.text_show_message)
            left_image_view = itemView.findViewById(R.id.left_image_view)
            text_seen = itemView.findViewById(R.id.text_seen)
            right_image_view = itemView.findViewById(R.id.right_image_view)

        }
    }

    override fun getItemViewType(position: Int): Int
    {
        return if (mChatList[position].getSender().equals(firebaseUser!!.uid))
        {
            1
        }
        else
        {
            0
        }
    }
    private fun deletesentmessage(position: Int, holder: ChatsAdapter.ViewHolder)
    {
        val ref=FirebaseDatabase.getInstance().reference.child("Chats").child(
            mChatList.get(position).getMessageId()!!
        ).removeValue()
                .addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(
                            holder.itemView.context,
                            "Message Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(
                            holder.itemView.context,
                            "Message Not Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }
}