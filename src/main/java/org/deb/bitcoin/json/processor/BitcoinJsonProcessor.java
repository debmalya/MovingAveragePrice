package org.deb.bitcoin.json.processor;


import org.mapdb.HTreeMap;

public interface BitcoinJsonProcessor {
    /**
     *
     * @param bitcoinData bitcoin price JSON data
     * @return
     */
    HTreeMap<Long,Double> processBitcoinData(String  bitcoinData);
}
