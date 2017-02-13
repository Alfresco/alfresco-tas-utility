package org.alfresco.utility.model;

import org.alfresco.utility.data.RandomData;

public class DataListModel extends ContentModel
{
    private String dataListItemType;
    
    public DataListModel(String name, String dataListItemType)
    {
        super(name);
        setDataListItemType(dataListItemType);
    }
    
    public DataListModel(String name, String title, String description, String dataListItemType)
    {
        super(name, title, description);
        setDataListItemType(dataListItemType);
    }
    
    public String getDataListItemType()
    {
        return dataListItemType;
    }

    public void setDataListItemType(String dataListItemType)
    {
        this.dataListItemType = dataListItemType;
    }
    
    /**
     * Generates a new random {@link DataListModel} object
     * @param dataListItemType e.g. dl:issue, dl:contact, dl:eventAgenda
     */
    public static DataListModel getRandomDataListModel(String dataListItemType)
    {
        DataListModel newDataListModel = new DataListModel(RandomData.getRandomName("dl"), dataListItemType);
        LOG.info("Generating new DataListModel: {}", newDataListModel.toString());
        return newDataListModel;
    }
}
