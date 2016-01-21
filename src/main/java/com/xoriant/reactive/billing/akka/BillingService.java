package com.xoriant.reactive.billing.akka;

import com.xoriant.reactive.billing.akka.models.Order;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;

public class BillingService extends UntypedActor {

	// This router is used both with lookup and deploy of routees. If you
	// have a router with only lookup of routees you can use Props.empty()
	// instead of Props.create(StatsWorker.class).
	ActorRef workerRouter = getContext().actorOf(FromConfig.getInstance().props(Props.create(BillingSupervisor.class)),
			"workerRouter");

	@Override
	public void onReceive(Object message) {
		if (message instanceof Order) {
			workerRouter.tell(message, getSelf());
		} else {
			unhandled(message);
		}
	}
}
