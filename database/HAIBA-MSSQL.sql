CREATE TABLE Data_medicine(
        [V_REGION] [varchar](4) NULL,
        [V_CPR] [varchar](10) NULL,
        [V_SHAK] [varchar](10) NULL,
        [D_ADM_START] [datetime] NULL,
        [D_ORD_START] [datetime] NULL,
        [D_ORD_SLUT] [datetime] NULL,
        [D_KONTAKT_START] [datetime] NULL,
        [D_KONTAKT_SLUT] [datetime] NULL,
        [V_ADM_VEJ] [varchar](50) NULL,
        [V_ADM_DOSIS] [varchar](50) NULL,
        [V_ADM_DOSIS_ENHED] [varchar](50) NULL,
        [V_ADM_VOLUMEN] [varchar](50) NULL,
        [V_ADM_VOLUMEN_ENHED] [varchar](50) NULL,
        [V_DRUGID] [varchar](15) NULL,
        [V_PRIM_ATC] [varchar](20) NULL,
        [V_LAEGEMIDDELNAVN] [varchar](50) NULL,
        [V_BEH_INDIC_KODE] [varchar](20) NULL,
        [V_BEH_INDIC] [nvarchar](150) NULL,
        [InsertRow_id] [bigint] NOT NULL
)

CREATE TABLE MedicinImporterStatus (
    Id BIGINT NOT NULL IDENTITY PRIMARY KEY,
    Type VARCHAR(20),
    StartTime DATETIME NOT NULL,
    EndTime DATETIME,
    Outcome VARCHAR(20),
    ErrorMessage VARCHAR(200)
)