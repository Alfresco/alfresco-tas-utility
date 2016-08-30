package org.alfresco.utility.data;

import java.io.File;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FileType;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public abstract class TestData<Data> {
	static Logger LOG = LogFactory.getLogger();
	
	/**
	 * This is the currentUser that we will use for creating specific test data
	 * If none specified we will use the admin user defined in default.properties
	 */
	private UserModel currentUser;

	@Autowired
	protected TasProperties tasProperties;

	public static String PASSWORD = "password";
	public static String EMAIL = "%s@tas-automation.org";

	/**
	 * Returns a random alphabetic number of 10 characters
	 * @return
	 */
	public static String getRandomAlphanumeric() {
		String value = RandomStringUtils.randomAlphabetic(10);
		LOG.debug("Generating alphanumeric string: {}", value);
		return value;
	}

	/**
	 * Check if <filename> passed as parameter is a file or not based on
	 * extension
	 */
	public static boolean isAFile(String filename) {
		return Files.getFileExtension(filename).length() == 3;
	}

	/**
	 * @param extention
	 *            - as "txt", "pdf", "doc"
	 * @return random file with <extention>
	 */
	public File getRandomFile(FileType fileType) {
		String fileName = String.format("%s.%s", getRandomName("file"), fileType.extention);
		return new File(fileName);
	}

	/**
	 * Generates a new random {@link FileModel} object
	 */
	public FileModel generateRandomFileModel(FileType fileType) {
		FileModel model = new FileModel(getRandomFile(fileType));
		LOG.info("Generating new Model: {}", model.toString());
		return model;
	}

	/**
	 * Generates a new random {@link FolderModel} object
	 */
	public FolderModel generateRandomFolderModel() {
		FolderModel model = new FolderModel(getRandomFolder().getName());
		LOG.info("Generating new Model: {}", model.toString());
		return model;
	}

	/**
	 * @return random folder
	 */
	public File getRandomFolder() {
		return new File(getRandomName("folder"));
	}

	public String getRandomName(String prefix) {
		return String.format("%s-%s", prefix, getRandomAlphanumeric());
	}

	/**
	 * Call this method if you want to use another user rather than admin defined in {@link TasProperties} properties file
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Data usingUser(UserModel user) {
		currentUser = user;
		return (Data) this;
	}
	
	@SuppressWarnings("unchecked")
	public Data usingAdmin() {
		currentUser = getAdminUser();
		return (Data) this;
	}
	
	protected UserModel getCurrentUser() {
		if (currentUser == null) {
			usingAdmin();
		}
		return currentUser;
	}
	
	public UserModel getAdminUser()
    {
        return new UserModel(tasProperties.getAdminUser(), tasProperties.getAdminPassword());
    }

}
