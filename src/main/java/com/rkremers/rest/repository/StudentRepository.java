package com.rkremers.rest.repository;

import java.util.Optional;

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
	
	public Optional<Student> findByStudentId(long studentId);

	public Student findByPassportNumber(String passportNumber);

	public void deleteByStudentId(long studentId);
}
