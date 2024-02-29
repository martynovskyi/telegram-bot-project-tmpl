package demo.project.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableConfigurationProperties
@EnableR2dbcRepositories(basePackages = "demo.project.persistence")
@EnableR2dbcAuditing
@EntityScan(basePackages = "demo.project.persistence")
public class AnnotationConfig {
}
