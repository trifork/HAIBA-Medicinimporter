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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import dk.nsi.haiba.medicinimporter.dao.HAIBADAO;
import dk.nsi.haiba.medicinimporter.dao.MedicinDAO;
import dk.nsi.haiba.medicinimporter.importer.ImportExecutor;
import dk.nsi.haiba.medicinimporter.importer.Medicin;

/*
 * Tests the HAIBADAO class
 * Spring transaction ensures rollback after test is finished
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional("medicinTransactionManager")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ImportExecutorIT {
    @Configuration
    @PropertySource("classpath:test.properties")
    @Import(MedicinIntegrationTestConfiguration.class)
    static class ContextConfiguration {
    }

    @Autowired
    @Qualifier("medicinJdbcTemplate")
    JdbcTemplate medicinJdbcTemplate;
    
    @Autowired
    @Qualifier("regionHMedicinJdbcTemplate")
    JdbcTemplate regionHMedicinJdbcTemplate;

    @Autowired
    @Qualifier("haibaJdbcTemplate")
    JdbcTemplate haibaJdbcTemplate;

    @Autowired
    MedicinDAO medicinDAO;

    @Autowired
    HAIBADAO haibaDAO;

    @Autowired
    ImportExecutor importExecutor;

    @Before
    public void init() {
        Logger.getLogger(ImportExecutor.class).setLevel(Level.DEBUG);
        // Logger.getLogger("org.springframework.jdbc.core").setLevel(Level.TRACE);

        // remove all
        haibaJdbcTemplate.update("delete from Data_medicine");
        medicinJdbcTemplate.update("delete from T_HAI_MEDICIN");
        regionHMedicinJdbcTemplate.update("delete from T_MEDICIN_1084");
    }

    private void insertRandomMedicinRow(int id) {
        // v_drugid is int or string (was int, kenn wants it to be string)
        Date d = new Date();
        medicinJdbcTemplate
                .update("INSERT INTO T_HAI_MEDICIN (V_REGION, V_CPR, V_SHAK, D_ADM_START, D_ORD_START, D_ORD_SLUT, D_KONTAKT_START, D_KONTAKT_SLUT, V_ADM_VEJ, V_ADM_DOSIS, V_ADM_DOSIS_ENHED, V_ADM_VOLUMEN, V_ADM_VOLUMEN_ENHED, V_DRUGID, V_PRIM_ATC, V_LAEGEMIDDELNAVN, V_BEH_INDIC_KODE, V_BEH_INDIC, c_source_file) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        1085, "1234567890", "SHAKSHAKSH", d, d, d, d, d, "VEJ", "DOSIS", "DOSIS_ENHED", "VOLUMEN",
                        "VOLUMEN_ENHED", id, "PRIM_ATC", "LAEGEMIDDELNAVN", "INDIC_KODE", "INDIC", id);
    }

    private void insertRandomRegionHMedicinRow(int id) {
        // v_drugid is int or string (was int, kenn wants it to be string)
        Date d = new Date();
        regionHMedicinJdbcTemplate
                .update("INSERT INTO T_MEDICIN_1084 (V_REGION, V_CPR, V_BEH_OE, D_ADM_START, D_MED_START, D_MED_END, D_K_START, D_K_SLUT, V_ADMVEJ, V_ADM_DOSIS, V_ADM_DOSIS_ENHED, V_ADM_VOLUMEN, V_ADM_VOLUMEN_ENHED, V_DRUGID, V_PRIM_ATC, V_NAVN, V_INDIC, c_source_file) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        1084, "1234567890", "SHAKSHAKSH", d, d, d, d, d, "VEJ", "DOSIS", "DOSIS_ENHED", "VOLUMEN",
                        "VOLUMEN_ENHED", id, "PRIM_ATC", "LAEGEMIDDELNAVN", "INDIC", id);
    }

    @Test
    public void testMedicin() {
        // insert 189 rows and test that they are all copied
        int count = 189;
        for (int i = 0; i < count; i++) {
            insertRandomMedicinRow(i);
        }
        Collection<Medicin> medicinFromSyncId = medicinDAO.getMedicinFromSyncId(10, 10);
        assertNotNull(medicinFromSyncId);
        assertEquals(10, medicinFromSyncId.size());
    }

    @Test
    public void testImportExecutor() {
        // insert 189 rows and test that they are all copied
        int count = 189;
        for (int i = 0; i < count; i++) {
            insertRandomMedicinRow(i);
            insertRandomRegionHMedicinRow(i);
        }
        long latestFromMedicin1085 = medicinJdbcTemplate.queryForLong("SELECT LAST_INSERT_ID()");
        long latestFromMedicin1084 = regionHMedicinJdbcTemplate.queryForLong("SELECT LAST_INSERT_ID()");

        importExecutor.doProcess();
        long latestSyncId1085 = haibaDAO.getLatestSyncId(1085);
        long latestSyncId1084 = haibaDAO.getLatestSyncId(1084);
        System.out.println("1085:" + latestSyncId1085 + ", 1084:" + latestSyncId1084 + ", latestFromMedicin1085:"
                + latestFromMedicin1085);
        assertEquals(latestFromMedicin1084, latestSyncId1084);
        assertEquals(latestFromMedicin1085, latestSyncId1085);
    }
}
