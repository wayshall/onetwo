package org.onetwo.dbm.ui.spi;

import java.io.IOException;

import org.onetwo.dbm.ui.meta.DUIFieldMeta;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author weishao zeng
 * <br/>
 */
public interface DUIJsonValueWriter<T> {
	
	void write(T value, DUIFieldMeta field, JsonGenerator jgen) throws IOException ;

}
