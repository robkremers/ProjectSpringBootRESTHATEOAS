package com.rkremers.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ParameterizedTests {
	
	private static final Logger LOG = LoggerFactory.getLogger(ParameterizedTests.class);


/**
 *  Use of SpringClassRule
 *  org.springframework.test.context.junit4.rules.SpringClassRule.
 *  https://github.com/Pragmatists/junitparams-spring-integration-example/blob/master/README.md
 *  
 */
	
	public ParameterizedTests() {
		// TODO Auto-generated constructor stub
	}

	@Test()
	public void testMethod4() {
		LOG.info("ParameterizedTests: Awaiting implementation.");
		
	}
}
