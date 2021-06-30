package co.kaua.palacepetz.Data.MyOrders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.MyOrders_Adapter;
import co.kaua.palacepetz.Methods.JsonHandler;


@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncOrders extends AsyncTask {
    ArrayList<DtoOrder> arrayListDto;
    Activity contexto;
    ConstraintLayout container_noOrder;
    RecyclerView recyclerOrders;
    SwipeRefreshLayout SwipeRefreshMyOrder;
    int id_user;
    LoadingDialog loadingDialog;

    public AsyncOrders(RecyclerView recyclerOrders, ConstraintLayout container_noOrder, Activity contexto, SwipeRefreshLayout SwipeRefresh, int id_user) {
        this.recyclerOrders = recyclerOrders;
        this.contexto = contexto;
        this.container_noOrder = container_noOrder;
        this.loadingDialog = new LoadingDialog(contexto);
        this.SwipeRefreshMyOrder = SwipeRefresh;
        this.id_user = id_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SwipeRefreshMyOrder.setRefreshing(true);
        recyclerOrders.setVisibility(View.GONE);
        container_noOrder.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/order/" + id_user);
        MyOrders_Adapter orders_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoOrder dtoOrder = new DtoOrder();
                dtoOrder.setCd_order(jsonArray.getJSONObject(i).getInt("cd_order"));
                dtoOrder.setId_user(jsonArray.getJSONObject(i).getInt("id_user"));
                dtoOrder.setDiscount(jsonArray.getJSONObject(i).getString("discount"));
                dtoOrder.setCoupom(jsonArray.getJSONObject(i).getString("coupom"));
                dtoOrder.setSub_total(jsonArray.getJSONObject(i).getString("sub_total"));
                dtoOrder.setTotalPrice(jsonArray.getJSONObject(i).getString("totalPrice"));
                dtoOrder.setDate_order(jsonArray.getJSONObject(i).getString("date_order"));
                dtoOrder.setCd_card(jsonArray.getJSONObject(i).getInt("cd_card"));
                dtoOrder.setStatus(jsonArray.getJSONObject(i).getString("status"));
                dtoOrder.setPayment(jsonArray.getJSONObject(i).getString("payment"));
                dtoOrder.setDeliveryTime(jsonArray.getJSONObject(i).getInt("deliveryTime"));

                arrayListDto.add(dtoOrder);
            }
            orders_adapter = new MyOrders_Adapter(arrayListDto, contexto);
            orders_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return orders_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerOrders.setVisibility(View.VISIBLE);
        container_noOrder.setVisibility(View.GONE);
        SwipeRefreshMyOrder.setRefreshing(false);
        //noinspection rawtypes
        recyclerOrders.setAdapter((RecyclerView.Adapter) category_adapter);
        recyclerOrders.getRecycledViewPool().clear();
    }
}