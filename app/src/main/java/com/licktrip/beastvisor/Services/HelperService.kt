package com.licktrip.beastvisor.Services

import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.Model.TrainKeyObj
import com.licktrip.beastvisor.Model.Training

class HelperService {

    fun getCpgEnumFromString(stringEnum: String): CpgEnum {
        return when (stringEnum) {
            CpgEnum.CONS.decript -> {
                CpgEnum.CONS
            }
            CpgEnum.PROS.decript -> {
                CpgEnum.PROS
            }
            CpgEnum.GOALS.decript -> {
                CpgEnum.GOALS
            }

            else -> CpgEnum.ERR
        }
    }

    fun filterTrainingsByUpcoming(trainKey: TrainKeyObj, isUpcoming: Boolean): TrainKeyObj {
        val trainingList = ArrayList<Training>()
        val keyList = ArrayList<String>()
        var counter = 0

        for (train in trainKey.trainings) {
            if (train.isUpcomin == isUpcoming) {
                trainingList.add(train)
                keyList.add(trainKey.keys[counter])
            }
            counter += 1
        }

        return TrainKeyObj(trainingList, keyList)
    }
}