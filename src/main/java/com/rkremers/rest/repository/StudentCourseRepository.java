package com.rkremers.rest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.model.StudentCourse;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
	
	public List<StudentCourse> findByCourse(Course course);
	
	public List<StudentCourse> findByStudent(Student student);
	
	public Optional<StudentCourse> findByStudentAndCourse(Student student, Course course);

}
