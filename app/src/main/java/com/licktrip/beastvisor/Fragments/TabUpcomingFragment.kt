package com.licktrip.beastvisor.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.licktrip.beastvisor.R
import kotlinx.android.synthetic.main.tab_frag_upcoming.*

class TabUpcomingFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab_frag_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerCount : Spinner = spinner_newOne_upcoming_repeat_count
        val spinnerAfter : Spinner = spinner_newOne_upcoming_repeat_after
        val switchRepeat : Switch = switch_newOne_upcoming_repeat

        val spinnerTimeList = Array(10) { i -> i + 1 }

        spinnerCount.adapter = ArrayAdapter<Int>(activity, R.layout.spinner_items_style, spinnerTimeList)
        spinnerCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                textView_newOne_upcoming_helper_count.text = (position + 1).toString()

            }
        }

        spinnerAfter.adapter = ArrayAdapter<Int>(activity, R.layout.spinner_items_style, spinnerTimeList)
        spinnerAfter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                textView_newOne_upcoming_helper_after.text = (position + 1).toString()
            }

        }

        switchRepeat.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                textView_newOne_upcoming_repeat_count.isEnabled = true
                textView_newOne_upcoming_repeat_count.visibility = TextView.VISIBLE
                textView_newOne_upcoming_repeat_after.isEnabled = true
                textView_newOne_upcoming_repeat_after.visibility = TextView.VISIBLE
                spinnerCount.isEnabled = true
                spinnerCount.visibility = Spinner.VISIBLE
                spinnerAfter.isEnabled = true
                spinnerAfter.visibility = Spinner.VISIBLE

            }else{
                textView_newOne_upcoming_repeat_count.isEnabled = false
                textView_newOne_upcoming_repeat_count.visibility = TextView.GONE
                textView_newOne_upcoming_repeat_after.isEnabled = false
                textView_newOne_upcoming_repeat_after.visibility = TextView.GONE
                spinnerCount.isEnabled = false
                spinnerCount.visibility = Spinner.GONE
                spinnerAfter.isEnabled = false
                spinnerAfter.visibility = Spinner.GONE
                resetObjects()
            }
        }
    }

    private fun resetObjects(){
        spinner_newOne_upcoming_repeat_count.setSelection(0)
        spinner_newOne_upcoming_repeat_after.setSelection(0)
        textView_newOne_upcoming_helper_count.text = "1"
        textView_newOne_upcoming_helper_after.text = "1"
    }
}