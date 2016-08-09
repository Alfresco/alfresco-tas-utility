package org.alfresco.tester.model;

import java.nio.file.Path;

public class FolderModel extends TestModel
{

    private Path path;

    public Path getPath()
    {
        return path;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

}
