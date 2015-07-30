/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.vertxci.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.postgresql.Driver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Alyx
 */
//@RunWith (SpringJUnit4ClassRunner.class)
public class VertxIntegrationTest {

    static RestTemplate restTemplate;

    @BeforeClass
    public static void suiteSetUp() {
        restTemplate = new RestTemplate();
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHTTPServer() {
        String result = restTemplate.getForObject("http://localhost:8081", String.class);
        assertEquals("Hello World from Vertx (changed 10th time)!", result);
    }
    
    @Test
    public void testDBConnectionOnLocalhostPostgresPost() throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", 
                "postgres", "post");
        
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from test");
        
        resultSet.next();
        
        assertEquals(1, resultSet.getInt(1));
        assertEquals("abc", resultSet.getString(2));
    }
    
    @Test
    public void testDBConnectionOnLocalhostUbuntuVertx() throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", 
                "ubuntu", "vertx");
        
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select 1");
        
        resultSet.next();
        
        assertEquals(1, resultSet.getInt(1));
    }

}
