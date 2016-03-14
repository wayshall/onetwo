package org.onetwo.common.db.filequery;

public interface SqlDirectiveExtractor {

	public boolean isDirective(String str);
	public String extractDirective(String str);
}
