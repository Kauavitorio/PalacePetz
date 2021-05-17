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
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_BiggestPrice;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_LowestPrice;
import co.kaua.palacepetz.Data.Products.AsyncFilterProducts_Species;
import co.kaua.palacepetz.Data.Products.AsyncPopularProducts;
import co.kaua.palacepetz.Data.Products.AsyncProducts;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.Data.category.AsyncCategory;
import co.kaua.palacepetz.R;

import java.util.ArrayList;
import java.util.Objects;

public class AllProductsFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    View view;
    private RecyclerView recyclerView_Products, recyclerCategorys;
    private SwipeRefreshLayout SwipeRefreshProducts;
    private LottieAnimationView anim_loading_allProducts, anim_loading_Categorys;
    private Spinner spinner_petFilter;

    private static String[] petFilter;

    //  User information
    String email_user;
    String specie;

    //  Filter Tools
    private CardView card_filter_lowestPrice, card_filter_biggestPrice, card_filter_popular;
    ArrayList<DtoProducts> arrayListDto = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_allproducts, container, false);
        Ids();
        Bundle args = getArguments();
        assert args != null;
        email_user = args.getString("email_user");
        loadCategorys();
        loadAllProducts();

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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SwipeRefreshProducts.setOnRefreshListener(this::loadAllProducts);

        //  When click here will filter all products by lowest price
        card_filter_lowestPrice.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_lowestPrice.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncFilterProducts_LowestPrice async = new AsyncFilterProducts_LowestPrice(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity());
            //noinspection unchecked
            async.execute();
        });

        //  When click here will filter all products by bigger price
        card_filter_biggestPrice.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_biggestPrice.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncFilterProducts_BiggestPrice async = new AsyncFilterProducts_BiggestPrice(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity());
            //noinspection unchecked
            async.execute();
        });

        //  When click here will filter all products by popular
        card_filter_popular.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_popular.setElevation(0);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_Products.setLayoutManager(layoutManager);
            arrayListDto.clear();
            AsyncPopularProducts asyncPopularProducts = new AsyncPopularProducts(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity());
            //noinspection unchecked
            asyncPopularProducts.execute();
        });


        return view;
    }

    private void FilterBySpecies(String specie) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_Products.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncFilterProducts_Species async = new AsyncFilterProducts_Species(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity(), specie);
        //noinspection unchecked
        async.execute();
    }

    private void loadCategorys() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerCategorys.setLayoutManager(layoutManager);

        AsyncCategory asyncCategory = new AsyncCategory(recyclerCategorys, anim_loading_Categorys, getActivity(), arrayListDto, recyclerView_Products,
                anim_loading_allProducts, SwipeRefreshProducts, email_user);
        //noinspection unchecked
        asyncCategory.execute();
    }

    private void loadAllProducts() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_Products.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncProducts asyncProducts = new AsyncProducts(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity());
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
        petFilter = new String[]{ getString(R.string.filter_animals), getString(R.string.dogs), getString(R.string.cats), getString(R.string.birds) };
        card_filter_lowestPrice = view.findViewById(R.id.card_filter_lowestPrice);
        card_filter_biggestPrice = view.findViewById(R.id.card_filter_biggestPrice);
        card_filter_popular = view.findViewById(R.id.card_filter_popular);
        recyclerView_Products = view.findViewById(R.id.recyclerView_Products);
        SwipeRefreshProducts = view.findViewById(R.id.SwipeRefreshProducts);
        anim_loading_allProducts = view.findViewById(R.id.anim_loading_allProducts);
        spinner_petFilter = view.findViewById(R.id.spinner_petFilter);
        recyclerCategorys = view.findViewById(R.id.recyclerCategorys);
        anim_loading_Categorys = view.findViewById(R.id.anim_loading_Categorys);
        setFilterElevation();
    }

    @Override
    public boolean onBackPressed() {
        if (email_user != null) {
            //action not popBackStack
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("email_user", email_user);
            mainFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, mainFragment);
            transaction.commit();
            return true;
        } else {
            Toast.makeText(getContext(), "False em", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
