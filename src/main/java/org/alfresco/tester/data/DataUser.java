package org.alfresco.tester.data;

import org.alfresco.dataprep.UserService;
import org.alfresco.tester.ServerProperties;
import org.alfresco.tester.exception.DataPreparationException;
import org.alfresco.tester.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Data Preparation for Users
 * 
 * @author Paul Brodner
 *
 */
@Service
public class DataUser extends TestData {
	@Autowired
	private UserService userService;

	static String USER_NOT_CREATED = "User %s  not created";

	/**
	 * Creates a new random user on test server defined in {@link ServerProperties}
	 * file.
	 * 
	 * @param userName
	 * @return
	 * @throws DataPreparationException
	 */
	public UserModel createUser(String userName) throws DataPreparationException {
		UserModel newUser = new UserModel(userName, userName);
		Boolean created = userService.create(
										properties.getAdminUser(), 
										properties.getAdminPassword(), 
										userName,
										PASSWORD, 
										String.format(userName, EMAIL), 
										String.format("%s FirstName", userName),
										String.format("LN-%s", userName));
		if (!created)
			throw new DataPreparationException(String.format(USER_NOT_CREATED, newUser.toString()));

		return newUser;
	}
}
