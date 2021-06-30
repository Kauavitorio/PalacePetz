package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import co.kaua.palacepetz.R;

public class Developers extends AppCompatActivity {
    private CardView btnBackDevs;

    //  Portfolio Btn
    ConstraintLayout ViewPortfolio_kaua, ViewPortfolio_matheus, ViewPortfolio_pedro, ViewPortfolio_Danilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        Ids();
        btnBackDevs.setOnClickListener(v -> finish());

        //  Kauã CLick
        ViewPortfolio_kaua.setOnClickListener(v -> {
            Uri portfolio = Uri.parse("https://www.kauavitorio.com");
            Intent i = new Intent(Intent.ACTION_VIEW, portfolio);
            startActivity(i);
        });

        //  Matheus CLick
        ViewPortfolio_matheus.setOnClickListener(v -> {
            Uri portfolio = Uri.parse("https://www.linkedin.com/in/matheusssleite/");
            Intent i = new Intent(Intent.ACTION_VIEW, portfolio);
            startActivity(i);
        });

        //  Pedro CLick
        ViewPortfolio_pedro.setOnClickListener(v -> {
           /* Uri portfolio = Uri.parse("https://www.linkedin.com/in/matheusssleite/");
            Intent i = new Intent(Intent.ACTION_VIEW, portfolio);
            startActivity(i);*/
        });

        //  Danilo CLick
        ViewPortfolio_Danilo.setOnClickListener(v -> {
            Uri portfolio = Uri.parse("https://github.com/daniloosi");
            Intent i = new Intent(Intent.ACTION_VIEW, portfolio);
            startActivity(i);
        });
    }

    private void Ids() {
        btnBackDevs = findViewById(R.id.btnBackDevs);
        ViewPortfolio_kaua = findViewById(R.id.ViewPortfolio_kaua);
        ViewPortfolio_matheus = findViewById(R.id.ViewPortfolio_matheus);
        ViewPortfolio_pedro = findViewById(R.id.ViewPortfolio_pedro);
        ViewPortfolio_Danilo = findViewById(R.id.ViewPortfolio_Danilo);
    }
}