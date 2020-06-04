package se.fortnox.boxinator.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ModelTest {

    Model model = new InMemoryModel(); //Use InMemoryModel as fake test double

    /**
     * Verify that interface default method correctly maps boxes to dispatches.
     * Indirectly exercises addBox and getAllBoxes.
     * */
    @Test
    public void getAllBoxesAsDispatches() {
        //Arrange - Given
        final Box chinaBox = new Box("习近平", 2.0, "#ff2e2e", "China");
        model.addBox(chinaBox);
        final Box brazilBox = new Box("Antônia do Rio Branco", 1.0, "#19b209", "Brazil");
        model.addBox(brazilBox);

        //Act - When
        final List<Dispatch> dispatches = model.getAllBoxesAsDispatches();

        //Assert - Then
        assertThat(dispatches).hasSize(2);
        assertThat(dispatches).extracting("receiver", "weight", "color", "shippingCost")
                .contains(tuple(chinaBox.receiver, chinaBox.weight, chinaBox.color, new BigDecimal("8.00")),
                        tuple(brazilBox.receiver, brazilBox.weight, brazilBox.color, new BigDecimal("8.60")));
    }
}