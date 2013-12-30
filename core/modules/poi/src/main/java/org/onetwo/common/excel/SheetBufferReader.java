package org.onetwo.common.excel;

public interface SheetBufferReader<T> {

	void initReader();

	T read();

}