package org.alfresco.utility.data.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface XMLDataConfig
{
    /**
     * define the *.XML file used by {@link XMLTestDataProvider}
     */
    public String file();
}
