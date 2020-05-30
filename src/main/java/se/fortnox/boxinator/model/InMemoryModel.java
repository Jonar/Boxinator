package se.fortnox.boxinator.model;

import java.util.ArrayList;
import java.util.List;

public class InMemoryModel implements Model {

    private List<Box> boxes = new ArrayList<>();

    @Override
    public Box addBox(Box box) {
        boxes.add(box);
        return box;
    }

    @Override
    public List<Box> getAllBoxes() {
        return boxes;
    }
}
