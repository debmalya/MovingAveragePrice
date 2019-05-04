package org.deb.bitcoin.service;

import org.deb.bitcoin.model.BitcoinPriceList;
import org.deb.bitcoin.model.BitcoinTimestampData;
import org.deb.bitcoin.repository.BitcoinPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BitcoinService {
    private static final Logger logger = Logger.getLogger("BitcoinService");
    /**
     * Number of milliseconds in a day.
     */
    private static final long ONE_DAY = 86400000L;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");


    private static TemporalField temporalField;

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private DateTimeFormatter dateTimeFormatter;
    @Autowired
    private BitcoinPriceRepository bitcoinPriceRepository;

    /**
     * @param startDate of price movement analysis.
     * @param endDate   of price movement analysis.
     * @return list of bitcoin prices within date ranges.
     */
    public BitcoinPriceList getPriceMovement(String startDate, String endDate) {
        BitcoinPriceList bitcoinPriceList = new BitcoinPriceList();

        try {
            long startTime = simpleDateFormat.parse(startDate).getTime();
            long endTime = simpleDateFormat.parse(endDate).getTime();
            double cumulativeSum = 0.00d;
            int count = 0;

            for (long start = startTime; start <= endTime; start += ONE_DAY) {

                double price = bitcoinPriceRepository.getBitcoinPriceMap().get(start);
                cumulativeSum += price;
                count++;
                double ma = cumulativeSum / count;

                BitcoinTimestampData eachPriceMovement = new BitcoinTimestampData();
                eachPriceMovement.setPrice(price);
                Date priceDate = new Date();
                priceDate.setTime(start);
                eachPriceMovement.setPriceDate(simpleDateFormat.format(priceDate));
                eachPriceMovement.setMovingAveragePrice(ma);

                bitcoinPriceList.getPriceList().add(eachPriceMovement);
            }

        } catch (ParseException pe) {
            logger.log(Level.INFO, pe.getMessage(), pe);
        }

        return bitcoinPriceList;
    }

    /**
     * @param startTime of price movement analysis.
     * @param endTime   of price movement analysis.
     * @return list of bitcoin prices within date ranges.
     */
    public BitcoinPriceList getPriceMovement(long startTime, long endTime) {
        BitcoinPriceList bitcoinPriceList = new BitcoinPriceList();


        double cumulativeSum = 0.00d;
        int count = 0;

        for (long start = startTime; start <= endTime; start += ONE_DAY) {

            double price = bitcoinPriceRepository.getBitcoinPriceMap().get(start);
            cumulativeSum += price;
            count++;
            double ma = cumulativeSum / count;

            BitcoinTimestampData eachPriceMovement = new BitcoinTimestampData();
            eachPriceMovement.setPrice(price);
            Date priceDate = new Date();
            priceDate.setTime(start);
            eachPriceMovement.setPriceDate(simpleDateFormat.format(priceDate));
            eachPriceMovement.setMovingAveragePrice(ma);

            bitcoinPriceList.getPriceList().add(eachPriceMovement);
        }


        return bitcoinPriceList;
    }

    /**
     * Get yearly price movement.
     *
     * @return list containing yearly price movement.
     */
    public BitcoinPriceList getYearlyPriceMovement() {
        LocalDate now = LocalDate.now();     // The current date and time
        LocalDate oneYearBefore = now.minus(1, ChronoUnit.YEARS);
        ZonedDateTime zdt = now.atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime zdtOneYearBefore = oneYearBefore.atStartOfDay(ZoneId.of("UTC"));
        return getPriceMovement(zdtOneYearBefore.toEpochSecond() * 1000, zdt.toEpochSecond() * 1000);
    }

    /**
     * Get monthly price movement.
     *
     * @return list containing yearly price movement.
     */
    public BitcoinPriceList getMonthlyPriceMovement() {
        LocalDate now = LocalDate.now();     // The current date and time
        LocalDate oneMonthBefore = now.minus(1, ChronoUnit.MONTHS);
        ZonedDateTime zdt = now.atStartOfDay().atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOneMonthBefore = oneMonthBefore.atStartOfDay().atZone(ZoneId.of("UTC"));
        return getPriceMovement(zdtOneMonthBefore.toEpochSecond() * 1000, zdt.toEpochSecond() * 1000);
    }

    /**
     * Get monthly price movement.
     *
     * @return list containing yearly price movement.
     */
    public BitcoinPriceList getWeeklyPriceMovement() {
        LocalDate now = LocalDate.now();     // The current date and time
        LocalDate oneWeekBefore = now.minus(1, ChronoUnit.WEEKS);
        ZonedDateTime zdt = now.atStartOfDay().atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOneWeekBefore = oneWeekBefore.atStartOfDay().atZone(ZoneId.of("UTC"));
        return getPriceMovement(zdtOneWeekBefore.toEpochSecond() * 1000, zdt.toEpochSecond() * 1000);
    }

    /**
     * Get monthly price movement.
     *
     * @return list containing yearly price movement.
     */
    public BitcoinPriceList getDailyPriceMovement() {
        LocalDate now = LocalDate.now();     // The current date and time
        LocalDate oneDayBefore = now.minus(0, ChronoUnit.DAYS);
        ZonedDateTime zdt = now.atStartOfDay().atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOneDayBefore = oneDayBefore.atStartOfDay().atZone(ZoneId.of("UTC"));
        return getPriceMovement(zdtOneDayBefore.toEpochSecond() * 1000,zdt.toEpochSecond() * 1000);
    }

}
