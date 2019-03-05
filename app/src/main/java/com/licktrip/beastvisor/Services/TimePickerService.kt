package com.licktrip.beastvisor.Services

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.util.*

class TimePickerService {

    fun callDatePicker(editText: EditText, context: Context) : Calendar{
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val datePickerD = DatePickerDialog(context, R.style.Theme_Holo_Dialog, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            val iterForText : Int = month + 1
            editText.setText("$day. $iterForText. $year")
            calendar.set(year, month, day)
        }, year, month, day)

//        calendar.set(year, month, day)
        datePickerD.show()
        return calendar
    }

    fun callTimePicker(editText: EditText, context: Context, is24: Boolean) : Calendar{
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, R.style.Theme_Holo_Dialog, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            editText.setText("$hour:$minute")
            calendar.set(0, 0, 0, hour, minute)
        }, hour, minute, is24)

//        calendar.set(0, 0, 0, hour, minute)
        timePickerDialog.show()
        return calendar
    }

    fun combineCalendars(calendarDate: Calendar, calendarTime: Calendar) : Calendar {

        calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY))
        calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE))
        calendarDate.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND))
        calendarDate.set(Calendar.MILLISECOND, calendarTime.get(Calendar.MILLISECOND))

        return calendarDate
    }

    fun calendarToString(calendar: Calendar, onlyTime: Boolean): String{
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        month += 1
        val year = calendar.get(Calendar.YEAR)

        var hour : Int = calendar.get(Calendar.HOUR_OF_DAY)
        hour += 1
        val min : Int = calendar.get(Calendar.MINUTE)

        return if(onlyTime){
            "$hour:$min"
        }else{
            "$day. $month. $year  $hour:$min"
        }

    }

    fun longDateToCal(long: Long):Calendar{
        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = long
        return calendar
    }
}