package com.example.whatsappclone.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.whatsappclone.*
import com.example.whatsappclone.ModelClasses.MessageChatActivity
import kotlinx.android.synthetic.main.fragment_crypto.*
import kotlinx.android.synthetic.main.fragment_crypto.view.*


class CryptoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        val view= inflater.inflate(R.layout.fragment_crypto, container, false)
        view.aes_image.setOnClickListener{
            val intent= Intent(requireContext(), AES::class.java)
            startActivity(intent)

        }
        view.des_image.setOnClickListener {
            val intent= Intent(requireContext(), DES::class.java)
            startActivity(intent)

        }
        view.rsa_image.setOnClickListener {
            val intent= Intent(requireContext(), RSA::class.java)
            startActivity(intent)

        }
        view.md5_image.setOnClickListener {
            val intent= Intent(requireContext(), MD5::class.java)

            startActivity(intent)

        }
        return view




    }



}