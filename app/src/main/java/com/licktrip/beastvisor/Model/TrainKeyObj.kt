package com.licktrip.beastvisor.Model

class TrainKeyObj(val trainings : List<Training>, val keys : List<String>) {
    constructor() : this(ArrayList<Training>(), ArrayList<String>())
}