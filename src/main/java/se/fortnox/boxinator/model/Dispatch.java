package se.fortnox.boxinator.model;

import java.math.BigDecimal;

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
        final BigDecimal shippingCost = ShippingCalculator.calculatePrice(weight, box.country);
        return new Dispatch(box.receiver, box.weight, box.color, shippingCost);
    }

}
