package com.rkremers.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.Course;
import com.rkremers.rest.model.StudentCourse;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
	
	public List<StudentCourse> findByCourse(Course course);

}
