package se.fortnox.boxinator.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class ShippingCalculatorTest {

    public static final String COUNTRY_SWEDEN = "Sweden";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    @Parameters({"Sweden, 1.30",
            "China, 4.00",
            "Brazil, 8.60",
            "Australia, 7.20"})
    public void priceEqualsMultiplierWhenWeightIsOneKg(String country, String expectedPrice) {
        BigDecimal shippingPrice = ShippingCalculator.calculatePrice(BigDecimal.ONE, country);
        assertThat(shippingPrice).isEqualTo(expectedPrice);
    }

    @Test
    public void usingUnknownCountryThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Unknown country");
        String country = "Unknown";
        ShippingCalculator.calculatePrice(BigDecimal.ONE, country);
    }

    @Test
    public void testRoundingUp() {
        BigDecimal weight = new BigDecimal("0.55");
        BigDecimal shippingPrice = ShippingCalculator.calculatePrice(weight, COUNTRY_SWEDEN);
        assertThat(shippingPrice).isEqualTo("0.72"); //Rounding up from 0.715
    }

    @Test
    public void testRoundingDown() {
        final BigDecimal weight = new BigDecimal("1.01");
        BigDecimal shippingPrice = ShippingCalculator.calculatePrice(weight, COUNTRY_SWEDEN);
        assertThat(shippingPrice).isEqualTo("1.31"); //Rounding down from 1.313
    }

    /**
     * Minimum price should become 0.01 when weight is very low. Test with 1 gram.
     * Rounding down to two digits from 0.001*1.3=0.0013 should yield 0,01 and not 0.00
     * */
    @Test
    public void testMinimumPrice() {
        BigDecimal oneGram = new BigDecimal("0.001");
        BigDecimal shippingPrice = ShippingCalculator.calculatePrice(oneGram, COUNTRY_SWEDEN);
        assertThat(shippingPrice).isEqualTo("0.01");
    }
}