package com.rkremers.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
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
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectSpringBootResthateoasApplication.class);

	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private StudentCourseRepository studentCourseRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectSpringBootResthateoasApplication.class, args);
	}

	/**
	 * For test purposes to fill the H2 database.
	 */
	@Override
	public void run(String... args) throws Exception {
		
		Student studentRob = new Student("Rob", "Kremers", "abc123", 55);
		Student studentMartijn = new Student("Martijn", "Salm van der", "blabla44421", 30);
		Student studentDennis = new Student("Dennis", "Geurts", "A4D9I08JE", 30);
				
		Course courseBasic = new Course("Basic", "Basic Course");
		Course courseMedium = new Course("Medium", "Medium Course");
		Course courseAdvanced = new Course("Advanced", "Advanced Course");

		studentRepository.save( studentRob );
		studentRepository.save( studentMartijn);
		studentRepository.save( studentDennis);
		logger.info("StudentRob has been saved.");
		logger.info("********** Overview of the Students: **********");
		logger.info(studentRob.toString());
		logger.info("********** End Overview of the Students: **********");
		
		courseRepository.save(courseBasic);
		courseRepository.save(courseMedium);
		courseRepository.save(courseAdvanced);
		logger.info("courseBasic has been saved.");
		logger.info("********** Overview of the Courses: **********");
		logger.info(courseBasic.toString());
		logger.info("********** End Overview of the Courses: **********");

		courseMedium.setPrecursorCourse(courseBasic);
		courseRepository.save(courseMedium);
		
		courseAdvanced.setPrecursorCourse(courseMedium);
		courseRepository.save(courseAdvanced);
		
//		studentRob.getCourses().add(courseMedium);
//
//		studentRob.getCourses().add(courseAdvanced);
//		studentRepository.save(studentRob);
//		
//		studentMartijn.getCourses().add(courseBasic);
//		studentRepository.save(studentMartijn);
		
		StudentCourse studentCourseRob = new StudentCourse(new Date() );
		studentCourseRepository.save(studentCourseRob);
		
		studentCourseRob.setStudent(studentRob);
		studentCourseRob.setCourse(courseBasic);
		studentCourseRob.setResult(9.0);
		logger.info("********** Overview of the StudentCourses: **********");
		logger.info(studentCourseRob.toString());
		logger.info("********** End Overview of the StudentCourses: **********");
		
		studentCourseRepository.save(studentCourseRob);
		
		logger.info("********** Saving student Iris: **********");
		Student studentIris = new Student("Iris", "Kikuchi", "PassportNrTest", 30);
		studentRepository.save(studentIris);
		logger.info("********** Saved student Iris: **********");

//		logger.info("********** Saving student Hahahiha: **********");
//		Student studentTest = new Student("Hahahiha", "Test", "PassportNrTest", 30);
//		studentRepository.save(studentTest);
//		logger.info("********** Saved student Iris: **********");

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
