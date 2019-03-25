package org.alfresco.utility.data;

import org.alfresco.utility.data.auth.DataAIS;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class extends {@link DataUser} by adding the functionality to create/delete
 * users in Alfresco Identity Service (AIS).
 * The behaviour of this class should be identical to that of
 * {@link DataUser} if AIS support is disabled, see {@link DataAIS}.
 */
@Service
@Scope(value = "prototype")
public class DataUserAIS extends DataUser
{

    @Autowired
    DataAIS dataAIS;

    @Override
    public UserModel createUser(String userName, String password) throws DataPreparationException
    {
        UserModel userModel = super.createUser(userName, password);
        createUserInAIS(userModel);
        return userModel;
    }

    @Override
    public UserModel createUserWithCustomEmailAddressInAlfresco(String userName, String password, String domain) throws DataPreparationException
    {
        UserModel userModel = super.createUserWithCustomEmailAddressInAlfresco(userName, password, domain);
        createUserInAIS(userModel);
        return userModel;
    }

    @Override
    public UserModel createUserWithTenant(String userName) throws DataPreparationException
    {
        UserModel userModel = super.createUserWithTenant(userName);
        createUserInAIS(userModel);
        return userModel;
    }

    @Override
    public void deleteUser(UserModel userToDelete) throws DataPreparationException
    {
        super.deleteUser(userToDelete);
        deleteUserFromAIS(userToDelete);
    }

    private void deleteUserFromAIS(UserModel userModel)
    {
        if (dataAIS.isEnabled())
        {
            dataAIS.perform().deleteUser(userModel);
        }
    }

    private void createUserInAIS(UserModel userModel)
    {
        if (dataAIS.isEnabled())
        {
            dataAIS.perform().createUser(userModel);
        }
    }
}
