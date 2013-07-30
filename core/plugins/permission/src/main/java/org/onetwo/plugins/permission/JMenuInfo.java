package org.onetwo.plugins.permission;

public class JMenuInfo extends JPermissionInfo {

	public JMenuInfo(String id, String label) {
		super(id, label);
	}

	@Override
	public boolean isMenu() {
		return true;
	}
	
	public String getMenuLink(){
		return getRequestInfos().iterator().next().getUri();
	}

}
