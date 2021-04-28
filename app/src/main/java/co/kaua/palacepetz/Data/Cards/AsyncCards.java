package co.kaua.palacepetz.Data.Cards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import co.kaua.palacepetz.Adapters.Cards_Adapter;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Methods.JsonHandler;
import co.kaua.palacepetz.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncCards extends AsyncTask {
    ArrayList<DtoCard> arrayListDto;
    Activity contexto;
    ConstraintLayout container_noCard;
    RecyclerView recyclerCards;
    String _Email;
    LoadingDialog loadingDialog;

    //  Retrofit
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/remove/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AsyncCards(RecyclerView recyclerCards, ConstraintLayout container_noCard, Activity contexto, String _Email) {
        this.recyclerCards = recyclerCards;
        this.contexto = contexto;
        this.container_noCard = container_noCard;
        this.loadingDialog = new LoadingDialog(contexto);
        this._Email = _Email;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerCards.setVisibility(View.GONE);
        container_noCard.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/card/" + _Email);
        Cards_Adapter cards_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Cards");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoCard dtoCard = new DtoCard();
                dtoCard.setCd_card(jsonArray.getJSONObject(i).getInt("cd_card"));
                dtoCard.setNmUser_card(jsonArray.getJSONObject(i).getString("nmUser_card"));
                dtoCard.setFlag_card(jsonArray.getJSONObject(i).getString("flag_card"));
                dtoCard.setNumber_card(jsonArray.getJSONObject(i).getString("number_card"));

                arrayListDto.add(dtoCard);
            }
            cards_adapter = new Cards_Adapter(arrayListDto, contexto);
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
        recyclerCards.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerCards,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(contexto)
                                .setTitle(R.string.yours_cards)
                                .setMessage(R.string.what_would_you_this_card)
                                .setPositiveButton(R.string.remove, (dialog, which) -> {
                                    loadingDialog.startLoading();
                                    int cd_card = arrayListDto.get(position).getCd_card();
                                    CardService cardService = retrofitCard.create(CardService.class);
                                    Call<DtoCard> menuCall = cardService.removeCard(_Email, cd_card);
                                    menuCall.enqueue(new Callback<DtoCard>() {
                                        @Override
                                        public void onResponse(Call<DtoCard> call, Response<DtoCard> response) {
                                            if (response.code() == 202){
                                                arrayListDto.remove(position);
                                                AsyncCards asyncCards = new AsyncCards(recyclerCards, container_noCard, contexto, _Email);
                                                asyncCards.execute();
                                                loadingDialog.dimissDialog();
                                            }else if (response.code() == 417){
                                                loadingDialog.dimissDialog();
                                                Toast.makeText(contexto, R.string.card_not_found, Toast.LENGTH_SHORT).show();
                                            }else{
                                                loadingDialog.dimissDialog();
                                                Toast.makeText(contexto, R.string.weHaveAProblem, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                                            Toast.makeText(contexto, R.string.weHaveAProblem, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .setNeutralButton(R.string.leave, null);
                        alert.show();
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