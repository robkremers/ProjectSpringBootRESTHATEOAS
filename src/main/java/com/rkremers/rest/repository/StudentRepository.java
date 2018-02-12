package com.rkremers.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkremers.rest.model.Student;

/**
 * Note:
 * The Long parameter in the generic notation stands for Student.studentId.
 * 
 * JpaRepository extends PagingAndSortingRepository extends CrudRepository.
 * 
 * @author LTAdmin
 *
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

	public Student findByPassportNumber(String passportNumber);
}
