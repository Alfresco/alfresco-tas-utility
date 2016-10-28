package org.alfresco.utility.data.provider;

import org.alfresco.utility.model.TestModel;

/**
 * Each XML item should have a name and should be created by a particular user
 * <item name="f1" createdBy="user1">
 */
public interface XMLDataItem
{
    public TestModel getModel();
}
