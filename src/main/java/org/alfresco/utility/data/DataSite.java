package org.alfresco.utility.data;

import org.alfresco.dataprep.SiteService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.alfresco.api.entities.Site.Visibility;
import org.springframework.stereotype.Service;

/**
 * Data Preparation for Sites
 */
@Service
public class DataSite extends TestData
{

    @Autowired
    private SiteService siteService;
    
    @Autowired
    private DataUser dataUser;
    
    private UserModel currentUser;

    static String SITE_NOT_CREATED = "Site %s  not created";

    /**
     * Creates a new random site on test server defined in {@link TasProperties} file.
     * 
     * @param siteModel
     * @param userModel
     * @return
     * @throws DataPreparationException
     */
    public SiteModel createSite(SiteModel siteModel) throws DataPreparationException
    {    	
    	LOG.debug("Creating site {} with user {}", siteModel.toString(), currentUser.toString());
    	
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
    
    public DataSite usingUser(UserModel user)
    {
    	currentUser = user;
    	return this;
    }
    
    protected UserModel getCurrentUser()
    {
    	if(currentUser==null)
    	{
    		currentUser = dataUser.getAdminUser();
    	}
    	return currentUser;
    }

    /**
     * Create public site immediately
     * 
     * @return
     */
    public SiteModel createPublicRandomSite()
    {
        String randomSite = getRandomName("site");
        SiteModel siteModel = new SiteModel(Visibility.PUBLIC, "", 
                                            randomSite, 
                                            randomSite,
                                            randomSite + " description");
        siteService.create(
        		getCurrentUser().getUsername(), 
        		getCurrentUser().getPassword(),        		
                String.format(EMAIL,RandomStringUtils.randomAlphanumeric(20)),
                siteModel.getId(), 
                siteModel.getTitle(), 
                siteModel.getDescription(), 
                siteModel.getVisibility());

        return siteModel;
    }

}