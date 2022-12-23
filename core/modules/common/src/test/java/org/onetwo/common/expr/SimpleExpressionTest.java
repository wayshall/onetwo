package org.onetwo.common.expr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.convert.Types;

import com.google.common.collect.Lists;

public class SimpleExpressionTest {

	@Test
	public void test() {
		List<String> varNames = Lists.newArrayList("100", "20", "5");
		Expression epr = ExpressionFacotry.BRACE;
		String res = epr.parse("{0}/{1}={2}", var -> {
			int idx = Types.asInteger(var);
			return varNames.get(idx);
		});
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("100/20=5");
	}

}
