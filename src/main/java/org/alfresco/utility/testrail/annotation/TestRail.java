package org.alfresco.utility.testrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.alfresco.utility.testrail.ExecutionType;
import org.alfresco.utility.testrail.TestType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestRail
{

    /**
     * Define the Test Rail sections where the current test will be created
     * section = {"level1", "level2", "level3"}
     */
    String[] section();

    String description() default "";

    /**
     * Define the test type as it exist in Test Rails
     */
    TestType testType() default TestType.FUNCTIONAL;

    /**
     * Define the Execution Type as it exist in Test Rails
     */
    ExecutionType[] executionType() default { ExecutionType.SANITY };       
}
