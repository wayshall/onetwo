package org.onetwo.boot.module.jms;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JmsMessage<T extends Serializable> implements Serializable {
	/**
	 * @author wayshall
	 * 
	 */
	private static final long serialVersionUID = -8805102399544290246L;
	String key;
	T body;

}
