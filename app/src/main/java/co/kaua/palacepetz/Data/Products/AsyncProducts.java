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
    ArrayList<DtoProducts> arrayListDto;
    Activity context;
    RecyclerView recyclerProducts;
    SwipeRefreshLayout SwipeRefreshProducts;
    String email_user;
    int _IdUser;
    Products_Adapter products_adapter;
    LottieAnimationView anim_loading_allProducts;

    public AsyncProducts(RecyclerView recyclerProducts, SwipeRefreshLayout SwipeRefreshProducts, LottieAnimationView anim_loading_allProducts, ArrayList<DtoProducts> arrayListDto, int _IdUser, String email_user, Activity context) {
        this.recyclerProducts = recyclerProducts;
        this.context = context;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.email_user = email_user;
        this._IdUser = _IdUser;
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
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/products/list/");
        products_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoProducts dtoProducts = new DtoProducts();
                dtoProducts.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoProducts.setCd_category(jsonArray.getJSONObject(i).getInt("cd_category"));
                dtoProducts.setNm_category(jsonArray.getJSONObject(i).getString("nm_category"));
                dtoProducts.setNm_product(jsonArray.getJSONObject(i).getString("nm_product"));
                dtoProducts.setAmount(jsonArray.getJSONObject(i).getInt("amount"));
                dtoProducts.setSpecies(jsonArray.getJSONObject(i).getString("species"));
                dtoProducts.setProduct_price((float) jsonArray.getJSONObject(i).getDouble("product_price"));
                dtoProducts.setDescription(jsonArray.getJSONObject(i).getString("description"));
                dtoProducts.setDate_prod(jsonArray.getJSONObject(i).getString("date_prod"));
                dtoProducts.setShelf_life(jsonArray.getJSONObject(i).getString("shelf_life"));
                dtoProducts.setImage_prod(jsonArray.getJSONObject(i).getString("image_prod"));
                dtoProducts.setPopular(jsonArray.getJSONObject(i).getInt("popular"));
                arrayListDto.add(dtoProducts);
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
    }
}
