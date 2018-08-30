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