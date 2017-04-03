package gash.router.server;

import gash.router.container.RoutingConf;
import gash.router.server.edges.EdgeMonitor;
import gash.router.server.tasks.TaskList;
//import raft.Candidate;
import raft.FollowerState;
import raft.RaftManager;

public class ServerState {
	private RoutingConf conf;
	private EdgeMonitor emon;
	private TaskList tasks;	
	private RaftManager manager;
//	private int currentLeader=0;
//	private int currentTerm=0;
	private String state="";
	
	public RaftManager getManager() {
		return manager;
	}

	public void setManager(RaftManager mgr) {
		manager = mgr;
	}


	public String getState() { 
		return state;
    }

	public void setState(String state) { 
		this.state = state;
    }
	
	public RoutingConf getConf() {
		return conf;
	}
	
	public void setConf(RoutingConf conf) {
		this.conf = conf;
	}

	public EdgeMonitor getEmon() {
		return emon;
	}

	public void setEmon(EdgeMonitor emon) {
		this.emon = emon;
	}

	public TaskList getTasks() {
		return tasks;
	}

	public void setTasks(TaskList tasks) {
		this.tasks = tasks;
	}

}
