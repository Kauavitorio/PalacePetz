package co.kaua.palacepetz.Data.Purchase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Adapters.CardsPurchase_Adapter;
import co.kaua.palacepetz.Adapters.Cards_Adapter;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Data.Cards.AsyncCards;
import co.kaua.palacepetz.Data.Cards.CardService;
import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Methods.JsonHandler;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("StaticFieldLeak")
public class Async_PurchaseCard extends AsyncTask {
    ArrayList<DtoCard> arrayListDto;
    Activity contexto;
    ConstraintLayout container_noCard;
    RecyclerView recyclerCards;
    int id_user;
    LoadingDialog loadingDialog;

    //  Retrofit
    String baseurl = "https://palacepetzapi.herokuapp.com/user/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/remove/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Async_PurchaseCard(RecyclerView recyclerCards, ConstraintLayout container_noCard, Activity contexto,
                              int id_user) {
        this.recyclerCards = recyclerCards;
        this.contexto = contexto;
        this.container_noCard = container_noCard;
        this.loadingDialog = new LoadingDialog(contexto);
        this.id_user = id_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerCards.setVisibility(View.GONE);
        container_noCard.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/user/cards/list/" + id_user);
        CardsPurchase_Adapter cards_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoCard dtoCard = new DtoCard();
                dtoCard.setCd_card(jsonArray.getJSONObject(i).getInt("cd_card"));
                dtoCard.setFlag_card(jsonArray.getJSONObject(i).getString("flag_card"));
                dtoCard.setNumber_card(jsonArray.getJSONObject(i).getString("number_card"));
                dtoCard.setShelflife_card(jsonArray.getJSONObject(i).getString("shelflife_card"));
                dtoCard.setCvv_card(jsonArray.getJSONObject(i).getString("cvv_card"));
                dtoCard.setNmUser_card(jsonArray.getJSONObject(i).getString("nmUser_card"));

                arrayListDto.add(dtoCard);
            }
            cards_adapter = new CardsPurchase_Adapter(arrayListDto, contexto);
            cards_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return cards_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerCards.setVisibility(View.VISIBLE);
        container_noCard.setVisibility(View.GONE);
        //noinspection rawtypes
        recyclerCards.setAdapter((RecyclerView.Adapter) category_adapter);
        recyclerCards.getRecycledViewPool().clear();
    }
}