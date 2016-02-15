package com.xoriant.reactive.billing.akka;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;

public class BillingClusterNode extends UntypedActor {

	public void preStart() {
		Config config = getContext().system().settings().config();
		Config clusterConfig = ConfigFactory.parseFile(new File("/home/jujadhav/play-dev/reactive-ecomm-billing-app/resources/billingcluster.conf"));
		List<Address> seedNodes = new ArrayList<>();
		seedNodes.add(
				AddressFromURIString.parse(clusterConfig.getList("akka.cluster.seed-nodes").get(0).toString().split("\"")[1]));

		boolean isSeed = false;
		for (ConfigValue value : clusterConfig.getList("akka.cluster.seed-nodes")) {
			if (value.toString().contains(config.getString("akka.remote.netty.tcp.hostname"))
					&& value.toString().contains(config.getString("akka.remote.netty.tcp.port"))) {
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
