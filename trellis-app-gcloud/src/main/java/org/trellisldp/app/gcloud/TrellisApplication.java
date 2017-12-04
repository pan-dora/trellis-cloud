/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trellisldp.app.gcloud;

import io.dropwizard.Application;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.trellisldp.agent.SimpleAgent;
import org.trellisldp.api.*;
import org.trellisldp.app.gcloud.config.TrellisConfiguration;
import org.trellisldp.binary.DefaultBinaryService;
import org.trellisldp.binary.FileResolver;
import org.trellisldp.http.*;
import org.trellisldp.id.UUIDGenerator;
import org.trellisldp.io.JenaIOService;
import org.trellisldp.webac.WebACService;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.trellisldp.app.gcloud.TrellisUtils.*;


/**
 * @author acoburn
 */
public class TrellisApplication extends Application<TrellisConfiguration> {

    /**
     * The main entry point
     * @param args the argument list
     * @throws Exception if something goes horribly awry
     */
    public static void main(final String[] args) throws Exception {
        new TrellisApplication().run(args);
    }

    @Override
    public String getName() {
        return "Trellis LDP";
    }

    @Override
    public void initialize(final Bootstrap<TrellisConfiguration> bootstrap) {
        // Not currently used
    }

    @Override
    public void run(final TrellisConfiguration config,
                    final Environment environment) throws IOException {

        // Other configurations
        final Map<String, Properties> partitions = TrellisUtils.getPartitionConfigurations(config);

        // Partition data configuration
        final Map<String, String> partitionData = TrellisUtils.getResourceDataPaths(partitions);

        // Partition BaseURL configuration
        final Map<String, String> partitionUrls = TrellisUtils.getPartitionBaseUrls(partitions);

        final IdentifierService idService = new UUIDGenerator();

//        final ResourceService resourceService = new FileResourceService(partitionData, partitionUrls, curator,
//                producer, new KafkaPublisher(producer, TOPIC_EVENT), idService.getSupplier(), config.getAsync());

//        final NamespaceService namespaceService = new Namespaces(curator, new TreeCache(curator, ZNODE_NAMESPACES),
//                config.getNamespaces().getFile());

//        final IOService ioService = new JenaIOService(namespaceService, TrellisUtils.getAssetConfiguration(config));

        final BinaryService binaryService = new DefaultBinaryService(idService, partitions,
                asList(new FileResolver(TrellisUtils.getBinaryDataPaths(partitions))));

        getAuthFilters(config).ifPresent(filters -> environment.jersey().register(new ChainedAuthFilter<>(filters)));

        // Resource matchers
//        environment.jersey().register(new RootResource(ioService, partitionUrls, getServerProperties(config)));
//        environment.jersey().register(new LdpResource(resourceService, ioService, binaryService, partitionUrls));

        // Filters
        environment.jersey().register(new AgentAuthorizationFilter(new SimpleAgent(), emptyList()));
        environment.jersey().register(new CacheControlFilter(config.getCacheMaxAge()));

        // Authorization
//        getWebacConfiguration(config).ifPresent(webac -> environment.jersey().register(new WebAcFilter(
//                        partitionUrls, asList("Authorization"), new WebACService(resourceService))));

        // CORS
        getCorsConfiguration(config).ifPresent(cors -> environment.jersey().register(
                new CrossOriginResourceSharingFilter(cors.getAllowOrigin(), cors.getAllowMethods(),
                    cors.getAllowHeaders(), cors.getExposeHeaders(), cors.getAllowCredentials(), cors.getMaxAge())));
    }
}
