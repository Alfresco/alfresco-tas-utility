package org.alfresco.sample.howto;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-test-context.xml")
public class SampleTest extends AbstractTestNGSpringContextTests
{    
    @Test
    public void start()
    {
        
    }
}
