Date	: 2018-02-12.

Purpose:
An application to show the implementation of the HATEOAS design pattern in a Spring Boot REST application.

Git:
The application will be available on: 
https://github.com/robkremers/ProjectSpringBootRESTHATEOAS

git:
  501  cd e:
  502  cd JavaDevelopment/
  503  ls -l
  504  cd WebServiceWorkspace/
  505  ls -l
  506  cd ProjectSpringBootRESTHATEOAS
  507  git init
  508  ls -la
  509  git pull https://github.com/robkremers/ProjectSpringBootRESTHATEOAS.git
  510  git add .
  511  git status
  512  git commit -m "Initial commit"
  513  git remote add origin https://github.com/robkremers/ProjectSpringBootRESTHATEOAS.git
  514  git push -u origin master
  515  git status
  516  history

Sources:

API:
- https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/index.html
- https://docs.spring.io/spring-boot/docs/1.5.10.RELEASE/api/index.html
- https://docs.spring.io/spring-hateoas/docs/current/api/index.html
- https://docs.spring.io/spring-hateoas/docs/current/reference/html/
- https://docs.spring.io/spring-data/jpa/docs/current/api/index.html
- https://dzone.com/articles/applying-hateoas-to-a-rest-api-with-spring-boot
- https://courses.in28minutes.com/p/spring-boot-for-beginners-in-10-steps

- http://www.springboottutorial.com/spring-boot-crud-rest-service-with-jpa-hibernate
- https://dzone.com/articles/spring-boot-hateoas-for-restful-services
- http://www.baeldung.com/spring-hateoas-tutorial
- Een entiteit extenden voor ResourceSupport.
- https://dzone.com/articles/applying-hateoas-to-a-rest-api-with-spring-boot

Logging:
- https://www.quickprogrammingtips.com/spring-boot/using-log4j2-with-spring-boot.html
	
JUnit:
- https://junit.org
- https://junit.org/junit5/docs/current/user-guide/
- https://junit.org/junit5/docs/current/api/
- https://junit.org/junit4/javadoc/4.10/
- https://www.tutorialspoint.com/junit/index.htm



---------------------------------------------------------------------------------------------------
Execution:

H2 database:
- http://localhost:1024/student_db
	- Ensure that: JDBC URL: jdbc:h2:mem:student_db !!!
	- Username: sa
	- Password: <none>
- With the current setup the following should be visible:
	- Table STUDENT
	- in the SQL window:
		> select * from student; <ctrl-enter>

ID  	AGE  	FIRST_NAME  	LAST_NAME  		PASSPORT_NUMBER  
1			55		Rob						Kremers				abc123
2			30		Martijn				Salm van der	blabla44421
3			30		Dennis				Geurts				A4D9I08JE

Consuming the RESTful application:
GET http://localhost:1024/students
- Note that due to the fact that Student extends ResourceSupport each element will automatically contain "links" although nothing has as yet been added.

[
    {
        "studentId": 1,
        "firstName": "Rob",
        "lastName": "Kremers",
        "passportNumber": "abc123",
        "age": 55,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:1024/students/1"
            },
            {
                "rel": "all_students",
                "href": "http://localhost:1024/students"
            }
        ]
    },
    {
        "studentId": 2,
        "firstName": "Martijn",
        "lastName": "Salm van der",
        "passportNumber": "blabla44421",
        "age": 30,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:1024/students/2"
            },
            {
                "rel": "all_students",
                "href": "http://localhost:1024/students"
            }
        ]
    },
    {
        "studentId": 3,
        "firstName": "Dennis",
        "lastName": "Geurts",
        "passportNumber": "A4D9I08JE",
        "age": 30,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:1024/students/3"
            },
            {
                "rel": "all_students",
                "href": "http://localhost:1024/students"
            }
        ]
    }
]
		
GET http://localhost:1024/students/1

{
    "studentId": 1,
    "firstName": "Rob",
    "lastName": "Kremers",
    "passportNumber": "abc123",
    "age": 55
}

POST http://localhost:1024/students/

Body (JSON (application/json); otherwise an exception will occur ):
{
    "firstName": "Frank",
    "lastName": "Reijden",
    "passportNumber": "test4567",
    "age": 40
}
Response:
{
    "studentId": 4,
    "firstName": "Frank",
    "lastName": "Reijden",
    "passportNumber": "test4567",
    "age": 40,
    "_links": {
        "self": {
            "href": "http://localhost:1024/students/4"
        },
        "all_students": {
            "href": "http://localhost:1024/students"
        }
    }
}

(Test: after GET http://localhost:1024/students the new student will be visible).

PUT http://localhost:1024/students/

Body (JSON (application/json));

    {
        "studentId": 4,
        "firstName": "Frank",
        "lastName": "Reijden",
        "passportNumber": "test4567Updated",
        "age": 40
    }

Response:

{
    "studentId": 4,
    "firstName": "Frank",
    "lastName": "Reijden",
    "passportNumber": "test4567",
    "age": 40,
    "_links": {
        "self": {
            "href": "http://localhost:1024/students/4"
        },
        "all_students": {
            "href": "http://localhost:1024/students"
        }
    }
}

---------------------------------------------------------------------------------------------------
2018-02-13:

Study and implementation of unit testing.
- General use of JUnit.
- Testing in Spring Boot.

- For production and testing use separate databases.

Sources under study:
- https://junit.org/junit5/docs/current/user-guide/
- http://www.baeldung.com/spring-data-jpa-multiple-databases
- https://www.youtube.com/watch?v=ixIxXRoCr5w

---------------------------------------------------------------------------------------------------
2018-02-26:

Implementation of the ORM: student <==> course.
This works correctly now.

Test in the H2 database:
	select * from student;
	
	select * from course;
	
	select * from student_course;

2018-02-27:

Instead of a @ManyToMany relationship with @JoinTable I'll use a real join table
with 2 @ManyToOne, because the join table STUDENT_COURSE needs to contain a few specific properties:
- Year in which the course has been given.
- Result of the exam taken by the student.

Possible methods:
- http://www.codejava.net/frameworks/hibernate/hibernate-many-to-many-association-with-extra-columns-in-join-table-example
	- Using a Separate Primary Key for the Join Table.
	- Using Composite Key.
	* The first method is recommended because it is simpler.
	* My opinion: if the combination of the two entities (Student / Course) would be used more often I would
	  use the Composite Key.
	* Note that cascading / orphan removal may need special attention in the code.

I have had a problem, when saving the StudentCourse instance.
Ultimately this was caused by validation constraints.
At the end I have commented all constraints in Course and in Student.
Now it works.
I'll comment out the constraints one by one to figure out which constraint is causing the problem
and more important why it is causing the problem.


	