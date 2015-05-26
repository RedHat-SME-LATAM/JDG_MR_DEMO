package com.redhat.smelatam.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.AdvancedCache;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;

import com.redhat.smelatam.mr.BillingMapper;
import com.redhat.smelatam.mr.BillingReducer;
import com.redhat.smelatam.mr.StatsMapper;
import com.redhat.smelatam.mr.StatsReducer;

@Stateless
public class BillingService {

	@Inject
	AdvancedCache<String,String> reqCache;
	
	
	public Set<Entry<Long, Double>> processBills(){
		int count=30;
		Map<Long,Double> lines = 
				new MapReduceTask<String,String,Long,Double>(
						reqCache.getAdvancedCache(),true)
						.mappedWith(new BillingMapper())
						.reducedWith(new BillingReducer())
						.execute();
		
		Set<Entry<Long, Double>> linesTrunc = new HashSet<Entry<Long,Double>>();
		Iterator<Entry<Long, Double>> iter = lines.entrySet().iterator();
		for(int i=0;i<count && iter.hasNext();i++){
			Entry<Long, Double> entry = iter.next();
			linesTrunc.add(entry);
		}
		return linesTrunc;
	}
	
	public Set<Entry<String, Double>> processStats(){
		Map<String,Double> charges = 
				new MapReduceTask<String,String,String,Double>(
						reqCache.getAdvancedCache(),true)
						.mappedWith(new StatsMapper())
						.reducedWith(new StatsReducer())
						.execute();
		
		
		return charges.entrySet();
	}
	
	
	
	public String getClusterStats(){
		EmbeddedCacheManager ecm = reqCache.getCacheManager();
		
		String cluster = ecm.getClusterName();
		List<Address> members = ecm.getMembers();
		String membersArr = "";
		Set<String> cacheNames = ecm.getCacheNames();
		String cacheNamesStr ="";
		Iterator<String> iter = cacheNames.iterator();
		while(iter.hasNext()){
			cacheNamesStr+="\"" + iter.next() + "\"";
			if(iter.hasNext())
				cacheNamesStr+=",";
		}
		
		for(int i=0;i<members.size();i++){
			membersArr+="\"" + members.get(i).toString() + "\"";
			if(i!=(members.size()-1))
				membersArr+=",";
		}
		String resp = "{\"clusterName\": \"" + cluster + "\", \"clusterMembers\":[" + membersArr + "]" +", \"cacheNames\":[" + cacheNamesStr + "]" +  "}";
		return resp;
	}
}
