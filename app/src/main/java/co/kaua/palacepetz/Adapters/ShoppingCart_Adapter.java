package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Data.ShoppingCart.CartServices;
import co.kaua.palacepetz.Data.ShoppingCart.DtoShoppingCart;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("SetTextI18n")
public class ShoppingCart_Adapter extends RecyclerView.Adapter<ShoppingCart_Adapter.MyHolderProducts> {
    private final ArrayList<DtoShoppingCart> dtoProductsArrayList;
    private final Activity context;
    static int selectedItem = 1000000000;
    static int productAmount = 0;
    static int subOrPlus = 0;
    int _IdUser;
    int longClick = 0;
    static float TotalPrice;
    float valueGet;
    TextView txtTotal, txtOrderNow;
    private final LoadingDialog loadingDialog;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    RecyclerView recyclerShoppingCart;
    SwipeRefreshLayout SwipeRefreshShoppingCart;
    final Retrofit cartUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ShoppingCart_Adapter(ArrayList<DtoShoppingCart> dtoProductsArrayList, Activity context, TextView txtTotal, TextView txtOrderNow,
                                int _IdUser, RecyclerView recyclerShoppingCart, SwipeRefreshLayout SwipeRefreshShoppingCart) {
        this.dtoProductsArrayList = dtoProductsArrayList;
        this.context = context;
        this.txtTotal = txtTotal;
        this.txtOrderNow = txtOrderNow;
        this._IdUser = _IdUser;
        this.recyclerShoppingCart = recyclerShoppingCart;
        this.SwipeRefreshShoppingCart = SwipeRefreshShoppingCart;
        this.loadingDialog = new LoadingDialog(context);
    }

    @NonNull
    @Override
    public MyHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shoppingcart, parent, false);
        return new MyHolderProducts(listItem);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MyHolderProducts holder, int position) {

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

        Picasso.get().load(dtoProductsArrayList.get(position).getImage_prod()).into(holder.img_prod_shoppingCart);
        holder.txt_name_prod_shoppingCart.setText(dtoProductsArrayList.get(position).getNm_product());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txtQt_prod_shoppingCart_ad.setText((dtoProductsArrayList.get(position).getProduct_amount() < 10 ? "0" : "") + dtoProductsArrayList.get(position).getProduct_amount() + "");
        productAmount = dtoProductsArrayList.get(position).getProduct_amount();
        holder.txt_unit_price_prod_shoppingCart.setText("R$ " + dtoProductsArrayList.get(position).getProduct_price()
                + " (" + context.getString(R.string.unitary) + ")");
        holder.txt_full_price_prod_shoppingCart.setText("R$ " + numberFormat.format(Float.parseFloat(dtoProductsArrayList.get(position).getTotalPrice())));
        holder.card_adapter_shoppingCart.setElevation(15);
        UpdatePriceTexts(numberFormat);

        if (position == selectedItem){
            productAmount = dtoProductsArrayList.get(position).getProduct_amount();
            if (subOrPlus == 1)
                LessAmount(holder);
            if(subOrPlus == 2)
                PlusAmount(holder);

            if (longClick == 154){
                longClick = 0;
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle(dtoProductsArrayList.get(position).getNm_product())
                        .setMessage(R.string.what_would_you_this_product)
                        .setPositiveButton(R.string.remove, (dialog, which) -> {
                            loadingDialog.startLoading();
                            int cd_prod = dtoProductsArrayList.get(position).getCd_prod();
                            CartServices menuService = cartUser.create(CartServices.class);
                            Call<DtoShoppingCart> call = menuService.removeProd(_IdUser, cd_prod);
                           call.enqueue(new Callback<DtoShoppingCart>() {
                                @Override
                                public void onResponse(@NonNull Call<DtoShoppingCart> call, @NonNull Response<DtoShoppingCart> response) {
                                    if (response.code() == 200){
                                        loadingDialog.dimissDialog();
                                        MainActivity mainActivity = (MainActivity) context;
                                        mainActivity.CheckShoppingCart();
                                        mainActivity.ReOpenCart();
                                    }else if(response.code() == 417){
                                        loadingDialog.dimissDialog();
                                        MainActivity mainActivity = (MainActivity) context;
                                        mainActivity.CheckShoppingCart();
                                        mainActivity.ReOpenCart();
                                    }else{
                                        loadingDialog.dimissDialog();
                                        Warnings.showWeHaveAProblem(context);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<DtoShoppingCart> call, @NonNull Throwable t) {
                                    Warnings.showWeHaveAProblem(context);
                                }
                            });

                        })
                        .setNegativeButton(R.string.cancel, null);
                alert.show();
            }
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

    @SuppressLint("SetTextI18n")
    private void PlusAmount(@NonNull MyHolderProducts holder) {
        subOrPlus = 10000;

        String toastAlert;
        if (dtoProductsArrayList.get(holder.getAdapterPosition()).getAmount() < 20)
            toastAlert = context.getString(R.string.maximum_amount_reached_no_stock, dtoProductsArrayList.get(holder.getAdapterPosition()).getAmount() + "");
        else
            toastAlert = context.getString(R.string.maximum_amount_reached);

        if (productAmount == 20 || productAmount == dtoProductsArrayList.get(holder.getAdapterPosition()).getAmount())
            ToastHelper.toast(context, toastAlert);
        else{
            productAmount = dtoProductsArrayList.get(holder.getAdapterPosition()).getProduct_amount();
            productAmount++;
            dtoProductsArrayList.get(holder.getAdapterPosition()).setProduct_amount(productAmount);
            holder.txtQt_prod_shoppingCart_ad.setText((productAmount < 10 ? "0" : "") + productAmount + "");
            TotalPrice = Float.parseFloat(dtoProductsArrayList.get(holder.getAdapterPosition()).getProduct_price()) * productAmount;
            dtoProductsArrayList.get(holder.getAdapterPosition()).setTotalPrice(String.valueOf(TotalPrice));
            updatePrice(holder, TotalPrice);
            updateUserCat(dtoProductsArrayList.get(holder.getAdapterPosition()).getCd_prod(), productAmount, TotalPrice, TotalPrice);
        }
    }

    private void LessAmount(@NonNull MyHolderProducts holder) {
        subOrPlus = 10000;
        if (productAmount == 1)
            ToastHelper.toast(context, context.getString(R.string.one_is_the_minumum_quantity));
        else{
            productAmount = dtoProductsArrayList.get(holder.getAdapterPosition()).getProduct_amount();
            productAmount--;
            dtoProductsArrayList.get(holder.getAdapterPosition()).setProduct_amount(productAmount);
            holder.txtQt_prod_shoppingCart_ad.setText((productAmount < 10 ? "0" : "") + productAmount + "");
            TotalPrice = Float.parseFloat(dtoProductsArrayList.get(holder.getAdapterPosition()).getProduct_price()) * productAmount;
            dtoProductsArrayList.get(holder.getAdapterPosition()).setTotalPrice(String.valueOf(TotalPrice));
            updatePrice(holder, TotalPrice);
            updateUserCat(dtoProductsArrayList.get(holder.getAdapterPosition()).getCd_prod(), productAmount, TotalPrice, TotalPrice);
        }
    }

    private void updateUserCat(int cd_prod, int productAmount, float totalPrice, float SubTotal) {
        CartServices cartServices = cartUser.create(CartServices.class);
        Call<DtoShoppingCart> call = cartServices.UpdateCartWithNewAmount(cd_prod, productAmount, totalPrice, SubTotal, _IdUser);
        call.enqueue(new Callback<DtoShoppingCart>() {
            @Override
            public void onResponse(@NonNull Call<DtoShoppingCart> call, @NonNull Response<DtoShoppingCart> response) {}
            @Override
            public void onFailure(@NonNull Call<DtoShoppingCart> call, @NonNull Throwable t) {}
        });
    }

    private void updatePrice(@NonNull MyHolderProducts holder, float TotalPrice) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txt_full_price_prod_shoppingCart.setText("R$ " + numberFormat.format(TotalPrice));

        UpdatePriceTexts(numberFormat);
    }

    private void UpdatePriceTexts(NumberFormat numberFormat) {
        valueGet =  0;
        for (int i = 0; i < dtoProductsArrayList.size(); i++){
            valueGet += Float.parseFloat(dtoProductsArrayList.get(i).getTotalPrice());
        }
        txtTotal.setText(context.getString(R.string.total) + ": R$ " + numberFormat.format(valueGet));
        txtOrderNow.setText(context.getString(R.string.txt_continue) + " (" + numberFormat.format(valueGet) + ")");
    }

    @Override
    public int getItemCount() {
        return dtoProductsArrayList.size();
    }

    class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txt_name_prod_shoppingCart, txt_full_price_prod_shoppingCart, txt_unit_price_prod_shoppingCart, btnLessQT_Prod_shoppingCart_ad, btnPlusQT_Prod_shoppingCart_ad;
        ImageView img_prod_shoppingCart;
        TextView txtQt_prod_shoppingCart_ad;
        CardView card_adapter_shoppingCart;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txt_name_prod_shoppingCart = itemView.findViewById(R.id.txt_name_prod_shoppingCart);
            txt_full_price_prod_shoppingCart = itemView.findViewById(R.id.txt_full_price_prod_shoppingCart);
            txt_unit_price_prod_shoppingCart = itemView.findViewById(R.id.txt_unit_price_prod_shoppingCart);
            img_prod_shoppingCart = itemView.findViewById(R.id.img_prod_shoppingCart);
            txtQt_prod_shoppingCart_ad = itemView.findViewById(R.id.txtQt_prod_shoppingCart_ad);
            card_adapter_shoppingCart = itemView.findViewById(R.id.card_adapter_shoppingCart);
            btnLessQT_Prod_shoppingCart_ad = itemView.findViewById(R.id.btnLessQT_Prod_shoppingCart_ad);
            btnPlusQT_Prod_shoppingCart_ad = itemView.findViewById(R.id.btnPlusQT_Prod_shoppingCart_ad);

            btnLessQT_Prod_shoppingCart_ad.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                subOrPlus = 1;
                notifyDataSetChanged();
            });

            btnPlusQT_Prod_shoppingCart_ad.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                subOrPlus = 2;
                notifyDataSetChanged();
            });

            card_adapter_shoppingCart.setOnLongClickListener(v -> {
                selectedItem = getAdapterPosition();
                longClick = 154;
                notifyDataSetChanged();
                return false;
            });
        }
    }
}
