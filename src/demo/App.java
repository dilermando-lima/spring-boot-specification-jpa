package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {

        var newArgs = new String[] {
                // server 
                "--server.port=8081",
                // database source h2
                "--spring.datasource.url=jdbc:h2:mem:testdb",
                "--spring.datasource.driverClassName=org.h2.Driver",
                "--spring.datasource.username=sa",
                "--spring.datasource.password=password",
                "--spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "--spring.h2.console.enabled=true",
                "--spring.jpa.show-sql=true",
                "--spring.jpa.properties.hibernate.format_sql=true",
                // handle 404 mapping response
                "--spring.mvc.throw-exception-if-no-handler-found=true",
                "--spring.web.resources.add-mappings=false",
                // default swagger config
                "--springdoc.swagger-ui.path=/swagger",
                "--springdoc.api-docs.path=/swagger/api-docs"
        };

        SpringApplication.run(App.class, newArgs);
     }

}
