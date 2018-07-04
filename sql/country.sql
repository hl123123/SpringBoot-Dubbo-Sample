create table country
(
  id        integer     not null  constraint country_pkey primary key,
  name      varchar(50) not null,
  name_en   varchar(100),
  continent varchar(50)
);

comment on table country
is '国家表';

create unique index country_id_uindex
  on country (id);

insert into country (id, name, name_en, continent) values (1, '中华人民共和国', 'Republic of China', '亚洲')
insert into country (id, name, name_en, continent) values (2, '大不列颠及北爱尔兰联合王国', 'United Kingdom of Great Britain and Northern Ireland', '欧洲')
insert into country (id, name, name_en, continent) values (3, '德意志联邦共和国', 'Federal Republic of Germany', '欧洲')
insert into country (id, name, name_en, continent) values (4, '朝鲜民主主义人民共和国', 'Democratic People''s Republic of Ko-rea', '亚洲')
insert into country (id, name, name_en, continent) values (5, '法兰西共和国', 'French Republic', '欧洲')