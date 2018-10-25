drop table if exists data_mq_receive;

/*==============================================================*/
/* Table: data_mq_receive                                       */
/*==============================================================*/
create table data_mq_receive
(
   id                   bigint not null,
   msgkey               varchar(64) not null,
   raw_msgid            varchar(128) not null comment '消息原始id',
   consume_group        varchar(64) not null,
   state                tinyint not null default 1 comment '消息状态：
            已消费：1
            ',
   create_at            datetime not null,
   update_at            datetime not null,
   primary key (id),
   unique key AK_key_data_rmq_rec_grp_key (msgkey, consume_group)
);

alter table data_mq_receive comment 'rmq消息接收记录表';
