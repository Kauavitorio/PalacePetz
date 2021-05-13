package co.kaua.palacepetz.Data.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.ProductDetailsActivity;
import co.kaua.palacepetz.Adapters.Products_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
@SuppressLint("StaticFieldLeak")
public class AsyncProdCategory extends AsyncTask {
    ArrayList<DtoProducts> arrayListDto;
    Activity context;
    RecyclerView recyclerProducts;
    SwipeRefreshLayout SwipeRefreshProducts;
    LottieAnimationView AnimationProductsLoading;
    int cd_cat;
    String email_user, img_prod_st;

    public AsyncProdCategory(RecyclerView recyclerProducts, LottieAnimationView AnimationProductsLoading, ArrayList<DtoProducts> arrayListDto, String email_user, SwipeRefreshLayout SwipeRefreshProducts,
                             int cd_cat, Activity contexto) {
        this.recyclerProducts = recyclerProducts;
        this.context = contexto;
        this.AnimationProductsLoading = AnimationProductsLoading;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.cd_cat = cd_cat;
        this.email_user = email_user;
        this.arrayListDto = arrayListDto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AnimationProductsLoading.playAnimation();
        recyclerProducts.setVisibility(View.GONE);
        AnimationProductsLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/products/list/filter/category/" + cd_cat);
        Products_Adapter products_adapter = null;
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
        recyclerProducts.setVisibility(View.VISIBLE);
        AnimationProductsLoading.setVisibility(View.GONE);
        AnimationProductsLoading.pauseAnimation();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2,StaggeredGridLayoutManager.VERTICAL);
        recyclerProducts.setLayoutManager(layoutManager);
        recyclerProducts.setAdapter((RecyclerView.Adapter) products_adapter);
        //((RecyclerView.Adapter) products_adapter).notifyDataSetChanged();
        SwipeRefreshProducts.setRefreshing(false);

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