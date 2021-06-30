package co.kaua.palacepetz.Data.Schedule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.ScheduledService_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncScheduledServices extends AsyncTask {
    ArrayList<DtoSchedule> arrayListDto;
    Activity contexto;
    RecyclerView RecyclerSchedules;
    int id_user;
    LoadingDialog loadingDialog;

    public AsyncScheduledServices(RecyclerView RecyclerSchedules, Activity contexto, int id_user) {
        this.RecyclerSchedules = RecyclerSchedules;
        this.contexto = contexto;
        this.loadingDialog = new LoadingDialog(contexto);
        this.id_user = id_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.startLoading();
        RecyclerSchedules.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/user/schedules/" + id_user);
        ScheduledService_Adapter service_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoSchedule dtoSchedule = new DtoSchedule();
                dtoSchedule.setCd_schedule(jsonArray.getJSONObject(i).getInt("cd_schedule"));
                dtoSchedule.setService_type(jsonArray.getJSONObject(i).getInt("service_type"));
                dtoSchedule.setNm_animal(jsonArray.getJSONObject(i).getString("nm_animal"));
                dtoSchedule.setNm_veterinary(jsonArray.getJSONObject(i).getString("nm_veterinary"));
                dtoSchedule.setDate_schedule(jsonArray.getJSONObject(i).getString("date_schedule"));
                dtoSchedule.setTime_schedule(jsonArray.getJSONObject(i).getString("time_schedule"));

                arrayListDto.add(dtoSchedule);
            }
            service_adapter = new ScheduledService_Adapter(arrayListDto, contexto);
            service_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return service_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        loadingDialog.dimissDialog();
        RecyclerSchedules.setVisibility(View.VISIBLE);
        //noinspection rawtypes
        RecyclerSchedules.setAdapter((RecyclerView.Adapter) category_adapter);
        RecyclerSchedules.getRecycledViewPool().clear();
    }
}