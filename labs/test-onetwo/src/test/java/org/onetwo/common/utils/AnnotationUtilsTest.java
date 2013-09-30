package org.onetwo.common.utils;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.junit.Assert;
import org.junit.Test;

import test.entity.UserEntity;

public class AnnotationUtilsTest {

	@Test
	public void testContainsAny(){
		Annotation[] annos = UserEntity.class.getAnnotations();
		boolean rs = AnnotationUtils.containsAny(annos, Entity.class);
		Assert.assertTrue(rs);
		rs = AnnotationUtils.containsAny(annos, ManyToOne.class);
		Assert.assertFalse(rs);
	}
}
