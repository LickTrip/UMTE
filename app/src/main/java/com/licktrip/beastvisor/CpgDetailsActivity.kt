package com.licktrip.beastvisor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.Services.DatabaseUserService
import com.licktrip.beastvisor.Services.HelperService
import kotlinx.android.synthetic.main.activity_cpg_details.*

class CpgDetailsActivity : AppCompatActivity() {

    companion object {
        const val NAME : String = "name"
        const val DATE : String = "date"
        const val DATE_LONG : String = "dateLong"
        const val DESCRIPT : String = "descript"
        const val KEY : String = "key"
        const val USER_ID : String = "userId"
        const val CPG_ENUM : String = "cpgEnum"
    }
    private lateinit var name : String
    private lateinit var date : String
    private lateinit var descript : String
    private lateinit var key : String
    private lateinit var userId : String
    private lateinit var cpgEnum : String
    private var dateLong : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_cpg_details)

        name = intent.getStringExtra(NAME)
        date = intent.getStringExtra(DATE)
        descript = intent.getStringExtra(DESCRIPT)
        key = intent.getStringExtra(KEY)
        userId = intent.getStringExtra(USER_ID)
        dateLong = intent.getLongExtra(DATE_LONG, Long.MAX_VALUE)
        cpgEnum = intent.getStringExtra(CPG_ENUM)

        editText_details_cpg_name.setText(name)
        editText_details_cpg_date.setText(date)
        editText_details_cpg_description.setText(descript)
    }

    fun clickUpdateDetailsCpg(view: View){
        val cpg = ConsProsGoal()
        cpg.myName = editText_details_cpg_name.text.toString()
        cpg.descript = editText_details_cpg_description.text.toString()
        cpg.createTimeDate = dateLong

        val transCpgEnum : CpgEnum = HelperService().getCpgEnumFromString(cpgEnum)
        if (transCpgEnum == CpgEnum.ERR){
            Log.e("CpgEnum", "CPG Enum was translate as  error")
            return
        }

        DatabaseUserService(userId).updateConsProsGoals(key, cpg, transCpgEnum,
            object : DatabaseUserService.ConsProsGoalDataStatus{
                override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                }

                override fun DataIsInsert() {

                }

                override fun DataIsUpdate() {
                    Toast.makeText(this@CpgDetailsActivity, "Data has been updated successfully", Toast.LENGTH_LONG).show()
                }

                override fun DataIsDelete() {

                }

            })


    }

    fun clickDeleteDetailsCpg(view: View){
        val transCpgEnum : CpgEnum = HelperService().getCpgEnumFromString(cpgEnum)
        if (transCpgEnum == CpgEnum.ERR){
            Log.e("CpgEnum", "CPG Enum was translate as  error")
            return
        }

        DatabaseUserService(userId).deleteConsProsGoals(key, transCpgEnum,
            object : DatabaseUserService.ConsProsGoalDataStatus{
                override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                }

                override fun DataIsInsert() {

                }

                override fun DataIsUpdate() {

                }

                override fun DataIsDelete() {
                    Toast.makeText(this@CpgDetailsActivity, "Data has been deleted successfully", Toast.LENGTH_LONG).show()
                    finish()
                    return
                }

            })
    }
}
