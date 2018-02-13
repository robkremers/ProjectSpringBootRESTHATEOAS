package com.rkremers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import com.rkremers.rest.model.Student;
import com.rkremers.rest.repository.StudentRepository;

/**
 * Note:
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * @Configuration tags the class as a source of bean definitions for the application context.
 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. 
 * This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 * @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.
 * 
 * @author LTAdmin
 *
 */
@SpringBootApplication
public class ProjectSpringBootResthateoasApplication implements CommandLineRunner {
	
	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectSpringBootResthateoasApplication.class, args);
	}

	/**
	 * For test purposes to fill the H2 database.
	 */
	@Override
	public void run(String... args) throws Exception {
		studentRepository.save( new Student("Rob", "Kremers", "abc123", 55) );
		studentRepository.save( new Student("Martijn", "Salm van der", "blabla44421", 30));
		studentRepository.save( new Student("Dennis", "Geurts", "A4D9I08JE", 30));
		
	}
	
	/**
	 * Note:
	 * For future test investigation: production vs. testing.
	 * 
	 * @author LTAdmin
	 *
	 */
	@Profile("test")
	class Test {
		
	}
	
	@Profile("production") 
    class Production {
    	
	}
}
