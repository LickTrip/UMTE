package com.licktrip.beastvisor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.Model.Training
import com.licktrip.beastvisor.Services.DatabaseUserService
import com.licktrip.beastvisor.Services.TimePickerService
import kotlinx.android.synthetic.main.activity_training_details.*
import kotlinx.android.synthetic.main.activity_training_details.view.*
import java.util.*

class TrainingDetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY: String = "key"
        const val DATE: String = "date"
        const val DATE_LONG: String = "dateLong"
        const val START_TIME_LONG: String = "startTimeLong"
        const val GYM: String = "gym"
        const val CONTENT_T: String = "contentT"
        const val TRAINING_TIME_LONG: String = "trainingTimeLong"
        const val SPARING_TIME_LONG: String = "sparingTimeLong"
        const val PROS_DETAIL: String = "prosD"
        const val CONS_DETAIL: String = "consD"
        const val GOAL_DETAIL: String = "goalD"
        const val PROS_DETAIL_ID: String = "prosDid"
        const val CONS_DETAIL_ID: String = "consDid"
        const val GOAL_DETAIL_ID: String = "goalDid"
        const val TRAINER: String = "trainer"
        const val NOTE: String = "note"
        const val USER_ID_DETAIL: String = "userId"
        const val IS_UPCOMING: String = "isUpcoming"
    }

    private lateinit var date: String
    private lateinit var key: String
    private var dateL: Long = 0
    private lateinit var startTime: String
    private var startTimeL: Long = 0
    private lateinit var gym: String
    private lateinit var trainContent: String
    private lateinit var trainingTime: String
    private var trainingTimeL: Long = 0
    private lateinit var sparingTime: String
    private var sparingTimeL: Long = 0
    private lateinit var pros: String
    private lateinit var cons: String
    private lateinit var goals: String
    private lateinit var prosId: String
    private lateinit var consId: String
    private lateinit var goalsId: String
    private lateinit var trainer: String
    private lateinit var note: String
    private lateinit var userId: String

    private var isUpcoming: Boolean = false
    private lateinit var timeService: TimePickerService
    private lateinit var training: Training
    private lateinit var calDate: Calendar
    private var calDateClick: Boolean = false
    private lateinit var calSparing: Calendar
    private var calSparingClick: Boolean = false
    private lateinit var calTime: Calendar
    private var calTimeClick: Boolean = false
    private lateinit var calStartTime: Calendar
    private var calStartTimeClick: Boolean = false

    private lateinit var dbService: DatabaseUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_details)

        userId = intent.getStringExtra(USER_ID_DETAIL)

        timeService = TimePickerService()
        training = Training()
        dbService = DatabaseUserService(userId)

        isUpcoming = intent.getBooleanExtra(IS_UPCOMING, false)

        key = intent.getStringExtra(KEY)
        date = intent.getStringExtra(DATE)
        dateL = intent.getLongExtra(DATE_LONG, Long.MAX_VALUE)
        startTimeL = intent.getLongExtra(START_TIME_LONG, Long.MAX_VALUE)
        gym = intent.getStringExtra(GYM)
        goals = intent.getStringExtra(GOAL_DETAIL)
        goalsId = intent.getStringExtra(GOAL_DETAIL_ID)
        trainer = intent.getStringExtra(TRAINER)
        note = intent.getStringExtra(NOTE)

        if (!isUpcoming) {
            trainContent = intent.getStringExtra(CONTENT_T)
            trainingTimeL = intent.getLongExtra(TRAINING_TIME_LONG, Long.MAX_VALUE)
            sparingTimeL = intent.getLongExtra(SPARING_TIME_LONG, Long.MAX_VALUE)
            pros = intent.getStringExtra(PROS_DETAIL)
            cons = intent.getStringExtra(CONS_DETAIL)
            prosId = intent.getStringExtra(PROS_DETAIL_ID)
            consId = intent.getStringExtra(CONS_DETAIL_ID)
        }

        editText_trainings_detail_date.setText(date)
        startTime = timeService.calendarToString(timeService.longDateToCal(trainingTimeL), true)
        editText_trainings_detail_start_time.setText(startTime)
        editText_trainings_detail_gym.setText(gym)
        editText_trainings_detail_goal.setText(goals)
        editText_trainings_detail_note.setText(note)

        if (!isUpcoming) {
            editText_trainings_detail_content.setText(trainContent)
            trainingTime = timeService.calendarToString(timeService.longDateToCal(trainingTimeL), true)
            editText_trainings_detail_time.setText(trainingTime)
            sparingTime = timeService.calendarToString(timeService.longDateToCal(sparingTimeL), true)
            editText_trainings_detail_sparing.setText(sparingTime)
            editText_trainings_detail_pros.setText(pros)
            editText_trainings_detail_cons.setText(cons)
            editText_trainings_detail_trainer.setText(trainer)
        } else {
            editText_trainings_detail_content.visibility = EditText.GONE
            editText_trainings_detail_time.visibility = EditText.GONE
            editText_trainings_detail_sparing.visibility = EditText.GONE
            editText_trainings_detail_pros.visibility = EditText.GONE
            editText_trainings_detail_cons.visibility = EditText.GONE
            editText_trainings_detail_trainer.visibility = EditText.GONE
        }
    }

    fun clickUpdateDetailsTraining(view: View) {

        training.gym = editText_trainings_detail_gym.text.toString().trim()
        training.note = editText_trainings_detail_note.text.toString().trim()
        training.isUpcomin = true

        if (calDateClick) {
            if (calStartTimeClick) {
                training.startDateTime = timeService.combineCalendars(calDate, calStartTime).timeInMillis
            } else {
                training.startDateTime = calDate.timeInMillis
            }
        }

        if (editText_trainings_detail_goal.text.toString() != "" && editText_trainings_detail_goal.text.toString() != goals) {
            val newGoal = ConsProsGoal()
            newGoal.descript = editText_trainings_detail_goal.text.toString()
            training.goalId = dbService.saveNewConsProsGoal(
                newGoal,
                DbReferences.GOALS,
                object : DatabaseUserService.ConsProsGoalDataStatus {
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {

                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
        } else {
            training.goalId = goalsId
        }


        if (!isUpcoming) {
            training.isUpcomin = false
            training.content = editText_trainings_detail_content.text.toString().trim()
            training.trainer = editText_trainings_detail_trainer.text.toString().trim()

            if (calTimeClick) {
                training.trainingTime = calTime.timeInMillis
            }

            if (calSparingClick) {
                training.sparingTime = calSparing.timeInMillis
            }

            if (editText_trainings_detail_pros.text.toString() != "" && editText_trainings_detail_pros.text.toString() != pros) {
                val newPros = ConsProsGoal()
                newPros.descript = editText_trainings_detail_pros.text.toString()
                training.prosId = dbService.saveNewConsProsGoal(
                    newPros,
                    DbReferences.PROS,
                    object : DatabaseUserService.ConsProsGoalDataStatus {
                        override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                        }

                        override fun DataIsInsert() {

                        }

                        override fun DataIsUpdate() {

                        }

                        override fun DataIsDelete() {

                        }
                    })
            } else {
                training.prosId = prosId
            }

            if (editText_trainings_detail_cons.text.toString() != "" && editText_trainings_detail_cons.text.toString() != cons) {
                val newCons = ConsProsGoal()
                newCons.descript = editText_trainings_detail_cons.text.toString()
                training.consId = dbService.saveNewConsProsGoal(
                    newCons,
                    DbReferences.CONS,
                    object : DatabaseUserService.ConsProsGoalDataStatus {
                        override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                        }

                        override fun DataIsInsert() {

                        }

                        override fun DataIsUpdate() {

                        }

                        override fun DataIsDelete() {

                        }
                    })
            } else {
                training.consId = consId
            }
        }

        dbService.updateTraining(key, training, object : DatabaseUserService.TrainingDataStatus {
            override fun DataIsLoad(training: List<Training>, key: List<String>) {

            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {
                Toast.makeText(this@TrainingDetailsActivity, "Data has been updated secceddfully", Toast.LENGTH_LONG)
                    .show()
            }

            override fun DataIsDelete() {

            }
        })
    }

    fun clickDeleteDetailsTraining(view: View) {
        dbService.deleteTraining(key, object : DatabaseUserService.TrainingDataStatus {
            override fun DataIsLoad(training: List<Training>, key: List<String>) {

            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {
                Toast.makeText(this@TrainingDetailsActivity, "Data has been deleted successfully", Toast.LENGTH_LONG)
                    .show()
                finish()
                return
            }
        })
    }

    fun callTimePickerTrainedSparingTrainingDetails(view: View) {
        val myEdit: EditText = view.editText_trainings_detail_sparing
        calStartTimeClick = true
        calSparing = timeService.callTimePicker(myEdit, view.context, true)
    }

    fun callTimePickerTrainedTimeTrainingDetails(view: View) {
        val myEdit: EditText = view.editText_trainings_detail_time
        calTimeClick = true
        calTime = timeService.callTimePicker(myEdit, view.context, true)
    }

    fun callTimePickerStartTimeTrainingDetails(view: View) {
        val myEdit: EditText = view.editText_trainings_detail_start_time
        calStartTimeClick = true
        calStartTime = timeService.callTimePicker(myEdit, view.context, true)
    }

    fun callDatePickerTrainDateTrainingDetails(view: View) {
        val myEdit: EditText = view.editText_trainings_detail_date
        calDateClick = true
        calStartTimeClick = false
        editText_trainings_detail_start_time.setText("")
        calDate = timeService.callDatePicker(myEdit, view.context)
    }
}
