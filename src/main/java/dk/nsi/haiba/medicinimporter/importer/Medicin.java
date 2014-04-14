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
package dk.nsi.haiba.medicinimporter.importer;

import java.util.Date;

public class Medicin {
    private String v_region;
    private String v_cpr;
    private String v_shak;
    private Date d_adm_start;
    private Date d_ord_start;
    private Date d_ord_slut;
    private Date d_kontakt_start;
    private Date d_kontakt_slut;
    private String v_adm_vej;
    private String v_adm_dosis;
    private String v_adm_dosis_enhed;
    private String v_adm_volumen;
    private String v_ad_volumen_enhed;
    private String v_drugid;
    private String v_prim_atc;
    private String v_laegemiddelnavn;
    private String v_beh_indic_kode;
    private String v_beh_indic;
    private long insertrow_id;
    private long c_source_file;
    public String getV_region() {
        return v_region;
    }
    public void setV_region(String v_region) {
        this.v_region = v_region;
    }
    public String getV_cpr() {
        return v_cpr;
    }
    public void setV_cpr(String v_cpr) {
        this.v_cpr = v_cpr;
    }
    public String getV_shak() {
        return v_shak;
    }
    public void setV_shak(String v_shak) {
        this.v_shak = v_shak;
    }
    public Date getD_adm_start() {
        return d_adm_start;
    }
    public void setD_adm_start(Date d_adm_start) {
        this.d_adm_start = d_adm_start;
    }
    public Date getD_ord_start() {
        return d_ord_start;
    }
    public void setD_ord_start(Date d_ord_start) {
        this.d_ord_start = d_ord_start;
    }
    public Date getD_ord_slut() {
        return d_ord_slut;
    }
    public void setD_ord_slut(Date d_ord_slut) {
        this.d_ord_slut = d_ord_slut;
    }
    public Date getD_kontakt_start() {
        return d_kontakt_start;
    }
    public void setD_kontakt_start(Date d_kontakt_start) {
        this.d_kontakt_start = d_kontakt_start;
    }
    public Date getD_kontakt_slut() {
        return d_kontakt_slut;
    }
    public void setD_kontakt_slut(Date d_kontakt_slut) {
        this.d_kontakt_slut = d_kontakt_slut;
    }
    public String getV_adm_vej() {
        return v_adm_vej;
    }
    public void setV_adm_vej(String v_adm_vej) {
        this.v_adm_vej = v_adm_vej;
    }
    public String getV_adm_dosis() {
        return v_adm_dosis;
    }
    public void setV_adm_dosis(String v_adm_dosis) {
        this.v_adm_dosis = v_adm_dosis;
    }
    public String getV_adm_dosis_enhed() {
        return v_adm_dosis_enhed;
    }
    public void setV_adm_dosis_enhed(String v_adm_dosis_enhed) {
        this.v_adm_dosis_enhed = v_adm_dosis_enhed;
    }
    public String getV_adm_volumen() {
        return v_adm_volumen;
    }
    public void setV_adm_volumen(String v_adm_volumen) {
        this.v_adm_volumen = v_adm_volumen;
    }
    public String getV_ad_volumen_enhed() {
        return v_ad_volumen_enhed;
    }
    public void setV_ad_volumen_enhed(String v_ad_volumen_enhed) {
        this.v_ad_volumen_enhed = v_ad_volumen_enhed;
    }
    public String getV_drugid() {
        return v_drugid;
    }
    public void setV_drugid(String v_drugid) {
        this.v_drugid = v_drugid;
    }
    public String getV_prim_atc() {
        return v_prim_atc;
    }
    public void setV_prim_atc(String v_prim_atc) {
        this.v_prim_atc = v_prim_atc;
    }
    public String getV_laegemiddelnavn() {
        return v_laegemiddelnavn;
    }
    public void setV_laegemiddelnavn(String v_laegemiddelnavn) {
        this.v_laegemiddelnavn = v_laegemiddelnavn;
    }
    public String getV_beh_indic_kode() {
        return v_beh_indic_kode;
    }
    public void setV_beh_indic_kode(String v_beh_indic_kode) {
        this.v_beh_indic_kode = v_beh_indic_kode;
    }
    public String getV_beh_indic() {
        return v_beh_indic;
    }
    public void setV_beh_indic(String v_beh_indic) {
        this.v_beh_indic = v_beh_indic;
    }
    public long getInsertrow_id() {
        return insertrow_id;
    }
    public void setInsertrow_id(long insertrow_id) {
        this.insertrow_id = insertrow_id;
    }
    public long getC_source_file() {
        return c_source_file;
    }
    public void setC_source_file(long c_source_file) {
        this.c_source_file = c_source_file;
    }
}   
