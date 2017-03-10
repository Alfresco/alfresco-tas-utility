package org.alfresco.utility.report;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identify one test case as being a fixed or an opened bug
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Bug
{
    String id();

    String description() default "";
    
    Status status() default Status.OPENED;
    
    public enum Status
    {
        OPENED, FIXED;
    }
}
