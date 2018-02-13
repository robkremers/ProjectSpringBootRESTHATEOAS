package com.rkremers.rest;

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
	
	private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

	@BeforeClass
	public static void beforeAll() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: beforeAll()");
	}
	
	@Test
	public void contextLoads() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: contextLoads()");
	}

}
