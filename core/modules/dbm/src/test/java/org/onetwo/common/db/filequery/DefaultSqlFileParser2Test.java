package org.onetwo.common.db.filequery;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl.CommonNamespaceProperties;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager.DialetNamedSqlConf;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.propconf.ResourceAdapter;

public class DefaultSqlFileParser2Test {
	
	@Test
	public void test(){
		String fileName = FileUtils.getResourcePath("sql/org.onetwo.common.jfishdbm.model.dao.UserAutoidDao.jfish.sql");
		List<String> lines = FileUtils.readAsList(fileName);
		System.out.println("line:"+lines);
		DefaultSqlFileParser2<JFishNamedFileQueryInfo> parser = new DefaultSqlFileParser2<>();
		ResourceAdapter<File> f = FileUtils.adapterResource(new File(fileName));
		
		DialetNamedSqlConf<JFishNamedFileQueryInfo> conf = new DialetNamedSqlConf<JFishNamedFileQueryInfo>(){
			{
				setDatabaseType(DataBase.of("mysql"));
//				setPostfix(SQL_POSTFIX);
				setWatchSqlFile(false);
				setPropertyBeanClass(JFishNamedFileQueryInfo.class);
			}
		};
		
		CommonNamespaceProperties<JFishNamedFileQueryInfo> np = new CommonNamespaceProperties<>("org.onetwo.common.jfishdbm.model.dao.UserAutoidDao");
		parser.parseToNamespaceProperty(conf, np, f);
		Assert.assertEquals(3, np.getNamedProperties().size());
		Assert.assertEquals("insert into test_user_autoid (birthday, email, gender, mobile, nick_name, password, status, user_name) values (:birthday, :email, :gender, :mobile, :nickName, :password, :status, :userName) ", np.getNamedProperty("batchInsert").getValue());
		Assert.assertEquals("insert into test_user_autoid (birthday, email, gender, mobile, nick_name, password, status, user_name) values (:allBirthday, :email, :gender, :mobile, :nickName, :password, :status, :userName) ", np.getNamedProperty("batchInsert2").getValue());
		Assert.assertEquals("delete from test_user_autoid [#if userName?has_content] where user_name like :userName?likeString [/#if] ", np.getNamedProperty("removeByUserName").getValue());
	}

}
