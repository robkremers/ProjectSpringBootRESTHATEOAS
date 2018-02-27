package com.rkremers.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.StudentCourse;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

}
