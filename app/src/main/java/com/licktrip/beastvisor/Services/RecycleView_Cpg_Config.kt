package com.licktrip.beastvisor.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.licktrip.beastvisor.CpgDetailsActivity
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.R

class RecycleView_Cpg_Config {
    private lateinit var myContext: Context
    private lateinit var myCpgAdapter: CpgAdapter

    fun setConfig(
        recyclerView: RecyclerView,
        context: Context,
        cpgList: List<ConsProsGoal>,
        keysList: List<String>,
        cpgEnum: CpgEnum,
        userId: String
    ) {
        myContext = context
        myCpgAdapter = CpgAdapter(cpgList, keysList, cpgEnum, userId)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = myCpgAdapter
    }

    inner class CpgItemView(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(myContext).inflate(
            R.layout.cpg_list_item,
            parent,
            false
        )
    ) {
        var textName: TextView = itemView.findViewById(R.id.textView_cpg_Name)
        var textDate: TextView = itemView.findViewById(R.id.textView_cpg_date)
        var textText: TextView = itemView.findViewById(R.id.textView_cpg_text)
        var line: View = itemView.findViewById(R.id.cpq_line)

        lateinit var key: String

        fun bind(consProsGoal: ConsProsGoal, key: String, cpgEnum: CpgEnum, userId: String) {
            val timeService = TimePickerService()
            textName.text = consProsGoal.myName
            val dateInString: String =
                timeService.calendarToString(timeService.longDateToCal(consProsGoal.createTimeDate),false)
            textDate.text = dateInString
            textText.text = consProsGoal.descript
            this.key = key

            when (cpgEnum) {
                CpgEnum.CONS -> {
                    line.setBackgroundColor(ContextCompat.getColor(myContext, R.color.colorRed))
                }
                CpgEnum.PROS -> {
                    line.setBackgroundColor(ContextCompat.getColor(myContext, R.color.colorGreen))
                }
                CpgEnum.GOALS -> {
                    line.setBackgroundColor(ContextCompat.getColor(myContext, R.color.colorDirtyWhite))
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(myContext, CpgDetailsActivity::class.java)
                intent.putExtra(CpgDetailsActivity.KEY, key)
                intent.putExtra(CpgDetailsActivity.NAME, consProsGoal.myName)
                intent.putExtra(CpgDetailsActivity.DATE, dateInString)
                intent.putExtra(CpgDetailsActivity.DESCRIPT, consProsGoal.descript)
                intent.putExtra(CpgDetailsActivity.DATE_LONG, consProsGoal.createTimeDate)
                intent.putExtra(CpgDetailsActivity.USER_ID, userId)
                intent.putExtra(CpgDetailsActivity.CPG_ENUM, cpgEnum.decript)
                myContext.startActivity(intent)
            }
        }
    }

    inner class CpgAdapter(
        private var consProsGoalList: List<ConsProsGoal>,
        private var consKeysList: List<String>,
        private var cpgEnum: CpgEnum,
        private var userId: String
    ) : RecyclerView.Adapter<CpgItemView>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpgItemView {
            return CpgItemView(parent)
        }

        override fun getItemCount(): Int {
            return consProsGoalList.size
        }

        override fun onBindViewHolder(holder: CpgItemView, position: Int) {
            holder.bind(consProsGoalList[position], consKeysList[position], cpgEnum, userId)
        }

    }
}