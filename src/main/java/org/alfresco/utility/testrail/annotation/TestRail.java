package org.alfresco.utility.testrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.alfresco.utility.testrail.TestType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestRail
{

    String[] section();

    String description() default "";

    TestType type() default TestType.AUTOMATED;  
}
