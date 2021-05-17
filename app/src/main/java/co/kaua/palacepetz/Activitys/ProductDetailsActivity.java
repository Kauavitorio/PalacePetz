package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import co.kaua.palacepetz.R;

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
    int cd_prod;
    String image_prod, nm_product, description;

    //  User information
    String _Email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTheme(R.style.DevicePresentation);
        Ids();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);

        Bundle bundle = getIntent().getExtras();
        _Email = bundle.getString("email_user");
        if(bundle.getString("image_prod") == null || bundle.getString("image_prod").length() <= 8){
            Log.d("LoadUrl", "LoadfromQrCode");
        }else{
            cd_prod = bundle.getInt("cd_prod");
            image_prod = bundle.getString("image_prod");
            nm_product = bundle.getString("nm_product");
            description = bundle.getString("description");
            unit_prod_price = bundle.getFloat("product_price");
            qt_prodGet = bundle.getInt("amount");
            loadProdsTexts();
            if(qt_prodGet <= 0){
                txt_desc_prod.setText(getString(R.string.warning_no_stock));
                txtQt_prod.setText(0 + "");
                txt_AddToCart.setText(getString(R.string.no_stock));
                btnLessQT_Prod.setEnabled(false);
                btnPlusQT_Prod.setEnabled(false);
                cardBtn_AddToCart.setEnabled(false);
            }
        }


        btnLessQT_Prod.setOnClickListener(v -> {
            if (qt_prod == 1){
                Toast.makeText(this, R.string.one_is_the_minumum_quantity, Toast.LENGTH_SHORT).show();
            }else{
                qt_prod--;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        btnPlusQT_Prod.setOnClickListener(v -> {
            if (qt_prod == qt_prodGet || qt_prod == 20){
                Toast.makeText(this, R.string.maximum_amount_reached, Toast.LENGTH_SHORT).show();
            }else {
                qt_prod++;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        arrowGoBack_ProductDetails.setOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void loadProdsTexts() {
        txt_product_name.setText(nm_product);
        txt_desc_prod.setText(description);
        txt_price_product.setText(unit_prod_price + "");
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