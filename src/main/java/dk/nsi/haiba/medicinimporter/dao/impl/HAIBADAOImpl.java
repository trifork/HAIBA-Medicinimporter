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
package dk.nsi.haiba.medicinimporter.dao.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import dk.nsi.haiba.medicinimporter.dao.CommonDAO;
import dk.nsi.haiba.medicinimporter.dao.HAIBADAO;
import dk.nsi.haiba.medicinimporter.exception.DAOException;
import dk.nsi.haiba.medicinimporter.importer.Medicin;

public class HAIBADAOImpl extends CommonDAO implements HAIBADAO {
    @Autowired
    JdbcTemplate haibaJdbcTemplate;

    @Value("${jdbc.haibatableprefix:}")
    String haibaTablePrefix;

    public HAIBADAOImpl(String dialect) {
        super(dialect);
    }
    
    @Override
    public long getLatestSyncId() {
        return haibaJdbcTemplate.queryForLong("SELECT max(insertrow_id) FROM " + haibaTablePrefix + "Data_medicine");
    }

    @Override
    public void saveMedicinList(Collection<Medicin> medicinFromSyncId) throws DAOException {
        // @formatter:off
        String sql = ""
                + "INSERT INTO " + haibaTablePrefix + "Data_medicine "
                + "            (v_cpr, "
                + "             v_shak, "
                + "             v_region, "
                + "             d_adm_start, "
                + "             d_ord_start, "
                + "             d_ord_slut, "
                + "             d_kontakt_start, "
                + "             d_kontakt_slut, "
                + "             v_adm_vej, "
                + "             v_adm_dosis, "
                + "             v_adm_dosis_enhed, "
                + "             v_adm_volumen, "
                + "             v_adm_volumen_enhed, "
                + "             v_drugid, "
                + "             v_prim_atc, "
                + "             v_laegemiddelnavn, "
                + "             v_beh_indic_kode, "
                + "             v_beh_indic, "
                + "             insertrow_id) "
                + "VALUES      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (Medicin m : medicinFromSyncId) {
            haibaJdbcTemplate.update(sql, 
                    m.getV_cpr(), 
                    m.getV_shak(), 
                    m.getV_region(), 
                    m.getD_adm_start(), 
                    m.getD_ord_start(), 
                    m.getD_ord_slut(), 
                    m.getD_kontakt_start(), 
                    m.getD_kontakt_slut(), 
                    m.getV_adm_vej(),
                    m.getV_adm_dosis(), 
                    m.getV_adm_dosis_enhed(), 
                    m.getV_adm_volumen(), 
                    m.getV_ad_volumen_enhed(),
                    m.getV_drugid(),
                    m.getV_prim_atc(),
                    m.getV_laegemiddelnavn(),
                    m.getV_beh_indic_kode(),
                    m.getV_beh_indic(),
                    m.getInsertrow_id());
        }
        // @formatter:on
    }
}
