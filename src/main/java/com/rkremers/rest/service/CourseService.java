package com.rkremers.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.exception.CourseNotFoundException;
import com.rkremers.rest.exception.StudentNotFoundException;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);

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

	public List<Course> getAllCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses;
	}
	
	public Optional<Course> getCourse(long courseId) {
		
		Optional<Course> course = courseRepository.findByCourseId(courseId);
		if (!course.isPresent() ) {
			throw new CourseNotFoundException("A course with id " + courseId + " has not been found.");
		}
		
		return course;
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
		List<StudentCourse> studentCourses = studentCourseRepository.findByCourse(course);
		List<Student> students = new ArrayList<Student>();
		for (StudentCourse studentCourse : studentCourses) {
			if (studentCourse.getStudent() != null) {
				students.add(studentCourse.getStudent());
			}
		}
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
		}

		courseRepository.save(course);
		Optional<Course> savedCourse = courseRepository.findByName(course.getName());
		
		return savedCourse.get();
	}
	
	public Course updateCourse(Course course) {
		
		Optional<Course> updateCourse = courseRepository.findByCourseId(course.getCourseId());

		if (!updateCourse.isPresent())
			throw new CourseNotFoundException("A course with id " + course.getCourseId() + " has not been found.");

		courseRepository.save(updateCourse.get() );
		updateCourse = courseRepository.findByCourseId(course.getCourseId());
		
		return updateCourse.get();
		
	}
	
	public void deleteCourse(Course course) {
		courseRepository.delete(course);
	}
	
}
