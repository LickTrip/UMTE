package com.licktrip.beastvisor.Model

class ConsProsGoal(var myName : String, var descript : String, var createTimeDate : Long) {
    constructor() : this("", "", System.currentTimeMillis())
}