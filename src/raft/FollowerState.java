package raft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gash.router.server.ServerState;
import gash.router.server.edges.EdgeInfo;
import gash.router.server.edges.EdgeMonitor;
import pipe.election.Election.Vote;
import pipe.work.Work.WorkMessage;

public class FollowerState implements RaftState {
	protected static Logger logger = LoggerFactory.getLogger("Follower State");
	private RaftManager Manager;
	private int votedFor=-1;
	public synchronized void process()
	{
		try {
			if (Manager.getElectionTimeout() <= 0 && (System.currentTimeMillis() - Manager.getLastKnownBeat() > Manager.getHbBase())) {
				Manager.setCurrentState(Manager.Candidate);
				System.out.println("all set for leader election"); 
				return;
			} else {
				Thread.sleep(200);
				long dt = Manager.getElectionTimeout() - (System.currentTimeMillis() - Manager.getTimerStart());
				System.out.println("in  setting election timeout");
				System.out.println("value "+dt); 		
				Manager.setElectionTimeout(dt);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public synchronized void setManager(RaftManager Mgr) {
		this.Manager = Mgr;
	}

	@Override
	public synchronized RaftManager getManager() {
		return Manager;
	}
	
	
	//giving vote after receiving request votes
	public synchronized void onRequestVoteReceived(WorkMessage msg){
		System.out.println("i am in follower's votelogic");
    	//EdgeMonitor emon=Manager.getEdgeMonitor(); 
		Manager.setCurrentState(Manager.Follower);
		System.out.println("changed state to follower");
    	if(Manager.getTerm()<msg.getReqvote().getCurrentTerm())
    	{
    		votedFor=-1;
    
    		if(votedFor==-1)
    		{
    			votedFor=msg.getHeader().getNodeId();
    			Manager.randomizeElectionTimeout();
    			Manager.setTerm(Manager.getTerm()+1);
    			System.out.println(System.currentTimeMillis() + " : You voted for " + votedFor + " in term "
						+ Manager.getTerm() + ". Timeout is : " + Manager.getElectionTimeout());

				replyVote(msg, true);
    			
    			
    		}
    		else
    		{
    			replyVote(msg,false);
    		}
    	}else
    	{
    		replyVote(msg,false);
    	}
    		
    		
//    	logger.info("sendng vote to "+msg.getReqvote().getCandidateID()+ " updating my term to "+ msg.getReqvote().getCurrentTerm());	        	
//    	Manager.setTerm(msg.getReqvote().getCurrentTerm());
//    	WorkMessage wm=Vote(Manager.getNodeId(), msg.getReqvote().getCandidateID());
    	//emon.sendVote(wm);
    	
    	//sending votes
//    	for(EdgeInfo ei:Manager.getEdgeMonitor().getOutBoundEdges().map.values())
//		{
//			if(ei.isActive()&&ei.getChannel()!=null)
//			{
//				if(wm.getVote().getCandidateID()==ei.getRef()){
//					if (ei.isActive() && ei.getChannel() != null) {
//	                    	System.out.println("sending to cand "+ei.getRef());
//	                    
//	         				ei.getChannel().writeAndFlush(wm);    						
//	                    	
//					}
//				}	
//				
//			}
//		}	
    	
    	
    	}
	
	
	//replyvote
	
	public synchronized void replyVote(WorkMessage msg,boolean VoteSent)
	{
		int toNode=msg.getReqvote().getCandidateID();
		int fromNode=Manager.getNodeId();
		EdgeInfo ei=Manager.getEdgeMonitor().getOutBoundEdges().map.get(toNode);
		if(ei.isActive()&&ei.getChannel()!=null)
		{
			WorkMessage wm=Vote(fromNode, toNode);
			
			ei.getChannel().writeAndFlush(wm);
			
		}
	}
	
	
	
	public synchronized WorkMessage Vote(int NodeId,int CandidateId) {		
		Vote.Builder vb=Vote.newBuilder();		
		vb.setVoterID(NodeId);
		vb.setCandidateID(CandidateId);
		WorkMessage.Builder wb = WorkMessage.newBuilder();	
		wb.setVote(vb);
		wb.setSecret(10);
		System.out.println("returned from building vote message");
		return wb.build();
	}
	
	@Override
	public synchronized void receivedVoteReply(WorkMessage msg) {
		// TODO Auto-generated method stub
		return;
		
	}
	
	@Override
	public synchronized void receivedHeartBeat(WorkMessage msg)
	{
		Manager.randomizeElectionTimeout();
		Manager.randomizeElectionTimeout();
		System.out.println("received hearbeat from the Leader: "+msg.getHeader().getNodeId());
		Manager.setLastKnownBeat(System.currentTimeMillis());
	}
	
	
	
	
	
	
	
}

//private ServerState state;
////public FollowerState(ServerState state) {
////	if (state == null)
////		throw new RuntimeException("state is null");
////	this.state = state;		
////}
////CREATE VOTE MESSAGE
//	public WorkMessage Vote(int NodeId,int CandidateId) {		
//		Vote.Builder vb=Vote.newBuilder();		
//		vb.setVoterID(NodeId);
//		vb.setCandidateID(CandidateId);
//			
//		
//		WorkMessage.Builder wb = WorkMessage.newBuilder();	
//		wb.setVote(vb);
//		wb.setSecret(10);	
//		
//		return wb.build();
//	}
