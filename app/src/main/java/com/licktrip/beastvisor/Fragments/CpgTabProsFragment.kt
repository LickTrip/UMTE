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
import com.licktrip.beastvisor.CpgAddActivity
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.R
import com.licktrip.beastvisor.Services.DatabaseUserService
import com.licktrip.beastvisor.Services.RecycleView_Cpg_Config
import kotlinx.android.synthetic.main.cpg_tab_frag_pros.*

class CpgTabProsFragment() : Fragment(){
    private var userId: String = ""
    private lateinit var mActivity : FragmentActivity

    @SuppressLint("ValidFragment")
    constructor(userId: String) : this() {
        this.userId = userId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cpg_tab_frag_pros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbService = DatabaseUserService(userId)
        val prosRecyclerView: RecyclerView = recyclerView_cpg_pros

        fabPros.setOnClickListener { view ->
            val intent = Intent(activity, CpgAddActivity::class.java)
            intent.putExtra(CpgAddActivity.USER_ID, userId)
            intent.putExtra(CpgAddActivity.CPG_ENUM, CpgEnum.PROS.decript)
            startActivity(intent)
        }

        dbService.readConsProsGoal(20, DbReferences.PROS,object : DatabaseUserService.ConsProsGoalDataStatus{
            override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {
                RecycleView_Cpg_Config().setConfig(prosRecyclerView, mActivity, cpg, key, CpgEnum.PROS, userId)
                Log.i("fragmentProsCPG", "Data has been loaded")
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