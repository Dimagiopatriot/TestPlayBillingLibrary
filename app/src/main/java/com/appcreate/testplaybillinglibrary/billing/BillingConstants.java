package com.appcreate.testplaybillinglibrary.billing;

import com.android.billingclient.api.BillingClient;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dmitriysmishnyi on 27.02.18.
 */

public final class BillingConstants {

    public static final String SKU_TEST = "test_sub";
    public static final String SKU_GOLD_TEST = "gold_test_sub";
    public static final String SKU_SILVER_TEST = "silver_test_sub";

    //for items (not subscriptions)
    private static final String[] IN_APP_SKUS = {};

    private static final String[] SUBSCRIPTIONS_SKUS = {SKU_TEST, SKU_GOLD_TEST, SKU_SILVER_TEST};

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public static final List<String> getSkuList(@BillingClient.SkuType String billingType) {
        return (billingType == BillingClient.SkuType.INAPP) ? Arrays.asList(IN_APP_SKUS)
                : Arrays.asList(SUBSCRIPTIONS_SKUS);
    }
}
