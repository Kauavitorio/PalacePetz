package co.kaua.palacepetz.Data.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.ProductDetailsActivity;
import co.kaua.palacepetz.Adapters.PopularProducts_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked", "FieldCanBeLocal"})
@SuppressLint("StaticFieldLeak")
public class AsyncPopularProductsMain extends AsyncTask {
    private final ArrayList<DtoProducts> arrayListDto;
    private final Activity context;
    private final RecyclerView recyclerProducts;
    private final int _IdUser;
    private PopularProducts_Adapter products_adapter;
    private final LottieAnimationView loadingPopularProducts;

    public AsyncPopularProductsMain(RecyclerView recyclerProducts, LottieAnimationView loadingPopularProducts, ArrayList<DtoProducts> arrayListDto, int _IdUser, Activity context) {
        this.recyclerProducts = recyclerProducts;
        this.context = context;
        this._IdUser = _IdUser;
        this.arrayListDto = arrayListDto;
        this.loadingPopularProducts = loadingPopularProducts;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerProducts.setVisibility(View.GONE);
        loadingPopularProducts.setVisibility(View.VISIBLE);
        loadingPopularProducts.playAnimation();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/products/list/filter/popular");
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
            products_adapter = new PopularProducts_Adapter(arrayListDto, context);
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
        recyclerProducts.setVisibility(View.VISIBLE);
        loadingPopularProducts.setVisibility(View.GONE);
        loadingPopularProducts.pauseAnimation();

        //  Need to create Recycler Clicker
        recyclerProducts.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerProducts,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent goToDetails = new Intent(context, ProductDetailsActivity.class);
                        goToDetails.putExtra("cd_prod", arrayListDto.get(position).getCd_prod());
                        goToDetails.putExtra("image_prod", arrayListDto.get(position).getImage_prod());
                        goToDetails.putExtra("nm_product", arrayListDto.get(position).getNm_product());
                        goToDetails.putExtra("description", arrayListDto.get(position).getDescription());
                        goToDetails.putExtra("product_price", arrayListDto.get(position).getProduct_price());
                        goToDetails.putExtra("amount", arrayListDto.get(position).getAmount());
                        goToDetails.putExtra("id_user", _IdUser);
                        context.startActivity(goToDetails);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));
    }
}
