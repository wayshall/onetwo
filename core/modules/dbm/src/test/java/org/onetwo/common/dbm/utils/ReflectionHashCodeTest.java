package org.onetwo.common.dbm.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.onetwo.common.dbm.model.entity.DepartmentEntity;

public class ReflectionHashCodeTest {


	@Test
	public void testHashCode(){
		DepartmentEntity depart1 = createDepartment(1L, 1);
		DepartmentEntity depart2 = createDepartment(1L, 1);
		Integer hc1 = HashCodeBuilder.reflectionHashCode(depart1);
		Integer hc2 = HashCodeBuilder.reflectionHashCode(depart2);
		assertThat(hc1).isEqualTo(hc2);
	}

	public DepartmentEntity createDepartment(Long companyId, int index){
		DepartmentEntity department = new DepartmentEntity();
		department.setName("部门-"+index);
		department.setEmployeeNumber(10);
		department.setCompanyId(companyId);
		return department;
	}

}
