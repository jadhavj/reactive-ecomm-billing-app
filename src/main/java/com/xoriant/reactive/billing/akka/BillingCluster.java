package com.xoriant.reactive.billing.akka;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class BillingCluster {

	public static void main(String[] args) {
		ActorSystem clusterSystem = ActorSystem.create("ClusterSystem", ConfigFactory.load());
		clusterSystem.actorOf(Props.create(SimpleClusterListener.class), "listener");
	}
}
