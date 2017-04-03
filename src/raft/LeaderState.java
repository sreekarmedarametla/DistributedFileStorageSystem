package raft;


import gash.router.server.edges.EdgeInfo;
import pipe.common.Common.Body;
import pipe.common.Common.Header;
import pipe.work.Work.Heartbeat;
import pipe.work.Work.WorkMessage;
import pipe.work.Work.WorkState;

public class LeaderState implements RaftState {
 private RaftManager Manager;
	
 @Override
 public synchronized void process()
 
 {
	 
		 for(EdgeInfo ei:Manager.getEdgeMonitor().getOutBoundEdges().map.values())
			{
				if(ei.isActive()&&ei.getChannel()!=null)
				{
					WorkState.Builder sb = WorkState.newBuilder();
					sb.setEnqueued(-1);
					sb.setProcessed(-1);
					
					Heartbeat.Builder bb = Heartbeat.newBuilder();
					bb.setState(sb);
					
					Header.Builder hb = Header.newBuilder();
					hb.setNodeId(Manager.getNodeId());
					hb.setDestination(-1);
					hb.setTime(System.currentTimeMillis());
						
					WorkMessage.Builder wb = WorkMessage.newBuilder();		
					wb.setHeader(hb);		
					wb.setBeat(bb);
					wb.setSecret(10);
					
					
				}
			}
	 }
	 
 //received hearbeat no need to implement here
 public synchronized void receivedHeartBeat(WorkMessage msg)
 {
	 
	 return;
 }
	 
	
	 
		 
		 
	 
	 
	 
 
 
   @Override
	public synchronized void setManager(RaftManager Mgr) {
		this.Manager = Mgr;
	}

	@Override
	public synchronized RaftManager getManager() {
		return Manager;
	}

	 @Override
	 public void onRequestVoteReceived(WorkMessage msg) {
	 	// TODO Auto-generated method stub
	 	
	 }	
	 @Override
	 public void receivedVoteReply(WorkMessage msg) {
	 	// TODO Auto-generated method stub
		 return;
	 	
	 }	
}

//private ServerState state;
//public LeaderState(ServerState state) {
//	if (state == null)
//		throw new RuntimeException("state is null");
//	this.state = state;		
//}
////CREATE BODY
//public WorkMessage createWorkBody() {
//	Header.Builder hb = Header.newBuilder();
//	hb.setNodeId(state.getConf().getNodeId());
//	//for (EdgeInfo ei : this.outboundEdges.map.values()) {
//	hb.setDestination(-1);	
//	//}
//	Body.Builder bd=Body.newBuilder();
//	bd.setContent("sending from "+state.getConf().getNodeId());
//	
//	WorkMessage.Builder wb = WorkMessage.newBuilder();
//	wb.setBody(bd);
//	wb.setHeader(hb);
//	wb.setSecret(10);	
//		
//	return wb.build();
//}
//	
//
//// CREATE HEARTBEAT
//public WorkMessage createHB() {
//	WorkState.Builder sb = WorkState.newBuilder();
//	sb.setEnqueued(-1);
//	sb.setProcessed(-1);
//	
//	Heartbeat.Builder bb = Heartbeat.newBuilder();
//	bb.setState(sb);
//	
//	Header.Builder hb = Header.newBuilder();
//	hb.setNodeId(state.getConf().getNodeId());
//	hb.setDestination(5);
//	hb.setTime(System.currentTimeMillis());
//		
//	WorkMessage.Builder wb = WorkMessage.newBuilder();		
//	wb.setHeader(hb);		
//	wb.setBeat(bb);
//	wb.setSecret(10);				
//	return wb.build();
//		
//}










