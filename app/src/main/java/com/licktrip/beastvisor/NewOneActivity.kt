package com.licktrip.beastvisor

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.licktrip.beastvisor.Fragments.TabGoalFragment
import com.licktrip.beastvisor.Fragments.TabTechFragment
import com.licktrip.beastvisor.Fragments.TabTrainedFragment
import com.licktrip.beastvisor.Fragments.TabUpcomingFragment
import com.licktrip.beastvisor.Model.*
import com.licktrip.beastvisor.Services.DatabaseUserService
import com.licktrip.beastvisor.Services.TimePickerService
import kotlinx.android.synthetic.main.activity_new_one.*
import kotlinx.android.synthetic.main.tab_frag_goal.*
import kotlinx.android.synthetic.main.tab_frag_tech.*
import kotlinx.android.synthetic.main.tab_frag_trained.*
import kotlinx.android.synthetic.main.tab_frag_trained.view.*
import kotlinx.android.synthetic.main.tab_frag_upcoming.*
import kotlinx.android.synthetic.main.tab_frag_upcoming.view.*
import java.util.*



class NewOneActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var timePickerService: TimePickerService
    private lateinit var dataBaseUserService : DatabaseUserService

    private lateinit var calTrainedDate: Calendar
    private lateinit var calTrainedTime: Calendar
    private lateinit var calTrainedStartTime: Calendar
    private lateinit var calTrainedSparing: Calendar
    private lateinit var calUpcomingDate: Calendar
    private lateinit var calUpcomingStartTime: Calendar

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    companion object {
        const val USER_ID = "user_id"
        const val TRAINER_USER: String = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_new_one)
        timePickerService = TimePickerService()

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.currentItem = 2

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        userId = intent.getStringExtra(USER_ID)
        dataBaseUserService = DatabaseUserService(userId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSectionsPagerAdapter!!.destroyAllItem()
        finish()
        return
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        var fragmentsList : ArrayList<Int> = ArrayList()
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    fragmentsList.add(0)
                    return TabTechFragment()
                }
                1 -> {
                    fragmentsList.add(1)
                    return TabGoalFragment()
                }
                2 -> {
                    fragmentsList.add(2)
                    return TabUpcomingFragment()
                }
                3 -> {
                    fragmentsList.add(3)
                    return TabTrainedFragment()
                }
                else -> {
                    fragmentsList.add(0)
                    return TabUpcomingFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 4
        }

        fun destroyAllItem() {
            var mPosition = container.currentItem
            val mPositionMax = container.currentItem + 1
            if (fragmentsList.size > 0 && mPosition < fragmentsList.size) {
                if (mPosition > 0) {
                    mPosition--
                }

                for (i in mPosition until mPositionMax) {
                    try {
                        val objectobject = this.instantiateItem(container, fragmentsList[i])
                        destroyItem(container, fragmentsList[i], objectobject)
                    } catch (e: Exception) {
                        Log.i("Fragments", "no more Fragment in FragmentPagerAdapter")
                    }

                }
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)

            if (position <= count) {
                val manager = (`object` as Fragment).fragmentManager
                val trans = manager!!.beginTransaction()
                trans.remove(`object`)
                trans.commit()
            }
        }
    }

    fun clickAddNewOneTrained(view: View) {
        val isTrainer = switch_newOne_trained_trainerType.isChecked
        val tGym = editText_newOne_trained_gym.text.toString().trim()
        val tContent = editText_newOne_trained_training_content.text.toString().trim()
        val tPros = editText_newOne_trained_pros.text.toString().trim()
        val tCons = editText_newOne_trained_cons.text.toString().trim()
        val tGoal = editText_newOne_trained_goal.text.toString().trim()
        val tNote = editText_newOne_trained_note.text.toString().trim()

        val tTrainer: String = if (isTrainer) {
            TRAINER_USER
        } else {
            editText_newOne_trained_trainer.text.toString().trim()
        }

        val training = Training(System.currentTimeMillis(), isTrainer, 0,
            0, 0,tGym,tContent,tPros,tCons,tGoal,tNote,tTrainer,false)

        if (editText_newOne_trained_date.text.toString().trim() != ""){
            if (editText_newOne_start_time.text.toString().trim() != ""){
                val cal : Calendar = timePickerService.combineCalendars(calTrainedDate, calTrainedStartTime)
                training.startDateTime = cal.timeInMillis
            }else{
                training.startDateTime = calTrainedStartTime.timeInMillis
            }
        }

        if (editText_newOne_trained_sparing.text.toString().trim() != ""){
            training.sparingTime = calTrainedSparing.timeInMillis
        }

        if (editText_newOne_trained_time.text.toString().trim() != ""){
            training.trainingTime = calTrainedTime.timeInMillis
        }

        dataBaseUserService.saveNewTraining(training, object : DatabaseUserService.UserDataStatus{
            override fun DataIsLoad(user: User, key: String) {

            }

            override fun DataIsInsert() {
                Toast.makeText(this@NewOneActivity, "Training has been added successfully", Toast.LENGTH_LONG).show()
                switch_newOne_trained_trainerType.isChecked = false
                editText_newOne_trained_gym.setText("")
                editText_newOne_trained_training_content.setText("")
                editText_newOne_trained_pros.setText("")
                editText_newOne_trained_cons.setText("")
                editText_newOne_trained_goal.setText("")
                editText_newOne_trained_note.setText("")
                editText_newOne_trained_date.setText("")
                editText_newOne_trained_time.setText("")
                editText_newOne_trained_sparing.setText("")
                editText_newOne_start_time.setText("")
            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })
    }

    fun clickAddNewOneUpcoming(view: View) {
        val isTrainer = switch_newOne_upcoming_trainerType.isChecked
        val uGym = editText_newOne_upcoming_gym.text.toString().trim()
        val uGoal = editText_newOne_upcoming_goal.text.toString().trim()
        val uNote = editText_newOne_upcoming_note.text.toString().trim()

        val uTrainer: String = if (isTrainer) {
            TRAINER_USER
        } else {
            editText_newOne_upcoming_trainer.text.toString().trim()
        }

        var uRepCount: String = ""
        var uRepAfter: String = ""
        val isRepeat = switch_newOne_upcoming_repeat.isChecked
        if (isRepeat) {
            uRepCount = textView_newOne_upcoming_helper_count.text.toString().trim()
            uRepAfter = textView_newOne_upcoming_helper_after.text.toString().trim()
        } //TODO cykleni

        val training = Training()
        training.isMyTrainer = isTrainer
        training.gym = uGym
        training.goalId = uGoal
        training.note = uNote
        training.trainer = uTrainer
        training.isUpcomin = true

        if (editText_newOne_upcoming_date.text.toString().trim() != ""){
            if (editText_upcoming_start_time.text.toString().trim() != ""){
                val cal : Calendar = timePickerService.combineCalendars(calUpcomingDate, calUpcomingStartTime)
                training.startDateTime = cal.timeInMillis
            }else{
                training.startDateTime = calUpcomingDate.timeInMillis
            }
        }

        dataBaseUserService.saveNewTraining(training, object : DatabaseUserService.UserDataStatus{
            override fun DataIsLoad(user: User, key: String) {

            }

            override fun DataIsInsert() {
                Toast.makeText(this@NewOneActivity, "Training has been added successfully", Toast.LENGTH_LONG).show()
                switch_newOne_upcoming_trainerType.isChecked = false
                editText_newOne_upcoming_gym.setText("")
                editText_newOne_upcoming_goal.setText("")
                editText_newOne_upcoming_note.setText("")
                editText_newOne_upcoming_date.setText("")
                editText_upcoming_start_time.setText("")
            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }

        })
    }

    fun clickAddNewOneGoal(view: View) {
        val goalName = editText_newOne_goal_name.text.toString().trim()
        val goalDesc = editText_upcoming_goal_description.text.toString().trim()

        val goal = ConsProsGoal(goalName, goalDesc, System.currentTimeMillis())
        dataBaseUserService.saveNewConsProsGoal(goal, DbReferences.GOALS, object : DatabaseUserService.ConsProsGoalDataStatus{
            override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

            }

            override fun DataIsInsert() {
                Toast.makeText(this@NewOneActivity, "New Goal has been added successfully", Toast.LENGTH_LONG).show()
                editText_newOne_goal_name.setText("")
                editText_upcoming_goal_description.setText("")
            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }

        })
    }

    fun clickAddNewOneTechnique(view: View) {
        val techName = editText_newOne_technique_name.text.toString().trim()
        val techDesc = editText_upcoming_technique_description.text.toString().trim()
        val techSource = editText_newOne_technique_source.text.toString().trim()

        val technique = Technique(techName, techDesc, techSource, System.currentTimeMillis())
        dataBaseUserService.saveTechnique(technique, object : DatabaseUserService.TechniqueDataStatus{
            override fun DataIsLoad(tech: List<Technique>, key: List<String>) {

            }

            override fun DataIsInsert() {
                Toast.makeText(this@NewOneActivity, "New Technique has been added successfully", Toast.LENGTH_LONG).show()
                editText_newOne_technique_name.setText("")
                editText_upcoming_technique_description.setText("")
                editText_newOne_technique_source.setText("")
            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })
    }

    fun callDatePickerTrainedDate(view: View) {
        val myEdit: EditText = view.editText_newOne_trained_date
        calTrainedDate = timePickerService.callDatePicker(myEdit, view.context)
    }

    fun callTimePickerTrainedTime(view: View) {
        val myEdit: EditText = view.editText_newOne_trained_time
        calTrainedTime = timePickerService.callTimePicker(myEdit, view.context, true)
    }

    fun callTimePickerUpcomingDate(view: View) {
        val myEdit: EditText = view.editText_newOne_upcoming_date
        calUpcomingDate = timePickerService.callDatePicker(myEdit, view.context)
    }

    fun callTimePickerTrainedSparing(view: View) {
        val myEdit: EditText = view.editText_newOne_trained_sparing
        calTrainedSparing = timePickerService.callTimePicker(myEdit, view.context, true)
    }

    fun callTimePickerStartTime(view: View) {
        val myEdit: EditText = view.editText_newOne_start_time
        calTrainedStartTime = timePickerService.callTimePicker(myEdit, view.context, true)
    }

    fun callTimePickerUpcomingStartTime(view: View) {
        val myEdit: EditText = view.editText_upcoming_start_time
        calUpcomingStartTime = timePickerService.callTimePicker(myEdit, view.context, true)
    }
}
