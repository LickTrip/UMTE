package com.licktrip.beastvisor.Model

class Training(val createTimeDate : Long, var isMyTrainer : Boolean, var startDateTime : Long, var sparingTime : Long,
               var trainingTime : Long, var gym : String, var content : String, var prosId : String, var consId : String,
               var goalId : String, var note : String, var trainer: String, var isUpcomin : Boolean) {
    constructor() : this(System.currentTimeMillis(), false,0,0,
        0,"","","","","","","", false)
}