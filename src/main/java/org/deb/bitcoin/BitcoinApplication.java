package org.deb.bitcoin;

import org.deb.bitcoin.json.processor.BitcoinDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan
public class BitcoinApplication implements ApplicationRunner {

    @Autowired
    BitcoinDataProcessor bitcoinDataProcessor;

    public static void main(String[] args) {
        SpringApplication.run(BitcoinApplication.class, args);
    }

    @RequestMapping("/")
    public String doIt() {
        return "bitcoin";
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.coinbase.com/api/v2/prices/BTC-USD/historic?period=all", String.class);
        bitcoinDataProcessor.processBitcoinData(responseEntity.getBody());
    }
}
