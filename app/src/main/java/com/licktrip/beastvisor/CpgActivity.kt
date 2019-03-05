package com.licktrip.beastvisor

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import com.licktrip.beastvisor.Fragments.CpgTabConsFragment
import com.licktrip.beastvisor.Fragments.CpgTabGoalsFragment
import com.licktrip.beastvisor.Fragments.CpgTabProsFragment
import com.licktrip.beastvisor.Services.DatabaseUserService
import kotlinx.android.synthetic.main.activity_cpg.*



class CpgActivity : AppCompatActivity() {

    companion object {
        const val USER_ID = "user_id"
        const val RESULT = "result"
    }

    private lateinit var userId : String
    lateinit var dbService : DatabaseUserService

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_cpg)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        containerCpg.adapter = mSectionsPagerAdapter
        containerCpg.currentItem = 1

        containerCpg.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsCpg))
        tabsCpg.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(containerCpg))

        userId = intent.getStringExtra(USER_ID)
        dbService = DatabaseUserService(userId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val manager : FragmentManager = this@CpgActivity.supportFragmentManager
//        val trans : FragmentTransaction =  manager.beginTransaction()
//        trans.remove(CpgTabConsFragment())
//        trans.commit()
//        manager.popBackStack()
//
//  containerCpg.adapter!!.destroyItem(containerCpg, 0, CpgTabConsFragment())
//        containerCpg.adapter!!.destroyItem(containerCpg, 1, CpgTabConsFragment())
//        containerCpg.adapter!!.destroyItem(containerCpg, 2, CpgTabConsFragment())

        // CpgTabConsFragment().exitTransition

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
                    return CpgTabProsFragment(userId)
                }
                1 -> {
                    fragmentsList.add(1)
                    return CpgTabConsFragment(userId)
                }
                2 -> {
                    fragmentsList.add(2)
                    return CpgTabGoalsFragment(userId)
                }
                else -> {
                    fragmentsList.add(0)
                    return CpgTabConsFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }

        fun destroyAllItem() {
            var mPosition = containerCpg.currentItem
            val mPositionMax = containerCpg.currentItem + 1
            if (fragmentsList.size > 0 && mPosition < fragmentsList.size) {
                if (mPosition > 0) {
                    mPosition--
                }

                for (i in mPosition until mPositionMax) {
                    try {
                        val objectobject = this.instantiateItem(containerCpg, fragmentsList[i])
                        destroyItem(containerCpg, fragmentsList[i], objectobject)
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
}
