package com.example.whatsappclone.ModelClasses

class Users {
    private var cover:String=""
    private var facebook:String=""
    private var  instagram:String=""
    private var profile:String=""
    private var search:String=""
    private var status:String=""
    private var uid:String=""
    private var username:String=""
    private var website:String=""

    constructor()
    constructor(
        cover: String,
        facebook: String,
        instagram: String,
        profile: String,
        search: String,
        status: String,
        uid: String,
        username: String,
        website: String
    ) {
        this.cover = cover
        this.facebook = facebook
        this.instagram = instagram
        this.profile = profile
        this.search = search
        this.status = status
        this.uid = uid
        this.username = username
        this.website = website
    }
    fun getuid():String?{
        return uid
    }
    fun setuid(uid:String)
    {
        this.uid=uid
    }
    fun getcover():String?{
        return cover
    }
    fun setcover(cover: String)
    {
        this.cover=cover
    }
    fun getfacebook():String?{
        return facebook
    }
    fun setfacebook(facebook: String)
    {
        this.facebook=facebook
    }
    fun getinstagram():String?{
        return instagram
    }
    fun setinstagram(instagram: String)
    {
        this.instagram=instagram
    }
    fun getprofile():String?{
        return profile
    }
    fun setprofile(profile: String)
    {
        this.profile=profile
    }
    fun getsearch():String?{
        return search
    }
    fun setsearch(search: String)
    {
        this.search=search
    }
    fun getstatus():String?{
        return status
    }
    fun setstatus(status: String)
    {
        this.status=status
    }
    fun getusername():String?{
        return username
    }
    fun setusername(username:String)
    {
        this.username=username
    }
    fun getwebsite():String?{
        return website
    }
    fun setwebsite(website: String)
    {
        this.website=website
    }


}