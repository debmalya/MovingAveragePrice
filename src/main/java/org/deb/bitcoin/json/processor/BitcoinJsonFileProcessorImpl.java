package org.deb.bitcoin.json.processor;

import org.deb.bitcoin.repository.BitcoinPriceRepository;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class BitcoinJsonFileProcessorImpl implements BitcoinJsonFileProcessor{
   

    @Autowired
    BitcoinDataProcessor bitcoinDataProcessor;


    @Override
    public void processJsonFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(s->{
                sb.append(s);
            });
            bitcoinDataProcessor.processBitcoinData(sb.toString());
        }


    }
}
