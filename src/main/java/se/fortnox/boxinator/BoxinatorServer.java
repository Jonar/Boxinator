package se.fortnox.boxinator;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoxinatorServer {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Box> boxes = new ArrayList<>();

        post("/box", "application/json", (request, response) -> {
            Box box = gson.fromJson(request.body(), Box.class);
            //TODO: validate box input
            boxes.add(box);
            response.type("application/json");
            return box;
        }, gson::toJson);

        get("/dispatches", "application/json", (request, response) -> {
            response.type("application/json");
            //TODO: read boxes from DB
            List<Dispatch> dispatches = boxes.stream()
                    .map(Dispatch::fromBox)
                    .collect(Collectors.toList());
            return dispatches;
        }, gson::toJson);
    }
}

class Box { //DAO
    String receiver;
    int weight;
    String color; //TODO: Color class
    String country;

    Box(String receiver, int weight, String color, String country) {
        this.receiver = receiver;
        this.weight = weight;
        this.color = color;
        this.country = country;
    }
}

class Dispatch { //DTO
    String receiver;
    int weight;
    String color; //TODO: Color class
    double shippingCost;

    Dispatch(String receiver, int weight, String color, double shippingCost) {
        this.receiver = receiver;
        this.weight = weight;
        this.color = color;
        this.shippingCost = shippingCost;
    }

    public static Dispatch fromBox(Box box) {
        double shippingCost = calculateShipping(box.weight, box.country);
        return new Dispatch(box.receiver, box.weight, box.color, shippingCost);
    }

    private static double calculateShipping(int weight, String country) {
        double shippingCost;
        switch (country) {
            case "Sweden":
                shippingCost = 1.3 * weight;
                break;
            case "China":
                shippingCost = 4 * weight;
                break;
            case "Brazil":
                shippingCost = 8.6 * weight;
                break;
            case "Australia":
                shippingCost = 7.2 * weight;
                break;
            default:
                throw new IllegalArgumentException("Unknown country");
        }
        return shippingCost;
    }
}
