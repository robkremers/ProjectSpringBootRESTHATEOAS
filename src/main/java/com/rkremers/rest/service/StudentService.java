package com.rkremers.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkremers.rest.exception.CourseNotFoundException;
import com.rkremers.rest.exception.MissingPassPortNumberException;
import com.rkremers.rest.exception.PrecursorCourseNotFollowedException;
import com.rkremers.rest.exception.PrecursorCourseNotPassedException;
import com.rkremers.rest.exception.StudentNotFoundException;
import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;
import com.rkremers.rest.repository.CourseRepository;
import com.rkremers.rest.repository.StudentCourseRepository;
import com.rkremers.rest.repository.StudentRepository;
import com.rkremers.rest.repository.StudyConfigurationRepository;

/**
 * Functionality: - Student: - Get all students - Get a specific student and
 * it's courses - Post student - Put student. - Put student: add a course. -
 * Delete student.
 * 
 * 
 * @author rokremer
 *
 */
@Service
public class StudentService {
	private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentCourseRepository studentCourseRepository;
	
	@Autowired
	private StudyConfigurationRepository studyConfigurationRepository;

	public StudentService() {
	}
	
	@PostConstruct
	private void init() {
		// Continue here tomorrow: the passing value 5.5 should be configurable.
	}

	public List<Student> getAllStudents() {
		List<Student> students = studentRepository.findAll();
		return students;
	}

	/**
	 * Purpose:
	 * Find all courses that have been followed by a given student.
	 * 
	 * Functionality:
	 * - Find all CourseStudent entities for a given student.
	 * 		- Find all Course entities for the given CourseStudent entities.
	 * @param course
	 * @return
	 */
	public List<Course> getAllCourses(Student student) {
		List<StudentCourse> studentCourses = studentCourseRepository.findByStudent(student);
		List<Course> courses = new ArrayList<Course>();
		for (StudentCourse studentCourse: studentCourses) {
			if ( studentCourse.getCourse() != null) {
				courses.add( studentCourse.getCourse() );
			}
		}
		return courses;
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
	 * Purpose: Add an existing course to an existing student.
	 * 
	 * Functionality: 
	 * - Check that the student exists. 
	 * - Check that the course exists. 
	 * - Check whether the course has a preceding course. In that case the
	 * student should have followed the preceding course or not be able to enroll.
	 * If the student is enrolling for a course that does not have a precursor
	 * course or if the student has passed the precursor course the student will be
	 * linked to the course. 
	 * Return an overview of the StudentCourse in which the
	 * student has been involved.
	 * 
	 * @param student
	 * @param course
	 * @return
	 */
	public StudentCourse addCourseToStudent(Student student, Course course) {
		Optional<Student> updateStudent = studentRepository.findByStudentId(student.getStudentId());

		if (!updateStudent.isPresent())
			throw new StudentNotFoundException("A student with id " + student.getStudentId() + " has not been found.");

		Optional<Course> addCourse = courseRepository.findByCourseId(course.getCourseId());

		if (!addCourse.isPresent())
			throw new CourseNotFoundException("A course with id " + course.getCourseId() + " has not been found.");

		StudentCourse newStudentCourse = new StudentCourse();
		StudentCourse precursorStudentCourse = new StudentCourse();

		Course precursorCourse = addCourse.get().getPrecursorCourse();

		/* Check whether the student has followed the precursorCourse.
		 * If the new course has a precursor course:
		 * - it must have been followed.
		 * - the result must have been satisfactory (> 5.5.).
		 * 
		 */
		if (precursorCourse != null) {
			boolean hasFollowedPrecursorCourse = false;

			List<StudentCourse> previousStudentCourses = studentCourseRepository.findByStudent(student);
			for (StudentCourse studentCourse : previousStudentCourses) {
				if (studentCourse.getCourse().getCourseId() == precursorCourse.getCourseId()) {
					hasFollowedPrecursorCourse = true;
					precursorStudentCourse = studentCourse;
					break;
				}
			}

			if (hasFollowedPrecursorCourse == false ) {
				throw new PrecursorCourseNotFollowedException(
						"The student with id" + student.getStudentId() + " can not enroll for the course with id "
								+ course.getCourseId() + ". " + " First course " + precursorCourse.getCourseName()
								+ " with id " + precursorCourse.getCourseId() + " has to be finished.");
			}

			if (hasFollowedPrecursorCourse == true && precursorStudentCourse.getResult() < 5.5 ) {
					throw new PrecursorCourseNotPassedException(
							"The student with id" + student.getStudentId() + " can not enroll for the course with id "
									+ course.getCourseId() + ". " + " The student first has to pass precursor course "
									+ precursorStudentCourse.getCourse().getCourseName() + " with id "
									+ precursorStudentCourse.getCourse().getCourseId() + " successfully.");
				}

		}
		
		newStudentCourse = new StudentCourse(student, course, new Date());
		studentCourseRepository.save(newStudentCourse);

		return newStudentCourse;
	}

	/**
	 * Notes: It should be thoroughly checked that this works as intended: - If the
	 * student is deleted the corresponding StudentCourse entities, if any, should
	 * also be deleted. I'm curious whether this works because the StudentCourse
	 * entities also hold a link to courses. - It should normally not be possible to
	 * delete a student and it's history. So to be implemented is authentication
	 * (e.g. this should only be possible for a user with role ADMIN).
	 * 
	 * @param student
	 */
	public void deleteStudent(Student student) {
		studentRepository.delete(student);
	}

	/**
	 * Purpose: In case a new student entity is being entered a passport number is
	 * compulsory. This is also caught at database level (see Student) but I want to
	 * give back a controlled exception message.
	 * 
	 * @param student
	 */
	private void checkPassPortNumber(Student student) {
		if (student.getPassportNumber() == null || student.getPassportNumber().equals(""))
			throw new MissingPassPortNumberException("Student " + student.getFirstName() + " " + student.getLastName()
					+ " does not have a passport number.");
	}

}
