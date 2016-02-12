package com.xoriant.reactive.billing.akka;

import akka.actor.Props;

public class BillingCluster {

	public BillingCluster() {
		Application.system().actorOf(Props.create(SimpleClusterListener.class), "listener");
	}
}
