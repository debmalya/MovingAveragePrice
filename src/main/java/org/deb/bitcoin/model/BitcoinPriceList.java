package org.deb.bitcoin.model;

import java.util.ArrayList;
import java.util.Collection;

public class BitcoinPriceList {

    Collection<BitcoinTimestampData> priceList = new ArrayList<>();

    public Collection<BitcoinTimestampData> getPriceList() {
        return priceList;
    }

    public void setPriceList(Collection<BitcoinTimestampData> priceList) {
        this.priceList = priceList;
    }

    @Override
    public String toString() {
        return "BitcoinPriceList{" +
                "priceList=" + priceList +
                '}';
    }
}
