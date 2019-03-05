package com.licktrip.beastvisor.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.licktrip.beastvisor.Model.TrainKeyObj
import com.licktrip.beastvisor.Model.Training
import com.licktrip.beastvisor.NewOneActivity
import com.licktrip.beastvisor.R
import com.licktrip.beastvisor.Services.DatabaseUserService
import com.licktrip.beastvisor.Services.HelperService
import com.licktrip.beastvisor.Services.RecycleView_Training_Config
import kotlinx.android.synthetic.main.training_tab_frag_upcoming.*

class TrainingTabUpcomingFragment() : Fragment() {
    private var userId: String = ""
    private lateinit var mActivity: FragmentActivity

    @SuppressLint("ValidFragment")
    constructor(userId: String) : this() {
        this.userId = userId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.training_tab_frag_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbService = DatabaseUserService(userId)
        val upcomingRecyclerView: RecyclerView = recyclerView_train_upcoming

        fabTabTrainUpcoming.setOnClickListener { view ->
            val intent = Intent(activity, NewOneActivity::class.java)
            intent.putExtra(NewOneActivity.USER_ID, userId)
            startActivity(intent)
        }

        dbService.readTrainings(20, object : DatabaseUserService.TrainingDataStatus{
            override fun DataIsLoad(training: List<Training>, key: List<String>) {
                val filteredTrainKeyObj = HelperService().filterTrainingsByUpcoming(TrainKeyObj(training, key),true)
                RecycleView_Training_Config().setConfig(upcomingRecyclerView, mActivity, filteredTrainKeyObj.trainings, filteredTrainKeyObj.keys, true, userId)
                Log.i("fragmentTrainingUp", "Data has been loaded")
            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Activity) {
            mActivity = context as FragmentActivity
        }
    }
}