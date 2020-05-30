package se.fortnox.boxinator.model;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oPersistenceModel implements Model {

    private Sql2o sql2o;
    //TODO: Add simple caching (List + isValid)

    public Sql2oPersistenceModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Box addBox(Box box) {
        try (Connection conn = sql2o.open()) {
            UUID boxId = UUID.randomUUID();
            conn.createQuery("INSERT INTO boxes VALUES (:uuid, :receiver, :weight, :color, :country)")
                    .addParameter("uuid", boxId)
                    .addParameter("receiver", box.receiver)
                    .addParameter("weight", box.weight)
                    .addParameter("color", box.color)
                    .addParameter("country", box.country)
                    .executeUpdate();
            return getBox(boxId);
        }
    }

    private Box getBox(UUID boxId) {
        try (Connection conn = sql2o.open()) {
            List<Box> boxes = conn.createQuery("SELECT * FROM boxes WHERE id=:uuid")
                    .addParameter("uuid", boxId)
                    .executeAndFetch(Box.class);
            return boxes.isEmpty() ? null : boxes.get(0);
        }
    }

    @Override
    public List<Box> getAllBoxes() {
        try (Connection conn = sql2o.open()) {
            List<Box> boxes = conn.createQuery("SELECT * FROM boxes")
                    .executeAndFetch(Box.class);
            return boxes;
        }
    }
}
