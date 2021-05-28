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

import co.kaua.palacepetz.Data.Historic.DtoHistoric;
import co.kaua.palacepetz.R;

public class Historic_Adapter extends RecyclerView.Adapter<Historic_Adapter.MyHolderHistoric> {
    ArrayList<DtoHistoric> historicArrayList;
    Context context;
    static int selectedItem = 0;

    public Historic_Adapter(ArrayList<DtoHistoric> historicArrayList, Context context) {
        this.historicArrayList = historicArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderHistoric onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_historic, parent, false);
        return new MyHolderHistoric(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderHistoric holder, int position) {
        holder.card_adapter_historic.setElevation(19);
        Picasso.get().load(historicArrayList.get(position).getImage_prod()).into(holder.img_product_historic);
        holder.txt_product_name_historic.setText(historicArrayList.get(position).getNm_product());
        holder.txt_product_price_historic.setText(historicArrayList.get(position).getProduct_price());
        holder.txt_product_accessed_at_historic.setText( context.getString(R.string.product_accessed_at) + "\n" + historicArrayList.get(position).getDatetime());

    }

    @Override
    public int getItemCount() {
        return historicArrayList.size();
    }

    static class MyHolderHistoric extends RecyclerView.ViewHolder{
        TextView txt_product_name_historic, txt_product_price_historic, txt_product_accessed_at_historic;
        ImageView img_product_historic;
        CardView card_adapter_historic;


        public MyHolderHistoric(@NonNull View itemView) {
            super(itemView);
            card_adapter_historic = itemView.findViewById(R.id.card_adapter_historic);
            img_product_historic = itemView.findViewById(R.id.img_product_historic);
            txt_product_name_historic = itemView.findViewById(R.id.txt_product_name_historic);
            txt_product_price_historic = itemView.findViewById(R.id.txt_product_price_historic);
            txt_product_accessed_at_historic = itemView.findViewById(R.id.txt_product_accessed_at_historic);
        }
    }
}
