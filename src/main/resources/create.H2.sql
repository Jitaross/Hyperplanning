DROP TABLE IF EXISTS PERSONNE CASCADE ;
create table PERSONNE
(
    ID     LONG auto_increment,
    NOM    VARCHAR not null,
    PRENOM VARCHAR not null
);

alter table PERSONNE
    add constraint PERSONNE_PK
        primary key (ID);

create unique index PERSONNE_ID_UINDEX
    on PERSONNE (ID);

DROP TABLE IF EXISTS CHIEN;
create table CHIEN
(
    ID        LONG auto_increment,
    NOM       VARCHAR not null,
    MAITRE_ID LONG,
    constraint CHIEN_PERSONNE_ID_FK
        foreign key (MAITRE_ID) references PERSONNE (ID)
            on delete set null
);

create unique index CHIEN_ID_UINDEX
    on CHIEN (ID);

alter table CHIEN
    add constraint CHIEN_PK
        primary key (ID);



