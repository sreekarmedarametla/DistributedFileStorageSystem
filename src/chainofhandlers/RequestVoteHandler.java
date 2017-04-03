//package chainofhandlers;
///**
// * @author Labhesh
// * @since 29 Mar,2017.
// */
//
//import gash.router.server.MessageServer;
//import gash.router.server.PrintUtil;
//import gash.router.server.ServerState;
//import gash.router.server.edges.EdgeMonitor;
//import io.netty.channel.Channel;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import pipe.common.Common.Body;
//import pipe.election.Election.Vote;
//import pipe.work.Work;
//import pipe.work.Work.WorkMessage;
////import raft.Candidate;
//import raft.FollowerState;
//import routing.Pipe;
//
//
//public class RequestVoteHandler extends Handler {
//    public RequestVoteHandler(ServerState state) {
//		super(state);
//		// TODO Auto-generated constructor stub
//	}
//
//
//	Logger logger = LoggerFactory.getLogger(BodyHandler.class);
//    
//
//    @Override
//    public void processWorkMessage(Work.WorkMessage message, Channel channel) {
//        if (message.hasReqvote()) {
//        	//check term here and decide if the candidate is worthy
//        	System.out.println("im in req vote handler");
//        	EdgeMonitor emon=state.getEmon(); 
//        	FollowerState follower=new FollowerState(state);
//        	System.out.println(message.getReqvote().getCandidateID()+" "+ message.getReqvote().getCurrentTerm());
//        	if(state.getCurrentTerm()<message.getReqvote().getCurrentTerm()){
//        		state.setCurrentTerm(message.getReqvote().getCurrentTerm());
//        		WorkMessage wm=follower.Vote(state.getConf().getNodeId(), message.getReqvote().getCandidateID());
//        		emon.sendVote(wm);
//        	}
//        } else {        	
//            next.processWorkMessage(message, channel);
//            System.out.println("I honestly dont know where to go ");
//        }
//    }
///*
//    @Override
//    public void processCommandMessage(Pipe.CommandMessage message, Channel channel) {
//        if (message.hasDuty()) {
//            server.onDutyMessage(message, channel);
//        } else {
//            next.processCommandMessage(message, channel);
//        }
//    }
//
//    @Override
//    public void processGlobalMessage(Global.GlobalMessage message, Channel channel) {
//        if (message.getGlobalHeader().getDestinationId() == server.getGlobalConf().getClusterId()) {
//            logger.info("I got back my request");
//        } else {
//            if (message.hasRequest()) {
//                server.onGlobalDutyMessage(message, channel);
//            } else {
//                next.processGlobalMessage(message, channel);
//            }
//        }
//
//    }
//
//*/
//    }
//
