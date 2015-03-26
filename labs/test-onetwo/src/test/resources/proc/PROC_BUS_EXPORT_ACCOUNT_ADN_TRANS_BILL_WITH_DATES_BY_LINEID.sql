USE [iccard]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[PROC_BUS_EXPORT_ACCOUNT_ADN_TRANS_BILL_WITH_DATES_BY_LINEID] 
(
	@lineid varchar(20)
)
AS
begin

	declare @bus_id varchar(20);
	declare @sql varchar(4000);

	DECLARE cursor_rs CURSOR SCROLL FOR 
	select busno from BASE_BUS bb where bb.lineid=@lineid and isdelete=0

	open cursor_rs;

	fetch next from cursor_rs into @bus_id;
	while(@@FETCH_STATUS=0)
		begin
			set @sql='bcp "exec dbo.PROC_BUS_FIND_BUS_ACCOUNT_ADN_TRANS_BILL_WITH_DATES '+@bus_id+'" queryout D:\busdata\'+@bus_id+'.xls -c -q -S"127.0.0.1" -U"iccarduser@web" -P"2014db@server@qyscard"'

			EXEC master..xp_cmdshell @sql

			fetch next from cursor_rs into @bus_id;
		end

	CLOSE cursor_rs;
	DEALLOCATE cursor_rs;
end



