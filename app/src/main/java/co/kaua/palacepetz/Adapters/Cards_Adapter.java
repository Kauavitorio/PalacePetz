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

import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Cards_Adapter extends RecyclerView.Adapter<Cards_Adapter.MyHolderCards> {
    ArrayList<DtoCard> dtoCardArrayList;
    Context context;
    static int selectedItem = 0;
    //  Flags Images
    String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";
    String flag_elo = "https://www.kauavitorio.com/palacepetz/Cadastrar_Cartao/elo.png";
    String flag_hiperCard = "https://www.kauavitorio.com/palacepetz/Cadastrar_Cartao/hipercard.png";

    public Cards_Adapter(ArrayList<DtoCard> dtoCardArrayList, Context context) {
        this.dtoCardArrayList = dtoCardArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderCards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listcard_mycard, parent, false);
        return new MyHolderCards(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderCards holder, int position) {
        switch (dtoCardArrayList.get(position).getFlag_card()) {
            case "MasterCard":
                Picasso.get().load(flag_mastercard).into(holder.imgFlag_CardAdapter);
                holder.cardCardList.setCardBackgroundColor(context.getColor(R.color.mastercard));
                break;
            case "Visa":
                Picasso.get().load(flag_visa).into(holder.imgFlag_CardAdapter);
                holder.cardCardList.setCardBackgroundColor(context.getColor(R.color.visacard));
                break;
            case "Maestro":
                Picasso.get().load(flag_maestro).into(holder.imgFlag_CardAdapter);
                holder.cardCardList.setCardBackgroundColor(context.getColor(R.color.maestrocard));
                break;
            case "Elo":
                Picasso.get().load(flag_elo).into(holder.imgFlag_CardAdapter);
                holder.cardCardList.setCardBackgroundColor(context.getColor(R.color.elo));
                break;
            case "Hipercard":
                Picasso.get().load(flag_hiperCard).into(holder.imgFlag_CardAdapter);
                holder.cardCardList.setCardBackgroundColor(context.getColor(R.color.hipercard));
                break;
        }

        holder.cardCardList.setElevation(20);
        holder.txt_nmCard_cardRegister.setText(dtoCardArrayList.get(position).getNmUser_card());
        String fullNumber = dtoCardArrayList.get(position).getNumber_card();
        String[]  SelectNumber = fullNumber.split(" ");
        String LastNumbers = SelectNumber[3];
        holder.txt_numberCard_cardAdapter.setText("•••• " + LastNumbers);
    }

    @Override
    public int getItemCount() {
        return dtoCardArrayList.size();
    }

    class MyHolderCards extends RecyclerView.ViewHolder{
        TextView txt_numberCard_cardAdapter, txt_nmCard_cardRegister;
        ImageView imgFlag_CardAdapter;
        CardView cardCardList;


        public MyHolderCards(@NonNull View itemView) {
            super(itemView);
            txt_numberCard_cardAdapter = itemView.findViewById(R.id.txt_numberCard_cardAdapter);
            txt_nmCard_cardRegister = itemView.findViewById(R.id.txt_nmCard_cardRegister);
            imgFlag_CardAdapter = itemView.findViewById(R.id.imgFlag_CardAdapter);
            cardCardList = itemView.findViewById(R.id.cardCardList);

            cardCardList.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
