package org.onetwo.common.web.s2.tag;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RowType {
	
	public static final String ROW = "row";
	public static final String HEADER = "header";
	public static final String ITERATOR = "iterator";
	public static final String PAGE = "page";
	public static final String PAGE_NUMBER = "page-number";
	public static final String TREE = "tree";
	public static final String NEW_ITERATOR = "new-iterator";

	public static final List<String> VALUES = new ArrayList<String>() {
		{
			add(ROW);
			add(HEADER);
			add(ITERATOR);
			add(PAGE);
			add(PAGE_NUMBER);
			add(TREE);
		}
	};
	
	public static boolean isHeader(String type) {
		return RowType.HEADER.equals(type);
	}

	public static boolean isIterator(String type) {
		return RowType.ITERATOR.equals(type) || RowType.TREE.equals(type);
	}

	public static boolean isPage(String type) {
		return RowType.PAGE.equals(type) || RowType.PAGE_NUMBER.equals(type);
	}

	public static boolean isRow(String type) {
		return RowType.ROW.equals(type);
	}
}
