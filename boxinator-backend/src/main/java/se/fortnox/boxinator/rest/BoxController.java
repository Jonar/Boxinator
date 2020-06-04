package se.fortnox.boxinator.rest;

import com.google.gson.Gson;
import se.fortnox.boxinator.model.Box;
import se.fortnox.boxinator.model.Model;
import spark.Request;
import spark.Response;
import spark.Route;

public class BoxController implements Route {
    private Model model;
    private Gson convert = new Gson();

    public BoxController(Model model) {
        this.model = model;
    }

    @Override
    public Object handle(Request request, Response response) {
        Box box = convert.fromJson(request.body(), Box.class);
        //TODO improvement: validate box input here
        model.addBox(box);
        return box;
    }
}
