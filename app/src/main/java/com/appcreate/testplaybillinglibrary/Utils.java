package com.appcreate.testplaybillinglibrary;

import com.android.billingclient.api.Purchase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitriysmishnyi on 01.03.18.
 */

public class Utils {

    public static List<String> getSkuIdsFromPurchases(List<Purchase> purchases) {
        List<String> result = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if (purchase.isAutoRenewing())
                result.add(purchase.getSku());
        }
        return result;
    }
}
