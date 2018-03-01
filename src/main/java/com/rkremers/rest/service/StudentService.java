package com.rkremers.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.exception.CourseNotFoundException;
import com.rkremers.rest.exception.MissingPassPortNumberException;
import com.rkremers.rest.exception.StudentNotFoundException;
import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;

/**
 * Functionality:
 * - Student:
 *  - Get students
 *  - Get students/{studentId}/courses
 *  - Post student
 *  - Put student: add a course.
 *  - Delete student.
 *  
 *  
 * 
 * @author rokremer
 *
 */
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
		List<StudentCourse> studentCourses = studentCourseRepository.findByCourse(course);
		List<Student> students = new ArrayList<Student>();
		for (StudentCourse studentCourse: studentCourses) {
			if ( studentCourse.getStudent() != null) {
				students.add( studentCourse.getStudent() );
			}
		}
		return students;
	}
	
	public Student addStudent(Student student) {
		
		checkPassPortNumber(student);
		
		studentRepository.save(student);
		Optional<Student> savedStudent = studentRepository.findByPassportNumber(student.getPassportNumber());
		return savedStudent.get();
	}
	
	public Student updateStudent(Student student) {
		
		Optional<Student> updateStudent = studentRepository.findByStudentId(student.getStudentId());

		if (!updateStudent.isPresent())
			throw new StudentNotFoundException("A student with id " + student.getStudentId() + " has not been found.");

		checkPassPortNumber(updateStudent.get());
		
		studentRepository.save(updateStudent.get());
		updateStudent = studentRepository.findByStudentId(student.getStudentId());
		
		return updateStudent.get();
		
	}
	
	/**
	 * Purpose:
	 * Add an existing course to an existing student.
	 * 
	 * Functionality:
	 * - Check that the student exists.
	 * - Check that the course exists.
	 * - Check that the 
	 * 
	 * @param student
	 * @param course
	 * @return
	 */
	public Student addCourseToStudent(Student student, Course course) {
		Optional<Student> updateStudent = studentRepository.findByStudentId(student.getStudentId());

		if (!updateStudent.isPresent())
			throw new StudentNotFoundException("A student with id " + student.getStudentId() + " has not been found.");

		Optional<Course> addCourse = courseRepository.findByCourseId( course.getCourseId() );
		
		if ( !addCourse.isPresent() )
			throw new CourseNotFoundException("A course with id " + course.getCourseId() + " has not been found.");
		
		Course precursorCourse = addCourse.get().getPrecursorCourse();
		if (precursorCourse != null) {
			boolean hasFollowedPrecursorCourse = false;
			List<StudentCourse> previousStudentCourses = studentCourseRepository.findByStudent(student);
			for(StudentCourse studentCourse: previousStudentCourses) {
				if (studentCourse.getCourse().getCourseId() == precursorCourse.getCourseId() ) {
					hasFollowedPrecursorCourse = true;
					break;
				}
			}
		} else {
			StudentCourse newStudentCourse = new StudentCourse(student, course, new Date()  );
		}
		
		// Continue here tomorrow.
		
		return null;
	}

	public void deleteStudent(Student student) {
		studentRepository.delete(student);
	}
	
	private void checkPassPortNumber(Student student) {
		if (student.getPassportNumber() == null || student.getPassportNumber().equals(""))
			throw new MissingPassPortNumberException( "Student " 
		                                            + student.getFirstName() 
		                                            + " " 
		                                            + student.getLastName() 
		                                            + " does not have a passport number."
		                                            );		
	}

}
