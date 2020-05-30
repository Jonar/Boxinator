package se.fortnox.boxinator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Dispatch { //DTO
    String receiver;
    double weight;
    String color; //TODO: Color class. Use Color.decode("#FFCCEE") from Java AWT;
    BigDecimal shippingCost;

    Dispatch(String receiver, double weight, String color, double shippingCost) {
        this(receiver, weight, color, BigDecimal.valueOf(shippingCost));
    }

    Dispatch(String receiver, double weight, String color, BigDecimal shippingCost) {
        this.receiver = receiver;
        this.weight = weight;
        this.color = color;
        this.shippingCost = shippingCost;
    }

    public static Dispatch fromBox(Box box) {
        final BigDecimal weight = BigDecimal.valueOf(box.weight);
        final BigDecimal shippingCost = calculateShipping(weight, box.country);
        return new Dispatch(box.receiver, box.weight, box.color, shippingCost);
    }

    private static BigDecimal calculateShipping(BigDecimal weight, String country) {
        //TODO: Use Map<String,BigDecimal>
        final BigDecimal PRICE_SWEDEN = new BigDecimal("1.3");
        final BigDecimal PRICE_CHINA = new BigDecimal("4");
        final BigDecimal PRICE_BRAZIL = new BigDecimal("8.6");
        final BigDecimal PRICE_AUSTRALIA = new BigDecimal("7.2");
        BigDecimal price;

        switch (country) {
            case "Sweden":
                price = PRICE_SWEDEN;
                break;
            case "China":
                price = PRICE_CHINA;
                break;
            case "Brazil":
                price = PRICE_BRAZIL;
                break;
            case "Australia":
                price = PRICE_AUSTRALIA;
                break;
            default:
                throw new IllegalArgumentException("Unknown country");
        }
        BigDecimal shippingCost = price.multiply(weight)
                .setScale(2, RoundingMode.HALF_UP) //two decimals
                .max(BigDecimal.valueOf(0.01)); //don't round to zero; minimum price is 0.01

        return shippingCost;
    }
}
