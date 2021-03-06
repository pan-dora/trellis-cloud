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
package org.trellisldp.app.gcloud.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author acoburn
 */
public class TrellisConfiguration extends Configuration {

    private Boolean async = false;

    private Integer cacheMaxAge = 86400;

    @NotNull
    private AuthConfiguration auth = new AuthConfiguration();

    @NotNull
    private String defaultName = "Trellis";

    @NotNull
    private AssetConfiguration assets = new AssetConfiguration();

    @NotNull
    private List<PartitionConfiguration> partitions;

    @NotNull
    private CORSConfiguration cors = new CORSConfiguration();

    @NotNull
    private NamespaceConfiguration namespaces = new NamespaceConfiguration();

    /**
     * Set async mode
     * @param async true if async mode is enabled; false otherwise
     */
    @JsonProperty
    public void setAsync(final Boolean async) {
        this.async = async;
    }

    /**
     * Get async mode
     * @return true if async mode is enabled; false otherwise
     */
    @JsonProperty
    public Boolean getAsync() {
        return async;
    }

    /**
     * Set the asset configuration
     * @param assets the asset config
     */
    @JsonProperty
    public void setAssets(final AssetConfiguration assets) {
        this.assets = assets;
    }

    /**
     * Get the asset configuration
     * @return the asset config
     */
    @JsonProperty
    public AssetConfiguration getAssets() {
        return assets;
    }

    /**
     * Get the storage partitions for this repository
     * @return the storage partitions
     */
    @JsonProperty
    public List<PartitionConfiguration> getPartitions() {
        return partitions;
    }

    /**
     * Set the partitions for this repository
     * @param partitions the partitions
     */
    @JsonProperty
    public void setPartitions(final List<PartitionConfiguration> partitions) {
        this.partitions = partitions;
    }

    /**
     * Get the application name
     * @return the name
     */
    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    /**
     * Set the application name
     * @param name the name
     */
    @JsonProperty
    public void setDefaultName(final String name) {
        this.defaultName = name;
    }

    /**
     * Set the cache max-age value
     * @param cacheMaxAge the cache max age header value
     */
    @JsonProperty
    public void setCacheMaxAge(final Integer cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
    }

    /**
     * Get the value of the cache max age
     * @return the cache max age header value
     */
    @JsonProperty
    public Integer getCacheMaxAge() {
        return cacheMaxAge;
    }

    /**
     * Set the CORS configuration
     * @param cors the CORS configuration
     */
    @JsonProperty
    public void setCors(final CORSConfiguration cors) {
        this.cors = cors;
    }

    /**
     * Get the CORS configuration
     * @return the CORS configuration
     */
    @JsonProperty
    public CORSConfiguration getCors() {
        return cors;
    }

    /**
     * Set the Auth configuration
     * @param auth the Auth configuration
     */
    @JsonProperty
    public void setAuth(final AuthConfiguration auth) {
        this.auth = auth;
    }

    /**
     * Get the Auth configuration
     * @return the Auth configuration
     */
    @JsonProperty
    public AuthConfiguration getAuth() {
        return auth;
    }

    /**
     * Set the namespaces configuration
     * @param namespaces the namespaces configuration
     */
    @JsonProperty
    public void setNamespaces(final NamespaceConfiguration namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * Get the namespace configuration
     * @return the namespace configuration
     */
    @JsonProperty
    public NamespaceConfiguration getNamespaces() {
        return namespaces;
    }
}
