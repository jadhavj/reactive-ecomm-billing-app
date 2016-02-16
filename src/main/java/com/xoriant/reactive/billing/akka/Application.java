package com.xoriant.reactive.billing.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.EndpointManager.Pass;

public class Application extends UntypedActor {

	private static Config config = ConfigFactory.load();
	private static ActorSystem system = ActorSystem.create("ClusterSystem", config);
	private static String seedNode;
	private static String rabbitHost;
	private static String username;
	private static String password;
	private static String clusterConfigFile;
	private static String mongoHost;
	
	public static ActorSystem system() {
		return system;
	}
	
	public static void main(String args[]) {
		if (args.length > 1) {
			if ("start".equals(args[0])) {
				switch (args[1]) {
				case "cluster": 
					new BillingCluster();
					break;
				case "node":
					if(args.length>2)
					{
					clusterConfigFile = args[2];	
					system.actorOf(Props.create(BillingClusterNode.class,clusterConfigFile));
					}
					break;
				case "cart-reader":
					if(args.length>6)
					{
						seedNode = args[2];
						rabbitHost = args[3];
						username = args[4];
						password = args[5];
						mongoHost = args[6];
						system.actorOf(Props.create(CartReader.class,seedNode,rabbitHost,username,password,mongoHost));
					}
					
					break;
				}
			}
		}
	}
	@Override
	public void onReceive(Object arg0) throws Exception {
	}
}
