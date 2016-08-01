package org.alfresco.tester.dsl;

import org.alfresco.tester.model.UserModel;

public interface DSLClient<Client> {
	public Client withAuthUser(UserModel userModel);

	public Client disconnect();

	public Client usingTestServerFromProperties();

}
