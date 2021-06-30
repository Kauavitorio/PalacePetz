package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.kaua.palacepetz.Activitys.FollowOrderActivity;
import co.kaua.palacepetz.Data.MyOrders.DtoOrder;
import co.kaua.palacepetz.R;

public class MyOrders_Adapter extends RecyclerView.Adapter<MyOrders_Adapter.MyHolderCards> {
    ArrayList<DtoOrder> dtoOrders;
    Context context;
    static int selectedItem = 453610654;

    public MyOrders_Adapter(ArrayList<DtoOrder> dtoOrders, Context context) {
        this.dtoOrders = dtoOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderCards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_myorders, parent, false);
        return new MyHolderCards(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderCards holder, int position) {
        holder.txt_order_id.setText(context.getString(R.string.order) + ": #" + dtoOrders.get(position).getCd_order());
        holder.txt_order_date.setText(context.getString(R.string.created_in) + ": " + dtoOrders.get(position).getDate_order());
        String[] cardNumber = dtoOrders.get(position).getPayment().split(" ");
        holder.txt_order_payment.setText(context.getString(R.string.payment) + ": •••• •••• •••• " + cardNumber[3]);
        holder.txt_order_status.setText(context.getString(R.string.status) + ": " + dtoOrders.get(position).getStatus());
        if(dtoOrders.get(position).getStatus().equals("Aguardando Aprovação")){
            holder.container_myOrders_adapter.setMinHeight(240);
            holder.btnTrackOrder.setVisibility(View.GONE);
        }

        if (position == selectedItem){
            selectedItem = 99999;
            Intent goTo_followOrder = new Intent(context, FollowOrderActivity.class);
            goTo_followOrder.putExtra("cd_order", dtoOrders.get(position).getCd_order());
            goTo_followOrder.putExtra("status", dtoOrders.get(position).getStatus());
            goTo_followOrder.putExtra("deliveryTime", dtoOrders.get(position).getDeliveryTime());
            context.startActivity(goTo_followOrder);
        }
    }

    @Override
    public int getItemCount() {
        return dtoOrders.size();
    }

    class MyHolderCards extends RecyclerView.ViewHolder{
        TextView txt_order_id, txt_order_date, txt_order_payment, txt_order_status;
        CardView btnTrackOrder;
        ConstraintLayout container_myOrders_adapter;

        public MyHolderCards(@NonNull View itemView) {
            super(itemView);
            txt_order_id = itemView.findViewById(R.id.txt_order_id);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_order_payment = itemView.findViewById(R.id.txt_order_payment);
            txt_order_status = itemView.findViewById(R.id.txt_order_status);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
            container_myOrders_adapter = itemView.findViewById(R.id.container_myOrders_adapter);

            btnTrackOrder.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
