package org.deb.bitcoin.controller;

import org.deb.bitcoin.model.BitcoinPriceList;
import org.deb.bitcoin.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin
@RestController
public class BitcoinController {
    private static final Logger logger = Logger.getLogger("BitcoinController");

    @Autowired
    BitcoinService bitcoinService;

    @RequestMapping(value = "/mv", produces = "application/json")
    public ResponseEntity<BitcoinPriceList> getMovingAverage(@RequestParam(value = "startDate", required = true) String startDate,
                                                             @RequestParam(value = "endDate", required = true) String endDate) {
        ResponseEntity<BitcoinPriceList> response = null;
        try {
            response = new ResponseEntity<>(bitcoinService.getPriceMovement(startDate, endDate), HttpStatus.OK);
        } catch (Throwable th) {
            logger.log(Level.SEVERE,th.getMessage(),th);
            response = new ResponseEntity<>(new BitcoinPriceList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @RequestMapping(value = "/mvcategory", produces = "application/json")
    public ResponseEntity<BitcoinPriceList> getMovingAverageWithCategory(@RequestParam(value = "timePeriod", required = true) String timePeriod) {
        ResponseEntity<BitcoinPriceList> response = null;
        try {
            if ("yearly".equalsIgnoreCase(timePeriod)) {
                response = new ResponseEntity<>(bitcoinService.getYearlyPriceMovement(), HttpStatus.OK);
            } else if ("monthly".equalsIgnoreCase(timePeriod)) {
                response = new ResponseEntity<>(bitcoinService.getMonthlyPriceMovement(), HttpStatus.OK);
            }else if ("weekly".equalsIgnoreCase(timePeriod)) {
                response = new ResponseEntity<>(bitcoinService.getWeeklyPriceMovement(), HttpStatus.OK);
            }else if ("daily".equalsIgnoreCase(timePeriod)) {
                response = new ResponseEntity<>(bitcoinService.getDailyPriceMovement(), HttpStatus.OK);
            }
        } catch (Throwable th) {
            logger.log(Level.SEVERE,th.getMessage(),th);
            response = new ResponseEntity<>(new BitcoinPriceList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }


}
