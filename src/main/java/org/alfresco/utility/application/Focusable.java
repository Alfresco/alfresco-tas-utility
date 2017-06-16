package org.alfresco.utility.application;

/**
 * Focused application
 * 
 * @author Paul Brodner
 */
public interface Focusable<App>
{
    App focus() throws Exception;
}
