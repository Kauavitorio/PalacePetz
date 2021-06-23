package co.kaua.palacepetz.Data.Veterinary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.Services.ScheduleAppointmentActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncVeterinary_SearchScheduleAppointment extends AsyncTask {
    ArrayList<String> arrayListDto = new ArrayList<>();
    Activity context;
    int id_employee = 1444;
    LoadingDialog loadingDialog;

    public AsyncVeterinary_SearchScheduleAppointment(Activity context) {
        this.context = context;
        this.loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.startLoading();
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/employee/employee/" + id_employee);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length() ; i++) {
                if(jsonArray.getJSONObject(i).getInt("user_type") == 2){
                    arrayListDto.add(jsonArray.getJSONObject(i).getString("name_user"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return arrayListDto;
    }

    @Override
    protected void onPostExecute(Object arrayListDto) {
        super.onPostExecute(arrayListDto);
        loadingDialog.dimissDialog();
        ScheduleAppointmentActivity.getInstance().UpdateSearchVeterinary((ArrayList<String>) arrayListDto);
    }
}
