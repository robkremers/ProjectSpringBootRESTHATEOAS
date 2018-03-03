package com.rkremers.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.exception.CourseNotFoundException;
import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;

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
	
	public CourseService() {}

	public List<Course> getAllCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses;
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
	public List<Student> getAllStudents(Course course) {
		List<StudentCourse> studentCourses = studentCourseRepository.findByCourse(course);
		List<Student> students = new ArrayList<Student>();
		for (StudentCourse studentCourse : studentCourses) {
			if (studentCourse.getStudent() != null) {
				students.add(studentCourse.getStudent());
			}
		}
		return students;
	}
	
	public Course addCourse(Course course) {
		
		courseRepository.save(course);
		Optional<Course> savedCourse = courseRepository.findByName(course.getCourseName());
		
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
	
}
