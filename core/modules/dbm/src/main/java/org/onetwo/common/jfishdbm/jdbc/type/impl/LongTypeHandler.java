package org.onetwo.common.jfishdbm.jdbc.type.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.onetwo.common.jfishdbm.jdbc.type.AbstractTypeHandler;

public class LongTypeHandler extends AbstractTypeHandler<Long> {

	public LongTypeHandler() {
		super(Arrays.asList(Long.class, long.class));
	}


}
