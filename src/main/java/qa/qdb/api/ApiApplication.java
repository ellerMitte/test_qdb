package qa.qdb.api;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import qa.qdb.api.model.User;
import qa.qdb.api.repository.UserRepository;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ApplicationRunner initializer(UserRepository userRepository) {
        userRepository.deleteAll();
        return args -> userRepository.saveAll(Arrays.asList(
                User.builder().id(1L).username("user").password("$2a$12$hDOBbp4ZhgWoJiel57ldP.GgOGX/eWJB9N3YUV81O4P.hIqLLBbbS").build(),
                User.builder().id(2L).username("user2").password("$2a$12$hDOBbp4ZhgWoJiel57ldP.GgOGX/eWJB9N3YUV81O4P.hIqLLBbbS").build()
        ));
    }
}
