package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.R;

public class PopularProducts_Adapter extends RecyclerView.Adapter<PopularProducts_Adapter.MyHolderProducts> {
    ArrayList<DtoProducts> dtoProductsArrayList;
    Context context;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public PopularProducts_Adapter(ArrayList<DtoProducts> dtoProductsArrayList, Context context) {
        this.dtoProductsArrayList = dtoProductsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_products, parent, false);
        return new MyHolderProducts(listItem);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MyHolderProducts holder, int position) {


        if (position % 2 == 0)
            // Here you apply the animation when the view is bound
            setAnimation2(holder.itemView, position);
        else
            // Here you apply the animation when the view is bound
            setAnimation(holder.itemView, position);

        Picasso.get().load(dtoProductsArrayList.get(position).getImage_prod()).into(holder.img_popularProduct);
        holder.txt_nameProd_popular.setText(dtoProductsArrayList.get(position).getNm_product());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txt_price_popularProduct.setText("R$ " + numberFormat.format(dtoProductsArrayList.get(position).getProduct_price()));
        holder.container_popularProducts.setElevation(10);
        switch (dtoProductsArrayList.get(position).getNm_category()){
            case  "Alimentos":
                holder.container_popularProducts.setBackground( context.getDrawable(R.drawable.background_racoes) );
                break;
            case  "Acessórios":
                holder.container_popularProducts.setBackground( context.getDrawable(R.drawable.background_accessories) );
                break;
            case  "Medicamentos":
                holder.container_popularProducts.setBackground( context.getDrawable(R.drawable.background_medicines) );
                break;
            case  "Estética":
                holder.container_popularProducts.setBackground( context.getDrawable(R.drawable.background_aesthetics) );
                break;
        }
        if (dtoProductsArrayList.get(position).getAmount() <= 2)
            holder.container_popularProducts.setBackground( context.getDrawable(R.drawable.background_low_stock) );

        String fullDesc = dtoProductsArrayList.get(position).getDescription();
        if (fullDesc.length() <= 63)
            holder.txt_desc_popularProduct.setText(fullDesc);
        else{
            StringBuilder shortDesc = new StringBuilder();
            String[] splitDesc = fullDesc.split("");
            for (int i = 0; i < 64; i++){
                shortDesc.append(splitDesc[i]);
            }
            holder.txt_desc_popularProduct.setText(shortDesc + "...");
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.move_to_left_popular);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setAnimation2(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.move_to_right_popular);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return dtoProductsArrayList.size();
    }

    static class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txt_nameProd_popular, txt_desc_popularProduct, txt_price_popularProduct;
        ImageView img_popularProduct;
        ConstraintLayout container_popularProducts;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txt_nameProd_popular = itemView.findViewById(R.id.txt_nameProd_popular);
            txt_desc_popularProduct = itemView.findViewById(R.id.txt_desc_popularProduct);
            txt_price_popularProduct = itemView.findViewById(R.id.txt_price_popularProduct);
            img_popularProduct = itemView.findViewById(R.id.img_popularProduct);
            container_popularProducts = itemView.findViewById(R.id.container_popularProducts);
        }
    }
}
