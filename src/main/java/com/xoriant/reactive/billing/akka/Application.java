package com.xoriant.reactive.billing.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xoriant.reactive.billing.util.Mongo;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Application extends UntypedActor {

	private static Config config = ConfigFactory.load();
	private static ActorSystem system = ActorSystem.create("ClusterSystem", config);
	private static String seedNode;
	private static String rabbitHost;
	private static String username;
	private static String password;
	private static String clusterConfigFile;

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
					if (args.length == 6) {
						if (args[2].equals("clusterConfigFile"))
							clusterConfigFile = args[3];
						if (args[4].equals("mongoHost"))
							Mongo.init(args[5]);
						system.actorOf(Props.create(BillingClusterNode.class, clusterConfigFile));
					}
					break;
				case "cart-reader":
					if (args.length == 12) {
						if (args[2].equals("seedNode"))
							seedNode = args[3];
						if (args[4].equals("rabbitHost"))
							rabbitHost = args[5];
						if (args[6].equals("username"))
							username = args[7];
						if (args[8].equals("password"))
							password = args[9];
						if (args[10].equals("mongoHost"))
							Mongo.init(args[11]);
						system.actorOf(Props.create(CartReader.class, seedNode, rabbitHost, username, password));
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
