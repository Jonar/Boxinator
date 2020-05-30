package se.fortnox.boxinator.model;

import java.util.UUID;

public class Box { //DAO
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
