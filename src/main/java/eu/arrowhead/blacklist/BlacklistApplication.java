package eu.arrowhead.blacklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import eu.arrowhead.common.Constants;

import eu.arrowhead.common.jpa.RefreshableRepositoryImpl;

@SpringBootApplication
@ComponentScan(basePackages = Constants.BASE_PACKAGE,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = eu.arrowhead.common.http.filter.authorization.BlacklistFilter.class
    ))
@EntityScan(BlacklistConstants.DATABASE_ENTITY_PACKAGE)
@EnableJpaRepositories(basePackages = BlacklistConstants.DATABASE_REPOSITORY_PACKAGE, repositoryBaseClass = RefreshableRepositoryImpl.class)
public class BlacklistApplication {

	public static void main(final String[] args) {
		SpringApplication.run(BlacklistApplication.class, args);
	}

}
