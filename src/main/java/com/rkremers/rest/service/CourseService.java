package com.rkremers.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.exception.CourseAddedToStudentException;
import com.rkremers.rest.exception.CourseNotFoundException;
import com.rkremers.rest.exception.PrecursorCourseException;
import com.rkremers.rest.exception.StudyConfigurationNotFound;
import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.model.StudyConfiguration;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;
import com.rkremers.rest.repository.StudyConfigurationRepository;

/**
 * Functionality:
 * - Course:
 *  - Get all courses
 *  - Get a specific course and it's students
 *  - Post course
 *  - Put course.
 *  - Put course: add a student.
 *  	- This is actually already treated in the case of students: add a course.
 *  - Delete course.
 *  
 * 
 * @author rokremer
 *
 */
@Service
public class CourseService {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentCourseRepository studentCourseRepository;
	
	@Autowired
	private StudyConfigurationRepository studyConfigurationRepository;
	
	private Optional<StudyConfiguration> standardPassValue;
	
	public CourseService() {}

	/**
	 * Note:
	 * The precursor course is @OneToOne with fetch = FetchType.LAZY.
	 * In this situation:
	 * - One cursor is used.
	 * - Also the lazily fetched precursorCourse is fetched.
	 * 
	 * @return
	 */
	public List<Course> getAllCourses() {
		logger.info("***** Retrieve the courses from the database: OneToMany taking care of fetching lazy problem.");
		List<Course> courses = courseRepository.findAll();
		return courses;
	}
	
	public Optional<Course> getCourse(long courseId) {
		
		Optional<Course> course = courseRepository.findCourseWithPossiblePrecursor(courseId);
		
		if (!course.isPresent() ) {
			logger.info("***** A course with id " + courseId + " has not been found. *****\n");
			throw new CourseNotFoundException("A course with id " + courseId + " has not been found.");
		} else {
			logger.info("***** A course with id " + courseId + " has been found. *****\n");
			return course;
		}	
	}
	
	/**
	 * Purpose: Find all students who have followed a given course.
	 * 
	 * Functionality: - Find all CourseStudent entities for a given course. - Find
	 * all Student entities for the given CourseStudent entities.
	 * 
	 * @param course
	 * @return
	 */
	public List<Student> getAllCourseStudents(Course course) {
		
		/**
		 * This method works, but it is very inefficient. Internally the following is done by Spring Boot:
		 * - One query for determining whether the course exists in table COURSE.
		 * - For each student a separate query is executed! This will be very costly in case of a large university...
		 */
//		List<StudentCourse> studentCourses = studentCourseRepository.findByCourse(course);
//		List<Student> students = new ArrayList<Student>();
//		for (StudentCourse studentCourse : studentCourses) {
//			if (studentCourse.getStudent() != null) {
//				students.add(studentCourse.getStudent());
//			}
//		}
//		return students;
		
		List<Student> students = studentCourseRepository.findStudents(course.getCourseId());
		return students;
	}
	
	/**
	 * Purpose:
	 * Save a new type of course.
	 * 
	 * Functionality:
	 * If the course does not already contain a value for the minimum score that needs to be passed in order 
	 * to successfully pass the exam it's value will be set here, using the setting in table STUDY_CONFIGURATION
	 * for parameter value "Standard Pass Value".
	 * 
	 * @param course
	 * @return
	 */
	public Course addCourse(Course course) {

		standardPassValue = studyConfigurationRepository.findByParameterName("Standard Pass Value");
		if ( !standardPassValue.isPresent() ) {
			throw new StudyConfigurationNotFound("The value for Standard Pass Value could not be found.");
		}
		
		double doubleStandardPassValue = Double.parseDouble(standardPassValue.get().getParameterStrValue());
		
		if (course.getMinimumScore() == 0.0F) {
			course.setMinimumScore(doubleStandardPassValue);
			logger.info( "For course " + course.getName() 
			           + " with id " + course.getCourseId()
			           + " the minimum score " + doubleStandardPassValue
			           + " for passing the course has been set.");
		} else {
			logger.info( "For course " + course.getName() 
	                   + " with id " + course.getCourseId()
	                   + " the minimum score is " + course.getMinimumScore()
	                   + ".");
		}

		courseRepository.save(course);
		Optional<Course> savedCourse = courseRepository.findByName(course.getName());
		
		return savedCourse.get();
	}
	
	/**
	 * Functionality:
	 * In class Course the property precursorCourse is annotated with fetch type FETCH.LAZY.
	 * This means that normally when returning the updated entity the precursor course will not be shown.
	 * Instead an exception may be thrown:
	 * 		Caused by: org.hibernate.LazyInitializationException: could not initialize proxy - no Session
	 * 
	 * In order to resolve this the annotation @Transactional (with default TxType.REQUIRED) is used.
	 * 
	 * 
	 * @param course
	 * @return
	 */
	@Transactional
	public Course updateCourse(Course course) {
		logger.info("***** Starting method updateCourse *****\n\n ");
		logger.info("Entered course: " + course.toString() );
		
		Optional<Course> updateCourse = courseRepository.findByCourseId(course.getCourseId());
		
		if (!updateCourse.isPresent()) {
			throw new CourseNotFoundException("A course with id " + course.getCourseId() + " has not been found.");
		}
		else {
			logger.info("Course " + course.getName() 
            + " with courseId " + course.getCourseId()
            + " exists.");
		}

		Course updatedCourse = courseRepository.save(course );
		logger.info("After saving the content of newCourse: " + updatedCourse.toString() );
		
		return updatedCourse;
		
	}
	
	/**
	 * Functionality:
	 * If a course is to be deleted it should not contain references to another entity.
	 * Caused by: org.h2.jdbc.JdbcSQLException: Referential integrity constraint violation: "FKD8USNCISFGXUHSXP67CIVUB1R: PUBLIC.COURSE FOREIGN KEY(PRECURSOR_COURSE_ID) REFERENCES PUBLIC.COURSE(COURSE_ID) (4)"; SQL statement:
	 *
	 * Before possibly deleting check in the following order:
	 * 1. If the course is present in an entity of StudentCourse the course should never be deleted.
	 *   (if only for historic reasons).
	 * 2. Check whether the course is a precursor Course. If this is the case the course should not be deleted.
	 * 3. If the course has a precursor course this should be put to null.
	 * 
	 * After this the course can be deleted.
	 * 
	 * Note:
	 * For this test an H2 database is being used.
	 * An H2 database does not support deferred checking!!
	 * 
	 * @param course
	 */
//	@Transactional
	public void deleteCourse(Course course) {
		logger.info("***** Starting method deleteCourse *****\n\n ");
		logger.info("Entered course: " + course.toString() + "\n" );
		
		logger.info("Check whether the course is being followed / has been followed by students.\n" );
		List<StudentCourse> studentCourses = studentCourseRepository.findByCourse(course);
		if ( studentCourses.size() > 0)
			throw new CourseAddedToStudentException( "Course " + course.getName() 
			                                       + " with courseId " + course.getCourseId()
			                                       + " has been handed out to students and therefore can not be deleted."
			                                       );
		else {
			logger.info( "Course " + course.getName() 
			           + " with courseId " + course.getCourseId()
			           + " has not been handed out to students.\n");
		}
		
		logger.info("Check whether the course is a precursor course.\n" );
		List<Course> courses = courseRepository.findByPrecursorCourse(course);
		if (courses.size() > 0) {
			logger.info("Course " + course.getName() + " is a precursor course and therefore can not be deleted.");
			throw new PrecursorCourseException( "Course " + course.getName() 
                                              + " with courseId " + course.getCourseId()
                                              + " is a precursor course and therefore can not be deleted."
                                              );
		}		
		else {
			logger.info( "Course " + course.getName()
					   + " with courseId " + course.getCourseId()
	                   + " is not a precursor course.");
		}
		
		logger.info("Check whether the course has a precursor course.\n" );
		if (course.getPrecursorCourse() != null) {
			logger.info("Course " + course.getName() + " has a precursor course. This info will be removed.");
			course.setPrecursorCourse(null);
			
			courseRepository.save(course);
			
//			course = courseRepository.findOne(course.getCourseId());
//			logger.info("New status of the course: " + course.toString() );
			logger.info("For Course " + course.getName() + " the precursor course has been removed.\n");
		} else {
			logger.info( "Course " + course.getName()
			           + " with courseId " + course.getCourseId()
                       + " does not have a precursor course.");
		}
		
		/**
		 *At this point Hibernate will execute the following:
		 * 1. Check whether the course has a foreign key to a precursor course.
		 * 2. Check whether the course is present in a StudentCourse entity.
		 * 3. Delete the Course entity.
		 */
		courseRepository.delete(course);
		logger.info("Course " + course.getName() + " has been removed.");
	}
	
}
