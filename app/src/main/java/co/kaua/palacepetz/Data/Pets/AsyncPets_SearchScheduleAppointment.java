package co.kaua.palacepetz.Data.Pets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.Services.ScheduleAppointmentActivity;
import co.kaua.palacepetz.Activitys.Services.ScheduleBathAndTosaActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncPets_SearchScheduleAppointment extends AsyncTask {
    ArrayList<String> arrayListDto = new ArrayList<>();
    Activity context;
    int _IdUser;
    LoadingDialog loadingDialog;

    public AsyncPets_SearchScheduleAppointment(int _IdUser, Activity context) {
        this.context = context;
        this._IdUser = _IdUser;
        this.loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.startLoading();
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/user/pet/" + _IdUser);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length() ; i++) {
                arrayListDto.add(jsonArray.getJSONObject(i).getString("nm_animal"));
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
        ScheduleAppointmentActivity.getInstance().UpdateSearch((ArrayList<String>) arrayListDto);
    }
}
