package org.alfresco.utility.exception;

public class CouldNotFindApplicationActionImage extends Exception
{    
    private static final long serialVersionUID = 1L;

    public CouldNotFindApplicationActionImage(String imagePath, String applicationName, String message)
    {        
        super(String.format("Cannot find image [%s] for GUI Application [%s]. Error Message: %s", imagePath, applicationName, message));
    }
}
