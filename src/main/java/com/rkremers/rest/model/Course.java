package com.rkremers.rest.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.hateoas.ResourceSupport;

@Entity
public class Course extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -4254287335591834230L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long courseId;
	private String name;
	private String topic;
	
	@ManyToMany(mappedBy="courses")
	private Set<Student> students = new HashSet<Student>();
//	private List<Student> students = new ArrayList<Student>();
	
	public Course() {
		// TODO Auto-generated constructor stub
	}

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

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
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