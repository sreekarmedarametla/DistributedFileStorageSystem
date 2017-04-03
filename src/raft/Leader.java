package raft;

import gash.router.server.ServerState;
import pipe.common.Common.Body;
import pipe.common.Common.Header;
import pipe.work.Work.Heartbeat;
import pipe.work.Work.WorkMessage;
import pipe.work.Work.WorkState;

public class Leader {
	private ServerState state;
	public Leader(ServerState state) {
		if (state == null)
			throw new RuntimeException("state is null");
		this.state = state;		
	}
	//CREATE BODY
	public WorkMessage createWorkBody() {
		Header.Builder hb = Header.newBuilder();
		hb.setNodeId(state.getConf().getNodeId());
		//for (EdgeInfo ei : this.outboundEdges.map.values()) {
		hb.setDestination(-1);	
		//}
		Body.Builder bd=Body.newBuilder();
		bd.setContent("sending from "+state.getConf().getNodeId());
		
		WorkMessage.Builder wb = WorkMessage.newBuilder();
		wb.setBody(bd);
		wb.setHeader(hb);
		wb.setSecret(10);	
			
		return wb.build();
	}
		

	// CREATE HEARTBEAT
	public WorkMessage createHB() {
		WorkState.Builder sb = WorkState.newBuilder();
		sb.setEnqueued(-1);
		sb.setProcessed(-1);
		
		Heartbeat.Builder bb = Heartbeat.newBuilder();
		bb.setState(sb);
		
		Header.Builder hb = Header.newBuilder();
		hb.setNodeId(state.getConf().getNodeId());
		hb.setDestination(5);
		hb.setTime(System.currentTimeMillis());
			
		WorkMessage.Builder wb = WorkMessage.newBuilder();		
		wb.setHeader(hb);		
		wb.setBeat(bb);
		wb.setSecret(10);				
		return wb.build();
			
	}		
}
