package se.fortnox.boxinator;

import static spark.Spark.*;

import com.google.gson.Gson;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BoxinatorServer {
    public static void main(String[] args) {
        Gson convert = new Gson();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/boxinator",
                "postgres", "docker", new PostgresQuirks() {
            {
                // make sure default UUID converter is used
                converters.put(UUID.class, new UUIDConverter());
            }
        });
        Model model = new Sql2oPersistenceModel(sql2o);

        enableCORS("http://localhost:3000", "GET, POST", "Accept, Content-Type");

        post("/box", "application/json", (request, response) -> {
            Box box = convert.fromJson(request.body(), Box.class);
            //TODO: validate box input
            model.addBox(box);
            return box;
        }, convert::toJson);

        get("/dispatches", "application/json",
                (request, response) -> model.getAllBoxesAsDispatches(), convert::toJson);
    }

    /** Enables CORS on requests. This method is an initialization method and should be called once. */
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);

            response.type("application/json");
        });
    }
}

class Box { //DAO
    public UUID id;
    public String receiver;
    public double weight;
    public String color; //TODO: Color class
    public String country;

    Box(String receiver, double weight, String color, String country) {
        this.receiver = receiver;
        this.weight = weight;
        this.color = color;
        this.country = country;
    }
}

class Dispatch { //DTO
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

interface Model {
    Box addBox(Box box);
    List<Box> getAllBoxes();

    default List<Dispatch> getAllBoxesAsDispatches() {
        return getAllBoxes().stream()
                .map(Dispatch::fromBox)
                .collect(Collectors.toList());
    }
}

class InMemoryModel implements Model {

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

class Sql2oPersistenceModel implements Model {

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

