package co.kaua.palacepetz.Activitys.Services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Pets.AsyncPets_SearchScheduleAppointment;
import co.kaua.palacepetz.Data.Schedule.DtoSchedule;
import co.kaua.palacepetz.Data.Schedule.ScheduleServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.Veterinary.AsyncVeterinary_SearchScheduleAppointment;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleAppointmentActivity extends AppCompatActivity {
    private ConstraintLayout ScheduleAppoint_time, ScheduleAppoint_date;
    private Spinner spinner_animal, spinner_veterinary, spinner_paymentForm;
    private LottieAnimationView arrowGoBack_ScheduleConsultation;
    private CardView btnScheduleAppointment;
    private EditText edit_Description_consultation;
    private TextView txt_scheduleAppoint_date, txt_scheduleAppoint_time;
    private final Calendar myCalendar = Calendar.getInstance();
    private static DatePickerDialog.OnDateSetListener date;
    private final ArrayList<String> PetsSearch = new ArrayList<>();
    private final ArrayList<String> VeterinarySearch = new ArrayList<>();
    private static ScheduleAppointmentActivity instance;
    private LoadingDialog loadingDialog;

    //  Spinner Lists
    private static String[] UserPets, VeterinaryList, PaymentFormList;

    //  Schedule Info
    String PetSelected, VeterinarySelected, TimeSelected, DateSelected, PaymentFormSelected, DescriptionInsert;
    int payment_type;

    private int _IdUser;

    private final Retrofit retrofitSchedule = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        Ids();
        instance = this;
        UserPets = new String[]{ getString(R.string.select_your_pet) };
        VeterinaryList = new String[]{ getString(R.string.select_a_veterinarian) };
        PaymentFormList = new String[]{ getString(R.string.select_payment_method),
                getString(R.string.debit), getString(R.string.credit), getString(R.string.money) };
        DtoUser dtoUser = MainActivity.getInstance().GetUserBaseInformation();
        _IdUser = dtoUser.getId_user();
        SetSpinnerAdapter();
        SetSpinnerClick();
        Warnings.PaymentInThePlace(ScheduleAppointmentActivity.this);

        //  Creating Calendar
        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnScheduleAppointment.setOnClickListener(v -> {
            DescriptionInsert = edit_Description_consultation.getText().toString().trim();
            if(PetSelected == null || PetSelected.length() <= 0 || PetSelected.equals(getString(R.string.select_your_pet)))
                ToastHelper.toast(ScheduleAppointmentActivity.this, getString(R.string.pet_is_not_selected));
            else if(VeterinarySelected == null || VeterinarySelected.length() <= 0 || VeterinarySelected.equals(getString(R.string.select_a_veterinarian)))
                ToastHelper.toast(ScheduleAppointmentActivity.this, getString(R.string.veterinary_is_not_selected));
            else if(TimeSelected == null || TimeSelected.length() <= 2)
                ToastHelper.toast(ScheduleAppointmentActivity.this, getString(R.string.time_is_not_selected));
            else if(DateSelected == null || DateSelected.length() <= 4)
                ToastHelper.toast(ScheduleAppointmentActivity.this, getString(R.string.date_is_not_selected));
            else if(PaymentFormSelected == null || PaymentFormSelected.length() <= 4 || PaymentFormSelected.equals(getString(R.string.select_payment_method)))
                ToastHelper.toast(ScheduleAppointmentActivity.this, getString(R.string.date_is_not_selected));
            else{
                loadingDialog = new LoadingDialog(ScheduleAppointmentActivity.this);
                loadingDialog.startLoading();
                if (PaymentFormSelected.equals(getString(R.string.debit))) payment_type = 1;
                else if (PaymentFormSelected.equals(getString(R.string.credit))) payment_type = 2;
                else payment_type = 3;

                DtoSchedule dtoSchedule = new DtoSchedule(_IdUser, PetSelected, VeterinarySelected, TimeSelected, DateSelected, payment_type, 1, DescriptionInsert);
                ScheduleServices services = retrofitSchedule.create(ScheduleServices.class);
                Call<DtoSchedule> call = services.CreateSchedule(dtoSchedule);
                call.enqueue(new Callback<DtoSchedule>() {
                    @Override
                    public void onResponse(@NonNull Call<DtoSchedule> call, @NonNull Response<DtoSchedule> response) {
                        loadingDialog.dimissDialog();
                        if(response.code() == 201){
                            Intent servicesSchedule = new Intent(ScheduleAppointmentActivity.this, ScheduledServicesActivity.class);
                            servicesSchedule.putExtra("id_user", _IdUser);
                            servicesSchedule.putExtra("now", true);
                            startActivity(servicesSchedule);
                            finish();
                        }else
                            Warnings.showWeHaveAProblem(ScheduleAppointmentActivity.this);
                    }
                    @Override
                    public void onFailure(@NonNull Call<DtoSchedule> call, @NonNull Throwable t) {
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(ScheduleAppointmentActivity.this);
                    }
                });
            }
        });

        arrowGoBack_ScheduleConsultation.setOnClickListener(v -> finish());

        ScheduleAppoint_time.setOnClickListener(v -> ShowTimerDialog());

        ScheduleAppoint_date.setOnClickListener(v -> ShowCalendar());
    }

    private void SetSpinnerClick() {
        spinner_animal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PetSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinner_veterinary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VeterinarySelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinner_paymentForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { PaymentFormSelected = parent.getItemAtPosition(position).toString(); }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public static ScheduleAppointmentActivity getInstance() { return instance; }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void UpdateSearch(@NonNull ArrayList<String> list){
        PetsSearch.clear();
        PetsSearch.addAll(Arrays.asList(UserPets));
        PetsSearch.addAll(list);
        spinner_animal.setPopupBackgroundDrawable(getDrawable(R.drawable.background_adapter_pets));
        spinner_animal.setAdapter(new ArrayAdapter<>(ScheduleAppointmentActivity.this, android.R.layout.simple_list_item_1, PetsSearch));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void UpdateSearchVeterinary(@NonNull ArrayList<String> list){
        VeterinarySearch.clear();
        VeterinarySearch.addAll(Arrays.asList(VeterinaryList));
        VeterinarySearch.addAll(list);
        spinner_veterinary.setPopupBackgroundDrawable(getDrawable(R.drawable.background_adapter_pets));
        spinner_veterinary.setAdapter(new ArrayAdapter<>(ScheduleAppointmentActivity.this, android.R.layout.simple_list_item_1, VeterinarySearch));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @SuppressWarnings("unchecked")
    private void SetSpinnerAdapter() {
        //  Set User pet spinner Adapter
        AsyncPets_SearchScheduleAppointment asyncPets = new AsyncPets_SearchScheduleAppointment(_IdUser, ScheduleAppointmentActivity.this);
        asyncPets.execute();

        //  Set VeterinaryList spinner Adapter
        AsyncVeterinary_SearchScheduleAppointment async = new AsyncVeterinary_SearchScheduleAppointment(ScheduleAppointmentActivity.this);
        async.execute();

        //  Set PaymentFormList spinner Adapter
        ArrayAdapter<String> adapterPaymentFormList = new ArrayAdapter<>(ScheduleAppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, PaymentFormList);
        spinner_paymentForm.setPopupBackgroundDrawable(getDrawable(R.drawable.background_adapter_pets));
        spinner_paymentForm.setAdapter(adapterPaymentFormList);
    }

    private void Ids() {
        ScheduleAppoint_date = findViewById(R.id.ScheduleAppoint_date);
        ScheduleAppoint_time = findViewById(R.id.ScheduleAppoint_time);
        btnScheduleAppointment = findViewById(R.id.btnScheduleAppointment);
        spinner_animal = findViewById(R.id.spinner_animal);
        spinner_veterinary = findViewById(R.id.spinner_veterinary);
        spinner_paymentForm = findViewById(R.id.spinner_paymentForm);
        edit_Description_consultation = findViewById(R.id.edit_Description_consultation);
        txt_scheduleAppoint_date = findViewById(R.id.txt_scheduleAppoint_date);
        txt_scheduleAppoint_time = findViewById(R.id.txt_scheduleAppoint_time);
        arrowGoBack_ScheduleConsultation = findViewById(R.id.arrowGoBack_ScheduleConsultation);
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
                            txt_scheduleAppoint_time.setText(getString(R.string.select_a_time));
                        }else{
                            TimeSelected = fullTime;
                            txt_scheduleAppoint_time.setText(fullTime);
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
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleYear), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else if (month < NowMonth){
            ShowCalendar();
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleMonth), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else if (month == NowMonth && day < NowDay){
            ShowCalendar();
            txt_scheduleAppoint_date.setText(getString(R.string.select_a_date));
            Toast.makeText(this, getString(R.string.its_not_possible_scheduleDay), Toast.LENGTH_LONG).show();
            DateSelected = null;
        }else{
            DateSelected = sdf.format(myCalendar.getTime());
            txt_scheduleAppoint_date.setText(sdf.format(myCalendar.getTime()));
        }
    }
}