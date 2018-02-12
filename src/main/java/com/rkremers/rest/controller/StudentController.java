package com.rkremers.rest.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rkremers.rest.exception.StudentNotFoundException;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.repository.StudentRepository;

@RestController
@RequestMapping(value="/students")
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepository;

	public StudentController() {}

//	@RequestMapping(method=RequestMethod.GET)
	@GetMapping
	public List<Student> getStudents() {
		return studentRepository.findAll();
	}
	
	/**
	 * Note:
	 * Because of the use of Optional I had to explicitly define a method in the repository.
	 * Compare this with the method deleteStudent: it's possible to do this again but the JpaRepository will already define an appropriate method.
	 * For the newcomers: mind you: for this it is not necessary to implement it! The definition is enough for Spring Boot.
	 * 
	 * @param studentId
	 * @return
	 */
	@GetMapping("{/studentId}")
	public Student retrieveStudent(@PathVariable long studentId) {
		Optional<Student> student = studentRepository.findByStudentId(studentId);

		if (!student.isPresent())
			throw new StudentNotFoundException("A student with id " + studentId + " has not been found.");

		return student.get();
	}
	
	@DeleteMapping("/{studentId}")
	public void deleteStudent(@PathVariable long studentId) {
		
// Just to show that it is possible to custom define a method. Spring will check that the defined method is consistent with the Student declaration.
//		studentRepository.deleteByStudentId(studentId);
		studentRepository.delete(studentId);
	}

	@PostMapping
	public ResponseEntity<Object> createStudent(@RequestBody Student student) {
		Student savedStudent = studentRepository.save(student);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{studentId}")
				.buildAndExpand(savedStudent.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping
	public ResponseEntity<Object> updateStudent(@RequestBody Student student) {

		Optional<Student> studentOptional = studentRepository.findByStudentId(student.getStudentId());

		if (!studentOptional.isPresent())
			return ResponseEntity.notFound().build();
		
		studentRepository.save(student);

		return ResponseEntity.noContent().build();
	}
}
