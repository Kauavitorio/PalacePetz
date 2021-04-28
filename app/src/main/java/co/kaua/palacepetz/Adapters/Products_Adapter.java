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

import co.kaua.palacepetz.Data.Products.DtoMenu;
import co.kaua.palacepetz.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyHolderProducts> {
    ArrayList<DtoMenu> dtoMenuArrayList;
    Context context;

    public Products_Adapter(ArrayList<DtoMenu> dtoMenuArrayList, Context context) {
        this.dtoMenuArrayList = dtoMenuArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_all_products, parent, false);
        return new MyHolderProducts(listItem);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MyHolderProducts holder, int position) {
        Picasso.get().load(dtoMenuArrayList.get(position).getImg_prod_st()).into(holder.img_product);
        holder.txt_product_name_allProducts.setText(dtoMenuArrayList.get(position).getNm_prod());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txt_product_price_allProducts.setText("R$ " + numberFormat.format(dtoMenuArrayList.get(position).getPrice_prod()));
        holder.container_allProduct.setElevation(20);
        switch (dtoMenuArrayList.get(position).getNm_cat()){
            case  "Frappuccino":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_racoes) );
                break;
            case  "Donuts":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_medicines) );
                break;
            case  "Sucos":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_accessories) );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dtoMenuArrayList.size();
    }

    static class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txt_product_name_allProducts, txt_product_price_allProducts;
        ImageView img_product;
        ConstraintLayout container_allProduct;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txt_product_name_allProducts = itemView.findViewById(R.id.txt_product_name_allProducts);
            txt_product_price_allProducts = itemView.findViewById(R.id.txt_product_price_allProducts);
            img_product = itemView.findViewById(R.id.img_product);
            container_allProduct = itemView.findViewById(R.id.container_allProduct);
        }
    }
}
