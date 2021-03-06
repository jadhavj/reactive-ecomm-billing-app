package com.xoriant.reactive.billing.akka;

import java.util.ArrayList;
import java.util.List;

import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class BillingClusterNode extends UntypedActor {

	public void preStart() {
		Config clusterConfig = getContext().system().settings().config();
		List<Address> seedNodes = new ArrayList<>();
		seedNodes.add(AddressFromURIString
				.parse(clusterConfig.getList("akka.cluster.seed-nodes").get(0).toString().split("\"")[1]));

		boolean isSeed = false;
		for (ConfigValue value : clusterConfig.getList("akka.cluster.seed-nodes")) {
			if (value.toString().contains(clusterConfig.getString("akka.remote.netty.tcp.hostname"))
					&& value.toString().contains(clusterConfig.getString("akka.remote.netty.tcp.port"))) {
				isSeed = true;
			}
		}
		if (!isSeed) {
			Cluster.get(Application.system()).joinSeedNodes(seedNodes);
		}

		Application.system().actorOf(Props.create(BillingService.class), "billingService");
		
	}

	@Override
	public void onReceive(Object arg0) throws Exception {
	}

}
