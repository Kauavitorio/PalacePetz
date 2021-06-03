package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.R;

public class CardsPurchase_Adapter extends RecyclerView.Adapter<CardsPurchase_Adapter.MyHolderCards> {
    static ArrayList<DtoCard> dtoCardArrayList;
    Context context;
    static int selectedItem = 0;
    static  int CardId = 0;
    //  Flags Images
    String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";
    String flag_elo = "https://www.kauavitorio.com/palacepetz/Cadastrar_Cartao/elo.png";
    String flag_hiperCard = "https://www.kauavitorio.com/palacepetz/Cadastrar_Cartao/hipercard.png";


    public CardsPurchase_Adapter(ArrayList<DtoCard> dtoCardArrayList, Context context) {
        CardsPurchase_Adapter.dtoCardArrayList = dtoCardArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderCards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cards_purchase, parent, false);
        return new MyHolderCards(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderCards holder, int position) {
        switch (dtoCardArrayList.get(position).getFlag_card()) {
            case "MasterCard":
                Picasso.get().load(flag_mastercard).into(holder.img_flag_card_purchase);
                break;
            case "Visa":
                Picasso.get().load(flag_visa).into(holder.img_flag_card_purchase);
                break;
            case "Maestro":
                Picasso.get().load(flag_maestro).into(holder.img_flag_card_purchase);
                break;
            case "Elo":
                Picasso.get().load(flag_elo).into(holder.img_flag_card_purchase);
                break;
            case "Hipercard":
                Picasso.get().load(flag_hiperCard).into(holder.img_flag_card_purchase);
                break;
        }
        String fullNumber = dtoCardArrayList.get(position).getNumber_card();
        String[]  SelectNumber = fullNumber.split(" ");
        String LastNumbers = SelectNumber[3];
        holder.txt_numberCard_Purchase.setText("•••• •••• •••• " + LastNumbers);


        if (position == selectedItem){
            SetCardInfo(holder.getAdapterPosition());
            holder.cardCards_FinishPurchase.setBackgroundResource(R.drawable.card_selected_container_cards_purchase);
        }else{
            holder.cardCards_FinishPurchase.setBackgroundResource(R.drawable.card_no_selected_container_cards_purchase);
            CardId = 0;
        }
    }

    private static void SetCardInfo(int holder) {
        CardId = dtoCardArrayList.get(holder).getCd_card();
    }

    @Override
    public int getItemCount() {
        return dtoCardArrayList.size();
    }

    class MyHolderCards extends RecyclerView.ViewHolder{
        TextView txt_numberCard_Purchase;
        ImageView img_flag_card_purchase;
        ConstraintLayout cardCards_FinishPurchase;


        public MyHolderCards(@NonNull View itemView) {
            super(itemView);
            txt_numberCard_Purchase = itemView.findViewById(R.id.txt_numberCard_Purchase);
            img_flag_card_purchase = itemView.findViewById(R.id.img_flag_card_purchase);
            cardCards_FinishPurchase = itemView.findViewById(R.id.cardCards_FinishPurchase);

            cardCards_FinishPurchase.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });


        }
    }

    public static int getCardId(){
        SetCardInfo(selectedItem);
        return CardId;
    }
}
