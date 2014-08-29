if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_QUEUE')
            and   type = 'U')
   drop table TASK_QUEUE
go

/*==============================================================*/
/* Table: TASK_QUEUE                                            */
/*==============================================================*/
create table TASK_QUEUE (
   ID                   BIGINT               not null,
   STATUS               VARCHAR(20)          null,
   CURRENT_TIMES        INT                  null,
   TRY_TIMES            INT                  null,
   PLAN_TIME            DATETIME             null,
   TASK_CREATE_TIME     DATETIME             null,
   TASK_ID              BIGINT               null,
   LAST_EXEC_TIME       DATETIME             null,
   LAST_UPDATE_TIME     DATETIME             null,
   constraint PK_TASK_QUEUE primary key (ID)
)
go


if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_BASE')
            and   type = 'U')
   drop table TASK_BASE
go

/*==============================================================*/
/* Table: TASK_BASE                                             */
/*==============================================================*/
create table TASK_BASE (
   ID                   BIGINT               not null,
   NAME                 VARCHAR(100)         null,
   TYPE                 VARCHAR(20)          null,
   BIZ_TAG_ID           BIGINT               null,
   constraint PK_TASK_BASE primary key (ID)
)
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_EMAIL')
            and   type = 'U')
   drop table TASK_EMAIL
go

/*==============================================================*/
/* Table: TASK_EMAIL                                            */
/*==============================================================*/
create table TASK_EMAIL (
   ID                   BIGINT               not null,
   SUBJECT              VARCHAR(100)         null,
   CONTENT              TEXT                 null,
   ATTACHMENT_PATH      VARCHAR(2000)        null,
   IS_HTML              INT                  null,
   constraint PK_TASK_EMAIL primary key (ID)
)
go



if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_BIZ_TYPE')
            and   type = 'U')
   drop table TASK_BIZ_TYPE
go

/*==============================================================*/
/* Table: TASK_BIZ_TYPE                                         */
/*==============================================================*/
create table TASK_BIZ_TYPE (
   ID                   bigint               not null,
   NAME                 varchar(50)          null,
   constraint PK_TASK_BIZ_TYPE primary key (ID)
)
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_BIZ_TAG')
            and   type = 'U')
   drop table TASK_BIZ_TAG
go

/*==============================================================*/
/* Table: TASK_BIZ_TAG                                          */
/*==============================================================*/
create table TASK_BIZ_TAG (
   ID                   bigint               not null,
   NAME                 varchar(50)          null,
   constraint PK_TASK_BIZ_TAG primary key (ID)
)
go


if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_QUEUE_ARCHIVED')
            and   type = 'U')
   drop table TASK_QUEUE_ARCHIVED
go

/*==============================================================*/
/* Table: TASK_QUEUE_ARCHIVED                                   */
/*==============================================================*/
create table TASK_QUEUE_ARCHIVED (
   ID                   BIGINT               not null,
   RESULT               VARCHAR(20)          null,
   CURRENT_TIMES        INT                  null,
   TRY_TIMES            INT                  null,
   PLAN_TIME            DATETIME             null,
   TASK_CREATE_TIME     DATETIME             null,
   TASK_ID              BIGINT               null,
   EXECUTOR             VARCHAR(20)          null,
   ARCHIVED_TIME        DATETIME             null,
   LAST_EXEC_TIME       DATETIME             null,
   constraint PK_TASK_QUEUE_ARCHIVED primary key (ID)
)
go


if exists (select 1
            from  sysobjects
           where  id = object_id('TASK_EXEC_LOG')
            and   type = 'U')
   drop table TASK_EXEC_LOG
go

/*==============================================================*/
/* Table: TASK_EXEC_LOG                                         */
/*==============================================================*/
create table TASK_EXEC_LOG (
   ID                   BIGINT               not null,
   TASK_QUEUE_ID        BIGINT               null,
   RESULT               VARCHAR(20)          null,
   EXECUTE_TIME         DATETIME             null,
   EXECUTOR             VARCHAR(20)          null,
   COST_TIME            INT                  null,
   TASK_INPUT           VARCHAR(2000)        null,
   TASK_OUPUT           VARCHAR(2000)        null,
   constraint PK_TASK_EXEC_LOG primary key (ID)
)
go
