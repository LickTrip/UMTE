package com.licktrip.beastvisor.Services

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.R

class CpgBasicAdapter(val mContext: Context, val  layoutResId: Int, val cpgList: List<ConsProsGoal>, val cpgEnum: CpgEnum)
    : ArrayAdapter<ConsProsGoal>(mContext, layoutResId, cpgList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(layoutResId, null)

        val textViewCpg : TextView = view.findViewById(R.id.textView_basic_adapter_cpg)
        val cpgItem = cpgList[position]
        textViewCpg.text = cpgItem.descript

        when (cpgEnum){
            CpgEnum.CONS -> {
                textViewCpg.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed))
            }
            CpgEnum.PROS -> {
                textViewCpg.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen))
            }
            CpgEnum.GOALS -> {
                textViewCpg.setTextColor(ContextCompat.getColor(mContext, R.color.colorDirtyWhite))
            }
            else -> {
                textViewCpg.setTextColor(ContextCompat.getColor(mContext, R.color.yellow))
            }
        }
        return view
    }
}