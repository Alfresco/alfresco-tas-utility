package org.alfresco.utility.application;

/**
 * Application based interface
 */
public interface Application
{
    /**
     * add appropriate image that will open this application
     * 
     * @throws Exception
     */
    Application open() throws Exception;

    /**
     * add appropriate image that will close this application
     * 
     * @throws Exception
     */
    Application close() throws Exception;

    /**
     * @return the process name of this application
     * @throws Exception 
     */
    String getProcessName() throws Exception;

    /**
     * just kill the application
     * 
     * @return
     * @throws Exception
     */
    Application killProcess() throws Exception;

    /**
     * check if application is running
     * 
     * @return
     * @throws Exception 
     */
    boolean isRunning() throws Exception;
}
