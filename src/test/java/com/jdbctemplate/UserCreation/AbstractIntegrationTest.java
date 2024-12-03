package com.jdbctemplate.UserCreation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withExposedPorts(5432)
            .withInitScript("schema.sql");






    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            POSTGRES.start();

            String[] variables = {
                    "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES.getUsername(),
                    "spring.datasource.password=" + POSTGRES.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=update"
            };

            logger.info("initialize: {}", Arrays.toString(variables));

            var testPropertyValues = TestPropertyValues.of(variables);
            testPropertyValues.applyTo(applicationContext.getEnvironment());
        }
    }
}