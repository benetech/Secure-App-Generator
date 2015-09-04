package org.benetech.secureapp.generator;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.runner.RunWith;
import org.junit.Test;

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
