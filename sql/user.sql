create table users
(
  id         serial       not null  constraint user_pkey  primary key,
  account    varchar(255) not null,
  password   varchar(255) not null,
  user_state integer      not null
);

comment on table users
is '用户表';

comment on column users.id
is '主键id';

comment on column users.account
is '用户名';

comment on column users.password
is '密码';

comment on column users.user_state
is '状态';

