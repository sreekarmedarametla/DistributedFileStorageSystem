//package chainofhandlers;
//
//
///**
// * @author Labhesh
// * @since 29 Mar,2017.
// */
//import gash.router.server.MessageServer;
//import gash.router.server.PrintUtil;
//import gash.router.server.ServerState;
//import gash.router.server.edges.EdgeMonitor;
//import io.netty.channel.Channel;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import pipe.common.Common.Body;
//import pipe.work.Work;
//import pipe.work.Work.Heartbeat;
//import pipe.work.Work.WorkMessage;
//import routing.Pipe;
//
//public class VoteHandler extends Handler {
//    Logger logger = LoggerFactory.getLogger(BodyHandler.class);
//    public int requiredVotes=1;
//    Set<Integer> voteList=new HashSet<Integer>();
//    public VoteHandler(ServerState state) {
//        super(state);
//    }
//
//    @Override
//    public void processWorkMessage(Work.WorkMessage message, Channel channel) {
//        if (message.hasVote()) {        	
//        	System.out.println(" im handling vote");		  
//            int voterId = message.getVote().getVoterID();
//            logger.info("Vote Received from "+voterId );		               
//            voteList.add(voterId);
//            if (voteList.size() >= requiredVotes) {
//                state.setCurrentLeader(state.getConf().getNodeId());
//            }
//            System.out.println("Present Leader is "+state.getCurrentLeader());               
//        	System.out.println("after req vote handler");
//        } else {
//            next.processWorkMessage(message, channel);
//        	System.out.println("I dont have vote ");
//        }
//    }
//
//
//    /*@Override
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
//*/
//
//}