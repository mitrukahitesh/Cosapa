package com.skywalkers.cosapa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Payment extends AppCompatActivity implements PaymentResultListener {

    private final Intent intent = new Intent();
    private com.skywalkers.cosapa.fragments.doctor.Checkout.PaymentOption option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        option = getIntent().getParcelableExtra("option");
        startPayment();
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_p5D8NTY7br1DP6");
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            Log.i("Cosapa", option.getEmail());
            options.put("name", option.getName());
            options.put("description", "Transaction ID #" + option.getTransacId());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#6C63FF");
            options.put("currency", "INR");
            options.put("amount", option.getAmount() * 100);//pass amount in currency subunits
            options.put("prefill.email", option.getEmail());
            options.put("prefill.contact", option.getNumber());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("Cosapa: Razorpay", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        intent.putExtra("payment_success", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i("Cosapa: Razorpay", s);
        intent.putExtra("payment_success", false);
        setResult(RESULT_OK, intent);
        finish();
    }
}