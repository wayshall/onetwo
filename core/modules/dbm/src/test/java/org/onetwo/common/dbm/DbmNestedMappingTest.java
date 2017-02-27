package org.onetwo.common.dbm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.onetwo.common.dbm.model.dao.CompanyDao;
import org.onetwo.common.dbm.model.entity.CompanyEntity;
import org.onetwo.common.dbm.model.entity.DepartmentEntity;
import org.onetwo.common.dbm.model.entity.EmployeeEntity;
import org.onetwo.common.dbm.model.entity.EmployeeEntity.EmployeeGenders;
import org.onetwo.common.dbm.model.vo.DepartmentVO;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.dbm.support.DbmEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

@Rollback(false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbmNestedMappingTest extends DbmBaseTest {

	@Resource
	private DbmEntityManager dbmEntityManager;
	@Autowired
	private CompanyDao companyDao;
	
	public void clear(){
		dbmEntityManager.removeAll(EmployeeEntity.class);
		dbmEntityManager.removeAll(DepartmentEntity.class);
		dbmEntityManager.removeAll(CompanyEntity.class);
	}
	
	@Test
	public void test1Saves(){
		this.clear();
		
		List<CompanyEntity> companies = LangOps.ntimesMap(10, i->{
			return createCompany(i);
		});
		dbmEntityManager.save(companies);
		
		List<CompanyEntity> dbcompanies = dbmEntityManager.findAll(CompanyEntity.class);
		assertThat(dbcompanies.size()).isEqualTo(companies.size());
		Collection<String> names = ReflectUtils.getProperties(companies, "name");
		Collection<String> dbnames = ReflectUtils.getProperties(dbcompanies, "name");
		assertThat(dbnames).containsAll(names);
		
//		List<CompanyVO> nestedCompanies = companyDao.findNestedCompanies();
	}
	

	@Test
	public void test2find(){
		List<DepartmentVO> departments = companyDao.findDepartmentsWithComapny();
		assertThat(departments.size()).isEqualTo(100);
		departments.stream().forEach(depart->{
			assertThat(depart.getCompany()).isNotNull();
			assertThat(depart.getCompany().getId()).isEqualTo(depart.getCompanyId());
		});
		

		departments = companyDao.findNestedDepartments();
		assertThat(departments.size()).isEqualTo(100);
		departments.stream().forEach(depart->{
			assertThat(depart.getCompany()).isNotNull();
			assertThat(depart.getCompany().getId()).isEqualTo(depart.getCompanyId());

			assertThat(depart.getEmployees()).isNotNull();
			depart.getEmployees().stream().forEach(employee->{
				if(depart.getId()==null || employee.getDepartmentId()==null){
					System.out.println("test");
				}
				assertThat(employee.getDepartmentId()).isEqualTo(depart.getId());
			});
		});
	}
	
	public CompanyEntity createCompany(int index){
		int employeeNumber = 10;
		CompanyEntity company = new CompanyEntity();
		company.setName("测试公司-"+index);
		company.setEmployeeNumber(employeeNumber);
		company.setDescription("一个测试公司-"+index);
		dbmEntityManager.save(company);
		
		LangOps.ntimesMap(10, i->{
			return createDepartment(company.getId(), i);
		});
		return company;
	}
	
	public DepartmentEntity createDepartment(Long companyId, int index){
		DepartmentEntity department = new DepartmentEntity();
		department.setName("部门-"+index);
		department.setEmployeeNumber(10);
		department.setCompanyId(companyId);
		dbmEntityManager.save(department);
		List<EmployeeEntity> employees = LangOps.ntimesMap(10, i->{
			return createEmployee(department.getId(), i);
		});
		dbmEntityManager.saves(employees);
		return department;
	}
	
	public EmployeeEntity createEmployee(Long departmentId, int index){
		EmployeeEntity employee = new EmployeeEntity();
		employee.setName("员工-"+index);
		employee.setBirthday(new Date());
		employee.setDepartmentId(departmentId);
		employee.setJoinDate(new Date());
		employee.setGender(EmployeeGenders.MALE);
		return employee;
	}
	
}
