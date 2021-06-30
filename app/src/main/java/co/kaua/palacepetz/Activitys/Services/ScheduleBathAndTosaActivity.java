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
import co.kaua.palacepetz.Data.Pets.AsyncPets_SearchScheduleBath;
import co.kaua.palacepetz.Data.Schedule.DtoSchedule;
import co.kaua.palacepetz.Data.Schedule.ScheduleServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleBathAndTosaActivity extends AppCompatActivity {
    private ConstraintLayout ScheduleBath_time, ScheduleBath_date;
    private Spinner spinner_animalBath, spinner_DeliveryMethod, spinnerBath_paymentForm;
    private LottieAnimationView arrowGoBack_ScheduleBath;
    private CardView btnScheduleBath, btnLayoutSpinnerDeliveryMethod;
    private EditText edit_Description_Bath;
    private TextView txt_scheduleBath_time, txt_scheduleBath_date;
    private final Calendar myCalendar = Calendar.getInstance();
    private static DatePickerDialog.OnDateSetListener date;
    private final ArrayList<String> PetsSearch = new ArrayList<>();
    private static ScheduleBathAndTosaActivity instance;
    private LoadingDialog loadingDialog;

    //  Spinner Lists
    private static String[] UserPets, DeliveryMethod, PaymentFormList;

    //  Schedule Info
    private String PetSelected, DeliveryMethodSelected, TimeSelected, DateSelected, PaymentFormSelected, DescriptionInsert;
    int payment_type, delivery_select;

    private int _IdUser;

    private final Retrofit retrofitSchedule = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_bath_and_tosa);
        Ids();
        instance = this;
        UserPets = new String[]{ getString(R.string.select_your_pet) };
        DeliveryMethod = new String[]{ getString(R.string.WITHDRAWAL_DELIVERY), getString(R.string.optionOneDeliveryMethod),
                getString(R.string.optionTwoDeliveryMethod), getString(R.string.optionThreeDeliveryMethod), getString(R.string.optionFourDeliveryMethod) };
        PaymentFormList = new String[]{ getString(R.string.select_payment_method),
                getString(R.string.debit), getString(R.string.credit), getString(R.string.money) };
        DtoUser dtoUser = MainActivity.getInstance().GetUserBaseInformation();
        _IdUser = dtoUser.getId_user();
        SetSpinnerAdapter();
        SetSpinnerSelected();
        Warnings.PaymentInThePlace(ScheduleBathAndTosaActivity.this);

        //  Creating Calendar
        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnScheduleBath.setOnClickListener(v -> {
            DescriptionInsert = edit_Description_Bath.getText().toString().trim();
            if(PetSelected == null || PetSelected.length() <= 0 || PetSelected.equals(getString(R.string.select_your_pet)))
                ToastHelper.toast(this, getString(R.string.pet_is_not_selected));
            else if (DeliveryMethodSelected == null || DeliveryMethodSelected.length() <= 0)
                ToastHelper.toast(this, getString(R.string.delivery_is_not_selected));
            else if(TimeSelected == null || TimeSelected.length() <= 2)
                ToastHelper.toast(this, getString(R.string.time_is_not_selected));
            else if(DateSelected == null || DateSelected.length() <= 4)
                ToastHelper.toast(this, getString(R.string.date_is_not_selected));
            else if(PaymentFormSelected == null || PaymentFormSelected.length() <= 4 || PaymentFormSelected.equals(getString(R.string.select_payment_method)))
                ToastHelper.toast(this, getString(R.string.date_is_not_selected));
            else{
                loadingDialog = new LoadingDialog(this);
                loadingDialog.startLoading();
                if (PaymentFormSelected.equals(getString(R.string.debit))) payment_type = 1;
                else if (PaymentFormSelected.equals(getString(R.string.credit))) payment_type = 2;
                else payment_type = 3;

                DtoSchedule dtoSchedule = new DtoSchedule(_IdUser, PetSelected, TimeSelected, DateSelected, payment_type, 2, delivery_select, DescriptionInsert);
                ScheduleServices services = retrofitSchedule.create(ScheduleServices.class);
                Call<DtoSchedule> call = services.CreateSchedule(dtoSchedule);
                call.enqueue(new Callback<DtoSchedule>() {
                    @Override
                    public void onResponse(@NonNull Call<DtoSchedule> call, @NonNull Response<DtoSchedule> response) {
                        loadingDialog.dimissDialog();
                        if(response.code() == 201){
                            Intent servicesSchedule = new Intent(ScheduleBathAndTosaActivity.this, ScheduledServicesActivity.class);
                            servicesSchedule.putExtra("id_user", _IdUser);
                            servicesSchedule.putExtra("now", true);
                            startActivity(servicesSchedule);
                            finish();
                        }else
                            Warnings.showWeHaveAProblem(ScheduleBathAndTosaActivity.this);
                    }
                    @Override
                    public void onFailure(@NonNull Call<DtoSchedule> call, @NonNull Throwable t) {
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(ScheduleBathAndTosaActivity.this);
                    }
                });
            }
        });

        arrowGoBack_ScheduleBath.setOnClickListener(v -> finish());

        ScheduleBath_time.setOnClickListener(v -> ShowTimerDialog());

        ScheduleBath_date.setOnClickListener(v -> ShowCalendar());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void UpdateSearch(@NonNull ArrayList<String> list){
        PetsSearch.clear();
        PetsSearch.addAll(Arrays.asList(UserPets));
        PetsSearch.addAll(list);
        spinner_animalBath.setPopupBackgroundDrawable(getDrawable(R.drawable.background_adapter_pets));
        spinner_animalBath.setAdapter(new ArrayAdapter<>(ScheduleBathAndTosaActivity.this, android.R.layout.simple_list_item_1, PetsSearch));
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
                    if(DeliveryMethodSelected.equals(getString(R.string.optionOneDeliveryMethod)))
                        delivery_select = 1;
                    else if(DeliveryMethodSelected.equals(getString(R.string.optionTwoDeliveryMethod)))
                        delivery_select = 2;
                    else if(DeliveryMethodSelected.equals(getString(R.string.optionThreeDeliveryMethod)))
                        delivery_select = 3;
                    else if(DeliveryMethodSelected.equals(getString(R.string.optionFourDeliveryMethod)))
                        delivery_select = 4;
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

    public static ScheduleBathAndTosaActivity getInstance() { return instance; }

    @SuppressWarnings("unchecked")
    private void SetSpinnerAdapter() {
        //  Set User pet spinner Adapter
        AsyncPets_SearchScheduleBath asyncPets = new AsyncPets_SearchScheduleBath(_IdUser, ScheduleBathAndTosaActivity.this);
        asyncPets.execute();

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