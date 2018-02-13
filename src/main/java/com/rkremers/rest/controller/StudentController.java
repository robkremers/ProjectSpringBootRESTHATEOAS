package com.rkremers.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rkremers.rest.exception.StudentNotFoundException;
import com.rkremers.rest.model.Student;
import com.rkremers.rest.repository.StudentRepository;

@RestController
@RequestMapping(value="/students")
public class StudentController {
	
	private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	private StudentRepository studentRepository;

	public StudentController() {}

//	@RequestMapping(method=RequestMethod.GET)
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<Student> getStudents() {
		LOG.info("StudentController.getStudents()");
		
		List<Student> students = studentRepository.findAll();
		
		for (Student student: students) {
			student.add( linkTo(this.getClass()).slash(student.getStudentId()).withSelfRel() );
			student.add( linkTo(this.getClass()).withRel("all_students") );
		}
		
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
	
	@GetMapping("/{studentId}")
	public Student retrieveStudent(@PathVariable long studentId) {

		LOG.info("StudentController.retrieveStudent(): studentId = " + studentId);

		Optional<Student> student = studentRepository.findByStudentId(studentId);
		
		student.get().add( linkTo(this.getClass()).slash(student.get().getStudentId()).withSelfRel() );
		student.get().add( linkTo(this.getClass()).withRel("all_students") );

		if (!student.isPresent())
			throw new StudentNotFoundException("A student with id " + studentId + " has not been found.");
		return student.get();
	}
	
	@DeleteMapping("/{studentId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteStudent(@PathVariable long studentId) {
		
// Just to show that it is possible to custom define a method. Spring will check that the defined method is consistent with the Student declaration.
//		studentRepository.deleteByStudentId(studentId);
// And the method provided via JpaRepository<Student, Long>.
		studentRepository.delete(studentId);
	}

	/**
	 * After saving the object the new content is returned with Status 201 Created.
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Student createStudent(@RequestBody Student student) {
		Student savedStudent = studentRepository.save(student);
		
		savedStudent.add( linkTo(this.getClass()).slash(savedStudent.getStudentId()).withSelfRel() );
		savedStudent.add( linkTo(this.getClass()).withRel("all_students") );

		return savedStudent;

	}
	
	/**
	 * Note:
	 * It is a business choice whether an object, that can not be found in the database will be saved or not.
	 * In this case the decision is not to save the object, but to return a message: Status 404 Not Found.
	 * 
	 * @param student
	 * @return
	 */
	@PutMapping
	public Student updateStudent(@RequestBody Student student) {

		System.out.println("Student id: " + student.getStudentId());
		Optional<Student> updateStudent = studentRepository.findByStudentId(student.getStudentId());

		if (!updateStudent.isPresent())
			throw new StudentNotFoundException("A student with id " + student.getStudentId() + " has not been found.");
		
		studentRepository.save(updateStudent.get());
		updateStudent.get().add( linkTo(this.getClass()).slash(updateStudent.get().getStudentId()).withSelfRel() );
		updateStudent.get().add( linkTo(this.getClass()).withRel("all_students") );

		return updateStudent.get();
	}
	
}
