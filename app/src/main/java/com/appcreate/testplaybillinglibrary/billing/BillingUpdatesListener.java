package com.appcreate.testplaybillinglibrary.billing;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Created by troll on 26.02.2018.
 */

public interface BillingUpdatesListener {
    void onBillingClientSetupFinished();

    void onConsumeFinished(String token, @BillingClient.BillingResponse int result);

    void onPurchasesUpdated(List<Purchase> purchases);

}
