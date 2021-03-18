package org.onetwo.dbm.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.onetwo.dbm.ui.entity.UserUIEntity;
import org.onetwo.dbm.ui.entity.UserUIEntity.UserStatus;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.onetwo.dbm.ui.spi.DUISelectDataProviderService;
import org.onetwo.dbm.ui.vo.EnumDataVO;
import org.onetwo.dbm.ui.vo.UISelectDataRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */

public class DbmUISimpleTest extends DUIBaseTest {
	
	@Autowired
	private DUIMetaManager uiClassMetaManager;
	@Autowired
	private DUISelectDataProviderService selectDataProviderService;
	
	@Test
	public void testUIClass() {
		DUIEntityMeta classMeta = uiClassMetaManager.get(UserUIEntity.class);
		assertThat(classMeta).isNotNull();
		assertThat(classMeta.getFields().size()).isEqualTo(3);
		DUIFieldMeta status = classMeta.getField("status");
		assertThat(status).isNotNull();
		assertThat(status.getSelect()).isNotNull();
	}

	/*@Test
	public void testUIClassWithTable() {
		UIClassMeta classMeta = uiClassMetaManager.getByTable("TEST_USER");
		assertThat(classMeta).isNotNull();
		assertThat(classMeta.getFields().size()).isEqualTo(3);
	}*/
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSelectData() {
		UISelectDataRequest request = UISelectDataRequest.builder()
													.entity("TestUser")
													.field("status")
													.build();
		List<EnumDataVO> enumDatas = (List<EnumDataVO>)selectDataProviderService.getDatas(request);
		assertThat(enumDatas.size()).isEqualTo(3);
		Optional<EnumDataVO> normal = enumDatas.stream().filter(d -> {
			return d.getValue().equals(UserStatus.NORMAL.getValue());
		}).findAny();
		assertThat(normal).isPresent();
	}
	

}
