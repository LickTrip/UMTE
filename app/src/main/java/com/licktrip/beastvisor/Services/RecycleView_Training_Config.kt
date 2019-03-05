package com.licktrip.beastvisor.Services

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.Model.Training
import com.licktrip.beastvisor.R
import com.licktrip.beastvisor.TrainingDetailsActivity

class RecycleView_Training_Config {
    private lateinit var myContext: Context
    private lateinit var myTrainingAdapter: TrainingAdapter

    fun setConfig(
        recyclerView: RecyclerView,
        context: Context,
        trainingList: List<Training>,
        keyList : List<String>,
        isUpcoming: Boolean,
        userId: String
    ){
        myContext = context
        myTrainingAdapter = TrainingAdapter(trainingList, keyList, isUpcoming, userId)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = myTrainingAdapter
    }

    inner class TrainingItemView(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(myContext).inflate(
            R.layout.training_list_item,
            parent,
            false
        )
    ){
        var textGym : TextView = itemView.findViewById(R.id.textView_item_training_gym)
        var textDate : TextView = itemView.findViewById(R.id.textView_item_training_date)
        var textContent : TextView = itemView.findViewById(R.id.textView_item_training_content)
        var textCons : TextView = itemView.findViewById(R.id.textView_item_training_cons)
        var textPros : TextView = itemView.findViewById(R.id.textView_item_training_pros)
        var textGoal : TextView = itemView.findViewById(R.id.textView_item_training_goal)
        var textNote : TextView = itemView.findViewById(R.id.textView_item_training_note)

        lateinit var key: String

        fun bind(training: Training, key: String, isUpcoming : Boolean, userId: String){
            var cons : String = ""
            var pros : String = ""
            var goal : String = ""

            val timeService = TimePickerService()
            val dbService = DatabaseUserService(userId)
            textGym.text = training.gym
            val dateInString : String = timeService.calendarToString(timeService.longDateToCal(training.startDateTime), false)
            textDate.text = dateInString

            if (training.content != ""){
                textContent.text = training.content
            }else{
                textContent.visibility = TextView.GONE
            }

            if (training.note != ""){
                textNote.text = training.note
            }else{
                textNote.visibility = TextView.GONE
            }

            if (training.consId != ""){
                dbService.readOneConsProsGoal(training.consId, DbReferences.CONS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                        cons = cpg[0].descript
                        textCons.text = cons
                        Log.i("Training_recycler", "Cons loaded")
                    }

                    override fun DataIsInsert() {

                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }else{
                textCons.visibility = TextView.GONE
            }

            if (training.prosId != ""){
                dbService.readOneConsProsGoal(training.prosId, DbReferences.PROS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                        pros = cpg[0].descript
                        textPros.text = pros
                        Log.i("Training_recycler", "Pros loaded")
                    }

                    override fun DataIsInsert() {

                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }else{
                textPros.visibility = TextView.GONE
            }

            if (training.goalId != ""){
                dbService.readOneConsProsGoal(training.goalId, DbReferences.GOALS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                        goal = cpg[0].descript
                        textGoal.text = goal
                        Log.i("Training_recycler", "Goals loaded")
                    }

                    override fun DataIsInsert() {

                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }else{
                textGoal.visibility = TextView.GONE
            }

            itemView.setOnClickListener {
               val initent = Intent(myContext, TrainingDetailsActivity::class.java)
                initent.putExtra(TrainingDetailsActivity.KEY, key)
                initent.putExtra(TrainingDetailsActivity.DATE, dateInString)
                initent.putExtra(TrainingDetailsActivity.DATE_LONG, training.startDateTime)
                initent.putExtra(TrainingDetailsActivity.START_TIME_LONG, training.startDateTime)
                initent.putExtra(TrainingDetailsActivity.GYM, training.gym)
                initent.putExtra(TrainingDetailsActivity.CONTENT_T, training.content)
                initent.putExtra(TrainingDetailsActivity.TRAINING_TIME_LONG, training.trainingTime)
                initent.putExtra(TrainingDetailsActivity.SPARING_TIME_LONG, training.sparingTime)
                initent.putExtra(TrainingDetailsActivity.PROS_DETAIL, pros)
                initent.putExtra(TrainingDetailsActivity.PROS_DETAIL_ID, training.prosId)
                initent.putExtra(TrainingDetailsActivity.CONS_DETAIL, cons)
                initent.putExtra(TrainingDetailsActivity.CONS_DETAIL_ID, training.consId)
                initent.putExtra(TrainingDetailsActivity.GOAL_DETAIL, goal)
                initent.putExtra(TrainingDetailsActivity.GOAL_DETAIL_ID, training.goalId)
                initent.putExtra(TrainingDetailsActivity.TRAINER, training.trainer)
                initent.putExtra(TrainingDetailsActivity.NOTE, training.note)
                initent.putExtra(TrainingDetailsActivity.USER_ID_DETAIL, userId)
                initent.putExtra(TrainingDetailsActivity.IS_UPCOMING, isUpcoming)
                myContext.startActivity(initent)
            }

        }
    }

    inner class TrainingAdapter(
        private var trainingList: List<Training>,
        private var trainingKeyList: List<String>,
        private var isUpcoming: Boolean,
        private var userId: String
    ) : RecyclerView.Adapter<TrainingItemView>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingItemView {
            return TrainingItemView(parent)
        }

        override fun getItemCount(): Int {
            return trainingList.size
        }

        override fun onBindViewHolder(holder: TrainingItemView, position: Int) {
            holder.bind(trainingList[position], trainingKeyList[position], isUpcoming, userId)
        }
    }
}