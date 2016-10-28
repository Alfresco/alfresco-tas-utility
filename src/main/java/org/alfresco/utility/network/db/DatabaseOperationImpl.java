package org.alfresco.utility.network.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.alfresco.utility.TasProperties;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * Handle Database operations, like execution of queries over DB, getting status of db or search results
 */
@Service
public class DatabaseOperationImpl implements DatabaseOperation
{

    @Autowired
    protected TasProperties properties;

    private JdbcTemplate jdbcTemplate;

    private DataSource getDataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getDbUrl());
        String dbUrl = properties.getDbUrl();

        if (dbUrl.contains("oracle"))
        {
            dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        }

        if (dbUrl.contains("mysql"))
        {
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        }

        if (dbUrl.contains("mariadb"))
        {
            dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        }

        if (dbUrl.contains("postgre"))
        {
            dataSource.setDriverClassName("org.postgresql.Driver");
        }

        if (dbUrl.contains("db2"))
        {
            dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        }

        dataSource.setUsername(properties.getDbUsername());
        dataSource.setPassword(properties.getDbPassword());
        return dataSource;
    }

    public JdbcTemplate buildJdbcTemplate()
    {
        if (jdbcTemplate == null)
        {
            this.jdbcTemplate = new JdbcTemplate(getDataSource());
        }

        return jdbcTemplate;
    }

    @Override
    public boolean disconect() throws SQLException
    {
        Connection conn = buildJdbcTemplate().getDataSource().getConnection();

        if (conn.isClosed() == false)
        {
            conn.close();
        }

        return conn.isClosed();
    }

    @Override
    public String getSatus()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> executeQuery(String query)
    {
        return buildJdbcTemplate().query(query, new TestRowMapper());
    }

    /**
     * Establishes a database connection
     */
    @Override
    public boolean connect() throws SQLException
    {
        return !buildJdbcTemplate().getDataSource().getConnection().isClosed();

    }

    private static class TestRowMapper implements RowMapper<Object>
    {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return rs.getObject(1);
        }
    }
}
