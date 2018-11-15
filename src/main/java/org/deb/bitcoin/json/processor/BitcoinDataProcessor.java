package org.deb.bitcoin.json.processor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.deb.bitcoin.repository.BitcoinPriceRepository;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class BitcoinDataProcessor implements BitcoinJsonProcessor {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
    @Autowired
    BitcoinPriceRepository priceRepository;
    Logger logger = Logger.getLogger("BitcoinDataProcessor");

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public HTreeMap<Long, Double> processBitcoinData(String bitcoinData) {
//        logger.log(Level.INFO, String.format("bitcoin data :%s", bitcoinData));
        JsonReader jsonReader = new JsonReader(new StringReader(bitcoinData));
        jsonReader.setLenient(true);

        com.google.gson.JsonParser gsonJsonParser = new com.google.gson.JsonParser();

        JsonElement bitcoinElement = gsonJsonParser.parse(jsonReader);
        if (bitcoinElement.isJsonObject()) {
            JsonObject bitcoinObject = bitcoinElement.getAsJsonObject();
            JsonElement bitcoindData = bitcoinObject.get("data");
            if (bitcoindData.isJsonObject()) {
                JsonObject dataObject = bitcoindData.getAsJsonObject();
                JsonArray prices = dataObject.get("prices").getAsJsonArray();
                for (int i = 0; i < prices.size(); i++) {
                    JsonObject eachPrice = prices.get(i).getAsJsonObject();
                    double price = Double.parseDouble(eachPrice.get("price").getAsString());
                    String timeStr = eachPrice.get("time").getAsString();

                    try {
                        Date processingDate = simpleDateFormat.parse(timeStr);
                        priceRepository.getBitcoinPriceMap().put(processingDate.getTime(), price);
//                        logger.log(Level.INFO, String.format("Time %s, %d, price %.2f ", timeStr,processingDate.getTime(), price));
                    } catch (ParseException pe) {
                        logger.log(Level.SEVERE,pe.getMessage(),pe);
                    }
                }
            }

        } else {
            logger.log(Level.INFO, "Not a JSON object");
        }
        return null;
    }
}
