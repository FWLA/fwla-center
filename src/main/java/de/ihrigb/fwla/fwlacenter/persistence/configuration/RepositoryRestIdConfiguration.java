package de.ihrigb.fwla.fwlacenter.persistence.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

@Configuration
public class RepositoryRestIdConfiguration {

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurerAdapter() {

			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
				config.exposeIdsFor(RegexPattern.class, Station.class);
			}
		};
	}
}
