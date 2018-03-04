package com.rkremers.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import com.rkremers.rest.exception.StudyConfigurationNotFound;
import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.model.StudyConfiguration;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;
import com.rkremers.rest.repository.StudyConfigurationRepository;
import com.rkremers.rest.service.CourseService;

/**
 * Note:
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * @StudyConfiguration tags the class as a source of bean definitions for the application context.
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
	
	@Autowired
	private StudyConfigurationRepository studyConfigurationRepository;
	
	@Autowired
	private CourseService courseService;

	public static void main(String[] args) {
		SpringApplication.run(ProjectSpringBootResthateoasApplication.class, args);
	}

	/**
	 * For test purposes to fill the H2 database.
	 */
	@Override
	public void run(String... args) throws Exception {

		logger.info("********** Saving the configurations: **********\n");
		
		logger.info("********** Saving the standard note for passing an exam: **********");
		StudyConfiguration passValue = new StudyConfiguration("Standard Pass Value", "5.50", "Standard value for passing an exam.");
		studyConfigurationRepository.save(passValue);
		logger.info("********** Saved the standard note for passing an exam: **********");
		
		Optional<StudyConfiguration> standardPassValue = studyConfigurationRepository.findByParameterName("Standard Pass Value");
		logger.info("The value for Standard Pass Value is " + standardPassValue.get().getParameterStrValue() + "." );
		
		String strStandardPassValue = studyConfigurationRepository.findParameterValue("Standard Pass Value");
		logger.info("The value for Standard Pass Value is " + strStandardPassValue + "." );
		if ( !standardPassValue.isPresent() ) {
			throw new StudyConfigurationNotFound("The value for Standard Pass Value could not be found.");
		}
		

		logger.info("********** Saving the customer data: **********\n");
		
		Student studentRob = new Student("Rob", "Kremers", "abc123", 55);
		Student studentMartijn = new Student("Martijn", "Salm van der", "blabla44421", 30);
		Student studentDennis = new Student("Dennis", "Geurts", "A4D9I08JE", 30);
				
		Course courseBasic = new Course("Basic", "Basic Course", Double.parseDouble(standardPassValue.get().getParameterStrValue()));
		Course courseMedium = new Course("Medium", "Medium Course", Double.parseDouble(standardPassValue.get().getParameterStrValue()));
		Course courseAdvanced = new Course("Advanced", "Advanced Course", Double.parseDouble(standardPassValue.get().getParameterStrValue()));

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
				
		/*
		 * Tests regarding the CourseService.
		 */
		
		logger.info("********** Saving Course courseTest: **********");
		Course courseTest = new Course("Test", "Test Course");		
		courseService.addCourse(courseTest);
		logger.info("********** Saved Course courseTest: **********");
		
		logger.info("********** Overview of all courses: **********");
		List<Course> courses = courseService.getAllCourses();
		for (Course course: courses) {
			logger.info(course.toString() );
		}
		logger.info("********** End overview of all courses: **********");
		
		logger.info("********** Find course 1: **********");
		Optional<Course> course1 = courseService.getCourse(1);
		if (course1.isPresent()) {
		  logger.info(course1.get().toString());
		} else {
			logger.info("A course with courseId 1 could not be found");
		}
		logger.info("********** Found course 1: **********");

/*		logger.info("********** Find course 10: **********");
		Optional<Course> course10 = courseService.getCourse(10);
		if (course1.isPresent()) {
		  logger.info(course10.get().toString());
		} else {
			logger.info("A course with courseId 10 could not be found");
		}
		logger.info("********** Found course 10: **********");
*/		
		logger.info("********** Find all students that have followed course 1: **********");
		List<Student> students = courseService.getAllCourseStudents(courseBasic);
		for (Student student: students) {
			logger.info(student.toString());
		}
		logger.info("********** Found all students that have followed course 1: **********");
		
		logger.info("********** Add Course courseTest2: **********");
		Course courseTest2 = new Course("Test2", "Test Course");
		logger.info("new course courseTest2: " + courseTest2.toString());
		courseTest2 = courseService.addCourse(courseTest2);
		logger.info("new course courseTest2: " + courseTest2.toString());
		logger.info("********** Added Course courseTest2: **********");

		logger.info("********** Update Course courseTest2: **********");
		courseTest2.setPrecursorCourse(courseTest);
		logger.info("new course courseTest2: " + courseTest2.toString());
		// Continue here tomorrow: the service does not save the instance for the precursor course.
		courseTest2 = courseService.updateCourse(courseTest2);
		logger.info("new course courseTest2: " + courseTest2.toString());
		logger.info("********** Updated Course courseTest2: **********");

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
