drop index idx_data_rmq_send_sld on data_mq_send;

drop index idx_data_rmq_send_key on data_mq_send;

drop table if exists data_mq_send;

/*==============================================================*/
/* Table: data_mq_send                                          */
/*==============================================================*/
create table data_mq_send
(
   msgkey               varchar(64) not null,
   body                 blob,
   state                tinyint comment '消息状态：
            待发送：0
            已发送：1
            ',
   locker               varchar(50),
   deliver_at           datetime comment '发送时间',
   create_at            datetime,
   update_at            datetime,
   primary key (msgkey)
);

alter table data_mq_send comment 'rmq消息发送暂存表';

/*==============================================================*/
/* Index: idx_data_rmq_send_key                                 */
/*==============================================================*/
create unique index idx_data_rmq_send_key on data_mq_send
(
   msgkey
);
