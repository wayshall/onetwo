package org.onetwo.project.batch;

import org.onetwo.project.batch.entity.UserVo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class UserFieldMapper implements FieldSetMapper<UserVo>{

	public UserVo mapFieldSet(FieldSet field) throws BindException {
		UserVo user = new UserVo();
		user.setUserName(field.readString("user_name"));
		user.setAge(field.readInt("age"));
		user.setEmail(field.readString("email"));
		return user;
	}
	
	

}
