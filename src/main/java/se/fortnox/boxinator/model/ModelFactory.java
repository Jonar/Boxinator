package se.fortnox.boxinator.model;

import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.UUID;

public class ModelFactory {
    public static Model postgresPersistenceModel() {
        // Default are for devenv. Env values will be used in production.
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if(dbUrl == null) { dbUrl = "jdbc:postgresql://localhost:5432/boxinator"; }
        String user = System.getenv("POSTGRES_USER");
        if(user == null) { user = "postmaster"; }
        String password = System.getenv("POSTGRES_PASSWORD");
        if(password == null) { password = "docker"; }

        Sql2o sql2o = new Sql2o(dbUrl, user, password, new PostgresQuirks() {
            {
                converters.put(UUID.class, new UUIDConverter());
            }
        });
        return new Sql2oPersistenceModel(sql2o);
    }
}
