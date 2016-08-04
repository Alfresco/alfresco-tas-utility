package org.alfresco.tester.data;

import org.alfresco.dataprep.SiteService;
import org.alfresco.tester.TasProperties;
import org.alfresco.tester.exception.DataPreparationException;
import org.alfresco.tester.model.SiteModel;
import org.alfresco.tester.model.UserModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.alfresco.api.entities.Role;
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

    static String SITE_NOT_CREATED = "Site %s  not created";

    /**
     * Creates a new random site on test server defined in {@link TasProperties}
     * file.
     * 
     * @param role
     * @param visibility
     * @param userName
     * @param siteName
     * @param title
     * @return
     * @throws DataPreparationException
     */
    public SiteModel createSite(String role, Visibility visibility, String userName, String siteName, String title) throws DataPreparationException
    {
        SiteModel siteModel = new SiteModel(role, visibility, siteName, siteName, siteName, siteName);
        siteService.create(properties.getAdminUser(), properties.getAdminPassword(), String.format(userName, EMAIL), title, title, siteName, visibility);

        return siteModel;
    }

    public SiteModel createPublicSite(UserModel userModel)
    {
        String siteName = "SiteName" + RandomStringUtils.randomAlphanumeric(20);
        String userName = "UserName" + RandomStringUtils.randomAlphanumeric(20);

        SiteModel siteModel = new SiteModel(Role.SiteManager.toString(), Visibility.PUBLIC, siteName, siteName, siteName, siteName);
        siteService.create(userModel.getUsername(), userModel.getPassword(), String.format(userName, EMAIL), siteModel.getId(), siteModel.getTitle(),
                siteModel.getDescription(), siteModel.getVisibility());

        return siteModel;
    }

}