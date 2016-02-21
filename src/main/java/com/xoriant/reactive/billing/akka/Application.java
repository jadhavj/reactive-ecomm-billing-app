package com.xoriant.reactive.billing.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xoriant.reactive.billing.util.Mongo;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Application extends UntypedActor {

	private static Config config = ConfigFactory.load();
	private static ActorSystem system;
	private static String seedNode;
	private static String rabbitHost;
	private static String rabbitUser;
	private static String rabbitPass;
	private static String mongoHost;

	public static ActorSystem system() {
		return system;
	}

	public static void main(String args[]) {
		if (args.length > 1) {
			if ("start".equals(args[0])) {
				switch (args[1]) {
				case "cluster":
					system = ActorSystem.create("ClusterSystem", config);
					new BillingCluster();
					break;
				case "node":
					system = ActorSystem.create("NodeSystem", config);
					if (args.length == 5) {
						if (args[2].equals("--mongo-host"))
							Mongo.init(args[3], args[4]);
						system.actorOf(Props.create(BillingClusterNode.class));
					}
					break;
				case "cart-reader":
					system = ActorSystem.create("CartReaderSystem", config);
					if (args.length == 13) {
						int i = 2;
						for (String arg : args) {
							if (args.equals("--seed-node-ip"))
								seedNode = args[i+1];
							else if (arg.equals("--mongo-host"))
								mongoHost = args[i+1];
							else if (arg.equals("--rabbit-host"))
								rabbitHost = args[i+1];
							else if (arg.equals("--rabbit-user"))
								rabbitUser = args[i+1];
							else if (arg.equals("--rabbit-pass"))
								rabbitPass = args[i+1];
							i++;
						}
						Mongo.init(mongoHost, "react-app");
						system.actorOf(Props.create(CartReader.class, seedNode, rabbitHost, rabbitUser, rabbitPass));
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
