package co.kaua.palacepetz.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import co.kaua.palacepetz.Data.Historic.AsyncHistoric;
import co.kaua.palacepetz.Data.Historic.DtoHistoric;
import co.kaua.palacepetz.Data.Products.ProductsServices;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class HistoricFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    private View view;
    private Bundle args;
    private ConstraintLayout no_product_onHistoric;
    private RecyclerView recyclerView_Historic;
    private SwipeRefreshLayout SwipeRefreshHistoric;
    private LoadingDialog loadingDialog;
    private ImageView ic_clear_historic;

    //  User information
    private int _IdUser;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Retrofit
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_historic, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");
        loadHistoric(_IdUser);

        // Clear historic click
        ic_clear_historic.setOnClickListener(v -> {
            AlertDialog.Builder alert  = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.clear_historic)
                    .setMessage(R.string.really_clear_historic)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        loadingDialog = new LoadingDialog(getActivity());
                        loadingDialog.startLoading();
                        ProductsServices services = retrofitUser.create(ProductsServices.class);
                        Call<DtoHistoric> call = services.clearHistoric(_IdUser);
                        call.enqueue(new Callback<DtoHistoric>() {
                            @Override
                            public void onResponse(@NonNull Call<DtoHistoric> call, @NonNull Response<DtoHistoric> response) {
                                loadingDialog.dimissDialog();
                                if (response.code() == 200){
                                    recyclerView_Historic.setVisibility(View.GONE);
                                    no_product_onHistoric.setVisibility(View.VISIBLE);
                                }else
                                    Warnings.showWeHaveAProblem(getContext());
                            }
                            @Override
                            public void onFailure(@NonNull Call<DtoHistoric> call, @NonNull Throwable t) {
                                loadingDialog.dimissDialog();
                                Warnings.showWeHaveAProblem(getContext());
                            }
                        });
                    })
                    .setNegativeButton(R.string.no, null);
            alert.show();
        });

        //  Refresh Swipe
        SwipeRefreshHistoric.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHistoric(_IdUser);
            }
        });

        return view;
    }

    private void loadHistoric(int idUser) {
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        ProductsServices services = retrofitUser.create(ProductsServices.class);
        Call<DtoHistoric> call = services.getHistoric(idUser);
        call.enqueue(new Callback<DtoHistoric>() {
            @Override
            public void onResponse(@NonNull Call<DtoHistoric> call, @NonNull Response<DtoHistoric> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    recyclerView_Historic.setVisibility(View.VISIBLE);
                    no_product_onHistoric.setVisibility(View.GONE);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
                    recyclerView_Historic.setLayoutManager(linearLayout);
                    AsyncHistoric asyncHistoric = new AsyncHistoric(recyclerView_Historic, no_product_onHistoric, requireActivity(), SwipeRefreshHistoric, idUser);
                    asyncHistoric.execute();
                }else if(response.code() == 206){
                    loadingDialog.dimissDialog();
                    recyclerView_Historic.setVisibility(View.GONE);
                    no_product_onHistoric.setVisibility(View.VISIBLE);
                }else{
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(getContext());
                }
            }
            @Override
            public void onFailure(@NonNull Call<DtoHistoric> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(getContext());
            }
        });
    }

    private void Ids() {
        no_product_onHistoric = view.findViewById(R.id.no_product_onHistoric);
        recyclerView_Historic = view.findViewById(R.id.recyclerView_Historic);
        SwipeRefreshHistoric = view.findViewById(R.id.SwipeRefreshHistoric);
        ic_clear_historic = view.findViewById(R.id.ic_clear_historic);
    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", _IdUser);
            mainFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, mainFragment);
            transaction.commit();
            return true;
        } else {
            return false;
        }
    }
}
