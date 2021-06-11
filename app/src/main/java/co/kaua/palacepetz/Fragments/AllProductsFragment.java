package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_BiggestPrice;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_LowestPrice;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_Name;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_Species;
import co.kaua.palacepetz.Data.Products.AsyncPopularProducts;
import co.kaua.palacepetz.Data.Products.AsyncProducts;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.Data.Products.ProductsServices;
import co.kaua.palacepetz.Data.category.AsyncCategory;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class AllProductsFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    View view;
    private RecyclerView recyclerView_Products, recyclerCategorys;
    private SwipeRefreshLayout SwipeRefreshProducts;
    private LottieAnimationView anim_loading_allProducts, anim_loading_Categorys;
    private Spinner spinner_petFilter;
    Bundle args;
    private static String[] petFilter;

    //  User information
    String _Email;
    int _IdUser;
    String specie;

    //  Filter Tools
    private CardView card_filter_lowestPrice, card_filter_biggestPrice, card_filter_popular, card_filter_clearFilters;
    ArrayList<DtoProducts> arrayListDto = new ArrayList<>();

    private final Retrofit retrofitProduct = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/products/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_allproducts, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");
        if (args.getString("search") != null){
            loadProductsWithSearch(args.getString("search"));
        }else
            loadAllProducts();
        loadCategorys();

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, petFilter);
        spinner_petFilter.setAdapter(adapter);

        // Spinner click listener
        spinner_petFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String ItemSelect = parent.getItemAtPosition(position).toString();
                if (ItemSelect.equals(getString(R.string.dogs))){
                    specie = "Dogs";
                    FilterBySpecies(specie);
                }else if (ItemSelect.equals(getString(R.string.cats))){
                    specie = "Cats";
                    FilterBySpecies(specie);
                }else if (ItemSelect.equals(getString(R.string.birds))){
                    specie = "Birds";
                    FilterBySpecies(specie);
                }else if (ItemSelect.equals(getString(R.string.rabbit))){
                    specie = "Rabbit";
                    FilterBySpecies(specie);
                }else if (ItemSelect.equals(getString(R.string.fish))){
                    specie = "Fish";
                    FilterBySpecies(specie);
                }else if (ItemSelect.equals(getString(R.string.hamster))){
                    specie = "Hamster";
                    FilterBySpecies(specie);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SwipeRefreshProducts.setOnRefreshListener(this::loadAllProducts);

        //  When click here will filter all products by lowest price
        card_filter_lowestPrice.setOnClickListener(v -> {
            card_filter_clearFilters.setVisibility(View.VISIBLE);
            setFilterElevation();
            card_filter_lowestPrice.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncFilterProducts_LowestPrice async = new AsyncFilterProducts_LowestPrice(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, _Email, getActivity());
            //noinspection unchecked
            async.execute();
        });

        //  When click here will filter all products by bigger price
        card_filter_biggestPrice.setOnClickListener(v -> {
            card_filter_clearFilters.setVisibility(View.VISIBLE);
            setFilterElevation();
            card_filter_biggestPrice.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncFilterProducts_BiggestPrice async = new AsyncFilterProducts_BiggestPrice(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, _Email, getActivity());
            //noinspection unchecked
            async.execute();
        });

        //  When click here will filter all products by popular
        card_filter_popular.setOnClickListener(v -> {
            card_filter_clearFilters.setVisibility(View.VISIBLE);
            setFilterElevation();
            card_filter_popular.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncPopularProducts asyncPopularProducts = new AsyncPopularProducts(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, _Email, getActivity());
            //noinspection unchecked
            asyncPopularProducts.execute();
        });

        //  When click here will clear all filters
        card_filter_clearFilters.setOnClickListener(v -> loadAllProducts());

        return view;
    }

    private void loadProductsWithSearch(String nm_product) {

        ProductsServices services = retrofitProduct.create(ProductsServices.class);
        Call<DtoProducts> call = services.getFilterName(nm_product);
        call.enqueue(new Callback<DtoProducts>() {
            @Override
            public void onResponse(@NonNull Call<DtoProducts> call, @NonNull Response<DtoProducts> response) {
                if (response.code() == 200){
                    card_filter_clearFilters.setVisibility(View.GONE);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
                    recyclerView_Products.setLayoutManager(layoutManager);
                    arrayListDto.clear();
                    AsyncFilterProducts_Name async = new AsyncFilterProducts_Name(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, getActivity(), nm_product);
                    //noinspection unchecked
                    async.execute();
                }else if(response.code() == 204){
                    loadAllProducts();
                    Warnings.ProductNotFoundAlert(getContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DtoProducts> call, @NonNull Throwable t) {
                Warnings.showWeHaveAProblem(getContext());
            }
        });
    }

    private void FilterBySpecies(String specie) {
        card_filter_clearFilters.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_Products.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncFilterProducts_Species async = new AsyncFilterProducts_Species(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, _Email, getActivity(), specie);
        //noinspection unchecked
        async.execute();
    }

    private void loadCategorys() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerCategorys.setLayoutManager(layoutManager);

        AsyncCategory asyncCategory = new AsyncCategory(recyclerCategorys, anim_loading_Categorys, getActivity(), arrayListDto, recyclerView_Products,
                anim_loading_allProducts, SwipeRefreshProducts, _Email);
        //noinspection unchecked
        asyncCategory.execute();
    }

    private void loadAllProducts() {
        card_filter_clearFilters.setVisibility(View.GONE);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_Products.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncProducts asyncProducts = new AsyncProducts(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, _IdUser, _Email, getActivity());
        //noinspection unchecked
        asyncProducts.execute();
    }

    private void setFilterElevation() {
        card_filter_lowestPrice.setElevation(20);
        card_filter_biggestPrice.setElevation(20);
        card_filter_popular.setElevation(20);
        spinner_petFilter.setElevation(20);
    }

    private void Ids() {
        petFilter = new String[]{ getString(R.string.filter_animals), getString(R.string.dogs), getString(R.string.cats),
                getString(R.string.birds), getString(R.string.rabbit), getString(R.string.fish), getString(R.string.hamster) };
        card_filter_lowestPrice = view.findViewById(R.id.card_filter_lowestPrice);
        card_filter_biggestPrice = view.findViewById(R.id.card_filter_biggestPrice);
        card_filter_popular = view.findViewById(R.id.card_filter_popular);
        recyclerView_Products = view.findViewById(R.id.recyclerView_Products);
        SwipeRefreshProducts = view.findViewById(R.id.SwipeRefreshProducts);
        anim_loading_allProducts = view.findViewById(R.id.anim_loading_allProducts);
        spinner_petFilter = view.findViewById(R.id.spinner_petFilter);
        recyclerCategorys = view.findViewById(R.id.recyclerCategorys);
        anim_loading_Categorys = view.findViewById(R.id.anim_loading_Categorys);
        card_filter_clearFilters = view.findViewById(R.id.card_filter_clearfilters);
        setFilterElevation();
    }

    @Override
    public boolean onBackPressed() {
        //action not popBackStack
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("email_user", _Email);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
        return true;
    }
}
