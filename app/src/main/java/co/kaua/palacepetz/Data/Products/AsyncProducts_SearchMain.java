package co.kaua.palacepetz.Data.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Fragments.MainFragment;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncProducts_SearchMain extends AsyncTask {
    ArrayList<String> arrayListDto = new ArrayList<>();
    Activity context;
    int _IdUser;

    public AsyncProducts_SearchMain(int _IdUser, Activity context) {
        this.context = context;
        this._IdUser = _IdUser;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/products/list/");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length() ; i++) {
                arrayListDto.add(jsonArray.getJSONObject(i).getString("nm_product"));
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
        MainFragment.getInstance().UpdateSearch((ArrayList<String>) arrayListDto);
    }
}
