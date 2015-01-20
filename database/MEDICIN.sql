CREATE TABLE IF NOT EXISTS T_HAI_MEDICIN (
        V_REGION varchar(4) NULL,
        V_CPR varchar(10) NULL,
        V_SHAK varchar(10) NULL,
        D_ADM_START datetime NULL,
        D_ORD_START datetime NULL,
        D_ORD_SLUT datetime NULL,
        D_KONTAKT_START datetime NULL,
        D_KONTAKT_SLUT datetime NULL,
        V_ADM_VEJ varchar(50) NULL,
        V_ADM_DOSIS varchar(50) NULL,
        V_ADM_DOSIS_ENHED varchar(50) NULL,
        V_ADM_VOLUMEN varchar(50) NULL,
        V_ADM_VOLUMEN_ENHED varchar(50) NULL,
        V_DRUGID varchar(15) NULL,
        V_PRIM_ATC varchar(20) NULL,
        V_LAEGEMIDDELNAVN varchar(50) NULL,
        V_BEH_INDIC_KODE varchar(20) NULL,
        V_BEH_INDIC nvarchar(150) NULL,
        InsertRow_id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
        c_source_file int NOT NULL
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS t_medicin_1084 (
        V_REGION varchar(4) NULL,
        V_CPR varchar(10) NULL,
        V_BEH_OE varchar(10) NULL,
        D_ADM_START datetime NULL,
        D_MED_START datetime NULL,
        D_MED_END datetime NULL,
        D_K_START datetime NULL,
        D_K_SLUT datetime NULL,
        V_ADMVEJ varchar(50) NULL,
        V_ADM_DOSIS varchar(50) NULL,
        V_ADM_DOSIS_ENHED varchar(50) NULL,
        V_ADM_VOLUMEN varchar(50) NULL,
        V_ADM_VOLUMEN_ENHED varchar(50) NULL,
        V_DRUGID varchar(15) NULL,
        V_PRIM_ATC varchar(20) NULL,
        V_NAVN varchar(50) NULL,
        V_INDIC nvarchar(150) NULL,
        InsertRow_id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
        c_source_file int NOT NULL
) ENGINE=InnoDB COLLATE=utf8_bin;