package se.fortnox.boxinator.rest;

import com.google.gson.Gson;
import se.fortnox.boxinator.model.Model;

import static spark.Spark.*;

/**
 * Exposes Model resources via REST view.
 * Controllers handle mapping of incoming requests to Model.
 */
public class SparkRestService implements RestService {
    private Model model;

    public SparkRestService(Model model) {
        this.model = model;
    }

    @Override
    public void start() {
        enableCORS("http://localhost:3000", "GET, POST", "Accept, Content-Type");
        Gson response = new Gson();

        post("/box", "application/json", new BoxController(model), response::toJson);

        get("/dispatches", "application/json", new DispatchesController(model), response::toJson);
    }

    /**
     * Enables CORS for requests.
     * This initialization method should be called once.
     */
    private void enableCORS(final String origin, final String methods, final String headers) {

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
