package co.kaua.palacepetz.Activitys.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Schedule.AsyncScheduledServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.R;

@SuppressWarnings({"unchecked"})
public class ScheduledServicesActivity extends AppCompatActivity {
    private static RecyclerView RecyclerSchedules;
    private SwipeRefreshLayout SwipeScheduledServices;
    private CardView btnBackScheduled;
    private static ScheduledServicesActivity instance;

    private static int _IdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_services);
        Ids();
        instance = this;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getBoolean("now"))
                Warnings.showScheduleIsSuccessful(ScheduledServicesActivity.this);
        }
        DtoUser dtoUser = MainActivity.getInstance().GetUserBaseInformation();
        _IdUser = dtoUser.getId_user();
        LoadSchedules();

        btnBackScheduled.setOnClickListener(v -> finish());

        SwipeScheduledServices.setOnRefreshListener(() -> {
            LoadSchedules();
            SwipeScheduledServices.setRefreshing(false);
        });

    }

    public static ScheduledServicesActivity getInstance(){ return instance; }

    public static void LoadSchedules(){
        AsyncScheduledServices asyncScheduledServices = new AsyncScheduledServices(RecyclerSchedules, getInstance(), _IdUser);
        asyncScheduledServices.execute();
    }

    private void Ids() {
        RecyclerSchedules = findViewById(R.id.RecyclerSchedules);
        SwipeScheduledServices = findViewById(R.id.SwipeScheduledServices);
        btnBackScheduled = findViewById(R.id.btnBackScheduled);
        LinearLayoutManager linearLayout = new LinearLayoutManager(ScheduledServicesActivity.this);
        RecyclerSchedules.setLayoutManager(linearLayout);
    }
}