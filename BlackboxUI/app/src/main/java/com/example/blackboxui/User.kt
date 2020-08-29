package com.example.blackboxui

import java.util.regex.Pattern

class User {
    private var email: String = ""//id
    private var name : String = ""
    private var phoneNumber : String = ""

    fun User(){

    }

    fun setName(Name:String){
        name = Name.toUpperCase()
    }

    fun setPhoneNumber(PhoneNumber : String){
        val filtered = PhoneNumber.filter{ it.isDigit() }
        phoneNumber = filtered
    }

    fun getName(): String {
        return name
    }

    fun getPhoneNumber(): String {
        return phoneNumber
    }


    // functions by William
//    fun matchEmail(x: String?): Boolean {
//        return Pattern.matches("[a-z]{2,3}[0-9]{2,4}@cornell.edu$", x)
//    }

    fun matchNumber(x: String?): Boolean {
        val filtered = x?.filter{ it.isDigit() }
        return Pattern.matches("[0-9]{10,11}", filtered)
    }
}