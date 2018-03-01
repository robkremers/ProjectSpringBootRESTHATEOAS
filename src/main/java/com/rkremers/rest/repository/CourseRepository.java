package com.rkremers.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
	public Optional<Course> findByCourseId(long courseId);
	
	public Optional<Course> 


}
