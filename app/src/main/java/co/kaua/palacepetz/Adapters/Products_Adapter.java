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

import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyHolderProducts> {
    ArrayList<DtoProducts> dtoProductsArrayList;
    Context context;

    public Products_Adapter(ArrayList<DtoProducts> dtoProductsArrayList, Context context) {
        this.dtoProductsArrayList = dtoProductsArrayList;
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
        Picasso.get().load(dtoProductsArrayList.get(position).getImage_prod()).into(holder.img_product);
        holder.txt_product_name_allProducts.setText(dtoProductsArrayList.get(position).getNm_product());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txt_product_price_allProducts.setText("R$ " + numberFormat.format(dtoProductsArrayList.get(position).getProduct_price()));
        holder.container_allProduct.setElevation(20);
        switch (dtoProductsArrayList.get(position).getNm_category()){
            case  "Alimentos":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_racoes) );
                break;
            case  "Acessórios":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_accessories) );
                break;
            case  "Medicamentos":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_medicines) );
                break;
        }
        if (dtoProductsArrayList.get(position).getAmount() <= 10)
            holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_low_stock) );
    }

    @Override
    public int getItemCount() {
        return dtoProductsArrayList.size();
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
