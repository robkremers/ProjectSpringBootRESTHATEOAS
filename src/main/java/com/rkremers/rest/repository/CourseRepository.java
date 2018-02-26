package com.rkremers.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {


}
