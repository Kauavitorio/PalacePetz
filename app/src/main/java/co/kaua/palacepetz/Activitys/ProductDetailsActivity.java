package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.text.NumberFormat;
import java.util.Locale;

import co.kaua.palacepetz.R;

public class ProductDetailsActivity extends AppCompatActivity {
    private LottieAnimationView arrowGoBack_ProductDetails;
    private TextView txt_product_name, txt_desc_prod, txt_price_product, txtQt_prod;
    private CardView btnLessQT_Prod, btnPlusQT_Prod, cardBtn_AddToCart;
    private ImageView imgProductDetails;

    //  Product Info
    private int qt_prod = 1;
    private int qt_prodGet;
    private float unit_prod_price;
    private float full_prod_price = unit_prod_price;

    //  User information
    String _Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTheme(R.style.DevicePresentation);
        Ids();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        _Email = bundle.getString("email_user");

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

    private void Ids() {
        arrowGoBack_ProductDetails = findViewById(R.id.arrowGoBack_ProductDetails);
        btnLessQT_Prod = findViewById(R.id.btnLessQT_Prod);
        btnPlusQT_Prod = findViewById(R.id.btnPlusQT_Prod);
        txtQt_prod = findViewById(R.id.txtQt_prod);
        txt_price_product = findViewById(R.id.txt_price_product);
        imgProductDetails = findViewById(R.id.imgProductDetails);
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