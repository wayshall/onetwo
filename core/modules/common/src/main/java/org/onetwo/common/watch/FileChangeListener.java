
package org.onetwo.common.watch;

import org.onetwo.common.propconf.ResourceAdapter;


public interface FileChangeListener
{
	public void fileChanged(ResourceAdapter<?> file);
}
