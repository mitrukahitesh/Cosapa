package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.lab.Lab;
import com.skywalkers.cosapa.models.store.BppProvider;
import com.skywalkers.cosapa.models.store.Store;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.CustomVH> {

    private List<Store> stores = new ArrayList<>();
    private Context context;
    private View root;
    private boolean store;

    public StoreAdapter(Context context, boolean store, View root) {
        this.context = context;
        this.store = store;
        this.root = root;
        if (store)
            fetchStores();
        else
            fetchLabs();
    }

    private void fetchStores() {
        RetrofitAccessObject
                .getRetrofitAccessObject()
                .getStoresByNameOfMedicine(RetrofitAccessObject.getBodyStore())
                .enqueue(new Callback<ArrayList<com.skywalkers.cosapa.models.store.Store>>() {
                    @Override
                    public void onResponse(Call<ArrayList<com.skywalkers.cosapa.models.store.Store>> call, Response<ArrayList<com.skywalkers.cosapa.models.store.Store>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (com.skywalkers.cosapa.models.store.Store store : response.body()) {
                                for (BppProvider provider : store.getMessage().getCatalog().getBppProviders()) {
                                    Store s = new Store();
                                    s.setName(provider.getDescriptor().getName());
                                    s.setLocation(provider.getLocations().get(0).getGps());
                                    stores.add(s);
                                    notifyItemInserted(stores.size() - 1);
                                }
                            }
                        } else {
                            showNotFound();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<com.skywalkers.cosapa.models.store.Store>> call, Throwable t) {
                        Log.i("Casapa: StoreAdapter", t.getMessage());
                    }
                });
    }

    private void fetchLabs() {
        RetrofitAccessObject
                .getRetrofitAccessObject()
                .getLabsByNameOfTest(RetrofitAccessObject.getBodyLab())
                .enqueue(new Callback<ArrayList<Lab>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Lab>> call, Response<ArrayList<Lab>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Lab lab : response.body()) {
                                for (com.skywalkers.cosapa.models.lab.BppProvider provider : lab.getMessage().getCatalog().getBppProviders()) {
                                    Store s = new Store();
                                    s.setName(provider.getDescriptor().getName());
                                    s.setLocation(provider.getLocations().get(0).getGps());
                                    stores.add(s);
                                    notifyItemInserted(stores.size() - 1);
                                }
                            }
                        } else {
                            showNotFound();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Lab>> call, Throwable t) {
                        Log.i("Casapa: StoreAdapter", t.getMessage());
                    }
                });
    }

    private void showNotFound() {
        Snackbar.make(root, "No matching results", Snackbar.LENGTH_SHORT).show();
    }

    @NonNull
    @Override

    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.store_lab_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setView(stores.get(position));
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView navigate;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            navigate = itemView.findViewById(R.id.button);
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:" + stores.get(getAdapterPosition()).getLocation() + "?q=" + stores.get(getAdapterPosition()).getName());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });
        }

        public void setView(Store store) {
            name.setText(store.getName());
        }
    }

    public class Store {
        private String name, location;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
