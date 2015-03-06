package org.onetwo.common.guava;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.TestUtils;

import test.entity.UserEntity;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class SetTest {

	@Test
	public void test() {
		HashSet setA = Sets.newHashSet(1, 2, 3, 4, 5);
		HashSet setB = Sets.newHashSet(4, 5, 6, 7, 8);

		SetView<Integer> union = Sets.union(setA, setB);
		System.out.println("union:");
		for (Integer integer : union)
			System.out.println(integer);

		SetView<Integer> difference = Sets.difference(setA, setB);
		System.out.println("a difference b:");
		for (Integer integer : difference)
			System.out.println(integer);

		difference = Sets.difference(setB, setA);
		System.out.println("b difference a:");
		for (Integer integer : difference)
			System.out.println(integer);

		SetView<Integer> intersection = Sets.intersection(setA, setB);
		System.out.println("intersection:");
		for (Integer integer : intersection)
			System.out.println(integer);
	}

	@Test
	public void testFilter() {
		List<UserEntity> users = TestUtils.createUserList("users", 3);
		Collection<UserEntity> filterUsers = Collections2.filter(users, new Predicate<UserEntity>() {

			@Override
			public boolean apply(UserEntity input) {
				return input.getAge()>1;
			}
			
		});
		Assert.assertEquals(1, filterUsers.size());
	}
}
