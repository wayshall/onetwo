package org.onetwo.boot.module.redission;

import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.redisson.codec.JsonJacksonCodec;

/**
 * @author wayshall
 * <br/>
 */
public class FixJsonJacksonCodec extends JsonJacksonCodec {
	
    public FixJsonJacksonCodec() {
        super(ObjectMapperProvider.DEFAULT.createObjectMapper());
    }

}
