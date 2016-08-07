package org.alfresco.tester.model;

/**
 * {
  "entry": {
    "googleId": "423",
    "lastName": "LN-qLZ41D2QnXQcqySNK09J",
    "jobTitle": "jobtitle",
    "mobile": "2342",
    "emailNotificationsEnabled": true,
    "description": "sumar",
    "telephone": "34243",
    "enabled": true,
    "firstName": "qLZ41D2QnXQcqySNK09J FirstName",
    "skypeId": "4234",
    "avatarId": "931f9219-bc08-4009-8614-e448b01bff20",
    "instantMessageId": "3423",
    "location": "location",
    "company": {
      "organization": "myCompany",
      "address1": "Iasi",
      "address2": "City",
      "address3": "trei",
      "postcode": "1234556",
      "telephone": "564510213453",
      "fax": "541",
      "email": "qLZ41D2QnXQcqySNK09J@yahoo.com"
    },
    "id": "qLZ41D2QnXQcqySNK09J",
    "email": "qLZ41D2QnXQcqySNK09J@dsads.com"
  }
}
 *
 */
public class PersonModel extends TestModel
{
    private String firstName = "no-firstName";
    private String lastName = "no-lastname";
    private String emailNotificationsEnabled = "no-notification";
    private Company company;
    private String id = "no-id";
    private String enabled = "no-enabled";
    private String email = "no-email";
    private String avatarId = "no-avatar";
    private String location = "no-location";
    private String instantMessageId = "no-instantMessage";
    private String googleId = "no-google";
    private String skypeId = "no-skype";
    private String description = "no-description";
    private String telephone = "no-telephone";
    private String jobTitle = "no-job";
    private String mobile = "no-mobile";

    public PersonModel()
    {
    }

    public PersonModel(String firstName, String emailNotificationsEnabled, Company company, String id, String enabled, String email)
    {
        super();
        this.firstName = firstName;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
        this.company = company;
        this.id = id;
        this.enabled = enabled;
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmailNotificationsEnabled()
    {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(String emailNotificationsEnabled)
    {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEnabled()
    {
        return enabled;
    }

    public void setEnabled(String enabled)
    {
        this.enabled = enabled;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAvatarId()
    {
        return avatarId;
    }

    public void setAvatarId(String avatarId)
    {
        this.avatarId = avatarId;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getInstantMessageId()
    {
        return instantMessageId;
    }

    public void setInstantMessageId(String instantMessageId)
    {
        this.instantMessageId = instantMessageId;
    }

    public String getGoogleId()
    {
        return googleId;
    }

    public void setGoogleId(String googleId)
    {
        this.googleId = googleId;
    }

    public String getSkypeId()
    {
        return skypeId;
    }

    public void setSkypeId(String skypeId)
    {
        this.skypeId = skypeId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

}