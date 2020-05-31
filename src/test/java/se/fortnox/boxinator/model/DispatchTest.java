package se.fortnox.boxinator.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class DispatchTest {

    @Test
    public void creatingFromBoxCalculatesShippingCost() {
        Box box = new Box("Örjan Viking", 1.5, "#9e0004", "Sweden");
        Dispatch dispatch = Dispatch.fromBox(box);
        assertThat(dispatch.receiver).isEqualTo(box.receiver);
        assertThat(dispatch.weight).isEqualTo(box.weight);
        assertThat(dispatch.color).isEqualTo(box.color);
        assertThat(dispatch.shippingCost).isEqualTo(new BigDecimal("1.95"));
    }
}