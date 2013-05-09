package org.onetwo.common.fish.spring.config;


import org.onetwo.common.utils.list.JFishList;

/********
 * 
 * @author wayshall
 *
 */
//TODO
public class JFishContextConfigurerListenerManager  {

	private JFishList<JFishContextConfigurerListener> listeners = new JFishList<JFishContextConfigurerListener>();

	public JFishContextConfigurerListenerManager() {
	}
	
	public void addListener(JFishContextConfigurerListener l){
		listeners.add(l);
	}
	
}
