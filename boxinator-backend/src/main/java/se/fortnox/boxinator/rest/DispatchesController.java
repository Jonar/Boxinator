package se.fortnox.boxinator.rest;

import se.fortnox.boxinator.model.Model;
import spark.Request;
import spark.Response;
import spark.Route;

public class DispatchesController implements Route {
    private Model model;

    public DispatchesController(Model model) {
        this.model = model;
    }

    @Override
    public Object handle(Request request, Response response) {
        return model.getAllBoxesAsDispatches();
    }
}
