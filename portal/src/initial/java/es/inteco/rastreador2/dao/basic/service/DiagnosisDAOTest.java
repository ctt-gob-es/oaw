package es.inteco.rastreador2.dao.basic.service;

import org.apache.log4j.helpers.DateTimeDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by mikunis on 4/02/15.
 */
public class DiagnosisDAOTest {

    private Connection connection;

    @Before
    public void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/OAW", "root", "root");
    }

    @Test
    public void testGetBasicServiceRequestByDates() throws SQLException, ParseException {
        final DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        final String start = "05-01-2015";
        final String end = "14-01-2015";

        Calendar aux = new GregorianCalendar();
        aux.setTime(format.parse(end));
        aux.add(Calendar.DATE,1);

        final Date startDate = format.parse(start);
        final Date endDate = aux.getTime();

        final String csv = DiagnosisDAO.getBasicServiceRequestCSV(connection, startDate, endDate);
        Assert.assertNotNull(csv);
        System.out.println(csv);
    }
}
