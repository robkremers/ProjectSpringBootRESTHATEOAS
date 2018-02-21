package com.rkremers.rest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
	
	@Test(timeout=1000)
	public void testMethod1() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 1.");
	}
	
	@Test
	public void testMethod2() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 2.");
	}	
	
	@Ignore
	@Test
	public void testMethod3() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 3.");
	}
	
	/**
	 * Note:
	 * The following test will throw an ArithmeticException.
	 * However: the test will not crash. 
	 * Due to informing which Exception is to be expected JUnit will handle the exception.
	 * Ensure that in case of testing on e.g. a custom Exception the logging in advance is clear on what will be tested.
	 * 
	 */
	@Test(expected=ArithmeticException.class)
	public void testMethod4() {
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 4: Check on ArithmeticException.");
		int result = 1 / 0;
		LOG.info("ProjectSpringBootResthateoasApplicationTests: Test case 4: Check on ArithmeticException finished.");
		
	}	
}
