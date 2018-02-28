package com.rkremers.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;

@Service
public class StudentService {
	private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentCourseRepository studentCourseRepository;

	public StudentService() {}
	
	public List<Student> getAllStudents() {
		List<Student> students = studentRepository.findAll();
		return students;
	}
	
	/**
	 * Purpose:
	 * Find all students who have followed a given course.
	 * 
	 * Functionality:
	 * - Find all CourseStudent entities for a given course.
	 * 		- Find all Student entities for the given CourseStudent entities.
	 * @param course
	 * @return
	 */
	public List<Student> getAllStudents(Course course) {
		List<StudentCourse> studentCourses = studentCourseRepository
		return null;
	}

}
