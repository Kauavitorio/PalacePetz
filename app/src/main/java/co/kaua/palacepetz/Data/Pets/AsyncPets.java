package co.kaua.palacepetz.Data.Pets;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.Pets.RegisterPetActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Pets_Adapter;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Products.RecyclerItemClickListener;
import co.kaua.palacepetz.Methods.JsonHandler;

@SuppressWarnings({"rawtypes", "StaticFieldLeak", "FieldCanBeLocal", "deprecation"})
public class    AsyncPets extends AsyncTask {
    private final int id_user;
    private final RecyclerView recyclerAllPets;
    private final Activity context;
    private ArrayList<DtoPets> arrayListDto;
    private final LoadingDialog loadingDialog;

    public AsyncPets(int id_user, RecyclerView recyclerAllPets, Activity context) {
        this.id_user = id_user;
        this.recyclerAllPets = recyclerAllPets;
        this.context = context;
        this.loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.startLoading();
        recyclerAllPets.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://palacepetzapi.herokuapp.com/user/pet/" + id_user);
        Pets_Adapter pets_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoPets dtoPets = new DtoPets();
                dtoPets.setCd_animal(jsonArray.getJSONObject(i).getInt("cd_animal"));
                dtoPets.setNm_animal(jsonArray.getJSONObject(i).getString("nm_animal"));
                dtoPets.setId_user(jsonArray.getJSONObject(i).getInt("id_user"));
                dtoPets.setBreed_animal(jsonArray.getJSONObject(i).getString("breed_animal"));
                dtoPets.setAge_animal(jsonArray.getJSONObject(i).getString("age_animal"));
                dtoPets.setWeight_animal(jsonArray.getJSONObject(i).getString("weight_animal"));
                dtoPets.setSpecies_animal(jsonArray.getJSONObject(i).getString("species_animal"));
                dtoPets.setImage_animal(jsonArray.getJSONObject(i).getString("image_animal"));

                arrayListDto.add(dtoPets);
            }
            pets_adapter = new Pets_Adapter(arrayListDto, context);
            pets_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return pets_adapter;
    }

    @Override
    protected void onPostExecute(Object pets_adapter) {
        //noinspection unchecked
        super.onPostExecute(pets_adapter);
        loadingDialog.dimissDialog();
        recyclerAllPets.setVisibility(View.VISIBLE);
        recyclerAllPets.setAdapter((RecyclerView.Adapter) pets_adapter);
        recyclerAllPets.getRecycledViewPool().clear();

        recyclerAllPets.addOnItemTouchListener(new RecyclerItemClickListener(context,
                recyclerAllPets, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int cd_pet = arrayListDto.get(position).getCd_animal();
                Intent i = new Intent(context, RegisterPetActivity.class);
                i.putExtra("cd_animal", cd_pet);
                i.putExtra("id_user", id_user);
                i.putExtra("petName", arrayListDto.get(position).getNm_animal());
                i.putExtra("img_pet", arrayListDto.get(position).getImage_animal());
                i.putExtra("breed_animal", arrayListDto.get(position).getBreed_animal());
                i.putExtra("age_animal", arrayListDto.get(position).getAge_animal());
                i.putExtra("weight_animal", arrayListDto.get(position).getWeight_animal());
                i.putExtra("species_animal", arrayListDto.get(position).getSpecies_animal());
                context.startActivity(i);
            }
            @Override
            public void onLongItemClick(View view, int position) {
                Warnings.DeletePetAlert(context, arrayListDto.get(position).getCd_animal(), id_user);
            }
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        }));
    }
}
