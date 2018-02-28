package com.rkremers.rest.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.hateoas.ResourceSupport;

@Entity
public class Course extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -4254287335591834230L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="course_id")
	private long courseId;
	@Column(length=200, nullable=false)
	private String name;
	@Column(length=500, nullable=false)
	private String topic;
	
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<StudentCourse> studentCourses = new HashSet<StudentCourse>();
	
	public Course() {}

	public Course(String courseName, String courseTopic) {
		super();
		this.name = courseName;
		this.topic = courseTopic;
	}

	public String getCourseName() {
		return name;
	}

	public void setCourseName(String courseName) {
		this.name = courseName;
	}

	public String getCourseTopic() {
		return topic;
	}

	public void setCourseTopic(String courseTopic) {
		this.topic = courseTopic;
	}


	public Set<StudentCourse> getStudentCourses() {
		return studentCourses;
	}

	public void setStudentCourses(Set<StudentCourse> studentCourses) {
		this.studentCourses = studentCourses;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (courseId ^ (courseId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
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
		Course other = (Course) obj;
		if (courseId != other.courseId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", name=" + name + ", topic=" + topic + "]";
	}
	


}
