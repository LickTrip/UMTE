package com.licktrip.beastvisor.Services

import android.util.Log
import com.google.firebase.database.*
import com.licktrip.beastvisor.Model.*

class DatabaseUserService(private val userId: String) {
    private lateinit var myDbRef: DatabaseReference
    private val myDb: FirebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        const val SUCCESS_SAVE: String = "Data save success"
        const val SUCCESS_READ: String = "Data read success"
        const val READ_FAIL: String = "Failed to read value."
        const val NO_RESULT: String = "No result found"
        const val READ_USER: String = "Read_db_user"
        const val READ_TRAINING: String = "Read_db_training"
        const val READ_CPG: String = "Read_db_cpg"
        const val READ_TECHNIQUE: String = "Read_db_technique"

        const val INIT_WITHOUT_ID : String = "Object was init without userId"
    }

    /**
     * Interfaces
     */

    interface UserDataStatus {
        fun DataIsLoad(user: User, key: String)
        fun DataIsInsert()
        fun DataIsUpdate()
        fun DataIsDelete()
    }

    interface TrainingDataStatus {
        fun DataIsLoad(training: List<Training>, key: List<String>)
        fun DataIsInsert()
        fun DataIsUpdate()
        fun DataIsDelete()
    }

    interface ConsProsGoalDataStatus {
        fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>)
        fun DataIsInsert()
        fun DataIsUpdate()
        fun DataIsDelete()
    }

    interface TechniqueDataStatus {
        fun DataIsLoad(tech: List<Technique>, key: List<String>)
        fun DataIsInsert()
        fun DataIsUpdate()
        fun DataIsDelete()
    }


    /**
     * Saving methods
     */
    fun saveNewTraining(training: Training, userDataStatus: UserDataStatus): String {
        val consProsGoal = ConsProsGoal()
        if (!training.isUpcomin) {
            if (training.consId != "") {
                consProsGoal.descript = training.consId
                training.consId = saveNewConsProsGoal(consProsGoal, DbReferences.CONS, object : ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {
                        Log.i(DbReferences.CONS.desctipt, SUCCESS_SAVE)
                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }
            if (training.prosId != "") {
                consProsGoal.descript = training.prosId
                training.prosId = saveNewConsProsGoal(consProsGoal, DbReferences.PROS, object : ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {
                        Log.i(DbReferences.PROS.desctipt, SUCCESS_SAVE)
                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }

                })
            }
        }
        if (training.goalId != "") {
            consProsGoal.descript = training.goalId
            training.goalId = saveNewConsProsGoal(consProsGoal, DbReferences.GOALS, object : ConsProsGoalDataStatus{
                override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                }

                override fun DataIsInsert() {
                    Log.i(DbReferences.GOALS.desctipt, SUCCESS_SAVE)
                }

                override fun DataIsUpdate() {

                }

                override fun DataIsDelete() {

                }

            })
        }
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt)
        val myId: String = myDbRef.push().key!!
        myDbRef.child(userId).child(DbReferences.TRAININGS.desctipt).child(myId).setValue(training)
            .addOnCompleteListener {
                userDataStatus.DataIsInsert()
                Log.i(DbReferences.TRAININGS.desctipt, SUCCESS_SAVE)
            }
        return myId
    }

    fun saveNewConsProsGoal(consProsGoal: ConsProsGoal, references: DbReferences, cpgDataStatus: ConsProsGoalDataStatus): String {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt)
        val myId: String = myDbRef.push().key!!
        myDbRef.child(userId).child(references.desctipt).child(myId).setValue(consProsGoal).addOnSuccessListener{
            cpgDataStatus.DataIsInsert()
            Log.i("CPG", SUCCESS_SAVE)
        }
        return myId
    }

    fun saveTechnique(technique: Technique, techniqueDataStatus: TechniqueDataStatus): String {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt)
        val myId: String = myDbRef.push().key!!
        myDbRef.child(userId).child(DbReferences.TECHNIQUES.desctipt).child(myId).setValue(technique)
            .addOnCompleteListener {
                techniqueDataStatus.DataIsInsert()
                Log.i(DbReferences.TECHNIQUES.desctipt, SUCCESS_SAVE)
            }

        return myId
    }

    /**
     * Reading methods
     */
    fun readUser(userDataStatus: UserDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt)
        myDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    Log.w(READ_USER, NO_RESULT)
                    return
                }

                for (snap in p0.children) {
                    val u = snap.getValue(User::class.java)
                    if (u!!.id == userId) {
                        Log.w(READ_USER, SUCCESS_READ)
                        userDataStatus.DataIsLoad(u, snap.key!!)
                        break
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(READ_USER, READ_FAIL, p0.toException())
            }
        })
    }

    fun readTrainings(count: Int, trainingDataStatus: TrainingDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(DbReferences.TRAININGS.desctipt)
        val trainValList = ArrayList<Training>()

        myDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    Log.w(READ_TRAINING, NO_RESULT)
                    return
                }
                trainValList.clear()
                val trainKeyList = ArrayList<String>()
                var iter: Int = 1
                for (snap in p0.children) {
                    val t = snap.getValue(Training::class.java)
                    trainValList.add(t!!)
                    trainKeyList.add(snap.key!!)

                    if (iter == count) {
                        break
                    } else {
                        iter++
                    }
                }
                trainingDataStatus.DataIsLoad(trainValList, trainKeyList)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(READ_TRAINING, READ_FAIL, p0.toException())
            }
        })
    }

    fun readConsProsGoal(count: Int, dbReferences: DbReferences, consProsGoalDataStatus: ConsProsGoalDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(dbReferences.desctipt)
        val cpgValList = ArrayList<ConsProsGoal>()
        myDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    Log.w(READ_CPG, NO_RESULT)
                    return
                }
                cpgValList.clear()
                val cpgKeyList = ArrayList<String>()
                var iter: Int = 1
                for (snap in p0.children) {
                    val cpg = snap.getValue(ConsProsGoal::class.java)
                    cpgValList.add(cpg!!)
                    cpgKeyList.add(snap.key!!)

                    if (iter == count) {
                        break
                    } else {
                        iter++
                    }
                }
                consProsGoalDataStatus.DataIsLoad(cpgValList, cpgKeyList)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(READ_CPG, READ_FAIL, p0.toException())
            }
        })
    }

    /**
     * BE SURE THAT KEY EXIST
     */
    fun readOneConsProsGoal(key: String, dbReferences: DbReferences, consProsGoalDataStatus: ConsProsGoalDataStatus){
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(dbReferences.desctipt)
        val cpgValList = ArrayList<ConsProsGoal>()
        myDbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()){
                    Log.w(READ_CPG, NO_RESULT)
                    return
                }
                val cpgKeyList = ArrayList<String>()
                for (snap in p0.children) {
                    val cpg = snap.getValue(ConsProsGoal::class.java)
                    if (snap.key == key) {
                        Log.w(READ_USER, SUCCESS_READ)
                        cpgValList.add(cpg!!)
                        cpgKeyList.add(p0.key!!)
                        consProsGoalDataStatus.DataIsLoad(cpgValList, cpgKeyList)
                        break
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(READ_CPG, READ_FAIL, p0.toException())
            }
        })
    }

    fun readTechniques(count: Int, techniqueDataStatus: TechniqueDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(DbReferences.TECHNIQUES.desctipt)
        val techValList = ArrayList<Technique>()

        myDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    Log.w(READ_TECHNIQUE, NO_RESULT)
                    return
                }
                techValList.clear()
                val techKeyList = ArrayList<String>()
                var iter: Int = 1
                for (snap in p0.children) {
                    val tech = snap.getValue(Technique::class.java)
                    techValList.add(tech!!)
                    techKeyList.add(snap.key!!)

                    if (iter == count) {
                        break
                    } else {
                        iter++
                    }
                }
                techniqueDataStatus.DataIsLoad(techValList, techKeyList)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(READ_TECHNIQUE, READ_FAIL, p0.toException())
            }
        })
    }

    /**
     * Update methods
     */

    fun updateConsProsGoals(key: String, consProsGoal: ConsProsGoal, cpgEnum: CpgEnum, dataStatus: ConsProsGoalDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(cpgEnum.decript)
        myDbRef.child(key).setValue(consProsGoal)
            .addOnSuccessListener {
                dataStatus.DataIsUpdate()
            }
    }

    fun updateTraining(key: String, training: Training, dataStatus: TrainingDataStatus){
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(DbReferences.TRAININGS.desctipt)
        myDbRef.child(key).setValue(training)
            .addOnSuccessListener {
                dataStatus.DataIsUpdate()
            }
    }

    /**
     * Delete methods
     */

    fun deleteConsProsGoals(key: String, cpgEnum: CpgEnum, dataStatus: ConsProsGoalDataStatus) {
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(cpgEnum.decript)
        myDbRef.child(key).setValue(null)
            .addOnSuccessListener {
                dataStatus.DataIsDelete()
            }
    }

    fun deleteTraining(key: String, dataStatus: TrainingDataStatus){
        myDbRef = myDb.getReference(DbReferences.USERS.desctipt).child(userId).child(DbReferences.TRAININGS.desctipt)
        myDbRef.child(key).setValue(null)
            .addOnSuccessListener {
                dataStatus.DataIsDelete()
            }
    }
}