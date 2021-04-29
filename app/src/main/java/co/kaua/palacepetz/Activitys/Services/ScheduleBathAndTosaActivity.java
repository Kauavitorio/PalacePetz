package co.kaua.palacepetz.Activitys.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import co.kaua.palacepetz.R;

public class ScheduleBathAndTosaActivity extends AppCompatActivity {
    private ConstraintLayout ScheduleBath_time, ScheduleBath_date;
    private Spinner spinner_animalBath, spinner_DeliveryMethod, spinnerBath_paymentForm;
    private LottieAnimationView arrowGoBack_ScheduleBath;
    private CardView btnScheduleBath, btnLayoutSpinnerDeliveryMethod;
    private EditText edit_Description_Bath;
    private TextView txt_scheduleBath_time, txt_scheduleBath_date;
    private final Calendar myCalendar = Calendar.getInstance();
    private static DatePickerDialog.OnDateSetListener date;

    //  Spinner Lists
    private static String[] UserPets, DeliveryMethod, PaymentFormList;

    //  Schedule Info
    private String PetSelected, DeliveryMethodSelected, TimeSelected, DateSelected, PaymentFormSelected, DescriptionInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_bath_and_tosa);
        Ids();
        UserPets = new String[]{ getString(R.string.select_your_pet) };
        DeliveryMethod = new String[]{ getString(R.string.WITHDRAWAL_DELIVERY), getString(R.string.optionOneDeliveryMethod),
                getString(R.string.optionTwoDeliveryMethod), getString(R.string.optionThreeDeliveryMethod), getString(R.string.optionFourDeliveryMethod) };
        PaymentFormList = new String[]{ getString(R.string.select_payment_method) };
        SetSpinnerAdapter();
        SetSpinnerSelected();

        //  Creating Calendar
        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnScheduleBath.setOnClickListener(v -> {
            if (PetSelected == null || PetSelected.length() <= 0)
                Toast.makeText(this, "Sem pet", Toast.LENGTH_SHORT).show();
            else if (DeliveryMethodSelected == null || DeliveryMethodSelected.length() <= 0)
                Toast.makeText(this, "Sem deliv", Toast.LENGTH_SHORT).show();
            else if (TimeSelected == null || TimeSelected.length() <= 0)
                Toast.makeText(this, "Sem hora", Toast.LENGTH_SHORT).show();
            else if (DateSelected == null || DateSelected.length() <= 0)
                Toast.makeText(this, "Sem data", Toast.LENGTH_SHORT).show();
            else if (PaymentFormSelected == null || PaymentFormSelected.length() <= 0)
                Toast.makeText(this, "Sem Pay", Toast.LENGTH_SHORT).show();
            DescriptionInsert = edit_Description_Bath.getText().toString();
        });

        arrowGoBack_ScheduleBath.setOnClickListener(v -> finish());

        ScheduleBath_time.setOnClickListener(v -> ShowTimerDialog());

        ScheduleBath_date.setOnClickListener(v -> ShowCalendar());
    }

    private void SetSpinnerSelected() {

        //  UserPet Selected
        spinner_animalBath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelect = parent.getItemAtPosition(position).toString();
                if (ItemSelect.equals(getString(R.string.select_your_pet)))
                    PetSelected = null;
                else
                    PetSelected = ItemSelect;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PetSelected = null;
            }
        });

        //  DeliveryMethod Selected
        spinner_DeliveryMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelect = parent.getItemAtPosition(position).toString();
                if (ItemSelect.equals(getString(R.string.WITHDRAWAL_DELIVERY))) {
                    DeliveryMethodSelected = null;
                    btnLayoutSpinnerDeliveryMethod.setVisibility(View.VISIBLE);
                }else{
                    DeliveryMethodSelected = ItemSelect;
                    btnLayoutSpinnerDeliveryMethod.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DeliveryMethodSelected = null;
            }
        });

        //  PaymentForm Selected
        spinnerBath_paymentForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelect = parent.getItemAtPosition(position).toString();
                if (ItemSelect.equals(getString(R.string.select_payment_method)))
                    PaymentFormSelected = null;
                else
                    PaymentFormSelected = ItemSelect;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PaymentFormSelected = null;
            }
        });
    }

    private void Ids() {
        ScheduleBath_date = findViewById(R.id.ScheduleBath_date);
        ScheduleBath_time = findViewById(R.id.ScheduleBath_time);
        btnScheduleBath = findViewById(R.id.btnScheduleBath);
        spinner_animalBath = findViewById(R.id.spinner_animalBath);
        spinner_DeliveryMethod = findViewById(R.id.spinner_DeliveryMethod);
        spinnerBath_paymentForm = findViewById(R.id.spinnerBath_paymentForm);
        edit_Description_Bath = findViewById(R.id.edit_Description_Bath);
        txt_scheduleBath_date = findViewById(R.id.txt_scheduleBath_date);
        txt_scheduleBath_time = findViewById(R.id.txt_scheduleBath_time);
        arrowGoBack_ScheduleBath = findViewById(R.id.arrowGoBack_ScheduleBath);
        btnLayoutSpinnerDeliveryMethod = findViewById(R.id.btnLayoutSpinnerDeliveryMethod);
    }

    private void SetSpinnerAdapter() {
        //  Set User pet spinner Adapter
        ArrayAdapter<String> adapterUserPet = new ArrayAdapter<>(ScheduleBathAndTosaActivity.this, android.R.layout.simple_spinner_dropdown_item, UserPets);
        spinner_animalBath.setAdapter(adapterUserPet);
        //  Set VeterinaryList spinner Adapter
        ArrayAdapter<String> adapterDeliveryMethod = new ArrayAdapter<>(ScheduleBathAndTosaActivity.this, android.R.layout.simple_spinner_dropdown_item, DeliveryMethod);
        spinner_DeliveryMethod.setAdapter(adapterDeliveryMethod);
        //  Set PaymentFormList spinner Adapter
        ArrayAdapter<String> adapterPaymentFormList = new ArrayAdapter<>(ScheduleBathAndTosaActivity.this, android.R.layout.simple_spinner_dropdown_item, PaymentFormList);
        spinnerBath_paymentForm.setAdapter(adapterPaymentFormList);
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
                            Toast.makeText(this, getString(R.string.sorryButPalaceWillBeCosed), Toast.LENGTH_LONG).show();
                            TimeSelected = null;
                            txt_scheduleBath_time.setText(getString(R.string.select_a_time));
                        }else{
                            TimeSelected = fullTime;
                            txt_scheduleBath_time.setText(fullTime);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
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
            txt_scheduleBath_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleYear), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else if (month < NowMonth){
            ShowCalendar();
            txt_scheduleBath_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleMonth), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else if (month == NowMonth && day < NowDay){
            ShowCalendar();
            txt_scheduleBath_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleDay), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else{
            DateSelected = sdf.format(myCalendar.getTime());
            txt_scheduleBath_date.setText(sdf.format(myCalendar.getTime()));
        }
    }
}