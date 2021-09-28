package com.skywalkers.cosapa.fragments.healthDashboard;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.skywalkers.cosapa.Payment;
import com.skywalkers.cosapa.R;
import com.skywalkers.cosapa.adapters.DoctorAdapter;
import com.skywalkers.cosapa.models.doctorConfirm.DoctorConfirm;
import com.skywalkers.cosapa.models.doctorSelect.Breakup;
import com.skywalkers.cosapa.models.doctorSelect.DoctorSelect;
import com.skywalkers.cosapa.models.doctorSelect.Quote;
import com.skywalkers.cosapa.models.doctorStatus.DoctorStatus;
import com.skywalkers.cosapa.utility.RetrofitAccessObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout extends Fragment {

    private DoctorSelect select;
    private ConstraintLayout root;
    private TextView name, category, clinic, slot;
    private DoctorAdapter.Doctor doctor;
    private String id, slotStr;
    private LinearLayout ll1, ll2, ll3;
    private TextView total, totalVal;
    private final List<TextView> charges = new ArrayList<>();
    private final List<TextView> values = new ArrayList<>();
    private Button pay;
    private DoctorConfirm doctorConfirm;
    private LinearProgressIndicator progressIndicator;

    public Checkout() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelable("doctor") != null) {
                doctor = getArguments().getParcelable("doctor");
            }
            id = getArguments().getString("id");
            slotStr = getArguments().getString("slot");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack();
            }
        });
        root = view.findViewById(R.id.root);
        name = view.findViewById(R.id.name);
        category = view.findViewById(R.id.category);
        clinic = view.findViewById(R.id.clinic);
        slot = view.findViewById(R.id.slot);
        name.setText(doctor.getName());
        category.setText(doctor.getCategory());
        clinic.setText(doctor.getClinic());
        slot.setText(slotStr);
        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);
        charges.add(view.findViewById(R.id.charge1));
        charges.add(view.findViewById(R.id.charge2));
        values.add(view.findViewById(R.id.val1));
        values.add(view.findViewById(R.id.val2));
        total = view.findViewById(R.id.total);
        totalVal = view.findViewById(R.id.totalVal);
        pay = view.findViewById(R.id.pay);
        progressIndicator = view.findViewById(R.id.progress);
        fetchData();
        ActivityResultLauncher<PaymentOption> launcher = registerForActivityResult(new Checkout.Contract(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (!result) {
                    Log.i("Cosapa: Checkout", "Payment Failed");
                    return;
                }
                Log.i("Cosapa: Checkout", "Payment Success");
                Bundle bundle = new Bundle();
                bundle.putString("id", doctorConfirm.getMessage().getOrder().getId());
                Navigation.findNavController(view).navigate(R.id.action_checkout_to_confirmation, bundle);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicator.setVisibility(View.VISIBLE);
                RetrofitAccessObject
                        .getRetrofitAccessObject()
                        .confirmDoctorService(RetrofitAccessObject.getBodyDoctorConfirm())
                        .enqueue(new Callback<ArrayList<DoctorConfirm>>() {
                            @Override
                            public void onResponse(Call<ArrayList<DoctorConfirm>> call, Response<ArrayList<DoctorConfirm>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        PaymentOption option = new PaymentOption();
                                        option.setAmount(Double.parseDouble(select.getMessage().getOrder().getQuote().getPrice().getValue()));
                                        option.setCurrency(select.getMessage().getOrder().getQuote().getPrice().getCurrency());
                                        for (DoctorConfirm confirm : response.body()) {
                                            doctorConfirm = confirm;
                                            option.setTransacId(confirm.getContext().getTransactionId());
                                            option.setName(confirm.getMessage().getOrder().getFulfillment().getAgent().getName());
                                            option.setEmail(confirm.getMessage().getOrder().getBilling().getEmail());
                                            option.setNumber(confirm.getMessage().getOrder().getBilling().getPhone());
                                        }
                                        launcher.launch(option);
                                    } else {
                                        showErrorMessage();
                                    }
                                } else {
                                    showErrorMessage();
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<DoctorConfirm>> call, Throwable t) {

                            }
                        });
            }
        });
    }

    private class Contract extends ActivityResultContract<PaymentOption, Boolean> {

        @NonNull
        @NotNull
        @Override
        public Intent createIntent(@NonNull @NotNull Context context, PaymentOption option) {
            Intent intent = new Intent(context, Payment.class);
            intent.putExtra("option", option);
            return intent;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
            if (resultCode != RESULT_OK)
                return false;
            if (intent == null)
                return false;
            return intent.getBooleanExtra("payment_success", false);
        }
    }

    private void fetchData() {
        RetrofitAccessObject.getRetrofitAccessObject().selectDoctorService(RetrofitAccessObject.getBodyDoctorSelect()).enqueue(new Callback<ArrayList<DoctorSelect>>() {
            @Override
            public void onResponse(Call<ArrayList<DoctorSelect>> call, Response<ArrayList<DoctorSelect>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (DoctorSelect s : response.body()) {
                            select = s;
                        }
                        setData();
                    } else {
                        showErrorMessage();
                    }
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DoctorSelect>> call, Throwable t) {
                Log.i("Cosapa: Doctor Checkout", t.getMessage());
            }
        });
    }

    private void setData() {
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        ll3.setVisibility(View.VISIBLE);
        pay.setVisibility(View.VISIBLE);
        List<Breakup> breakups = select.getMessage().getOrder().getQuote().getBreakup();
        totalVal.setText(String.format("%s %s", select.getMessage().getOrder().getQuote().getPrice().getValue(), select.getMessage().getOrder().getQuote().getPrice().getCurrency()));
        for (int i = 0; i < breakups.size(); ++i) {
            charges.get(i).setText(breakups.get(i).getTitle());
            values.get(i).setText(String.format("%s %s", breakups.get(i).getPrice().getValue(), breakups.get(i).getPrice().getCurrency()));
        }
    }

    private void showErrorMessage() {
        Snackbar.make(root, "Some error occurred", Snackbar.LENGTH_SHORT).show();
    }

    public static class PaymentOption implements Parcelable {
        private Double amount;
        private String name;
        private String transacId;
        private String number, email, currency;

        public PaymentOption() {
        }

        protected PaymentOption(Parcel in) {
            if (in.readByte() == 0) {
                amount = null;
            } else {
                amount = in.readDouble();
            }
            name = in.readString();
            transacId = in.readString();
            number = in.readString();
            email = in.readString();
            currency = in.readString();
        }

        public static final Creator<PaymentOption> CREATOR = new Creator<PaymentOption>() {
            @Override
            public PaymentOption createFromParcel(Parcel in) {
                return new PaymentOption(in);
            }

            @Override
            public PaymentOption[] newArray(int size) {
                return new PaymentOption[size];
            }
        };

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTransacId() {
            return transacId;
        }

        public void setTransacId(String transacId) {
            this.transacId = transacId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (amount == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(amount);
            }
            dest.writeString(name);
            dest.writeString(transacId);
            dest.writeString(number);
            dest.writeString(email);
            dest.writeString(currency);
        }
    }
}