/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.haiba.medicinimporter.integrationtest;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

import dk.nsi.haiba.medicinimporter.config.MedicinConfiguration;
import dk.nsi.haiba.medicinimporter.dao.MedicinDAO;
import dk.nsi.haiba.medicinimporter.dao.impl.MedicinDAOImpl;
import dk.nsi.haiba.medicinimporter.importer.ImportExecutor;
import dk.nsi.haiba.medicinimporter.status.ImportStatusRepository;
import dk.nsi.haiba.medicinimporter.status.ImportStatusRepositoryJdbcImpl;
import dk.nsi.haiba.medicinimporter.status.TimeSource;
import dk.nsi.haiba.medicinimporter.status.TimeSourceRealTimeImpl;

@Configuration
@EnableTransactionManagement
@PropertySource("test.properties")
public class MedicinIntegrationTestConfiguration extends MedicinConfiguration {
    @Value("${jdbc.haibaJNDIName}")
    private String haibaJNDIName;

    @Value("${jdbc.medicinJNDIName}")
    private String medicinJNDIName;

    @Value("${test.mysql.port}")
    private int mysqlPort;
    @Value("${test.mysql.haibadbname}")
    private String testHAIBADbName;
    @Value("${test.mysql.haibadbusername}")
    private String testHAIBADbUsername;
    @Value("${test.mysql.haibadbpassword}")
    private String testHAIBADbPassword;
    @Value("${jdbc.haiba.dialect:MSSQL}")
    String haibadialect;
    @Value("${jdbc.medicin.dialect:MSSQL}")
    String medicindialect;

    @Bean
    public DataSource haibaDataSource() throws Exception {
        String jdbcUrlPrefix = "jdbc:mysql://127.0.0.1:" + mysqlPort + "/";

        return new SimpleDriverDataSource(new Driver(), jdbcUrlPrefix + testHAIBADbName
                + "?createDatabaseIfNotExist=true", testHAIBADbUsername, testHAIBADbPassword);
    }

    @Bean
    public DataSource medicinDataSource() throws Exception {
        String jdbcUrlPrefix = "jdbc:mysql://127.0.0.1:" + mysqlPort + "/";

        return new SimpleDriverDataSource(new Driver(), jdbcUrlPrefix + testHAIBADbName
                + "?createDatabaseIfNotExist=true", testHAIBADbUsername, testHAIBADbPassword);
    }

    @Bean
    public DataSource regionHMedicinDataSource() throws Exception {
        String jdbcUrlPrefix = "jdbc:mysql://127.0.0.1:" + mysqlPort + "/";

        return new SimpleDriverDataSource(new Driver(), jdbcUrlPrefix + testHAIBADbName
                + "?createDatabaseIfNotExist=true", testHAIBADbUsername, testHAIBADbPassword);
    }

    @Bean
    public JdbcTemplate medicinJdbcTemplate(@Qualifier("medicinDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public JdbcTemplate regionHMedicinJdbcTemplate(@Qualifier("regionHMedicinDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public ImportExecutor importExecutor() {
        return new ImportExecutor();
    }

    @Bean
    public ImportStatusRepository importStatusRepository() {
        return new ImportStatusRepositoryJdbcImpl(haibadialect);
    }

    @Bean
    public TimeSource timeSource() {
        return new TimeSourceRealTimeImpl();
    }

    @Bean
    public PlatformTransactionManager medicinTransactionManager(@Qualifier("medicinDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public MedicinDAO medicinDAO(@Qualifier("medicinJdbcTemplate") JdbcTemplate jt) {
        return new MedicinDAOImpl(medicindialect, jt, "T_HAI_MEDICIN", 1085, new MedicinDAOImpl.StandardRowMapper());
    }

    @Bean
    public MedicinDAO regionHMedicinDAO(@Qualifier("regionHMedicinJdbcTemplate") JdbcTemplate jt) {
        return new MedicinDAOImpl(medicindialect, jt, "T_MEDICIN_1084", 1084, new MedicinDAOImpl.RegionHRowMapper());
    }
}
