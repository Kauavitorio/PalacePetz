package co.kaua.palacepetz.Activitys.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Data.Schedule.AsyncScheduledServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.R;

@SuppressWarnings({"Convert2MethodRef", "unchecked"})
public class ScheduledServicesActivity extends AppCompatActivity {
    private RecyclerView RecyclerSchedules;
    private SwipeRefreshLayout SwipeScheduledServices;

    private int _IdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_services);
        Ids();
        DtoUser dtoUser = MainActivity.getInstance().GetUserBaseInformation();
        _IdUser = dtoUser.getId_user();
        AsyncScheduledServices asyncScheduledServices = new AsyncScheduledServices(RecyclerSchedules, ScheduledServicesActivity.this, _IdUser);
        asyncScheduledServices.execute();

        SwipeScheduledServices.setOnRefreshListener(() -> {
            AsyncScheduledServices asyncScheduledServices1 = new AsyncScheduledServices(RecyclerSchedules, ScheduledServicesActivity.this, _IdUser);
            asyncScheduledServices1.execute();
            SwipeScheduledServices.setRefreshing(false);
        });

    }

    private void Ids() {
        RecyclerSchedules = findViewById(R.id.RecyclerSchedules);
        SwipeScheduledServices = findViewById(R.id.SwipeScheduledServices);
        LinearLayoutManager linearLayout = new LinearLayoutManager(ScheduledServicesActivity.this);
        RecyclerSchedules.setLayoutManager(linearLayout);
    }
}