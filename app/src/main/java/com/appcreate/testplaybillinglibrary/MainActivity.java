package com.appcreate.testplaybillinglibrary;

import android.app.AlertDialog;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.appcreate.testplaybillinglibrary.adapter.CommonRecyclerViewAdapter;
import com.appcreate.testplaybillinglibrary.adapter.SkuDetailsAdapter;
import com.appcreate.testplaybillinglibrary.billing.BillingConstants;
import com.appcreate.testplaybillinglibrary.billing.BillingManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private MainViewController mMainViewCController;
    private BillingManager mBillingManager;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewCController = new MainViewController(this);
        mBillingManager = new BillingManager(this, mMainViewCController.getUpdateListener());

        mRecyclerView = findViewById(R.id.recyclerView);
    }

    public void onBillingManagerSetupFinished() {
        Toast.makeText(this, "It connected to billing service", Toast.LENGTH_SHORT).show();
    }

    public void alert(String message) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new RuntimeException("Dialog could be shown only from the main thread");
        }

        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setNeutralButton("OK", null);

        bld.setMessage(message);

        bld.create().show();
    }

    public void showAvailableSubscriptions(View v) {
        mBillingManager.querySkuDetailsAsync(BillingClient.SkuType.SUBS,
                BillingConstants.getSkuList(BillingClient.SkuType.SUBS), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        if (responseCode != BillingClient.BillingResponse.OK) {
                            // Handle any error responses.
                            Log.w(TAG, "Unsuccessful query for type: " + BillingClient.SkuType.SUBS
                                    + ". Error code: " + responseCode);
                        } else if (skuDetailsList != null
                                && skuDetailsList.size() > 0) {
                            Log.e(TAG, skuDetailsList.toString());
                            SkuDetailsAdapter adapter = new SkuDetailsAdapter();
                            adapter.setMainViewController(mMainViewCController);
                            setUpAdapter(adapter, skuDetailsList);
                        }
                    }
                });
        //mBillingManager.queryPurchases();
    }

    public void showUserHistory(View v) {

    }

    public BillingManager getBillingManager(){
        return mBillingManager;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying helper.");
        if (mBillingManager != null) {
            mBillingManager.destroy();
        }
        super.onDestroy();
    }

    public void setUpAdapter(CommonRecyclerViewAdapter adapter, List items) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.addItems(items);
    }
}
