package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.kaua.palacepetz.Data.MyOrders.DtoOrder;
import co.kaua.palacepetz.R;

public class MyOrders_Adapter extends RecyclerView.Adapter<MyOrders_Adapter.MyHolderCards> {
    ArrayList<DtoOrder> dtoOrders;
    Context context;
    static int selectedItem = 0;

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
        holder.txt_order_payment.setText(context.getString(R.string.payment) + ": •••• •••• " + cardNumber[3]);
        holder.txt_order_status.setText(context.getString(R.string.status) + ": " + dtoOrders.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return dtoOrders.size();
    }

    static class MyHolderCards extends RecyclerView.ViewHolder{
        TextView txt_order_id, txt_order_date, txt_order_payment, txt_order_status;


        public MyHolderCards(@NonNull View itemView) {
            super(itemView);
            txt_order_id = itemView.findViewById(R.id.txt_order_id);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_order_payment = itemView.findViewById(R.id.txt_order_payment);
            txt_order_status = itemView.findViewById(R.id.txt_order_status);
        }
    }
}
