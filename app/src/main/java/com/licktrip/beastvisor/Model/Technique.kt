package com.licktrip.beastvisor.Model

class Technique(val myName: String, val descript : String, val source : String, val createDateTime : Long) {
    constructor() : this("","","", System.currentTimeMillis())
}