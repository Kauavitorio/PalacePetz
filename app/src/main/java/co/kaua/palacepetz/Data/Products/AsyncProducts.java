package co.kaua.palacepetz.Data.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import co.kaua.palacepetz.Adapters.Products_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncProducts extends AsyncTask {
    ArrayList<DtoMenu> arrayListDto;
    Activity context;
    RecyclerView recyclerProducts;
    SwipeRefreshLayout SwipeRefreshProducts;
    String email_user, img_prod_ad;
    Products_Adapter products_adapter;
    LottieAnimationView anim_loading_allProducts;

    public AsyncProducts(RecyclerView recyclerProducts, SwipeRefreshLayout SwipeRefreshProducts, LottieAnimationView anim_loading_allProducts, ArrayList<DtoMenu> arrayListDto, String email_user, Activity context) {
        this.recyclerProducts = recyclerProducts;
        this.context = context;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.email_user = email_user;
        this.arrayListDto = arrayListDto;
        this.anim_loading_allProducts = anim_loading_allProducts;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        anim_loading_allProducts.setVisibility(View.VISIBLE);
        recyclerProducts.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/products");
        products_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Products");
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoMenu dtoMenu = new DtoMenu();
                dtoMenu.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoMenu.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                dtoMenu.setNm_cat(jsonArray.getJSONObject(i).getString("nm_cat"));
                dtoMenu.setPrice_prod((float) jsonArray.getJSONObject(i).getDouble("price_prod"));
                img_prod_ad = jsonArray.getJSONObject(i).getString("img_prod");
                dtoMenu.setImg_prod_st(img_prod_ad);

                arrayListDto.add(dtoMenu);
            }
            products_adapter = new Products_Adapter(arrayListDto, context);
            products_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return products_adapter;
    }

    @Override
    protected void onPostExecute(Object products_adapter) {
        super.onPostExecute(products_adapter);
        //((RecyclerView.Adapter) products_adapter).notifyDataSetChanged();
        recyclerProducts.setAdapter((RecyclerView.Adapter) products_adapter);
        SwipeRefreshProducts.setRefreshing(false);
        anim_loading_allProducts.setVisibility(View.GONE);
        recyclerProducts.setVisibility(View.VISIBLE);

        //  Need to create Recycler Clicker
    }
}
