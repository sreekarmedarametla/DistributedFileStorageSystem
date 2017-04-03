package raft;

import pipe.work.Work.WorkMessage;

public interface RaftState {
	
	public void setManager(RaftManager Mgr);

	public RaftManager getManager();

	public void process();
	
	//latest implementation
//	public void receivedVote(WorkMessage msg);
//
//	public void replyVote(WorkMessage msg);
//
//	public void voteRequested(WorkMessage msg);

}
