package org.deb.bitcoin.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

@Repository
public class BitcoinPriceRepository {
    /**
     * This will store date (in long format) and bit coin price.
     */
    private static final DB db = DBMaker.fileDB(new File("src/main/resources/db/BitCoinPrices.db"))
            .fileMmapEnableIfSupported()
            .allocateIncrement(512 * 1024*1024) // 512MB
            .checksumHeaderBypass()
            .closeOnJvmShutdown()
            .make();

    /**
     *
     * @return bit coin price map.
     */
    public HTreeMap<Long, Double> getBitcoinPriceMap() {
        return bitcoinPriceMap;
    }

    private HTreeMap<Long, Double> bitcoinPriceMap;

    /**
     *
     */
    public BitcoinPriceRepository() {

        bitcoinPriceMap = db
                .hashMap("bitcoinPriceMap")
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.DOUBLE)
                // to load a value (0.00 in this case) if the existing key is not found.
                .valueLoader(s -> 0.00)
                .expireCompactThreshold(0.4)
                .createOrOpen();

    }

}
