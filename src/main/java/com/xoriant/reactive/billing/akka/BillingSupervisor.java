package com.xoriant.reactive.billing.akka;

import java.util.HashMap;
import java.util.Map;

import com.xoriant.reactive.billing.akka.models.Order;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class BillingSupervisor extends UntypedActor {

	Map<String, Integer> cache = new HashMap<String, Integer>();
	ActorRef billingWorker = getContext().actorOf(Props.create(BillingWorker.class));

	@Override
	public void onReceive(Object message) {
		if (message instanceof Order) {
			billingWorker.tell(message, getSelf());
		} else {
			unhandled(message);
		}
	}

}