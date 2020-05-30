package se.fortnox.boxinator;

import static spark.Spark.*;

import com.google.gson.Gson;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import se.fortnox.boxinator.model.Box;
import se.fortnox.boxinator.model.Model;
import se.fortnox.boxinator.model.Sql2oPersistenceModel;

import java.util.UUID;

public class BoxinatorServer {
    public static void main(String[] args) {
        Gson convert = new Gson();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/boxinator",
                "postgres", "docker", new PostgresQuirks() {
            {
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

