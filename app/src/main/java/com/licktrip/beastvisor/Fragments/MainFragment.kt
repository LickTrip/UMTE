package com.licktrip.beastvisor.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.R
import com.licktrip.beastvisor.Services.CpgBasicAdapter
import com.licktrip.beastvisor.Services.DatabaseUserService
import kotlinx.android.synthetic.main.frag_main_home.*

class MainFragment() : Fragment() {
    private var userId: String = ""
    private lateinit var mActivity : FragmentActivity

    @SuppressLint("ValidFragment")
    constructor(userId: String) : this() {
        this.userId = userId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbService = DatabaseUserService(userId)



        dbService.readConsProsGoal(3, DbReferences.CONS, object : DatabaseUserService.ConsProsGoalDataStatus{
            override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                val adapterCons = CpgBasicAdapter(mActivity, R.layout.basic_adapter_item, cpg, CpgEnum.CONS)
                listView_main_cons.adapter = adapterCons

            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })

        dbService.readConsProsGoal(3, DbReferences.PROS, object : DatabaseUserService.ConsProsGoalDataStatus{
            override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                val adapterPros = CpgBasicAdapter(mActivity, R.layout.basic_adapter_item, cpg, CpgEnum.PROS)
                listView_main_pros.adapter = adapterPros
            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })
        dbService.readConsProsGoal(3, DbReferences.GOALS, object : DatabaseUserService.ConsProsGoalDataStatus{
            override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                val adapterGoal = CpgBasicAdapter(mActivity, R.layout.basic_adapter_item, cpg, CpgEnum.GOALS)
                listView_main_goals.adapter = adapterGoal
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

        if (context is Activity){
            mActivity = context as FragmentActivity
        }
    }
}