package co.kaua.palacepetz.Activitys.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import co.kaua.palacepetz.R;

public class ScheduleAppointmentActivity extends AppCompatActivity {
    private ConstraintLayout ScheduleAppoint_time, ScheduleAppoint_date;
    private TextView txt_scheduleAppoint_date, txt_scheduleAppoint_time;
    private final Calendar myCalendar = Calendar.getInstance();
    private static DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        Ids();

        //  Creating Calendar
        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        ScheduleAppoint_time.setOnClickListener(v -> ShowTimerDialog());

        ScheduleAppoint_date.setOnClickListener(v -> ShowCalendar());
    }

    private void ShowTimerDialog() {
        // Get Current Time
        int mHour, mMinute;
        String closeTime = "19:00";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

            String minuteFix = String.valueOf(minute);
            if (minuteFix.length() < 2)
                minuteFix = "0" + minute;

            String fullTime = hourOfDay + ":" + minuteFix;
                    try {
                        Date present = parser.parse(fullTime);
                        Date closed = parser.parse(closeTime);
                        assert present != null;
                        if (present.after(closed)) {
                            ShowTimerDialog();
                            Toast.makeText(this, getString(R.string.sorryButPalaceWillBeCosed), Toast.LENGTH_SHORT).show();
                        }
                        txt_scheduleAppoint_time.setText(fullTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void Ids() {
        ScheduleAppoint_date = findViewById(R.id.ScheduleAppoint_date);
        ScheduleAppoint_time = findViewById(R.id.ScheduleAppoint_time);
        txt_scheduleAppoint_date = findViewById(R.id.txt_scheduleAppoint_date);
        txt_scheduleAppoint_time = findViewById(R.id.txt_scheduleAppoint_time);
    }

    private void ShowCalendar(){
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String dateSelected = sdf.format(myCalendar.getTime());
        String[]  SelectingDate = dateSelected.split("/");
        int day = Integer.parseInt(SelectingDate[0]);
        int month = Integer.parseInt(SelectingDate[1]);
        int year = Integer.parseInt(SelectingDate[2]);

        Date data = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(data); // format and displays the date and time
        @SuppressLint("SimpleDateFormat") Format format = new SimpleDateFormat("MM");

        Calendar cal = GregorianCalendar.getInstance();
        int NowYear = cal.get(Calendar.YEAR);
        int NowMonth = Integer.parseInt(format.format(c.getTime()));
        int NowDay = cal.get(Calendar.DAY_OF_MONTH);

        if (year < NowYear || year > NowYear){
            ShowCalendar();
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleYear), Toast.LENGTH_LONG).show();
        }else if (month < NowMonth){
            ShowCalendar();
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleMonth), Toast.LENGTH_LONG).show();
        }else if (month == NowMonth && day < NowDay){
            ShowCalendar();
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleDay), Toast.LENGTH_LONG).show();
        }else{
            txt_scheduleAppoint_date.setText(sdf.format(myCalendar.getTime()));
        }
    }
}