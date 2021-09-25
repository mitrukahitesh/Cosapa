package com.skywalkers.cosapa.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.models.doctor.BppProvider;
import com.skywalkers.cosapa.models.doctor.Category;
import com.skywalkers.cosapa.models.doctor.Item;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.CustomVH> {

    private final List<Doctor> doctors = new ArrayList<>();
    private Context context;
    private String searchFor;
    private View root;
    private NavController controller;

    public DoctorAdapter(Context context, String searchFor, View root, NavController controller) {
        this.context = context;
        this.searchFor = searchFor;
        this.root = root;
        this.controller = controller;
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
                        doctors.clear();
                        for (com.skywalkers.cosapa.models.doctor.Doctor d : response.body()) {
                            for (BppProvider provider : d.getMessage().getCatalog().getBppProviders()) {
                                Map<String, String> categories = new HashMap<>();
                                for (Category category : provider.getCategories()) {
                                    categories.put(category.getId(), category.getDescriptor().getName());
                                }
                                for (Item item : provider.getItems()) {
                                    Doctor doctor = new Doctor();
                                    if (item.getParentItemId() != null) {
                                        Timing timing = new Timing();
                                        timing.setCost(item.getPrice().getValue());
                                        timing.setId(item.getId());
                                        timing.setCurrency(item.getPrice().getCurrency());
                                        timing.setStart(item.getTime().getRange().getStart().substring(11, 16));
                                        timing.setEnd(item.getTime().getRange().getEnd().substring(11, 16));
                                        for (int i = 0; i < doctors.size(); ++i) {
                                            Doctor docInList = doctors.get(i);
                                            if (item.getParentItemId().equals(docInList.getId())) {
                                                docInList.getTimings().add(timing);
                                                notifyItemChanged(i);
                                            }
                                        }
                                        continue;
                                    }
                                    boolean repeat = false;
                                    for (Doctor doctor1 : doctors) {
                                        if (doctor1.getId().equals(item.getId())) {
                                            repeat = true;
                                            break;
                                        }
                                    }
                                    if (repeat)
                                        continue;
                                    doctor.setId(item.getId());
                                    doctor.setClinic(provider.getDescriptor().getName());
                                    doctor.setName(item.getDescriptor().getName());
                                    doctor.setCategory(categories.get(item.getCategoryId()));
                                    if (searchFor.equals("") || searchFor.equalsIgnoreCase(doctor.getCategory())) {
                                        doctors.add(doctor);
                                        notifyItemInserted(doctors.size() - 1);
                                    }
                                }
                            }
                        }
                        if (doctors.size() == 0)
                            snackBarNoMatchingResults();
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

    private void snackBarNoMatchingResults() {
        Snackbar.make(root, "No matching results", Snackbar.LENGTH_SHORT).show();
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

        private final TextView name;
        private final TextView category;
        private final TextView clinic;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.doc_name);
            category = itemView.findViewById(R.id.doc_category);
            clinic = itemView.findViewById(R.id.clinic);
            Button getDetails = itemView.findViewById(R.id.button);
            getDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("doctor", doctors.get(getAdapterPosition()));
                    controller.navigate(R.id.action_doctors3_to_doctorDetails, bundle);
                }
            });
        }

        public void setView(Doctor d, int pos) {
            name.setText(d.getName());
            category.setText(d.getCategory());
            clinic.setText(d.getClinic());
        }
    }

    public class Doctor implements Parcelable {
        private String name, category, id, clinic;
        private final Set<Timing> timings = new HashSet<>();

        protected Doctor(Parcel in) {
            name = in.readString();
            category = in.readString();
            id = in.readString();
            clinic = in.readString();
        }

        public final Creator<Doctor> CREATOR = new Creator<Doctor>() {
            @Override
            public Doctor createFromParcel(Parcel in) {
                return new Doctor(in);
            }

            @Override
            public Doctor[] newArray(int size) {
                return new Doctor[size];
            }
        };

        public String getClinic() {
            return clinic;
        }

        public void setClinic(String clinic) {
            this.clinic = clinic;
        }

        public Set<Timing> getTimings() {
            return timings;
        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(category);
            dest.writeString(id);
            dest.writeString(clinic);
        }
    }

    public class Timing {
        private String start, end, cost, currency, id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
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

        @Override
        public boolean equals(Object o) {
            if (!o.getClass().equals(this.getClass()))
                return false;
            Timing timing = (Timing) o;
            return this.start.equals(timing.getStart()) && this.end.equals(timing.getEnd());
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, cost, currency);
        }
    }
}
