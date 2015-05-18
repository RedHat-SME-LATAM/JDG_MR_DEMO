package com.redhat.smelatam.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

@Startup
@Singleton
public class CacheConfig {
	
	private org.infinispan.Cache<String,String> localCache=null;
	private DefaultCacheManager dcm =null;
	
	@PostConstruct
	private void start(){
		System.out.println("************************* Starting cache..");
		getLocalCache().start();
		System.setProperty("infinispan.accurate.bulk.ops", "true");
	}
	
	@PreDestroy
	private void destroy(){
		System.out.println("************************* Stopping cache..");
		getLocalCache().stop();
		dcm.stop();
	}
	
	@Produces
	@ApplicationScoped
	public org.infinispan.AdvancedCache<String,String> getLocalCache(){
		if(localCache==null)
			localCache = getLocalCacheManager().getCache("default");
		return localCache.getAdvancedCache();
	}
	
	
	private EmbeddedCacheManager getLocalCacheManager() {
		GlobalConfiguration glob = new GlobalConfigurationBuilder().clusteredDefault()
					.transport().clusterName("CDR_Cluster").defaultTransport().addProperty("configurationFile", "jgroups-udp.xml")
					.globalJmxStatistics().allowDuplicateDomains(true).enable()
					.build(); // Builds the GlobalConfiguration object

		Configuration config = new ConfigurationBuilder()
			.clustering().cacheMode(CacheMode.DIST_ASYNC).hash().numOwners(1).expiration().maxIdle(60, TimeUnit.SECONDS).build();
		if(dcm==null)
			dcm = new DefaultCacheManager(glob,config, true);
		return dcm;
	}
}
