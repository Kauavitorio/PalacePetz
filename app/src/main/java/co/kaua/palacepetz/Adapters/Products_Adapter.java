package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import co.kaua.palacepetz.Activitys.ProductDetailsActivity;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyHolderProducts> {
    ArrayList<DtoProducts> items;
    Context context;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    static int selectedItem = 9999;

    public Products_Adapter(ArrayList<DtoProducts> items, Context context) {
        this.items = items;
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
        Picasso.get().load(items.get(position).getImage_prod()).into(holder.img_product);
        holder.txt_product_name_allProducts.setText(items.get(position).getNm_product());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txt_product_price_allProducts.setText("R$ " + numberFormat.format(items.get(position).getProduct_price()));
        holder.container_allProduct.setElevation(20);
        switch (items.get(position).getNm_category()){
            case  "Alimentos":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_racoes) );
                break;
            case  "Acessórios":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_accessories) );
                break;
            case  "Medicamentos":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_medicines) );
                break;
            case  "Estética":
                holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_aesthetics) );
                break;
        }
        if (items.get(position).getAmount() <= 0)
            holder.container_allProduct.setBackground( context.getDrawable(R.drawable.background_low_stock) );

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

        if (position == selectedItem){
            Animation anim;
            if (position % 2 == 0)
                anim = AnimationUtils.loadAnimation(context,R.anim.anim_select_product);
            else
                anim = AnimationUtils.loadAnimation(context,R.anim.anim_select_product_odd);

            holder.container_allProduct.setAnimation(anim);
            selectedItem = 9999;
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent goToDetails = new Intent(context, ProductDetailsActivity.class);
                goToDetails.putExtra("cd_prod", items.get(position).getCd_prod());
                goToDetails.putExtra("image_prod", items.get(position).getImage_prod());
                goToDetails.putExtra("nm_product", items.get(position).getNm_product());
                goToDetails.putExtra("description", items.get(position).getDescription());
                goToDetails.putExtra("product_price", items.get(position).getProduct_price());
                goToDetails.putExtra("amount", items.get(position).getAmount());
                context.startActivity(goToDetails);
            },300);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txt_product_name_allProducts, txt_product_price_allProducts;
        ImageView img_product;
        ConstraintLayout container_allProduct;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txt_product_name_allProducts = itemView.findViewById(R.id.txt_product_name_allProducts);
            txt_product_price_allProducts = itemView.findViewById(R.id.txt_product_price_allProducts);
            img_product = itemView.findViewById(R.id.img_product);
            container_allProduct = itemView.findViewById(R.id.container_allProduct);

            container_allProduct.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
