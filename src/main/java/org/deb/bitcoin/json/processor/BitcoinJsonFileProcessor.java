package org.deb.bitcoin.json.processor;

import org.mapdb.HTreeMap;

import java.io.IOException;

@FunctionalInterface
public interface BitcoinJsonFileProcessor {
    /**
     *
     * @param fileName file containing bitcoin prices.
     *
     */
    public void processJsonFile(String fileName) throws IOException;
}
