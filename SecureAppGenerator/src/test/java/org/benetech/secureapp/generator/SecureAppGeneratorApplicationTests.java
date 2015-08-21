package org.benetech.secureapp.generator;

import org.benetech.secureapp.generator.SecureAppGeneratorApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecureAppGeneratorApplication.class)
@WebAppConfiguration
public class SecureAppGeneratorApplicationTests 
{

	@Test
	public void contextLoads() 
	{
	}

}
