package se.fortnox.boxinator;

import se.fortnox.boxinator.model.Model;
import se.fortnox.boxinator.model.ModelFactory;
import se.fortnox.boxinator.rest.RestService;
import se.fortnox.boxinator.rest.SparkRestService;

public class BoxinatorServer {
    public static void main(String[] args) {
        Model model = ModelFactory.postgresPersistenceModel();
        RestService server = new SparkRestService(model);
        server.start();
    }
}



