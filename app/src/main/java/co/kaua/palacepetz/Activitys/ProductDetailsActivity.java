package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.Data.Products.ProductsServices;
import co.kaua.palacepetz.Data.ShoppingCart.CartServices;
import co.kaua.palacepetz.Data.ShoppingCart.DtoShoppingCart;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity {
    private LottieAnimationView arrowGoBack_ProductDetails;
    private TextView txt_product_name, txt_desc_prod, txt_price_product, txtQt_prod, txt_AddToCart;
    private CardView btnLessQT_Prod, btnPlusQT_Prod, cardBtn_AddToCart;
    private ImageView imgProductDetails;

    //  Product information
    private int qt_prod = 1;
    private int qt_prodGet;
    private float unit_prod_price;
    @SuppressWarnings("FieldCanBeLocal")
    private float full_prod_price = unit_prod_price;
    private LoadingDialog loadingDialog;
    int cd_prod;
    private String ToastAlert;
    String image_prod, nm_product, description;

    //  User information
    String _Email;
    int _IdUser;

    //  Retrofit
    final Retrofit CartRetrofit = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTheme(R.style.DevicePresentation);
        Ids();
        ToastAlert = getString(R.string.maximum_amount_reached);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);

        Bundle bundle = getIntent().getExtras();
        DtoUser userInfo = MainActivity.getInstance().GetUserBaseInformation();
        _Email = userInfo.getEmail();
        _IdUser = userInfo.getId_user();
        cd_prod = bundle.getInt("cd_prod");
        if (bundle.getString("nm_product") == null){
            loadingDialog = new LoadingDialog(ProductDetailsActivity.this);
            loadingDialog.startLoading();
            ProductsServices productsServices = CartRetrofit.create(ProductsServices.class);
            Call<DtoProducts> call = productsServices.getDetails(cd_prod);
            call.enqueue(new Callback<DtoProducts>() {
                @Override
                public void onResponse(@NonNull Call<DtoProducts> call, @NonNull Response<DtoProducts> response) {
                    if(response.code() == 200){
                        loadingDialog.dimissDialog();
                        assert response.body() != null;
                        image_prod = response.body().getImage_prod();
                        nm_product = response.body().getNm_product();
                        description = response.body().getDescription();
                        unit_prod_price = response.body().getProduct_price();
                        qt_prodGet = response.body().getAmount();
                        loadProdsTexts();
                        SaveOnHistoric();
                        setNewPrice(numberFormat);
                        if(qt_prodGet <= 0){
                            txt_desc_prod.setText(getString(R.string.warning_no_stock));
                            txtQt_prod.setText(0 + "");
                            txt_AddToCart.setText(getString(R.string.no_stock));
                            btnLessQT_Prod.setEnabled(false);
                            btnPlusQT_Prod.setEnabled(false);
                            cardBtn_AddToCart.setEnabled(false);
                        }
                    }else{
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(ProductDetailsActivity.this);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DtoProducts> call, @NonNull Throwable t) {
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(ProductDetailsActivity.this);
                }
            });
        }else{
            image_prod = bundle.getString("image_prod");
            nm_product = bundle.getString("nm_product");
            description = bundle.getString("description");
            unit_prod_price = bundle.getFloat("product_price");
            qt_prodGet = bundle.getInt("amount");
            loadProdsTexts();
            SaveOnHistoric();
            setNewPrice(numberFormat);
            if(qt_prodGet <= 0){
                txt_desc_prod.setText(getString(R.string.warning_no_stock));
                txtQt_prod.setText(0 + "");
                txt_AddToCart.setText(getString(R.string.no_stock));
                btnLessQT_Prod.setEnabled(false);
                btnPlusQT_Prod.setEnabled(false);
                cardBtn_AddToCart.setEnabled(false);
            }
        }

        if (qt_prodGet < 20)
            ToastAlert = getString(R.string.maximum_amount_reached_no_stock, qt_prodGet + "");


        btnLessQT_Prod.setOnClickListener(v -> {
            if (qt_prod == 1)
                ToastHelper.toast(ProductDetailsActivity.this, getString(R.string.one_is_the_minumum_quantity));
            else{
                qt_prod--;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        btnPlusQT_Prod.setOnClickListener(v -> {
            if (qt_prod == qt_prodGet || qt_prod == 20)
                ToastHelper.toast(ProductDetailsActivity.this, ToastAlert);
            else {
                qt_prod++;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        arrowGoBack_ProductDetails.setOnClickListener(v -> finish());

        cardBtn_AddToCart.setOnClickListener(v -> {
            if (_IdUser != 0){
                loadingDialog = new LoadingDialog(ProductDetailsActivity.this);
                loadingDialog.startLoading();
                DtoShoppingCart cartItems = new DtoShoppingCart(cd_prod, _IdUser, qt_prod, String.valueOf(unit_prod_price), String.valueOf(full_prod_price), String.valueOf(full_prod_price));
                CartServices cartServices = CartRetrofit.create(CartServices.class);
                Call<DtoShoppingCart> cartCall = cartServices.insertItemOnCart(cartItems);
                cartCall.enqueue(new Callback<DtoShoppingCart>() {
                    @Override
                    public void onResponse(@NonNull Call<DtoShoppingCart> call, @NonNull Response<DtoShoppingCart> response) {
                        if(response.code() == 201){
                            loadingDialog.dimissDialog();
                            ToastHelper.toast(ProductDetailsActivity.this, getString(R.string.product_successfully_inserted));
                            finish();
                        }else if(response.code() == 409){
                            loadingDialog.dimissDialog();
                            ToastHelper.toast(ProductDetailsActivity.this, getString(R.string.product_into_your_cart));
                        }
                        else{
                            loadingDialog.dimissDialog();
                            Warnings.showWeHaveAProblem(ProductDetailsActivity.this);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<DtoShoppingCart> call, @NonNull Throwable t) {
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(ProductDetailsActivity.this);
                    }
                });
            }else
                Warnings.NeedLoginAlert(ProductDetailsActivity.this);
        });
    }

    private void SaveOnHistoric() {
        if (_IdUser != 0 ){
            ProductsServices services = CartRetrofit.create(ProductsServices.class);
            Call<DtoProducts> call = services.saveOnHistoric(_IdUser, cd_prod);
            call.enqueue(new Callback<DtoProducts>() {
                @Override
                public void onResponse(@NonNull Call<DtoProducts> call, @NonNull Response<DtoProducts> response) {
                    if (response.isSuccessful()) Log.d("Historic", "Successfully saved"); }
                @Override
                public void onFailure(@NonNull Call<DtoProducts> call, @NonNull Throwable t) { Log.d("Historic", "Error on save"); }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadProdsTexts() {
        txt_product_name.setText(nm_product);
        txt_desc_prod.setText(description);
        txt_price_product.setText("R$ " + unit_prod_price);
        txtQt_prod.setText(qt_prod + "");
        Picasso.get().load(image_prod).into(imgProductDetails);
    }

    private void Ids() {
        arrowGoBack_ProductDetails = findViewById(R.id.arrowGoBack_ProductDetails);
        btnLessQT_Prod = findViewById(R.id.btnLessQT_Prod);
        cardBtn_AddToCart = findViewById(R.id.cardBtn_AddToCart);
        btnPlusQT_Prod = findViewById(R.id.btnPlusQT_Prod);
        txtQt_prod = findViewById(R.id.txtQt_prod);
        txt_price_product = findViewById(R.id.txt_price_product);
        imgProductDetails = findViewById(R.id.imgProductDetails);
        txt_AddToCart = findViewById(R.id.txt_AddToCart);
        txt_desc_prod = findViewById(R.id.txt_desc_prod);
        txt_product_name = findViewById(R.id.txt_product_name);
    }

    @SuppressLint("SetTextI18n")
    private void setNewPrice(NumberFormat numberFormat) {
        full_prod_price = unit_prod_price * qt_prod;
        txt_price_product.setText("R$ " + numberFormat.format(full_prod_price));
    }

    @SuppressLint("SetTextI18n")
    private void RefreshQtText() {
        txtQt_prod.setText(qt_prod + "");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}