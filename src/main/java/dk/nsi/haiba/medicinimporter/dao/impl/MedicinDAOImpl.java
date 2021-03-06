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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import dk.nsi.haiba.medicinimporter.dao.CommonDAO;
import dk.nsi.haiba.medicinimporter.dao.MedicinDAO;
import dk.nsi.haiba.medicinimporter.importer.Medicin;

public class MedicinDAOImpl extends CommonDAO implements MedicinDAO {
    JdbcTemplate jdbcTemplate;
    String medicinTable;
    private int aRegion;
    private RowMapper<Medicin> aRowMapper;

    public MedicinDAOImpl(String dialect, JdbcTemplate jt, String table, int region, RowMapper<Medicin> rowMapper) {
        super(dialect);
        jdbcTemplate = jt;
        medicinTable = table;
        aRegion = region;
        aRowMapper = rowMapper;
    }

    @Override
    public Collection<Medicin> getMedicinFromSyncId(long syncId, long batchSize) {
        Collection<Medicin> returnValue = new ArrayList<Medicin>();
        String sql = null;
        if (isMYSQL()) {
            sql = "SELECT * FROM " + medicinTable
                    + " where InsertRow_id>? ORDER BY InsertRow_id LIMIT " + batchSize;
        } else {
            // MSSQL
            sql = "SELECT TOP " + batchSize + " * FROM " + medicinTable
                    + " where InsertRow_id>? ORDER BY InsertRow_id";
        }
        returnValue = jdbcTemplate.query(sql, aRowMapper, syncId);
        return returnValue;
    }

    protected static String convertToString(Object object) {
        String returnValue = "" + object;
        return returnValue;
    }
    
    public static final class RegionHRowMapper implements RowMapper<Medicin> {
        @Override
        public Medicin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Medicin returnValue = new Medicin();
            returnValue.setC_source_file(rs.getLong("c_source_file")); // XXX check
            returnValue.setD_adm_start(rs.getTimestamp("D_ADM_START"));
            returnValue.setD_kontakt_slut(rs.getTimestamp("D_K_SLUT"));
            returnValue.setD_kontakt_start(rs.getTimestamp("D_K_START"));
            returnValue.setD_ord_slut(rs.getTimestamp("D_MED_END"));
            returnValue.setD_ord_start(rs.getTimestamp("D_MED_START"));
            returnValue.setInsertrow_id(rs.getLong("InsertRow_id"));
            returnValue.setV_ad_volumen_enhed(rs.getString("V_ADM_VOLUMEN_ENHED"));
            returnValue.setV_adm_dosis(rs.getString("V_ADM_DOSIS"));
            returnValue.setV_adm_dosis_enhed(rs.getString("V_ADM_DOSIS_ENHED"));
            returnValue.setV_adm_vej(rs.getString("V_ADMVEJ"));
            returnValue.setV_adm_volumen(rs.getString("V_ADM_VOLUMEN"));
            returnValue.setV_beh_indic(rs.getString("V_INDIC"));
//            returnValue.setV_beh_indic_kode(rs.getString("V_BEH_INDIC_KODE")); // not in region h
            returnValue.setV_cpr(rs.getString("V_CPR"));
            // returnValue.setV_drugid(rs.getObject("V_DRUGID", String.class)); // doesn't work on tomcat, old
            // version
            returnValue.setV_drugid(convertToString(rs.getObject("V_DRUGID")));
            returnValue.setV_laegemiddelnavn(rs.getString("V_NAVN"));
            returnValue.setV_prim_atc(rs.getString("V_PRIM_ATC"));
            returnValue.setV_region(rs.getString("V_REGION"));
            returnValue.setV_shak(rs.getString("V_BEH_OE"));
            return returnValue;
        }
    }
    
    public static final class StandardRowMapper implements RowMapper<Medicin> {
        @Override
        public Medicin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Medicin returnValue = new Medicin();
            returnValue.setC_source_file(rs.getLong("c_source_file"));
            returnValue.setD_adm_start(rs.getTimestamp("D_ADM_START"));
            returnValue.setD_kontakt_slut(rs.getTimestamp("D_KONTAKT_SLUT"));
            returnValue.setD_kontakt_start(rs.getTimestamp("D_KONTAKT_START"));
            returnValue.setD_ord_slut(rs.getTimestamp("D_ORD_SLUT"));
            returnValue.setD_ord_start(rs.getTimestamp("D_ORD_START"));
            returnValue.setInsertrow_id(rs.getLong("InsertRow_id"));
            returnValue.setV_ad_volumen_enhed(rs.getString("V_ADM_VOLUMEN_ENHED"));
            returnValue.setV_adm_dosis(rs.getString("V_ADM_DOSIS"));
            returnValue.setV_adm_dosis_enhed(rs.getString("V_ADM_DOSIS_ENHED"));
            returnValue.setV_adm_vej(rs.getString("V_ADM_VEJ"));
            returnValue.setV_adm_volumen(rs.getString("V_ADM_VOLUMEN"));
            returnValue.setV_beh_indic(rs.getString("V_BEH_INDIC"));
            returnValue.setV_beh_indic_kode(rs.getString("V_BEH_INDIC_KODE"));
            returnValue.setV_cpr(rs.getString("V_CPR"));
            // returnValue.setV_drugid(rs.getObject("V_DRUGID", String.class)); // doesn't work on tomcat, old
            // version
            returnValue.setV_drugid(convertToString(rs.getObject("V_DRUGID")));
            returnValue.setV_laegemiddelnavn(rs.getString("V_LAEGEMIDDELNAVN"));
            returnValue.setV_prim_atc(rs.getString("V_PRIM_ATC"));
            returnValue.setV_region(rs.getString("V_REGION"));
            returnValue.setV_shak(rs.getString("V_SHAK"));
            return returnValue;
        }
    }

    @Override
    public int getRegion() {
        return aRegion;
    }
}
