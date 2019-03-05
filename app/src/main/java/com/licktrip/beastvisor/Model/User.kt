package com.licktrip.beastvisor.Model

class User (val id: String, val name : String, val surname : String, val nick : String, val email : String, val role: String){
    constructor() : this("","","","","", Role.USER.name)
}