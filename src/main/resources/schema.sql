-- Declaration
    -- Organization table creation
    create table if not exists organization (
        id          bigint primary key,
        name        varchar(50),
        fullname    varchar(255),
        inn         varchar(12),
        kpp         varchar(9),
        address     varchar(255),
        phone       varchar(15),
        active      boolean
    );

    -- Office table creation
    create table if not exists office (
        id          bigint primary key,
        name        varchar(50),
        address     varchar(255),
        phone       varchar(15),
        active      boolean,
        org_id      bigint,
        foreign key (org_id) references organization(id)
    );

    -- Document table creation
    create table if not exists document (
        code        tinyint primary key,
        name        varchar(50)
    );

    -- Citizenship table creation
    create table if not exists citizenship (
        code        smallint primary key,
        name        varchar(50)
    );

    -- Person table creation
    create table if not exists person (
        id          bigint primary key,
        firstname   varchar(50),
        secondname  varchar(50),
        middlename  varchar(50),
        position    varchar(50),
        phone       varchar(15),
        docdate     date,
        identified  boolean,
        off_id      bigint,
        doc_id      tinyint,
        cs_id       smallint,
        foreign key (off_id) references office(id),
        foreign key (doc_id) references document(code),
        foreign key (cs_id) references citizenship(code)
    );

-- Initialization
    -- Organization table insertion
    insert into organization values(1, 'IC', 'International Communications', '153649503564', '345465792', '16a, Some str., California, USA', '105528657434566', true);
    insert into organization values(2, 'IC', 'International Communications the Second', '645645988021', '564235234', '17/2, Some str., California, USA', '105252352352353', true);
    insert into organization values(3, 'IC', 'International Communications', '153649503564', '131204325', '16a, Some str., California, USA', '105528657434566', false);
    insert into organization values(4, 'S-MVC', 'Spring MVC', '543630193503', '542938501', '10, Moscow str., Saratov, Russia', '79271072539', true);

    -- Office table insertion
    insert into office values(1, 'IC-24', '15, Some str., California, USA', '105528657434565', true, 1);
    insert into office values(2, 'IC-23', '15, Some str., California, USA', '105528657434565', false, 1);
    insert into office values(3, 'Central Office IC', '16, Some str., California, USA', '105528657434566', true, 1);
    insert into office values(4, 'IC#2 New Office', '10, Some str., California, USA', '105528657434565', true, 2);
    insert into office values(5, 'Head Office', '17/2, Some str., California, USA', '105252352352353', true, 2);
    insert into office values(6, 'IC#2 New Office', '10, Some str., California, USA', '105528657434566', false, 3);
    insert into office values(7, 'Head Office', '17/2, Some str., California, USA', '105364920012358', false, 3);
    insert into office values(8, 'S-MVC Head Office', '10, Moscow str., Saratov, Russia', '79271072539', true, 4);
    insert into office values(9, 'Office on Kirov Street', '48, Kirov str., Saratov, Russia', '79271075372', true, 4);

    -- Document table insertion
    insert into document values(3, 'Birth certificate');
    insert into document values(7, 'Military card');
    insert into document values(8, 'Temporary certificate (military card)');
    insert into document values(10, 'Foreign citizen passport');
    insert into document values(11, 'Refugee petition consideration certificate');
    insert into document values(12, 'Residence permit');
    insert into document values(13, 'Refugee certificate');
    insert into document values(15, 'Temporary residence permission');
    insert into document values(18, 'Temporary refuge certificate');
    insert into document values(21, 'Russian Federation passport');
    insert into document values(23, 'Another country birth certificate');
    insert into document values(24, 'Russian Federation serviceman`s identity card');
    insert into document values(91, 'Other documents');

    -- Citizenship table insertion
    insert into citizenship values(643, 'Russian Federation');
    insert into citizenship values(674, 'San Marino');
    insert into citizenship values(392, 'Japan');
    insert into citizenship values(840, 'United States of America');
    insert into citizenship values(826, 'United Kingdom');

    -- Person table insertion
    insert into person values(1, 'Alexandr', 'Fokin', 'Andreevich', 'What', '79271077950', '2017-03-20', true, 1, 21, 643);
    insert into person values(2, 'Alexandr', 'Serbin', 'Sergeevich', 'Does', '79271077950', '2005-01-01', true, 2, 10, 840);
    insert into person values(3, 'Dmitry', 'Orlov', 'Urievich', 'Position', '79271077950', '2010-09-14', true, 2, 91, 674);
    insert into person values(4, 'John', 'Feliks', 'Cena', 'Means?', '105237100584927', '1990-09-09', true, 2, 23, 392);
    insert into person values(5, 'Anthony', 'Conor', 'McGregor', 'Middle', '105237196774109', '2018-06-30', false, 3, 18, 826);