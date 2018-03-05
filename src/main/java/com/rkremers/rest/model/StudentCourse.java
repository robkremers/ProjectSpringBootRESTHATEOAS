package com.rkremers.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.hateoas.ResourceSupport;
/**
 * Background:
 * Between entities Student and Course a ManyToMany relation exists.
 * To maintain a correct relation a Join Table is necessary.
 * Because per student / course additional values need to be saved it was decided not to define a JoinTable
 * but instead create a separate table that can hold the additional fields.
 * 
 * @author rokremer
 *
 */
@Entity
@Table(name="STUDENT_COURSE")
public class StudentCourse extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -4221517579194514405L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "STUDENT_COURSE_ID")
	private long studentCourseId;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "student_id")
	private Student student;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "course_id")
	private Course course;
	
	// Additional fields (reason why not a Join Table has been used).
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date registrationDate;
	
	@Min(value=0)
	@Max(value=10)
	@Column(nullable = true)
	private double result;
	
	public StudentCourse() {
		super();
		this.registrationDate = new Date();		
	}

	public StudentCourse(Date registrationDate) {
		super();
		this.registrationDate = registrationDate;
	}	

	public StudentCourse(Student student, Course course, Date registrationDate) {
		super();
		this.student = student;
		this.course = course;
		this.registrationDate = registrationDate;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.result);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + (int) (studentCourseId ^ (studentCourseId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentCourse other = (StudentCourse) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (registrationDate == null) {
			if (other.registrationDate != null)
				return false;
		} else if (!registrationDate.equals(other.registrationDate))
			return false;
		if (Double.doubleToLongBits(result) != Double.doubleToLongBits(other.result))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (studentCourseId != other.studentCourseId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StudentCourse [studentCourseId=" + studentCourseId + ", student=" + student + ", course=" + course
				+ ", registrationDate=" + registrationDate + ", result=" + result + "]";
	}
	
}
