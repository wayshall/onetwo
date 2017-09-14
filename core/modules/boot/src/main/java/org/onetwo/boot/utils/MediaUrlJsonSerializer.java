package org.onetwo.boot.utils;

import java.util.Arrays;

/**
 * @author wayshall
 * <br/>
 */
public class MediaUrlJsonSerializer extends ImageUrlJsonSerializer {

	public MediaUrlJsonSerializer() {
		super();
		this.fileTypes = Arrays.asList("mp3", "mp4", "avi");
	}

}
