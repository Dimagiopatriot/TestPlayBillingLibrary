package com.appcreate.testplaybillinglibrary.billing;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by troll on 26.02.2018.
 */

public class BillingManager implements PurchasesUpdatedListener {
    // Default value of mBillingClientResponseCode until BillingManager was not yet initialized
    public static final int BILLING_MANAGER_NOT_INITIALIZED  = -1;

    private static final String TAG = "BillingManager";

    private Activity mActivity;
    private BillingUpdatesListener mBillingUpdatesListener;

    /** A reference to BillingClient **/
    private BillingClient mBillingClient;

    /**
     * True if billing service is connected now.
     */
    private boolean mIsServiceConnected;

    private int mBillingClientResponseCode = BILLING_MANAGER_NOT_INITIALIZED;


    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCg" +
            "KCAQEAm0kxbX8Y9pLTDay8EeYCAi/a0cLTgFkZbG/ZOtMTdsmuGo5aWwPYg0k9TlOlWZ1qcYO3UOnoo8fiEA/zb" +
            "Qd7xWSo8NB34kN1Zd1egGA44dWz510vv0udAahkWmJUNpED6eCCTzeYrB89C6JwmCgv64z0E7xhIXLHFLlIySlX" +
            "v7QIJQWkD1519rdMZdvDvkWuUZrxXPuvt8MLMuopsC1MpZ8MH4wUvsNtMwG9jnC22zveb39zSCllNLB1yMKTzqX" +
            "GHu+wUY+Nc+EAl6VgJXM9anyVdLlGo82PS1sGQVS/gW+RdM0P0IytpqaBUjCsbYFPKdJQC9Q+kvtqAwQ/iTUoOwIDAQAB";

    public BillingManager(Activity activity, final BillingUpdatesListener billingUpdatesListener){
        mActivity = activity;
        mBillingUpdatesListener = billingUpdatesListener;
        mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();

        // Start the setup asynchronously.
        // The specified listener is called once setup completes.
        // New purchases are reported through the onPurchasesUpdated() callback
        // of the class specified using the setListener() method above.
        startServiceConnection(new Runnable() {
            @Override
            public void run() {
                // Notify the listener that the billing client is ready.
                mBillingUpdatesListener.onBillingClientSetupFinished();
            }
        });
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                Log.d(TAG, "Setup finished. Response code: " + billingResponseCode);

                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
                mBillingClientResponseCode = billingResponseCode;
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }


    /**
     * Handle a callback that purchases were updated from the Billing library
     */
    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }

    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList,
                                     final SkuDetailsResponseListener listener) {
        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable queryRequest = new Runnable() {
            @Override
            public void run() {
                // Query the purchase async
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(itemType);
                mBillingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode,
                                                             List<SkuDetails> skuDetailsList) {
                                listener.onSkuDetailsResponse(responseCode, skuDetailsList);
                            }
                        });
            }
        };

        executeServiceRequest(queryRequest);
    }

    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }


    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {

        try {
            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
        } catch (IOException e) {
            Log.e(TAG, "Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }

    /**
     * Clear the resources
     */
    public void destroy() {
        Log.d(TAG, "Destroying the manager.");

        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }
}
