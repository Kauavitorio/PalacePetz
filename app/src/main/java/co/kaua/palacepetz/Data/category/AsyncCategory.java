package co.kaua.palacepetz.Data.category;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import co.kaua.palacepetz.Data.Products.AsyncProdCategory;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@SuppressLint("StaticFieldLeak")
public class AsyncCategory extends AsyncTask {
    ArrayList<DtoCategory> arrayListDto;
    Activity contexto;
    RecyclerView recyclerCategorys;
    LottieAnimationView AnimationcategoryLoading;
    String email_user;

    //  Products
    ArrayList<DtoProducts> arrayListDtomenu;
    RecyclerView recyclerProducts;
    LottieAnimationView AnimationproductsLoading;
    SwipeRefreshLayout SwipeRefreshProducts;

    public AsyncCategory(RecyclerView recyclerCategorys, LottieAnimationView AnimationcategoryLoading, Activity contexto, ArrayList<DtoProducts> arrayListDto, RecyclerView recyclerProducts,
                         LottieAnimationView AnimationproductsLoading, SwipeRefreshLayout SwipeRefreshProducts, String email_user) {
        this.recyclerCategorys = recyclerCategorys;
        this.contexto = contexto;
        this.AnimationcategoryLoading =  AnimationcategoryLoading;

        this.recyclerProducts = recyclerProducts;
        this.AnimationproductsLoading = AnimationproductsLoading;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.email_user = email_user;
        this.arrayListDtomenu = arrayListDto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerCategorys.setVisibility(View.GONE);
        AnimationcategoryLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/category/list/");
        Category_Adapter category_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoCategory dtoCategorys = new DtoCategory();
                dtoCategorys.setCd_category(jsonArray.getJSONObject(i).getInt("cd_category"));
                dtoCategorys.setNm_category(jsonArray.getJSONObject(i).getString("nm_category"));
                dtoCategorys.setImg_category(jsonArray.getJSONObject(i).getString("img_category"));

                arrayListDto.add(dtoCategorys);
            }
            category_adapter = new Category_Adapter(arrayListDto);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return category_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerCategorys.setVisibility(View.VISIBLE);
        AnimationcategoryLoading.setVisibility(View.GONE);
        recyclerCategorys.setAdapter((RecyclerView.Adapter) category_adapter);

        recyclerCategorys.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerCategorys,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int cd_cat = arrayListDto.get(position).getCd_category();
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2,StaggeredGridLayoutManager.VERTICAL);
                        recyclerProducts.setLayoutManager(layoutManager);
                        arrayListDtomenu.clear();
                        AsyncProdCategory asyncProdCategory = new AsyncProdCategory(recyclerProducts, AnimationproductsLoading, arrayListDtomenu, email_user, SwipeRefreshProducts, cd_cat, contexto);
                        asyncProdCategory.execute();
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
