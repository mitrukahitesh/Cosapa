package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.doctor.BppProvider;
import com.skywalkers.cosapa.models.doctor.Category;
import com.skywalkers.cosapa.models.doctor.Item;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.CustomVH> {

    private final List<Doctor> doctors = new ArrayList<>();
    private Context context;
    private String searchFor;
    private View root;

    public DoctorAdapter(Context context, String searchFor, View root) {
        this.context = context;
        this.searchFor = searchFor;
        this.root = root;
        searchDoctors();
    }

    private void searchDoctors() {
        RetrofitAccessObject
                .getRetrofitAccessObject()
                .getDoctorsByNameOfCategory(RetrofitAccessObject.getBodyDoctor())
                .enqueue(new Callback<ArrayList<com.skywalkers.cosapa.models.doctor.Doctor>>() {
                    @Override
                    public void onResponse(Call<ArrayList<com.skywalkers.cosapa.models.doctor.Doctor>> call, Response<ArrayList<com.skywalkers.cosapa.models.doctor.Doctor>> response) {
                        if (response.body() == null || !response.isSuccessful()) {
                            snackBarCouldNotFetch();
                            return;
                        }
                        for (com.skywalkers.cosapa.models.doctor.Doctor d : response.body()) {
                            for (BppProvider provider : d.getMessage().getCatalog().getBppProviders()) {
                                Map<String, String> categories = new HashMap<>();
                                for (Category category : provider.getCategories()) {
                                    categories.put(category.getId(), category.getDescriptor().getName());
                                }
                                Doctor doctor = new Doctor();
                                for (Item item : provider.getItems()) {
                                    if (item.getParentItemId() != null) {
                                        for (int i = 0; i < doctors.size(); ++i) {
                                            Doctor docInList = doctors.get(i);
                                            if (item.getParentItemId().equals(docInList.getId())) {
                                                docInList.setCost(item.getPrice().getValue());
                                                docInList.setCurrency(item.getPrice().getCurrency());
                                                notifyItemChanged(i);
                                                break;
                                            }
                                        }
                                        continue;
                                    }
                                    doctor.setId(item.getId());
                                    doctor.setName(item.getDescriptor().getName());
                                    doctor.setCategory(categories.get(item.getCategoryId()));
                                    doctors.add(doctor);
                                    notifyItemInserted(doctors.size() - 1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<com.skywalkers.cosapa.models.doctor.Doctor>> call, Throwable t) {
                        snackBarCouldNotFetch();
                    }
                });
    }

    private void snackBarCouldNotFetch() {
        Snackbar.make(root, "Could not fetch results", Snackbar.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setView(doctors.get(position), position);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private TextView name, category, price;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.doc_name);
            category = itemView.findViewById(R.id.doc_category);
            price = itemView.findViewById(R.id.docprice);
        }

        public void setView(Doctor d, int pos) {
            name.setText(d.getName());
            category.setText(d.getCategory());
            price.setText(String.format("%s %s", d.getCost(), d.getCurrency()));
        }
    }

    public class Doctor {
        private String name, category, cost, currency, id;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Doctor() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
