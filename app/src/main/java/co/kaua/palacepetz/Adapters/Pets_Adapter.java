package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Data.Pets.DtoPets;
import co.kaua.palacepetz.R;

public class Pets_Adapter extends RecyclerView.Adapter<Pets_Adapter.MyHolderPets> {
    ArrayList<DtoPets> dtoArrayList;
    Context context;
    static int selectedItem = 0;

    public Pets_Adapter(ArrayList<DtoPets> dtoArrayList, Context context) {
        this.dtoArrayList = dtoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderPets onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pets, parent, false);
        return new MyHolderPets(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderPets holder, int position) {
        Picasso.get().load(dtoArrayList.get(position).getImage_animal()).into(holder.img_pet_adapter);
        holder.txt_petName.setText(dtoArrayList.get(position).getNm_animal());
        holder.petAge_adapter.setText(context.getString(R.string.age) + ": " + dtoArrayList.get(position).getAge_animal());
        holder.petWeight_adapter.setText(context.getString(R.string.weight) + ": " + dtoArrayList.get(position).getWeight_animal());
        holder.petSpecies_adapter.setText(context.getString(R.string.species) + ": " + dtoArrayList.get(position).getSpecies_animal());
        holder.petBreed_adapter.setText(context.getString(R.string.breed) + ": " + dtoArrayList.get(position).getBreed_animal());
    }

    @Override
    public int getItemCount() {
        return dtoArrayList.size();
    }

    class MyHolderPets extends RecyclerView.ViewHolder{
        TextView txt_petName, petAge_adapter, petWeight_adapter, petSpecies_adapter, petBreed_adapter;
        ImageView img_pet_adapter;
        CardView cardCardList;


        public MyHolderPets(@NonNull View itemView) {
            super(itemView);
            img_pet_adapter = itemView.findViewById(R.id.img_pet_adapter);
            txt_petName = itemView.findViewById(R.id.txt_petName);
            petAge_adapter = itemView.findViewById(R.id.petAge_adapter);
            petWeight_adapter = itemView.findViewById(R.id.petWeight_adapter);
            petSpecies_adapter = itemView.findViewById(R.id.petSpecies_adapter);
            petBreed_adapter = itemView.findViewById(R.id.petBreed_adapter);
        }
    }
}
