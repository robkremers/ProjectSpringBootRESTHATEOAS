package com.rkremers.rest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rkremers.rest.controller.StudentController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectSpringBootResthateoasApplicationTests {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectSpringBootResthateoasApplicationTests.class);

	@BeforeClass
	public static void beforeAll() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Execution before class");
	}
	
	@AfterClass
	public static void afterClass() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Execution after class");
	}
	
	@Before
	public void before() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Execution before test method");
	}
	
	@After
	public void after() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Execution after test method");
	}
	
	@Test
	public void testMethod1() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 1.");
	}
	
	@Test
	public void testMethod2() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 2.");
	}	

}
