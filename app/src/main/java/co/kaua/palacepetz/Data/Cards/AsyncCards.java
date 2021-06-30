package co.kaua.palacepetz.Data.Cards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import co.kaua.palacepetz.Adapters.Cards_Adapter;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Fragments.MyCardsFragment;
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
    int id_user;
    LoadingDialog loadingDialog;

    //  Retrofit
    String baseurl = "https://palacepetzapi.herokuapp.com/user/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/remove/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AsyncCards(RecyclerView recyclerCards, ConstraintLayout container_noCard, Activity contexto, int id_user) {
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
        Cards_Adapter cards_adapter = null;
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
                                .setMessage(R.string.remove_registred_card)
                                .setPositiveButton(R.string.yes, (dialog, which) -> {
                                    loadingDialog.startLoading();
                                    int cd_card = arrayListDto.get(position).getCd_card();
                                    CardService cardService = retrofitCard.create(CardService.class);
                                    Call<DtoCard> menuCall = cardService.removeCard(id_user, cd_card);
                                    menuCall.enqueue(new Callback<DtoCard>() {
                                        @Override
                                        public void onResponse(Call<DtoCard> call, Response<DtoCard> response) {
                                            if (response.code() == 200){
                                                arrayListDto.remove(position);
                                                MyCardsFragment.getInstance().getCardsInformation();
                                                loadingDialog.dimissDialog();
                                            }else if (response.code() == 417){
                                                loadingDialog.dimissDialog();
                                                Toast.makeText(contexto, R.string.card_not_found, Toast.LENGTH_SHORT).show();
                                            }else{
                                                loadingDialog.dimissDialog();
                                                Warnings.showWeHaveAProblem(contexto);
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                                            loadingDialog.dimissDialog();
                                            Warnings.showWeHaveAProblem(contexto);
                                        }
                                    });
                                })
                                .setNeutralButton(R.string.no, null);
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