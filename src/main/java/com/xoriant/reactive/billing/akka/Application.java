package com.xoriant.reactive.billing.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Application extends UntypedActor {

	private static Config config = ConfigFactory.load();
	private static ActorSystem system = ActorSystem.create("ClusterSystem", config);
	public static ActorSystem system() {
		return system;
	}
	
	public static void main(String args[]) {
		if (args.length == 2) {
			if ("start".equals(args[0])) {
				switch (args[1]) {
				case "cluster": 
					new BillingCluster();
					break;
				case "node":
					system.actorOf(Props.create(BillingClusterNode.class));
					break;
				case "cart-reader":
					system.actorOf(Props.create(CartReader.class));
					break;
				}
			}
		}
	}
	@Override
	public void onReceive(Object arg0) throws Exception {
	}
}
