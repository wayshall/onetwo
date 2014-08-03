package org.onetwo.app.tasksys.model.service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.onetwo.app.tasksys.model.ReplyTaskData;
import org.onetwo.app.tasksys.model.TaskActor;
import org.onetwo.app.tasksys.model.TaskData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.pattern.Patterns;
import akka.util.Timeout;

@Component
public class TaskWorkerImpl implements InitializingBean {

	private ActorRef tasksys;
	private ActorSystem system;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		system = ActorSystem.create("tasksys");
		tasksys = system.actorOf(Props.create(TaskActor.class));
	}
	
	public void addTask(TaskData taskData){
		if(taskData instanceof ReplyTaskData){
//			Future<Object> futrue = Patterns.ask(tasksys, taskData, new Timeout(1, TimeUnit.SECONDS));
			Future<Object> futrue = Patterns.ask(tasksys, taskData, new Timeout(10, TimeUnit.SECONDS));
			/*Object rs = futrue.onComplete(new Mapper<Iterable<Object>, Object>() {

				@Override
				public Object apply(Iterable<Object> parameter) {
					// TODO Auto-generated method stub
					return super.apply(parameter);
				}
				
			}, system.dispatcher());*/
			final Future<Iterable<Object>> aggregate = Futures.sequence(Arrays.asList(futrue), system.dispatcher());
			Future<Object> rs = aggregate.map(new Mapper<Iterable<Object>, Object>() {
			      public Object apply(Iterable<Object> coll) {
			         return coll.iterator().next();
			        }
			      }, system.dispatcher());
			System.out.println("rs: " + rs);
		}else{
			tasksys.tell(taskData, ActorRef.noSender());
		}
	}

}
