package org.onetwo.common.excel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.utils.CommonBizUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CardUserInfoGeneratorTest {
	private String outputPath = "org/onetwo/common/excel/";
	
	private String batchCode = DateUtil.formatDateByPattern(new Date(), "yyMM");
	private String boxCode = DateUtil.formatDateByPattern(new Date(), "ddHH");
	private String packageCode = boxCode+"-"+DateUtil.formatDateByPattern(new Date(), "mm");

	private String province = "广东";
	private String birthYear = "1984";
	private int generatedCount = 90;
	private Set<String> generateIdCardList = Sets.newHashSetWithExpectedSize(generatedCount);
	
	@Test
	public void testGenerateUserInfo(){
		String path = FileUtils.getResourcePath(outputPath)+"card_userinfo_generate_template.xls";
		String templatePath = "org/onetwo/common/excel/card_userinfo_generate_template.xml";
		Map<String, Object> context = Maps.newHashMap();
		context.put("cardUserList", createUserList(generatedCount, "广楚天", "广州天河区", "黄埔大道中", "伟诚商务大厦"));
//		context.put("cardUserList", createUserList(generatedCount, "清楚天", "清城区", "凤鸣路", "和富广场"));
		TemplateGenerator g = DefaultExcelGeneratorFactory.createWorkbookGenerator(templatePath, context);
		g.generateTo(path);
	}
	
	private List<CardUserInfo> createUserList(int count, String name, String countyName, String townName, String villageName){
		List<CardUserInfo> userList = Lists.newArrayListWithCapacity(count);
		for (int i = 1; i <= count; i++) {
			CardUserInfo user = createCardUserInfo(i, name, countyName, townName, villageName);
			userList.add(user);
		}
		return userList;
	}
	
	private CardUserInfo createCardUserInfo(int i, String name, String countyName, String townName, String villageName){
		String postfix = ""+i;
		CardUserInfo userInfo = new CardUserInfo();
		userInfo.setId(Long.valueOf(i));
		userInfo.setName(name+postfix);
		String idCard = CommonBizUtils.generatedIdCardNo(province, birthYear);
		while(generateIdCardList.contains(idCard)){
			System.out.println("idCard["+idCard+"] is exist, generated again!");
			idCard = CommonBizUtils.generatedIdCardNo(province, birthYear);
		}
		userInfo.setIdCard(idCard);
		userInfo.setBatchCode(batchCode);
		userInfo.setBoxCode(boxCode);
		userInfo.setPackageCode(packageCode);
		userInfo.setCountyName(countyName+postfix);
		userInfo.setTownName(townName+postfix);
		userInfo.setVillageName(villageName+postfix);
		return userInfo;
	}

}
