package co.kaua.palacepetz.Data.Historic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.ProductDetailsActivity;
import co.kaua.palacepetz.Adapters.Historic_Adapter;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Methods.JsonHandler;


@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncHistoric extends AsyncTask {
    ArrayList<DtoHistoric> arrayListDto;
    Activity contexto;
    ConstraintLayout constraintLayout_noHistoric;
    RecyclerView recyclerHistoric;
    SwipeRefreshLayout SwipeRefreshHistoric;
    int id_user;
    LoadingDialog loadingDialog;

    public AsyncHistoric(RecyclerView recyclerHistoric, ConstraintLayout constraintLayout_noHistoric, Activity contexto, SwipeRefreshLayout SwipeRefresh, int id_user) {
        this.recyclerHistoric = recyclerHistoric;
        this.contexto = contexto;
        this.constraintLayout_noHistoric = constraintLayout_noHistoric;
        this.loadingDialog = new LoadingDialog(contexto);
        this.SwipeRefreshHistoric = SwipeRefresh;
        this.id_user = id_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SwipeRefreshHistoric.setRefreshing(true);
        recyclerHistoric.setVisibility(View.GONE);
        constraintLayout_noHistoric.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/user/historic-product/" + id_user);
        Historic_Adapter historic_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoHistoric dtoHistoric = new DtoHistoric();
                dtoHistoric.setCd_historic(jsonArray.getJSONObject(i).getInt("cd_historic"));
                dtoHistoric.setId_user(jsonArray.getJSONObject(i).getInt("id_user"));
                dtoHistoric.setDatetime(jsonArray.getJSONObject(i).getString("datetime"));
                dtoHistoric.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoHistoric.setImage_prod(jsonArray.getJSONObject(i).getString("image_prod"));
                dtoHistoric.setNm_product(jsonArray.getJSONObject(i).getString("nm_product"));
                dtoHistoric.setProduct_price(jsonArray.getJSONObject(i).getString("product_price"));

                arrayListDto.add(dtoHistoric);
            }
            historic_adapter = new Historic_Adapter(arrayListDto, contexto);
            historic_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return historic_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerHistoric.setVisibility(View.VISIBLE);
        constraintLayout_noHistoric.setVisibility(View.GONE);
        SwipeRefreshHistoric.setRefreshing(false);
        //noinspection rawtypes
        recyclerHistoric.setAdapter((RecyclerView.Adapter) category_adapter);
        recyclerHistoric.getRecycledViewPool().clear();

        recyclerHistoric.addOnItemTouchListener(new RecyclerItemClickListener(contexto,
                recyclerHistoric, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(contexto, ProductDetailsActivity.class);
                i.putExtra("cd_prod", arrayListDto.get(position).getCd_prod());
                contexto.startActivity(i);
            }
            @Override
            public void onLongItemClick(View view, int position) {}
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        }));
    }
}