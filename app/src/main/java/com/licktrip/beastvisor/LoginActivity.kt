package com.licktrip.beastvisor

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.licktrip.beastvisor.Fragments.LoginFragment
import com.licktrip.beastvisor.Fragments.RegisterFragment
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.Model.Role
import com.licktrip.beastvisor.Model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.frag_login_login.*
import kotlinx.android.synthetic.main.frag_login_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var manager: FragmentManager
    private lateinit var myAuth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference
    private lateinit var userId: String

    companion object {
        private const val EMPTY_UID: String = "Empty"
        private const val ERR_UID: String = "Err"
    }

    enum class RegErr {
        difPass, authErr, seveDbErr, ok
    }

    enum class LogRes {
        signErr, ok, empty
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        manager = supportFragmentManager
        initFragmentLogin()

        myAuth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().getReference(DbReferences.USERS.desctipt)


    }

    fun linkRegistrationOnClick(view: View) {
        initFragmentRegistration()
    }

    fun signInOnClick(view: View) {
        asyncTaskLog().execute()
    }

    fun registrationOnClick(view: View) {
        asyncTaskReg().execute()
    }

    @Suppress("DEPRECATION")
    internal inner class asyncTaskLog : AsyncTask<String, String, LogRes>() {

        lateinit var progressDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@LoginActivity, R.style.ThemeMyProgressDialog)
            progressDialog.setMessage("Login..")
            progressDialog.setCancelable(false)
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large)
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: String?): LogRes {
            return login()
        }

        override fun onPostExecute(result: LogRes?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            when (result) {
                LogRes.ok -> {
                    val intent = Intent(this@LoginActivity, MainActivity ::class.java)
                    intent.putExtra(MainActivity.USER_ID, userId)
                    startActivity(intent)
                    finish()
                }
                LogRes.signErr -> {
                    Snackbar.make(login_coordinatorLayout, "Login failed", Snackbar.LENGTH_LONG).show()
                    editText_log_password.text.clear()
                    textView_login_email.text.clear()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    internal inner class asyncTaskReg : AsyncTask<String, String, String>() {

        lateinit var progressDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@LoginActivity, R.style.ThemeMyProgressDialog)
            progressDialog.setMessage("Processing registration..")
            progressDialog.setCancelable(false)
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large)
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: String?): String {
            return when (newRegistration()) {
                RegErr.ok -> getString(R.string.reg_successful)
                RegErr.difPass -> getString(R.string.pass_match)
                RegErr.authErr -> getString(R.string.reg_auth_err)
                RegErr.seveDbErr -> getString(R.string.reg_db_err)
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            if (result == getString(R.string.reg_successful)) {
                manager.popBackStack()
                Snackbar.make(login_coordinatorLayout, result.toString(), Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(login_coordinatorLayout, result.toString(), Snackbar.LENGTH_LONG).show() // pak mohu zkusit obarvit na cerveno, v zalozkach prohlizece
                Log.e("onPostEx", result.toString())
                editText_reg_password.text.clear()
                editText_reg_password_conf.text.clear()
                textView_reg_email.text.clear()
            }
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            val myProgress = values.get(index = 0)
        }
    }

    private fun initFragmentLogin() {
        val transaction = manager.beginTransaction()
        val frag = LoginFragment()
        transaction.replace(R.id.fragment_holder_login, frag)
        transaction.addToBackStack("Login")
        transaction.commit()

    }

    private fun initFragmentRegistration() {
        val transaction = manager.beginTransaction()
        val frag = RegisterFragment()
        transaction.replace(R.id.fragment_holder_login, frag)
        transaction.addToBackStack("Registration")
        transaction.commit()
    }

    private fun createAuthentication(email: String, pass: String): String {
        var mySwitchAuth = false
        var uid = EMPTY_UID
        myAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { result ->
            if (result.isSuccessful) {
                uid = myAuth.currentUser!!.uid
                Log.i("auth", "Authentication success")
                mySwitchAuth = !mySwitchAuth
            } else {
                uid = ERR_UID
                Log.e("auth", "Authentication error")
                mySwitchAuth = !mySwitchAuth
                TODO("stejne emaily nesmy byt")
            }
        })

        while (!mySwitchAuth) {
            Thread.sleep(500)
        }
        return uid
    }

    private fun login(): LogRes {
        val email = textView_login_email.text.toString().trim()
        val pass = editText_log_password.text.toString().trim()
        var mySwitchLog = false
        var logRes = LogRes.empty
        //validateFormLog(email,pass)

        myAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { result ->
            if (result.isSuccessful) {
                userId = myAuth.currentUser!!.uid
                logRes = LogRes.ok
                Log.i("login", "Login to db success")
                mySwitchLog = !mySwitchLog
            } else {
                logRes = LogRes.signErr
                Log.e("login", "Login failed")
                mySwitchLog = !mySwitchLog
            }
        })

        while (!mySwitchLog) {
            Thread.sleep(500)
        }

        return logRes

        TODO("poresit stop cykleni") // + nemusim to delat v tomhle pripade pro globalni promene pravdepodobne
    }

    private fun newRegistration(): RegErr {
        val name = editText_reg_name.text.toString().trim()
        val surname = editText_reg_surname.text.toString().trim()
        val nick = editText_reg_nick.text.toString().trim()
        val userEmail = textView_reg_email.text.toString().trim()
        val pass = editText_reg_password.text.toString().trim()
        val passConf = editText_reg_password_conf.text.toString().trim()

        //validateForm(name, surname, nick, userEmail, pass, pass2)

        if (pass == passConf) {
            val userId = createAuthentication(userEmail, pass)
            if (userId != "err" || userId != "empty") {
                val user = User(userId, name, surname, nick, userEmail, Role.USER.name)
                return saveUserToDb(user)
            } else {
                return RegErr.authErr
            }
        } else {

            return RegErr.difPass
        }
    }

    private fun saveUserToDb(user: User): RegErr {
        var mySwitchDb = false
        myDbRef.child(user.id).setValue(user).addOnCompleteListener(this, OnCompleteListener { result ->
            if (result.isSuccessful) {
                Log.i("dbSave", "Save to db success")
                mySwitchDb = !mySwitchDb
            } else {
                Log.e("dbSave", "SAve to db error")
                mySwitchDb = !mySwitchDb
            }
        })

        while (!mySwitchDb) {
            Thread.sleep(500)
        }
        return RegErr.ok

        TODO("poresit stop cykleni + doresit overvani formularu a hesel musi bzt vetsi nez atd")
    }
}
