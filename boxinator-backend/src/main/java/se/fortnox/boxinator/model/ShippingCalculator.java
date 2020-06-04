package se.fortnox.boxinator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static java.util.Map.entry;

public final class ShippingCalculator {

    static final Map<String, BigDecimal> countryMultipliers = Map.ofEntries(
            entry("Sweden", new BigDecimal("1.3")),
            entry("China", new BigDecimal("4")),
            entry("Brazil", new BigDecimal("8.6")),
            entry("Australia", new BigDecimal("7.2"))
    );

    public static BigDecimal calculatePrice(BigDecimal weight, String country) {
        if(countryMultipliers.containsKey(country)) {
            BigDecimal price = countryMultipliers.get(country);
            return price.multiply(weight)
                    .setScale(2, RoundingMode.HALF_UP) //two decimals
                    .max(BigDecimal.valueOf(0.01)); //don't round to zero; minimum price is 0.01
        } else {
            throw new IllegalArgumentException("Unknown country");
        }
    }
}
