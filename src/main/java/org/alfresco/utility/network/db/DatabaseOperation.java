package org.alfresco.utility.network.db;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseOperation
{

    boolean connect() throws SQLException;

    boolean disconect() throws SQLException;

    String getSatus();

    List<Object> executeQuery(String query);
}
