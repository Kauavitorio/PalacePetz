package co.kaua.palacepetz.Data.ShoppingCart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.ShoppingCart_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncShoppingCart extends AsyncTask {
    ArrayList<DtoShoppingCart> arrayListDto;
    Activity context;
    RecyclerView recyclerShoppingCart;
    SwipeRefreshLayout SwipeRefreshShoppingCart;
    TextView txtTotal, txtOrderNow;
    int _IdUser;
    float priceTotal;
    ShoppingCart_Adapter products_adapter;
    LoadingDialog loadingDialog;
    final Retrofit cartUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AsyncShoppingCart(RecyclerView recyclerShoppingCart, SwipeRefreshLayout SwipeRefreshShoppingCart, ArrayList<DtoShoppingCart> arrayListDto, int _IdUser
            , Activity context, TextView txtTotal, TextView txtOrderNow) {
        this.recyclerShoppingCart = recyclerShoppingCart;
        this.context = context;
        this.SwipeRefreshShoppingCart = SwipeRefreshShoppingCart;
        this._IdUser = _IdUser;
        this.arrayListDto = arrayListDto;
        this.txtTotal = txtTotal;
        this.txtOrderNow = txtOrderNow;
        this.loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.startLoading();
        recyclerShoppingCart.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/shoppingcart/" + _IdUser);
        products_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoShoppingCart dtoShoppingCart = new DtoShoppingCart();
                dtoShoppingCart.setCd_cart(jsonArray.getJSONObject(i).getInt("cd_cart"));
                dtoShoppingCart.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoShoppingCart.setNm_product(jsonArray.getJSONObject(i).getString("nm_product"));
                dtoShoppingCart.setImage_prod(jsonArray.getJSONObject(i).getString("image_prod"));
                dtoShoppingCart.setAmount(jsonArray.getJSONObject(i).getInt("amount"));
                dtoShoppingCart.setId_user(jsonArray.getJSONObject(i).getInt("id_user"));
                dtoShoppingCart.setProduct_price( jsonArray.getJSONObject(i).getString("product_price"));
                priceTotal += Float.parseFloat(jsonArray.getJSONObject(i).getString("totalPrice"));
                dtoShoppingCart.setTotalPrice(  jsonArray.getJSONObject(i).getString("totalPrice"));
                dtoShoppingCart.setProduct_amount(jsonArray.getJSONObject(i).getInt("product_amount"));
                dtoShoppingCart.setSub_total(jsonArray.getJSONObject(i).getString("sub_total"));
                arrayListDto.add(dtoShoppingCart);
            }
            products_adapter = new ShoppingCart_Adapter(arrayListDto, context, txtTotal, txtOrderNow, _IdUser, recyclerShoppingCart, SwipeRefreshShoppingCart);
            products_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return products_adapter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Object products_adapter_obj) {
        super.onPostExecute(products_adapter_obj);
        //((RecyclerView.Adapter) products_adapter).notifyDataSetChanged();
        loadingDialog.dimissDialog();
        recyclerShoppingCart.setAdapter((RecyclerView.Adapter) products_adapter_obj);
        SwipeRefreshShoppingCart.setRefreshing(false);
        recyclerShoppingCart.setVisibility(View.VISIBLE);
    }
}
