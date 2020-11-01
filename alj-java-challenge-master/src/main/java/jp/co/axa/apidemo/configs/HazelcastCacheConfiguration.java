package jp.co.axa.apidemo.configs;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastCacheConfiguration {

    final String CACHE_NAME = "employees";
    final String INSTANCE_NAME = "employee-hazelcast-instance";

    /**
     * This configuration will be responsible for creating a Hazelcast instance and setting the TTL and IdealTime for the caching mechanism.
     * We need to provide an INSTANCE NAME and a CACHE NAME which will be used by Spring boot to Load the Hazelcast instance and cache will be loaded/operated by this instance.
     *
     * @return - a Hazelcast config for setting up the cache and a caching manager internally.
     */
    @Bean
    public com.hazelcast.config.Config getHazelcastConfig() {

        return new Config().setInstanceName(INSTANCE_NAME)
                .addMapConfig(
                        new MapConfig()
                                .setName(CACHE_NAME)
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(60)
                                .setMaxIdleSeconds(40));
    }
}
