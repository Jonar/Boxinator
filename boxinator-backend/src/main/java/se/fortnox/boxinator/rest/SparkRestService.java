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
    private Gson responseConverter = new Gson();

    public SparkRestService(Model model) {
        this.model = model;
    }

    @Override
    public void start() {
        enableCORS();
        // Expose endpoints
        post("/box", "application/json", new BoxController(model), responseConverter::toJson);
        get("/dispatches", "application/json", new DispatchesController(model), responseConverter::toJson);
    }

    /**
     * Enables CORS for requests.
     * This initialization method should be called once.
     */
    private void enableCORS() {
        String origin = System.getenv("ALLOW_ORIGIN");
        if(origin == null) { origin = "*"; }

        final String allowedOrigin = origin;
        final String allowedMethods = "GET, POST";
        final String allowedHeaders  ="Accept, Content-Type";

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
            response.header("Access-Control-Allow-Origin", allowedOrigin);
            response.header("Access-Control-Request-Method", allowedMethods);
            response.header("Access-Control-Allow-Headers", allowedHeaders);

            response.type("application/json");
        });
    }
}
