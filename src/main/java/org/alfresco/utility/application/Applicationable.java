package org.alfresco.utility.application;

/**
 * Application based interface
 */
public interface Applicationable
{
    /**
     * add appropriate image that will open this application
     * 
     * @throws Exception
     */
    Applicationable open() throws Exception;

    /**
     * add appropriate image that will close this application
     * 
     * @throws Exception
     */
    Applicationable close() throws Exception;

    /**
     * @return the process name of this application
     */
    String getProcessName();

    /**
     * just kill the application
     * 
     * @return
     * @throws Exception
     */
    Applicationable killProcess() throws Exception;

    /**
     * check if application is running
     * 
     * @return
     */
    boolean isRunning();
}
