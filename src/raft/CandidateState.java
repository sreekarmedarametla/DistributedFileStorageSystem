package raft;

import gash.router.server.edges.EdgeInfo;
import pipe.common.Common.Header;
import pipe.election.Election.RequestVote;
import pipe.work.Work.WorkMessage;


//package raft;
//
//import gash.router.server.ServerState;

//import pipe.election.Election.RequestVote;
//import pipe.election.Election.Vote;
//import pipe.work.Work.WorkMessage;
//
//public class Candidate {
//	private ServerState state;
//	public Candidate(ServerState state) {
//		if (state == null)
//			throw new RuntimeException("state is null");
//		this.state = state;		
//	}
//	
//	//CREATE REQUEST VOTE MESSAGE
//	public WorkMessage RequestVote() {
//		int newTerm=state.getCurrentTerm();
//	    state.setCurrentTerm(++newTerm);
//		Header.Builder hb = Header.newBuilder();
//		hb.setNodeId(state.getConf().getNodeId());
//		hb.setDestination(-1);	
//		
//		RequestVote.Builder rvb=RequestVote.newBuilder();
//		rvb.setCandidateID(1);	
//		rvb.setCurrentTerm(state.getCurrentTerm());
//		
//		WorkMessage.Builder wb = WorkMessage.newBuilder();
//		wb.setHeader(hb);
//		wb.setReqvote(rvb);
//		wb.setSecret(10);	
//		
//		System.out.println(wb.getReqvote().getCandidateID()+" "+wb.getReqvote().getCurrentTerm());
//		return wb.build();
//	}
//	
//}


public class CandidateState implements RaftState{
	private RaftManager Manager;
	private int voteCount=0;
	private int candidateId;
	private int votedFor=-1;
	private int activeCount=0;
	
	public void process(){
		System.out.println("reached candidate State");
		try {			
			if (Manager.getElectionTimeout() <= 0 && (System.currentTimeMillis() - Manager.getLastKnownBeat() > Manager.getHbBase())) {
				System.out.println("Node : " + Manager.getNodeId() + " timed out");
				requestVote();
				Manager.randomizeElectionTimeout();
				return;
			}else{
				Thread.sleep(200);
				long dt = Manager.getElectionTimeout() - (System.currentTimeMillis() - Manager.getTimerStart());
				Manager.setElectionTimeout(dt);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	public synchronized void requestVote(){
		
		System.out.println("reached requestVote");
		Manager.setTerm(Manager.getTerm()+1);
		activeCount=1;
		for(EdgeInfo ei:Manager.getEdgeMonitor().getOutBoundEdges().map.values())
		{
			if(ei.isActive()&&ei.getChannel()!=null)
			{
				activeCount++;
			}
		}
		//System.out.println("active count is"+activeCount);
		voteCount=0;
		System.out.println("voted for self");
		voteCount++;
		for(EdgeInfo ei:Manager.getEdgeMonitor().getOutBoundEdges().map.values())
		{
			
			if(ei.isActive()&&ei.getChannel()!=null)
			{
				System.out.println("reached here"); 
				
				System.out.println("voteRequest sent to"+ei.getRef());
				
				Header.Builder hb = Header.newBuilder();
				hb.setNodeId(Manager.getNodeId());
				hb.setDestination(-1);	
				
				RequestVote.Builder rvb=RequestVote.newBuilder();
				rvb.setCandidateID(1);	
				rvb.setCurrentTerm(Manager.getTerm());
				
				WorkMessage.Builder wb = WorkMessage.newBuilder();
				wb.setHeader(hb);
				wb.setReqvote(rvb);
				wb.setSecret(10);	
				
			     System.out.println("sending requestVotes to all");
				 Manager.getEdgeMonitor().sendMessage(wb.build());		
				
			}
			
		}
		return;		
	}
	
	
	//if somebody requests vote of candidate
	@Override
	public synchronized void onRequestVoteReceived(WorkMessage msg) {
		// TODO Auto-generated method stub
		System.out.println("Candidates Vote requested by "+msg.getHeader().getNodeId());
		if (msg.getReqvote().getCurrentTerm() > Manager.getTerm()) {
			votedFor = -1;
			Manager.randomizeElectionTimeout();			
			Manager.setCurrentState(Manager.Follower);
			Manager.getCurrentState().onRequestVoteReceived(msg);
			
		} 
	}
	
	//received vote
	@Override
	public synchronized void receivedVoteReply(WorkMessage msg)
	{
		System.out.println("received vote from: "+msg.getHeader().getNodeId()+" to me");
		voteCount++;
		if(voteCount>(activeCount/2))
		{
			Manager.randomizeElectionTimeout();
			System.out.println("Leader Elected and the Node Id is "+ Manager.getNodeId()+"total active nodes is"+activeCount);
			Manager.setLeaderId(Manager.getNodeId());
			votedFor=-1;
			activeCount=0;
			Manager.setCurrentState(Manager.Leader);
		}
	}
	
	
	
	
		
	
		@Override
		public synchronized void setManager(RaftManager Mgr){
			this.Manager = Mgr;
		}

		@Override
		public synchronized RaftManager getManager() {
			return Manager;
		}
		
		@Override
		public synchronized void receivedHeartBeat(WorkMessage msg)
		{
			System.out.println("i turned follower now");
			Manager.setCurrentState(Manager.Follower);
			Manager.randomizeElectionTimeout();
			System.out.println("Leader Elected. Leader is : " + msg.getHeader().getNodeId());
			Manager.getCurrentState().receivedHeartBeat(msg);
		}
	
	
}

