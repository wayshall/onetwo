package org.onetwo.common.cmd;

import org.hibernate.SessionFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.commandline.AbstractCommand;
import org.onetwo.common.utils.commandline.CmdContext;

public class StatusCommand extends AbstractCommand {

	public StatusCommand() {
		super("status");
		this.doc = "check hibernate status!";
	}

	public void doExecute(CmdContext context) {
		SpringApplication app = context.getVar(SpringApplication.class);
		if(app!=null){
			StatusObject status = new StatusObject((SessionFactory) app.getBean("sessionFactory"));
			System.out.println(status.toString());
		}
	}

}
