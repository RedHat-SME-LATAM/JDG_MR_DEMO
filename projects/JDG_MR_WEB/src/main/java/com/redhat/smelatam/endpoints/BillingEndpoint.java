package com.redhat.smelatam.endpoints;

import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.redhat.smelatam.services.BillingService;

@Stateless
@Path("/billing")
public class BillingEndpoint {
	@Inject
	BillingService bs;
	
	@GET
	@Path("/processBills")
	@Produces("application/json")
	public Set<Entry<Long, Double>> processBills(){
		return bs.processBills();
	}
	
	@GET
	@Path("/processStats")
	@Produces("application/json")
	public Set<Entry<String, Double>> processStats(){
		return bs.processStats();
	}
	@GET
	@Path("/clusterStats")
	@Produces("application/json")
	public String getClusterStats(){
		return bs.getClusterStats();
	}
}
