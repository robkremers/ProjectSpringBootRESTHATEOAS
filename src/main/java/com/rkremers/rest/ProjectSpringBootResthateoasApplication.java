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
import com.rkremers.rest.service.StudentService;

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
	
	@Autowired
	private StudentService studentService;

	public static void main(String[] args) {
		SpringApplication.run(ProjectSpringBootResthateoasApplication.class, args);
	}

	/**
	 * For test purposes to fill the H2 database.
	 */
	@Override
	public void run(String... args) throws Exception {

		List<Course> courses;
		
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
		
		logger.info("********** Testing correct find courses, even though the precursor course is fetched lazily. **********");
		courses = courseService.getAllCourses();
//		logger.info("Number of stored courses: " + courses.size());
		for (Course course: courses) {
			logger.info(course.toString());
		}
		logger.info("********** End Testing correct find courses. **********");
		
		
		StudentCourse studentCourseRob = new StudentCourse(new Date() );
		studentCourseRepository.save(studentCourseRob);
		
		studentCourseRob.setStudent(studentRob);
		studentCourseRob.setCourse(courseBasic);
		studentCourseRob.setResult(9.0);
		logger.info("********** Overview of the StudentCourses: **********");
		logger.info(studentCourseRob.toString());
		logger.info("********** End Overview of the StudentCourses: **********");
		
		studentCourseRepository.save(studentCourseRob);
		
		StudentCourse studentCourseMartijnBasic = new StudentCourse(new Date() );
		studentCourseRepository.save(studentCourseMartijnBasic);
		studentCourseMartijnBasic.setStudent(studentMartijn);
		studentCourseMartijnBasic.setCourse(courseBasic);
		studentCourseMartijnBasic.setResult(7.0D);
		studentCourseRepository.save(studentCourseMartijnBasic);
		
		StudentCourse studentCourseMartijnMedium = new StudentCourse(new Date() );
		studentCourseRepository.save(studentCourseMartijnMedium);
		studentCourseMartijnMedium.setStudent(studentMartijn);
		studentCourseMartijnMedium.setCourse(courseMedium);
		studentCourseMartijnMedium.setResult(8.0);
		studentCourseRepository.save(studentCourseMartijnMedium);
		
		
		/*
		 * Tests regarding the CourseService.
		 */
		
		logger.info("********** Saving Course courseTest: **********");
		Course courseTest = new Course("Test", "Test Course");		
		courseService.addCourse(courseTest);
		logger.info("********** Saved Course courseTest: **********");
		
		/**
		 * Check whether all courses can be fetched, whether or not they have a precursor course.
		 */
		logger.info("\n********** Overview of all courses: **********\n");
		courses = courseService.getAllCourses();
		for (Course course: courses) {
			logger.info(course.toString() );
		}
		logger.info("\n********** End overview of all courses: **********\n");
		
		logger.info("\n********** Find course 1, Course Basic: **********\n");
		Optional<Course> course1 = courseService.getCourse(1);
		if (course1.isPresent()) {
		  logger.info(course1.get().toString());
		} else {
			logger.info("A course with courseId 1 could not be found");
		}
		logger.info("\n********** End finding course 1: **********\n");

		logger.info("\n********** Find course 2, Course Medium: **********\n");
		Optional<Course> course2 = courseService.getCourse(2);
		if (course2.isPresent()) {
		  logger.info(course2.get().toString());
		} else {
			logger.info("A course with courseId 2 could not be found");
		}
		logger.info("\n********** End finding course 2: **********\n");
	
		logger.info("\n********** Find all students that have followed course 1: **********\n");
		List<Student> students = courseService.getAllCourseStudents(courseBasic);
		
		if ( students != null ) {
			for (Student student: students ) {
				logger.info(student.toString());
			}
		} else {
			logger.info("No corresponding students have been found.");
		}
		logger.info("\n********** Found all students that have followed course 1: **********\n");
		
		logger.info("********** Add Course courseTest2: **********");
		Course courseTest2 = new Course("Test2", "Test Course");
		logger.info("new course courseTest2: " + courseTest2.toString());
		courseTest2 = courseService.addCourse(courseTest2);
		logger.info("new course courseTest2: " + courseTest2.toString());
		logger.info("********** Added Course courseTest2: **********");

		logger.info("********** Update Course courseTest2 setting precursor course: **********");
		courseTest2.setPrecursorCourse(courseTest);
		logger.info("Course courseTest2 with precursor course, not yet persisted: " + courseTest2.toString());
		courseTest2 = courseService.updateCourse(courseTest2);
		logger.info("Updated and persisted course courseTest2: " + courseTest2.toString());
		logger.info("********** Updated Course courseTest2: **********");		
		
		logger.info("\n********** Create Course courseTest3 and add precursor course via CourseService: **********");
		Course courseTest3 = new Course("Test3", "Test Course 3");
		courseService.addCourse(courseTest3);
		courseTest3.setPrecursorCourse(courseTest);

		/**
		 * Below is tested whether after the update with a lazily attached precursor course
		 * the update (via courseRepository.save() the precursor course will be shown.
		 * This is the case (which means I do not have to separately use a getCourse to check 
		 * whether the precursor course is indeed present.
		 * 
		 */
		Course courseUpdated = courseService.updateCourse(courseTest3);
		logger.info("Updated and persisted course courseTest3: " + courseUpdated.toString() + "\n");
		courseUpdated = courseService.getCourse(courseTest3.getCourseId()).get();
		logger.info("Updated and persisted course courseTest3, including precursor course: " + courseUpdated.toString() + "\n");
		logger.info("\n********** End Create Course courseTest3 and add precursor course via CourseService: **********\n");
		
		logger.info("\nFinding all courses, including the info that is being fetched lazily.\n");
		courses = courseService.getAllCourses();
		logger.info("Number of stored courses: " + courses.size());
		for (Course course: courses) {
			logger.info(course.toString());
		}
		logger.info("\n********** End Finding all courses. **********\n");
		
		logger.info("\n********** Testing deletion of a course: **********\n\n");
		courseService.deleteCourse(courseTest3);
		logger.info("\n********** Tested deletion of a course: **********\n\n");

		/*
		 * Tests regarding the StudentService.
		 */
		logger.info("\n\n********** Testing StudentService: **********\n\n");
		
		logger.info("********** Testing getAllStudents: **********\n");
		students = studentService.getAllStudents();
		for (Student student: students) {
			logger.info(student.toString());
		}
		logger.info("********** End Testing getAllStudents: **********\n");

		logger.info("********** Testing getStudent: **********\n");
		Student testStudent = studentService.getStudent(studentRob.getStudentId());
		logger.info("testStudent: " + testStudent.toString());

		logger.info("********** End Testing getStudent: **********\n");

		logger.info("********** Testing getAllStudentCourses: **********\n");
		List<Course> testCourses = studentService.getAllStudentCourses(studentRob);
		logger.info("Courses found for studentRob: \n");
		for(Course course: testCourses) {
			logger.info(course.toString());
		}
		logger.info("********** End Testing getAllStudentCourses: **********\n");
		

		logger.info("********** End Testing StudentService: **********\n\n");

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
