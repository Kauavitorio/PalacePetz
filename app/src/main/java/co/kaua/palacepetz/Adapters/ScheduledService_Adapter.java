package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.kaua.palacepetz.Data.Schedule.DtoSchedule;
import co.kaua.palacepetz.R;

public class ScheduledService_Adapter extends RecyclerView.Adapter<ScheduledService_Adapter.MyHolderSchedules> {
    ArrayList<DtoSchedule> dtoSchedule;
    Context context;
    static int selectedItem = 1000000000;

    public ScheduledService_Adapter(ArrayList<DtoSchedule> dtoSchedule, Context context) {
        this.dtoSchedule = dtoSchedule;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderSchedules onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scheduled_services, parent, false);
        return new MyHolderSchedules(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderSchedules holder, int position) {
        switch (dtoSchedule.get(position).getService_type()){
            case 1:
                holder.txt_title_service.setText(context.getString(R.string.veterinary_consultation));
                holder.txt_title_veterinary.setText(context.getString(R.string.veterinary) + ": " + dtoSchedule.get(position).getNm_veterinary());
                break;
            case 2:
                holder.txt_title_service.setText(context.getString(R.string.bath_and_tosa));
                holder.txt_title_veterinary.setVisibility(View.GONE);
                break;
        }
        holder.txt_title_animal.setText(context.getString(R.string.animal) + ": " + dtoSchedule.get(position).getNm_animal());
        holder.txt_title_time.setText(context.getString(R.string.time) + ": " + dtoSchedule.get(position).getTime_schedule());
        holder.txt_title_date.setText(context.getString(R.string.date) + ": " + dtoSchedule.get(position).getDate_schedule());

        if (position == selectedItem){
            Warnings.CancelSchedule(context, dtoSchedule.get(position).getCd_schedule());
            selectedItem = 100000000;
        }
    }

    @Override
    public int getItemCount() {
        return dtoSchedule.size();
    }

    class MyHolderSchedules extends RecyclerView.ViewHolder{
        TextView txt_title_service, txt_title_animal, txt_title_veterinary, txt_title_time, txt_title_date;
        LinearLayout btn_cancel_schedule;


        public MyHolderSchedules(@NonNull View itemView) {
            super(itemView);
            txt_title_service = itemView.findViewById(R.id.txt_title_service);
            txt_title_animal = itemView.findViewById(R.id.txt_title_animal);
            txt_title_veterinary = itemView.findViewById(R.id.txt_title_veterinary);
            txt_title_time = itemView.findViewById(R.id.txt_title_time);
            txt_title_date = itemView.findViewById(R.id.txt_title_date);
            btn_cancel_schedule = itemView.findViewById(R.id.btn_cancel_schedule);

            btn_cancel_schedule.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
