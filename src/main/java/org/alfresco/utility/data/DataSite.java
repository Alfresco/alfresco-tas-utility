package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.dataprep.SiteService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.SiteModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.alfresco.api.entities.Site;
import org.springframework.stereotype.Service;


/**
 * Data Preparation for Sites
 * 
 * This class with handle all aspects of creating sites using dataprep project
 * In the future we will remove this dataprep dependencies if there will be another solution of creating sites.
 */
@Service
@Scope(value = "prototype")
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
    public synchronized SiteModel createSite(SiteModel siteModel) throws DataPreparationException
    {    
        STEP(String.format("Creating site %s with user %s", siteModel.toString(), getCurrentUser().toString()));
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
        STEP(String.format("Creating public site %s with user %s", randomSite, getCurrentUser().toString()));

        return createSite(new SiteModel(randomSite));
    }
    
    /**
     * Create moderated site immediately
     * 
     * @return
     * @throws DataPreparationException 
     */
    public SiteModel createModeratedRandomSite() throws DataPreparationException
    {
        SiteModel randomSite = new SiteModel(RandomData.getRandomName("site"));
        STEP(String.format("Creating moderated site %s with user %s", randomSite, getCurrentUser().toString()));

        randomSite.setVisibility(Site.Visibility.MODERATED);
        return createSite(randomSite);
    }
    
    /**
     * Create private site immediately
     * 
     * @return
     * @throws DataPreparationException 
     */
    public SiteModel createPrivateRandomSite() throws DataPreparationException
    {
        SiteModel randomSite = new SiteModel(RandomData.getRandomName("site"));
        STEP(String.format("Creating private site %s with user %s", randomSite, getCurrentUser().toString()));

        randomSite.setVisibility(Site.Visibility.PRIVATE);
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
        STEP(String.format("Creating public site %s with user %s and setting as IMAP Favourite", imapSite, getCurrentUser().toString()));

        SiteModel siteModel = createSite(new SiteModel(imapSite));
        siteService.setIMAPFavorite(getCurrentUser().getUsername(), getCurrentUser().getPassword(), imapSite);
        return siteModel;
    }

    /**
     * Set site as IMAP favorite
     *
     * @throws DataPreparationException
     */
    public void setIMAPFavorite(SiteModel siteModel) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Set site %s as IMAP Favorite", getCurrentSite()));
        siteService.setIMAPFavorite(getCurrentUser().getUsername(), getCurrentUser().getPassword(), siteModel.getId());
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
    
    /**
     * Check if site is created
     * @param siteModel
     * @return boolean
     */
    public boolean isSiteCreated(SiteModel siteModel)
    {
        return !siteService.getSiteNodeRef(getCurrentUser().getUsername(), getCurrentUser().getPassword(), siteModel.getId()).isEmpty();
    }

    public void deleteSite(SiteModel siteModel) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Delete site %s", siteModel.getId()));
        siteService.delete(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentUser().getDomain(), siteModel.getId());
    }
}