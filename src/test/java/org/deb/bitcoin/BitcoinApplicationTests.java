package org.deb.bitcoin;

import org.deb.bitcoin.json.processor.BitcoinDataProcessor;
import org.deb.bitcoin.json.processor.BitcoinJsonFileProcessorImpl;
import org.deb.bitcoin.repository.BitcoinPriceRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)

@SpringBootTest(
		classes = BitcoinApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

public class BitcoinApplicationTests {

	@Autowired
	BitcoinPriceRepository bitcoinPriceRepository;

	@Autowired
    BitcoinDataProcessor bitcoinDataProcessor;

	@Autowired
	BitcoinJsonFileProcessorImpl bitcoinJsonFileProcessor;

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

	@Test
	public void contextLoads() {
		Assert.assertNotNull(bitcoinPriceRepository.getBitcoinPriceMap());
	}

	@Test
    public void checkDataProcessing(){
	    Assert.assertNotNull(bitcoinDataProcessor);
	    bitcoinDataProcessor.processBitcoinData("{\"data\":{\"base\":\"BTC\",\"currency\":\"USD\",\"prices\":" +
				"[{\"price\":\"6352.47\",\"time\":\"2018-11-11T00:00:00Z\"}," +
				"{\"price\":\"6355.71\",\"time\":\"2018-11-10T00:00:00Z\"}]}}"
				);
    }

    @Test
	public void checkFileProcessing(){
		Assert.assertNotNull(bitcoinJsonFileProcessor);
		try {
			bitcoinJsonFileProcessor.processJsonFile("./src/main/resources/response.json");
			try {
                long dateTime = simpleDateFormat.parse("2013-01-02T00:00:00Z").getTime();
                double price = bitcoinPriceRepository.getBitcoinPriceMap().get(dateTime);
                Assert.assertEquals(13.32,price,0.00);

                dateTime = simpleDateFormat.parse("2018-11-11T00:00:00Z").getTime();
                price = bitcoinPriceRepository.getBitcoinPriceMap().get(dateTime);
                Assert.assertEquals(6352.47,price,0.00);
            }catch(ParseException pe){
			    Assert.assertFalse("Invalid date format",true);
            }
		}catch(IOException ioe){
			Assert.assertFalse(ioe.getMessage(),true);
		}
	}

}
