create table FORMATION
(
    ID           INTEGER auto_increment
        primary key,
    NOMFORMATION CHARACTER VARYING(255)
);

create table MATIERE
(
    ID          INTEGER auto_increment
        primary key,
    NOMMATIERE  CHARACTER VARYING(255),
    IDFORMATION INTEGER,
    constraint FK_IDFORMATION_MATIERE
        foreign key (IDFORMATION) references FORMATION
);

create table SALLE
(
    ID          INTEGER auto_increment
        primary key,
    NOMBREPLACE INTEGER,
    NOMSALLE    CHARACTER VARYING(100)
);

create table UTILISATEUR
(
    ID              INTEGER auto_increment
        primary key,
    NOM             CHARACTER VARYING(255),
    PRENOM          CHARACTER VARYING(255),
    MAIL            CHARACTER VARYING(255),
    DATENAISSANCE   DATE,
    PASSWORD        CHARACTER VARYING(1000),
    TYPEUTILISATEUR CHARACTER VARYING(20)
);

create table COURS
(
    ID           INTEGER auto_increment
        primary key,
    DESCRIPTION  CHARACTER VARYING(255),
    IDENSEIGNANT INTEGER not null,
    IDMATIERE    INTEGER,
    IDSALLE      INTEGER,
    DEBUT        TIME,
    FIN          TIME,
    DATE         DATE,
    TYPECOURS    INTEGER,
    constraint FK_COURS_ENSEIGNANT
        foreign key (IDENSEIGNANT) references UTILISATEUR,
    constraint FK_COURS_MATIERE
        foreign key (IDMATIERE) references MATIERE,
    constraint FK_COURS_SALLE
        foreign key (IDSALLE) references SALLE
);

create table DEVOIR
(
    ID          INTEGER auto_increment,
    NOTE        DOUBLE PRECISION not null,
    COMMENTAIRE CHARACTER VARYING(200),
    TYPEDEVOIR  CHARACTER VARYING(10),
    IDUSER      INTEGER,
    NOMMATIERE  CHARACTER VARYING(50),
    constraint "DEVOIR_pk"
        primary key (ID),
    constraint "DEVOIR_UTILISATEUR_ID_fk"
        foreign key (IDUSER) references UTILISATEUR
);

create table ENSEIGNANT
(
    ID  INTEGER not null
        primary key
        references UTILISATEUR,
    UFR CHARACTER VARYING(255)
);

create table ETUDIANT
(
    ID          INTEGER not null
        primary key
        references UTILISATEUR,
    IDFORMATION INTEGER
        references FORMATION
);

create table ABSENCE
(
    ID           INTEGER auto_increment
        primary key,
    IDCOURS      INTEGER
        references COURS,
    IDETUDIANT   INTEGER
        references ETUDIANT,
    JUSTIFICATIF BINARY LARGE OBJECT,
    MOTIF        CHARACTER VARYING(1000000000)
);