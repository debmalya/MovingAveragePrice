package org.deb.bitcoin.model;

public class BitcoinTimestampData  {
    @Override
    public String toString() {
        return "BitcoinTimestampData{" +
                "priceDate='" + priceDate + '\'' +
                ", price=" + price +
                ", movingAveragePrice=" + movingAveragePrice +
                '}';
    }

    private String priceDate;
        private double price;
        private double movingAveragePrice;

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMovingAveragePrice() {
        return movingAveragePrice;
    }

    public void setMovingAveragePrice(double movingAveragePrice) {
        this.movingAveragePrice = movingAveragePrice;
    }
}
