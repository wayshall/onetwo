USE [ICCARD]
GO
/****** Object:  StoredProcedure [dbo].[PROC_SIGN_IN]    Script Date: 01/13/2014 11:12:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--POS终端签到
ALTER PROCEDURE [dbo].[PROC_SIGN_IN](
       --输入参数
       @IN_PID VARCHAR(20),			--终端机编号
       @IN_CBLACK_VER VARCHAR(8),	--终端黑名单版本
       @IN_TIM CHAR(14),			--终端日期时间
       @IN_APP_VERSION	VARCHAR(16),
       --输出参数
       @OUT_RET CHAR(4) OUTPUT,		--返回码 ‘0000’成功  其他值失败
       @OUT_PID CHAR(12) OUTPUT,	--终端机编号
       @OUT_TIM CHAR(14) OUTPUT,	--服务器时间
       @OUT_SBLACK_VER VARCHAR(8) OUTPUT,	--服务器黑名单版本
       @OUT_MERCHANT   VARCHAR(40) OUTPUT	--商户名称
) AS 
BEGIN 
  BEGIN TRY
  DECLARE 
		  @V_PID VARCHAR(20),
		  @MAX_VER BIGINT,		--定义@MAX_VER：最高黑名单版本
		  @V_PRODUCT_NUM VARCHAR(20)='';--旅游商品编号
	  SET @OUT_RET='0000';--默认成功
	  SET @OUT_MERCHANT='';
	  SET @OUT_SBLACK_VER=@IN_CBLACK_VER;	--初始化@OUT_SBLACK_VER
	  SET @OUT_PID=@IN_PID;		--定义输出终端机编号等于输入终端机编号
	  SET @OUT_TIM=DBO.DATETIMETOSTR(); 
	  
	  --根据pos编号查询出商户ID，以得到商户名称
	  DECLARE @V_MERCHANT_ID BIGINT=0;
	  IF(CAST(@IN_PID AS BIGINT) >= 100000000)
		BEGIN
			SET @V_PID='00000000'+@IN_PID; --充值终端号
			SELECT @V_MERCHANT_ID=MERCHANT_ID FROM TERM_RECHARGE_POS WHERE RECHARGE_POS_NO=@V_PID;
		END
	  ELSE
		BEGIN
			SET @V_PID=@IN_PID; --消费终端号
			SELECT @V_MERCHANT_ID=MERCHANT_ID FROM TERM_POS WHERE POS_NO=@V_PID; 
		END
	  SELECT @OUT_MERCHANT=NAME FROM MERCHANT_INFO WHERE ID=@V_MERCHANT_ID;
	  
	  --未找到商户  返回‘1000’ 失败
	  IF(@OUT_MERCHANT IS NULL OR @OUT_MERCHANT='')
		BEGIN
			 SET @OUT_RET='FF03';
			 RETURN -1;
		END
	  --找到商户 对商户表LAST_UPDATE_TIME更新
	  ELSE
		BEGIN
			--zws 2014-05-25:去掉这句毫无意义的update
		  --UPDATE TERM_POS SET LAST_UPDATE_TIME=SYSDATETIME() WHERE TERM_POS.POS_NO=@V_PID;
		  --查询商户绑定的商品,若没有则设为''
		  SELECT @V_PRODUCT_NUM=@V_PRODUCT_NUM+PRODUCT_NUM FROM TRAVEL_PRODUCT WHERE MERCHANT_ID = @V_MERCHANT_ID;
			IF(@V_PRODUCT_NUM IS NULL)
				SET @V_PRODUCT_NUM='';	  
		  
		  SELECT @MAX_VER=MAX(VERSION_ID) FROM CLIENT_BLACK_LIST;	--查询出最高黑名单版本 赋给@MAX_VER
		  IF(@MAX_VER=@IN_CBLACK_VER OR @MAX_VER IS NULL)	--如果终端黑名单版本已是最高版本或最高版本为空
			BEGIN											--返回服务器黑名单版本=终端黑名单版本
				SET @OUT_SBLACK_VER=@IN_CBLACK_VER;
			END
		  ELSE IF(@MAX_VER>@IN_CBLACK_VER)		--终端黑名单版本小于终端黑名单版本
			BEGIN
				SET @OUT_SBLACK_VER=@MAX_VER;
			END;
		  
		  --记录终端黑名单版本号和数据库最高版本号到term_pos_blacklist_ver
		  BEGIN TRANSACTION
			 --对商户表LAST_UPDATE_TIME更新
				--zws 2014-05-25:去掉这句update
			  --UPDATE TERM_POS SET LAST_UPDATE_TIME=SYSDATETIME() WHERE TERM_POS.POS_NO=@V_PID;
			  --记录终端黑名单版本号和数据库最高版本号到term_pos_blacklist_ver
			  DECLARE @V_APP_VERSION VARCHAR(16)='';
			  --获取表中该终端程序版本号
			  SELECT @V_APP_VERSION=pos_app_version FROM term_pos_blacklist_ver WHERE PID=@IN_PID;
			  --程序未更新
			  IF(@V_APP_VERSION = @IN_APP_VERSION)
				UPDATE term_pos_blacklist_ver SET pos_version=@IN_CBLACK_VER, signin_time=SYSDATETIME(), server_version=@OUT_SBLACK_VER WHERE PID=@IN_PID;
			  --程序已升级
			  ELSE
				UPDATE term_pos_blacklist_ver SET pos_version=@IN_CBLACK_VER, signin_time=SYSDATETIME(), server_version=@OUT_SBLACK_VER, pos_app_version=@IN_APP_VERSION, app_time=SYSDATETIME() WHERE PID=@IN_PID;
			  --表中没有该终端的记录
			  IF(@@ROWCOUNT = 0)
				INSERT INTO term_pos_blacklist_ver(PID, pos_version, signin_time, server_version, pos_app_version) VALUES(@IN_PID, @IN_CBLACK_VER, SYSDATETIME(), @OUT_SBLACK_VER ,@IN_APP_VERSION);
		  COMMIT TRANSACTION;
		 
		  IF @@ERROR<>0  --更新失败 返回‘2000’
			BEGIN  
				SET @OUT_RET='FF02';
				ROLLBACK TRANSACTION;
				RETURN -2;  
			END	
		
		  --更新成功  返回‘0000’ 成功
		  SET @OUT_RET='0000';
		  --输出商户名和旅游产品编号
		  SET @OUT_MERCHANT=@OUT_MERCHANT+'\n'+@V_PRODUCT_NUM;
		  RETURN 0;
		END
	RETURN 0;
  END TRY
  BEGIN CATCH    --异常捕获 返回‘3000’ 失败
	  IF @@TRANCOUNT<>0
	  BEGIN
		ROLLBACK TRANSACTION;
	  END
	  SET @OUT_RET='FF01';
	  RETURN -3;
  END CATCH
END;
