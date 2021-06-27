package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Cards.AsyncCards;
import co.kaua.palacepetz.Data.MyOrders.AsyncOrders;
import co.kaua.palacepetz.Data.MyOrders.DtoOrder;
import co.kaua.palacepetz.Data.Purchase.DtoPurchase;
import co.kaua.palacepetz.Data.Purchase.PurchaseServices;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unchecked")
public class MyOrdersFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    private View view;
    private Bundle args;
    private RecyclerView recyclerView_MyOrders;
    private ConstraintLayout container_noOrder;
    private SwipeRefreshLayout SwipeRefreshMyOrders;
    private LoadingDialog loadingDialog;

    //  User information
    private int _IdUser;
    private String name_user, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Retrofit
    final Retrofit retrofitOrder = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/order/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_myorders, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _IdUser = args.getInt("id_user");
        loadOrders();

        SwipeRefreshMyOrders.setOnRefreshListener(this::loadOrders);

        return view;
    }

    private void loadOrders() {
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        PurchaseServices services = retrofitOrder.create(PurchaseServices.class);
        Call<DtoPurchase> call = services.getOrder(_IdUser);
        call.enqueue(new Callback<DtoPurchase>() {
            @Override
            public void onResponse(@NonNull Call<DtoPurchase> call, @NonNull Response<DtoPurchase> response) {
                if (response.code() == 200){
                    recyclerView_MyOrders.setVisibility(View.VISIBLE);
                    container_noOrder.setVisibility(View.GONE);
                    loadingDialog.dimissDialog();
                    LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
                    recyclerView_MyOrders.setLayoutManager(linearLayout);
                    AsyncOrders asyncOrders = new AsyncOrders(recyclerView_MyOrders, container_noOrder, getActivity(), SwipeRefreshMyOrders, _IdUser);
                    asyncOrders.execute();
                }else if(response.code() == 404){
                    loadingDialog.dimissDialog();
                    recyclerView_MyOrders.setVisibility(View.GONE);
                    container_noOrder.setVisibility(View.VISIBLE);
                }else{
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(getContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DtoPurchase> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(getContext());
            }
        });
    }

    private void Ids() {
        recyclerView_MyOrders = view.findViewById(R.id.recyclerView_MyOrders);
        container_noOrder = view.findViewById(R.id.container_noOrder);
        SwipeRefreshMyOrders = view.findViewById(R.id.SwipeRefreshMyOrders);
        recyclerView_MyOrders.setVisibility(View.GONE);
        container_noOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBackPressed() {
        //action not popBackStack
        requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt("id_user", _IdUser);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
        return true;
    }
}
