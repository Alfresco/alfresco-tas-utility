package org.alfresco.utility.exception;

public class CouldNotFindImageOnScreen extends Exception
{    
    private static final long serialVersionUID = 1L;
    
    private String imagePath = "";
    
    public CouldNotFindImageOnScreen(String imagePath, String applicationName, String message)
    {        
        super(String.format("Cannot find image [%s] for GUI Application [%s]. Error Message: %s", imagePath, applicationName, message));
        setImagePath(imagePath);
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }
}
