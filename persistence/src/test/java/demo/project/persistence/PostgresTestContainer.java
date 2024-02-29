package demo.project.persistence;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer implements BeforeAllCallback, AfterAllCallback {

    private PostgreSQLContainer<?> postgres;

    @Override
    public void beforeAll(ExtensionContext context) {
        postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .withExposedPorts(5432);

        postgres.start();
        System.setProperty("spring.flyway.url",
                String.format("jdbc:postgresql://localhost:%d/test", postgres.getFirstMappedPort()));
        System.setProperty("spring.flyway.user", postgres.getUsername());
        System.setProperty("spring.flyway.password", postgres.getPassword());

        System.setProperty("spring.r2dbc.url",
                String.format("r2dbc:postgresql://localhost:%d/test", postgres.getFirstMappedPort()));
        System.setProperty("spring.r2dbc.username", postgres.getUsername());
        System.setProperty("spring.r2dbc.password", postgres.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // do nothing, Testcontainers handles container shutdown
    }
}