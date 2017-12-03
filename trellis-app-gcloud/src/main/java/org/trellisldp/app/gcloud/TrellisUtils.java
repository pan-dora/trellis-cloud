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

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import org.trellisldp.app.gcloud.auth.AnonymousAuthFilter;
import org.trellisldp.app.gcloud.auth.AnonymousAuthenticator;
import org.trellisldp.app.gcloud.auth.BasicAuthenticator;
import org.trellisldp.app.gcloud.auth.JwtAuthenticator;
import org.trellisldp.app.gcloud.config.*;

import java.security.Principal;
import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author acoburn
 */
class TrellisUtils {

    public static final String RESOURCE_PATH = "resourcePath";
    public static final String BASE_URL = "baseUrl";
    public static final String BINARY_PATH = "path";
    public static final String FILE_PREFIX = "file:";
    public static final String PREFIX = "prefix";
    public static final String BINARY_LEVELS = "levels";
    public static final String BINARY_LENGTH = "length";

    public static Map<String, Properties> getPartitionConfigurations(final TrellisConfiguration config) {
        return config.getPartitions().stream().collect(toMap(PartitionConfiguration::getId, p -> {
                final Properties props = new Properties();
                props.setProperty(PREFIX, "file:" + p.getId());
                props.setProperty(BASE_URL, p.getBaseUrl());
                props.setProperty(RESOURCE_PATH, p.getResources().getPath());
                props.setProperty(BINARY_PATH, p.getBinaries().getPath());
                props.setProperty(BINARY_LEVELS, p.getBinaries().getLevels().toString());
                props.setProperty(BINARY_LENGTH, p.getBinaries().getLength().toString());
                return props;
            }));
    }

    public static Map<String, String> getResourceDataPaths(final Map<String, Properties> partitions) {
        return partitions.entrySet().stream().collect(toMap(Map.Entry::getKey,
                    e -> e.getValue().getProperty(RESOURCE_PATH)));
    }

    public static Map<String, String> getPartitionBaseUrls(final Map<String, Properties> partitions) {
        return partitions.entrySet().stream().collect(toMap(Map.Entry::getKey,
                    e -> e.getValue().getProperty(BASE_URL)));
    }

    public static Map<String, String> getBinaryDataPaths(final Map<String, Properties> partitions) {
        return partitions.entrySet().stream()
            .filter(e -> e.getValue().getProperty(PREFIX).startsWith(FILE_PREFIX + e.getKey()))
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().getProperty(BINARY_PATH)));
    }

    public static Map<String, String> getAssetConfiguration(final TrellisConfiguration config) {
        final Map<String, String> assetMap = new HashMap<>();
        assetMap.put("icon", config.getAssets().getIcon());
        assetMap.put("css", config.getAssets().getCss().stream().map(String::trim).collect(joining(",")));
        assetMap.put("js", config.getAssets().getJs().stream().map(String::trim).collect(joining(",")));

        return assetMap;
    }

    public static Optional<List<AuthFilter>> getAuthFilters(final TrellisConfiguration config) {
        // Authentication
        final List<AuthFilter> filters = new ArrayList<>();
        final AuthConfiguration auth = config.getAuth();

        if (auth.getJwt().getEnabled()) {
            filters.add(new OAuthCredentialAuthFilter.Builder<Principal>()
                    .setAuthenticator(new JwtAuthenticator(auth.getJwt().getKey(), auth.getJwt().getBase64Encoded()))
                    .setPrefix("Bearer")
                    .buildAuthFilter());
        }

        if (auth.getBasic().getEnabled()) {
            filters.add(new BasicCredentialAuthFilter.Builder<Principal>()
                    .setAuthenticator(new BasicAuthenticator(auth.getBasic().getUsersFile()))
                    .setRealm("Trellis Basic Authentication")
                    .buildAuthFilter());
        }

        if (auth.getAnon().getEnabled()) {
            filters.add(new AnonymousAuthFilter.Builder()
                .setAuthenticator(new AnonymousAuthenticator())
                .buildAuthFilter());
        }

        if (filters.isEmpty()) {
            return empty();
        }
        return of(filters);
    }

    public static Properties getServerProperties(final TrellisConfiguration config) {
        final Properties props = new Properties();
        props.setProperty("title", config.getDefaultName());
        return props;
    }

    public static Optional<WebacConfiguration> getWebacConfiguration(final TrellisConfiguration config) {
        if (config.getAuth().getWebac().getEnabled()) {
            return of(config.getAuth().getWebac());
        }
        return empty();
    }

    public static Optional<CORSConfiguration> getCorsConfiguration(final TrellisConfiguration config) {
        if (config.getCors().getEnabled()) {
            return of(config.getCors());
        }
        return empty();
    }

    private TrellisUtils() {
        // prevent instantiation
    }
}
