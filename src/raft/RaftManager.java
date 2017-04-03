package raft;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

import gash.router.container.RoutingConf;
import gash.router.server.ServerState;
import gash.router.server.edges.EdgeMonitor;

public class RaftManager implements Runnable {
		private ServerState state;
		private int nodeId = -1;
		private int leaderId = -1;
//		public Channel clientChannel;

		private String selfHost;
		private RoutingConf conf;
		private EdgeMonitor emon;

		private long timerStart = 0;
		// This servers states
		private volatile RaftState CurrentState;
//		public RaftState Leader;
		public RaftState Candidate;
		public RaftState Follower;

		private int heartBeatBase = 3000;
		private volatile long electionTimeout = 3000;
		private volatile long lastKnownBeat = 0;
		private Random rand;
		private int term = 0;
		// private int commitIndex = 0;

		public RaftManager(ServerState state) {
			this.state = state;
		}

		public void init() throws UnknownHostException {
			selfHost = Inet4Address.getLocalHost().getHostAddress();

			rand = new Random();
			
			Candidate = new CandidateState();
			Candidate.setManager(this);  
			
			Follower = new FollowerState();
			Follower.setManager(this);

			this.conf = state.getConf();
			this.emon = state.getEmon();

			lastKnownBeat = System.currentTimeMillis();
			heartBeatBase = conf.getHeartbeatDt();
			nodeId = conf.getNodeId();

			randomizeElectionTimeout();
			electionTimeout += 1000;

			CurrentState = Follower;
		}

		@Override
		public void run() {
			System.out.println("in raft");
			System.out.println("hearbeat initially is"+heartBeatBase);
			System.out.println("elec timeout initially is"+electionTimeout);
			while (true) {
				timerStart = System.currentTimeMillis();
				CurrentState.process();
			}

		}
		public synchronized void randomizeElectionTimeout() {

			int temp = rand.nextInt(heartBeatBase);
			temp = temp + heartBeatBase;
			electionTimeout = (long) temp;

			// System.out.println("Randomized Timeout value is : "+electionTimeout);
		}
		public synchronized void setElectionTimeout(long et) {
			electionTimeout = et;
		}
		public synchronized long getElectionTimeout() {
			return electionTimeout;
		}

		public synchronized int getHbBase() {
			return heartBeatBase;
		}
		public synchronized long getLastKnownBeat() {
			return lastKnownBeat;
		}

		public synchronized void setLastKnownBeat(long beatTime) {
			lastKnownBeat = beatTime;
		}
		public synchronized long getTimerStart() {
			return timerStart;
		}

		public synchronized void setTimerStart(long t) {
			timerStart = t;
		}
		
		public synchronized int getNodeId() {
			return nodeId;
		}
		public synchronized void setCurrentState(RaftState st) {
			CurrentState = st;
		}
		
		public synchronized EdgeMonitor getEdgeMonitor()
		{
			return emon;
		}
		
		public synchronized void setTerm(int val)
		{
			term=val;
		}
		public synchronized int getTerm()
		{
			return term;
		}



}
