drop table if exists data_rmq_send;

/*==============================================================*/
/* Table: data_rmq_send                                         */
/*==============================================================*/
create table data_rmq_send
(
   msgkey               varchar(64) not null,
   body                 blob,
   state                tinyint comment '消息状态：
            待发送：0
            已发送：1
            正常来说只有待发送，因为发送后一般不保留，直接删掉',
   create_at            datetime,
   update_at            datetime,
   primary key (msgkey)
);

alter table data_rmq_send comment 'rmq消息发送暂存表';
