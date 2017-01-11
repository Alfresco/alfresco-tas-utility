package org.alfresco.utility.constants;

/**
 * @author Cristina Axinte
 */
public enum PreferenceName
{
    SITES_FAVORITES_PREFIX("org.alfresco.share.sites.favourites.%s"),
    FOLDERS_FAVORITES_PREFIX("org.alfresco.share.folders.favourites"),
    DOCUMENTS_FAVORITES_PREFIX("org.alfresco.share.documents.favourites"),
    EXT_SITES_FAVORITES_PREFIX("org.alfresco.ext.sites.favourites.%s.createdAt"),
    EXT_FOLDERS_FAVORITES_PREFIX("org.alfresco.ext.folders.favourites.%s.createdAt");
    
    private final String preferenceName;

    private PreferenceName(final String preferenceName)
    {
        this.preferenceName = preferenceName;
    }

    @Override
    public String toString()
    {
        return preferenceName;
    }
}
