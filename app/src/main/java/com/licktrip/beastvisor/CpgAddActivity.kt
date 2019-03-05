package com.licktrip.beastvisor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.licktrip.beastvisor.Model.ConsProsGoal
import com.licktrip.beastvisor.Model.CpgEnum
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.Services.DatabaseUserService
import kotlinx.android.synthetic.main.activity_cpg_add.*

class CpgAddActivity : AppCompatActivity() {

    companion object {
        const val USER_ID : String = "userId"
        const val CPG_ENUM : String = "cpgEnum"
    }

    private lateinit var userId : String
    private lateinit var cpgEnum : String
    private lateinit var dbService: DatabaseUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_cpg_add)

        userId = intent.getStringExtra(USER_ID)
        cpgEnum = intent.getStringExtra(CPG_ENUM)

        dbService = DatabaseUserService(userId)
    }

    fun clickAddCpg(view: View){
        val name = editText_add_cpg_name.text.toString().trim()
        val descript = editText_add_cpg_description.text.toString().trim()
        val cpg = ConsProsGoal(name, descript, System.currentTimeMillis())
        when(cpgEnum){
            CpgEnum.CONS.decript -> {
                dbService.saveNewConsProsGoal(cpg, DbReferences.CONS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {
                        Toast.makeText(this@CpgAddActivity, "Con has been added successfully", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }
            CpgEnum.PROS.decript -> {
                dbService.saveNewConsProsGoal(cpg, DbReferences.PROS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {
                        Toast.makeText(this@CpgAddActivity, "Con has been added successfully", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }
            CpgEnum.GOALS.decript ->{
                dbService.saveNewConsProsGoal(cpg, DbReferences.GOALS, object : DatabaseUserService.ConsProsGoalDataStatus{
                    override fun DataIsLoad(cpg: List<ConsProsGoal>, key: List<String>) {

                    }

                    override fun DataIsInsert() {
                        Toast.makeText(this@CpgAddActivity, "Con has been added successfully", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }

                    override fun DataIsUpdate() {

                    }

                    override fun DataIsDelete() {

                    }
                })
            }
            else -> {
                Log.e("AddCpgActivity", "None CPG Enum found")
            }
        }
    }
}
