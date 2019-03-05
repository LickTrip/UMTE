package com.licktrip.beastvisor

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.licktrip.beastvisor.Fragments.MainFragment
import com.licktrip.beastvisor.Model.DbReferences
import com.licktrip.beastvisor.Model.User
import com.licktrip.beastvisor.Services.DatabaseUserService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val USER_ID = "logUserId"//"4dL5mBWvqpTu1w2GysMHPbjJoZu1" //TODO zapnout login
        const val RESULT = "result"
    }

    private lateinit var userId: String
    private lateinit var myDbRef: DatabaseReference
    private lateinit var manager: FragmentManager
    private lateinit var databaseUserService: DatabaseUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = supportFragmentManager
        userId = intent.getStringExtra(USER_ID)
        //userId = USER_ID
        myDbRef = FirebaseDatabase.getInstance().getReference(DbReferences.USERS.desctipt)
        initFragmentMain(userId)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, NewOneActivity::class.java)
            intent.putExtra(NewOneActivity.USER_ID, userId)
            startActivity(intent)
        }



        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        databaseUserService = DatabaseUserService(userId)

        databaseUserService.readUser(object : DatabaseUserService.UserDataStatus{
            override fun DataIsLoad(user: User, key: String) {
                menu_drawer_header.text = user.nick
                menu_drawer_second_info.text = user.email
            }

            override fun DataIsInsert() {

            }

            override fun DataIsUpdate() {

            }

            override fun DataIsDelete() {

            }
        })
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                //startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_stats -> {

            }
            R.id.nav_trainings -> {
                val intent = Intent(this@MainActivity, TrainingsActivity::class.java)
                intent.putExtra(TrainingsActivity.USER_ID, userId)
                startActivity(intent)

            }
            R.id.nav_cons_pros_goals -> {
                val intent = Intent(this@MainActivity, CpgActivity::class.java)
                intent.putExtra(CpgActivity.USER_ID, userId)
                startActivity(intent)
            }
            R.id.nav_technique -> {

            }
            R.id.nav_about -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initFragmentMain(userId : String){
        val transaction = manager.beginTransaction()
        val fragment = MainFragment(userId)
        transaction.replace(R.id.fragment_holder_main, fragment)
        transaction.addToBackStack("Main")
        transaction.commit()
    }
}
