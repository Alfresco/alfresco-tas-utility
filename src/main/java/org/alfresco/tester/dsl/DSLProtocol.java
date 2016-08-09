package org.alfresco.tester.dsl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.alfresco.tester.LogFactory;
import org.alfresco.tester.model.SiteModel;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;

public abstract class DSLProtocol<Client>
{
    protected Logger LOG = LogFactory.getLogger();

    private String currentSite = "undefined";

    @SuppressWarnings("unchecked")
    public Client usingSite(String siteId)
    {
        setCurrentSite(siteId);
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client usingSite(SiteModel siteModel)
    {
        setCurrentSite(siteModel.getId());
        return (Client) this;
    }

    /**
     * Return the path of /Sites/<siteId> concatenated with "documentLibrary" followed by all <filesOrFolders>
     * 
     * @param siteId
     * @param filesOrFolders
     * @return
     * @throws URIException
     * @throws MalformedURLException
     */
    protected Path getPathFromDocumentLibrary(String siteId, String... filesOrFolders) throws URIException, MalformedURLException
    {
        Path concatenatedPath = Paths.get(getDocumentLibraryPath(siteId).toString(), filesOrFolders);
        LOG.info("Using Site Document Library Path: {}", concatenatedPath.toString());
        return concatenatedPath;
    }

    /**
     * Return the Path of <siteID> documentLibrary
     * Example: /Sites/testOne/documentLibrary
     * 
     * @param siteId
     * @return
     */
    protected Path getDocumentLibraryPath(String siteId)
    {
        return Paths.get("/Sites", siteId, "documentLibrary");
    }

    protected Path getUserHomePath()
    {
        return Paths.get("/User Homes");
    }

    public String getCurrentSite()
    {
        return currentSite;
    }

    public void setCurrentSite(String currentSite)
    {
        this.currentSite = currentSite;
    }
}
