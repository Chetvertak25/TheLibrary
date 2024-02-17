package ru.chetvertak.TheLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ru.chetvertak.TheLibrary.util.Role;

@SpringBootApplication
public class TheLibraryApplication {

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

	@Bean
	public Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Bean
	public SecurityContextHolder securityContextHolder(){
		return new SecurityContextHolder();
	}

	public static void main(String[] args) {
		SpringApplication.run(TheLibraryApplication.class, args);
	}

}
