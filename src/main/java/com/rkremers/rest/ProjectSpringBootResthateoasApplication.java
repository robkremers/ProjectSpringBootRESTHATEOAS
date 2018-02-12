package com.rkremers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rkremers.rest.model.Student;
import com.rkremers.rest.repository.StudentRepository;

/**
 * Note:
 * This annotation includes:
 * - ComponentScan
 * - AutoConfiguration.
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
}
