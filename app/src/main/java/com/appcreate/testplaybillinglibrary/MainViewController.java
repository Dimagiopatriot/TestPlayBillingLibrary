package com.appcreate.testplaybillinglibrary;

import android.app.Activity;
import android.util.Log;

import com.android.billingclient.api.Purchase;
import com.appcreate.testplaybillinglibrary.billing.BillingUpdatesListener;
import com.android.billingclient.api.BillingClient.BillingResponse;

import java.util.List;

/**
 * Created by dmitriysmishnyi on 27.02.18.
 */

public class MainViewController {

    private final String TAG = "MainViewController";

    private MainActivity mActivity;
    private UpdateListener updateListener;

    MainViewController(MainActivity activity) {
        mActivity = activity;
        updateListener = new UpdateListener();
    }

    public UpdateListener getUpdateListener() {
        return updateListener;
    }

    private class UpdateListener implements BillingUpdatesListener {

        @Override
        public void onBillingClientSetupFinished() {
            mActivity.onBillingManagerSetupFinished();
        }

        @Override
        public void onConsumeFinished(String token, @BillingResponse int result) {
            Log.d(TAG, "Consumption finished. Purchase token: " + token + ", result: " + result);
            if (result == BillingResponse.OK) {
                // Successfully consumed, so we apply the effects of the item in our
                Log.d(TAG, "Consumption successful. Provisioning.");
                mActivity.alert("All right! You consume your sub/purchase!");
            } else {
                mActivity.alert("Ooops! Something goes wrong");
            }
            //some ui changes in activity/fragment
            Log.d(TAG, "End consumption flow.");
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {

        }
    }
}
