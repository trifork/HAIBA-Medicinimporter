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
package dk.nsi.haiba.medicinimporter.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import dk.nsi.haiba.medicinimporter.dao.HAIBADAO;
import dk.nsi.haiba.medicinimporter.dao.MedicinDAO;
import dk.nsi.haiba.medicinimporter.dao.impl.HAIBADAOImpl;
import dk.nsi.haiba.medicinimporter.dao.impl.MedicinDAOImpl;
import dk.nsi.haiba.medicinimporter.importer.ImportExecutor;
import dk.nsi.haiba.medicinimporter.status.ImportStatusRepository;
import dk.nsi.haiba.medicinimporter.status.ImportStatusRepositoryJdbcImpl;
import dk.nsi.haiba.medicinimporter.status.TimeSource;
import dk.nsi.haiba.medicinimporter.status.TimeSourceRealTimeImpl;

@Configuration
@EnableScheduling
public class MedicinConfiguration {
    @Value("${jdbc.haibaJNDIName}")
    private String haibaJNDIName;

    @Value("${jdbc.medicinJNDIName}")
    private String medicinJNDIName;
    
    @Value("${jdbc.regionHMedicinJNDIName}")
    private String regionHMedicinJNDIName;
    
    @Value("${jdbc.haiba.dialect:MSSQL}")
    String haibadialect;

    @Value("${jdbc.medicin.dialect:MSSQL}")
    String medicindialect;

    @Value("${jdbc.medicintableprefix:BASE.T_HAI_MEDICIN}")
    String medicintable;
    
    @Value("${jdbc.regionhmedicintable:BASE.T_MEDICIN_1084}")
    String regionhmedicintable;

    // this is not automatically registered, see https://jira.springsource.org/browse/SPR-8539
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(false);

        propertySourcesPlaceholderConfigurer
                .setLocations(new Resource[] { new ClassPathResource("default-config.properties"),
                        new ClassPathResource("medicinconfig.properties") });

        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    public DataSource haibaDataSource() throws Exception {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiName(haibaJNDIName);
        factory.setExpectedType(DataSource.class);
        factory.afterPropertiesSet();
        return (DataSource) factory.getObject();
    }

    @Bean
    public DataSource medicinDataSource() throws Exception {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiName(medicinJNDIName);
        factory.setExpectedType(DataSource.class);
        factory.afterPropertiesSet();
        return (DataSource) factory.getObject();
    }
    
    @Bean
    public DataSource regionHMedicinDataSource() throws Exception {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiName(regionHMedicinJNDIName);
        factory.setExpectedType(DataSource.class);
        factory.afterPropertiesSet();
        return (DataSource) factory.getObject();
    }

    @Bean
    public JdbcTemplate haibaJdbcTemplate(@Qualifier("haibaDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
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
    public PlatformTransactionManager haibaTransactionManager(@Qualifier("haibaDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public HAIBADAO haibaDAO() {
        return new HAIBADAOImpl(haibadialect);
    }

    @Bean
    public MedicinDAO medicinDAO(@Qualifier("medicinJdbcTemplate") JdbcTemplate jt) {
        return new MedicinDAOImpl(medicindialect, jt, medicintable, 1085, new MedicinDAOImpl.StandardRowMapper());
    }
    
    @Bean
    public MedicinDAO regionHMedicinDAO(@Qualifier("regionHMedicinJdbcTemplate") JdbcTemplate jt) {
        return new MedicinDAOImpl(medicindialect, jt, regionhmedicintable, 1084, new MedicinDAOImpl.RegionHRowMapper());
    }
}
