package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.dataprep.SiteService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.SiteModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Data Preparation for Sites
 * 
 * This class with handle all aspects of creating sites using dataprep project
 * In the future we will remove this dataprep dependencies if there will be another solution of creating sites.
 */
@Service
public class DataSite extends TestData<DataSite>
{
    @Autowired
    private SiteService siteService;
   
    static String SITE_NOT_CREATED = "Site %s  not created";

    /**
     * Creates a new random site on test server defined in {@link TasProperties} file.
     * 
     * You can also use the {@link #usingUser(org.alfresco.utility.model.UserModel)}
     * 
     * method for defining a new user rather than admin
     * @param siteModel
     * @param userModel
     * @return
     * @throws DataPreparationException
     */
    public SiteModel createSite(SiteModel siteModel) throws DataPreparationException
    {    	    	
        return createSite(siteModel.getTitle());
    }
    
    /**
     * Creates a new random site on test server defined in {@link TasProperties} file.
     * 
     * You can also use the {@link #usingUser(org.alfresco.utility.model.UserModel)}
     * 
     * method for defining a new user rather than admin
     * @param siteModel
     * @param userModel
     * @return
     * @throws DataPreparationException
     */
    public SiteModel createSite(String siteName) throws DataPreparationException
    {
    	SiteModel siteModel = new SiteModel(siteName);
    	LOG.info("Creating site {} with user {}", siteModel.toString(), getCurrentUser().toString());
    	
        siteService.create(
        		getCurrentUser().getUsername(), 
        		getCurrentUser().getPassword(), 
                String.format(RandomStringUtils.randomAlphanumeric(10), EMAIL),
                siteModel.getId(), 
                siteModel.getTitle(), 
                siteModel.getDescription(), 
                siteModel.getVisibility());

        return siteModel;
    }
    
    

    /**
     * Create public site immediately
     * 
     * @return
     * @throws DataPreparationException 
     */
    public SiteModel createPublicRandomSite() throws DataPreparationException
    {
        String randomSite = RandomData.getRandomName("site");       
        return createSite(randomSite);
    }
    
    /**
     * Create IMAP site immediately
     * 
     * @return
     * @throws DataPreparationException 
     */
    public SiteModel createIMAPSite() throws DataPreparationException
    {
        String imapSite = RandomData.getRandomName("IMAPsite");       
        SiteModel siteModel = createSite(imapSite);
        siteService.setIMAPFavorite(getCurrentUser().getUsername(), getCurrentUser().getPassword(), imapSite);
        return siteModel;
    }
    
    /**
     * Add current site to favorites
     * 
     * @return current site model
     * @throws DataPreparationException 
     */
    public SiteModel addSiteToFavorites() throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Add site %s to Favorites", getCurrentSite()));
        siteService.setFavorite(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite());
        return new SiteModel(getCurrentSite());
    }   
}