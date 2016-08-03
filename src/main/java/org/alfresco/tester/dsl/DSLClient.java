package org.alfresco.tester.dsl;

import org.alfresco.tester.model.UserModel;

public interface DSLClient<Client>
{
    public Client withAuthUser(UserModel userModel) throws Exception;

    public Client disconnect() throws Exception;

}
