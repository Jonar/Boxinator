package se.fortnox.boxinator;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoxinatorServer {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Box> boxes = new ArrayList<>();

        enableCORS("http://localhost:3000", "GET, POST", "Accept, Content-Type");

        post("/box", "application/json", (request, response) -> {
            Box box = gson.fromJson(request.body(), Box.class);
            //TODO: validate box input
            boxes.add(box);
            //TODO: write box to DB
            return box;
        }, gson::toJson);

        get("/dispatches", "application/json", (request, response) -> {
            //TODO: read boxes from DB
            List<Dispatch> dispatches = boxes.stream()
                    .map(Dispatch::fromBox)
                    .collect(Collectors.toList());
            return dispatches;
        }, gson::toJson);
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
    String receiver;
    double weight;
    String color; //TODO: Color class
    String country;

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
    String color; //TODO: Color class
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
