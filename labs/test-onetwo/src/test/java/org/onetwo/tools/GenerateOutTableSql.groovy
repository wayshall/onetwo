package org.onetwo.tools

import groovy.time.TimeCategory


Date startDate = Date.parse("yyyyMM", "201501");
Date endDate = Date.parse("yyyyMM", "201512");
def sqlList = [];
use(TimeCategory){
	while(startDate<=endDate){
		ym = startDate.format("yyyyMM");
		//双引号才是gstring，单引号是java的string
		sql = """

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[out_transaction_${ym}](
	[id] [int] NOT NULL,
	[pid] [varchar](16) NULL,
	[psn] [int] NULL,
	[tim] [datetime] NULL,
	[lcn] [varchar](30) NULL
) ON [PRIMARY]
SET ANSI_PADDING OFF
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [fcn] [varchar](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [tf] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [fee] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [bal] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [tt] [char](2) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [att] [char](2) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [crn] [varchar](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [xrn] [varchar](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [epid] [char](12) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [etim] [datetime] NULL
SET ANSI_PADDING ON
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [lpid] [varchar](16) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [ltim] [datetime] NULL
SET ANSI_PADDING OFF
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [tac] [char](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [bbal] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [opt] [varchar](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [line] [bigint] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [qs] [char](2) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [rfu] [varchar](80) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [area_code] [char](4) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [ltf] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [ltt] [char](2) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [status] [char](2) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [pron] [char](4) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [mactac] [char](8) NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [rcbefore] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [rcafter] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [upload_tim] [datetime] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [settle_tim] [datetime] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [check_num] [int] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [bus_id] [bigint] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [card_type] [bigint] NULL
ALTER TABLE [dbo].[out_transaction_${ym}] ADD [CSTACCFC] [bigint] NULL
/****** Object:  Index [PK__in_trans__3213E83F2F277119]    Script Date: 01/05/2015 16:46:25 ******/
ALTER TABLE [dbo].[out_transaction_${ym}] ADD PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO
			"""
		sqlList += sql
		startDate = startDate + 1.month;
	}
}
def viewSql = "use iccard; \n" + sqlList.join("\n \n")
println viewSql