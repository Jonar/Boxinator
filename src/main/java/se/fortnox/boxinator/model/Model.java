package se.fortnox.boxinator.model;

import java.util.List;
import java.util.stream.Collectors;

public interface Model {
    Box addBox(Box box);
    List<Box> getAllBoxes();

    default List<Dispatch> getAllBoxesAsDispatches() {
        return getAllBoxes().stream()
                .map(Dispatch::fromBox)
                .collect(Collectors.toList());
    }
}
