package org.onetwo.common.dbm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.onetwo.common.dbm.model.dao.CompanyDao;
import org.onetwo.common.dbm.model.entity.CompanyEntity;
import org.onetwo.common.dbm.model.entity.DepartmentEntity;
import org.onetwo.common.dbm.model.entity.EmployeeEntity;
import org.onetwo.common.dbm.model.entity.EmployeeEntity.EmployeeGenders;
import org.onetwo.common.dbm.model.vo.CompanyVO;
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
				assertThat(employee.getDepartmentId()).isEqualTo(depart.getId());
			});
		});
		

		departments = companyDao.findNestedDepartmentsWithEmployeeId();
		assertThat(departments.size()).isEqualTo(100);
		departments.stream().forEach(depart->{
			assertThat(depart.getCompany()).isNotNull();
			assertThat(depart.getCompany().getId()).isEqualTo(depart.getCompanyId());

			assertThat(depart.getEmployees()).isNotNull();
			depart.getEmployees().stream().forEach(employee->{
				assertThat(employee.getDepartmentId()).isEqualTo(depart.getId());
			});
		});
		
		List<CompanyVO> companies = companyDao.findNestedCompanies();
		assertThat(companies.size()).isEqualTo(10);
		companies.stream().forEach(company->{
			assertThat(company.getDepartments()).isNotNull();
			assertThat(company.getDepartments().size()).isEqualTo(10);
			
			company.getDepartments().stream().forEach(depart->{
//				assertThat(depart.getCompany()).isNotNull();
				assertThat(company.getId()).isEqualTo(depart.getCompanyId());

				assertThat(depart.getEmployees()).isNotNull();
				assertThat(depart.getEmployees().size()).isEqualTo(10);
				depart.getEmployees().stream().forEach(employee->{
					assertThat(employee.getDepartmentId()).isEqualTo(depart.getId());
				});
			});
		});
		

		LangOps.timeIt("findNestedDepartmentsWithEmployeeId", 1, ()->{
			companyDao.findNestedDepartmentsWithEmployeeId();
		});
		LangOps.timeIt("findNestedDepartments", 1, ()->{
			companyDao.findNestedDepartments();
		});
	}
	

	@Test
	public void test3findMap(){
		List<CompanyVO> companies = companyDao.findNestedCompaniesWithDepartmentMap();
		assertThat(companies.size()).isEqualTo(10);
		companies.stream().forEach(company->{
			assertThat(company.getDepartments()).isNull();
			assertThat(company.getDepartmentMap()).isNotNull();
			assertThat(company.getDepartmentMap().size()).isEqualTo(10);
			
			Map<Long, DepartmentVO> departMap = company.getDepartmentMap();
			departMap.entrySet().stream().forEach(entry->{
				DepartmentVO depart = entry.getValue();
//				assertThat(depart.getCompany()).isNotNull();
				assertThat(depart.getId()).isEqualTo(entry.getKey());
				assertThat(company.getId()).isEqualTo(depart.getCompanyId());

				assertThat(depart.getEmployees()).isNotNull();
				assertThat(depart.getEmployees().size()).isEqualTo(10);
				depart.getEmployees().stream().forEach(employee->{
					assertThat(employee.getDepartmentId()).isEqualTo(depart.getId());
				});
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
