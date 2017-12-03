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
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author acoburn
 */
public class PartitionConfiguration {

    @NotNull
    private ResourceConfiguration resources;

    @NotNull
    private BinaryConfiguration binaries;

    @NotEmpty
    private String id;

    @NotEmpty
    private String url;

    /**
     * Get the name of the partition
     * @return the partition id
     */
    @JsonProperty
    public String getId() {
        return id;
    }

    /**
     * Set the name of the partition
     * @param id the partition name
     */
    @JsonProperty
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the base URL for the partition
     * @return the partition baseURL
     */
    @JsonProperty
    public String getBaseUrl() {
        return url;
    }

    /**
     * Set the base URL for the partition
     * @param url the partition baseURL
     */
    @JsonProperty
    public void setBaseUrl(final String url) {
        this.url = url;
    }

    /**
     * Get the LDP-RS configuration
     * @return the LDP-RS configuration
     */
    @JsonProperty
    public ResourceConfiguration getResources() {
        return resources;
    }

    /**
     * Set the LDP-RS resource configuration
     * @param config the LDP-RS resource configuration
     */
    @JsonProperty
    public void setResources(final ResourceConfiguration config) {
        this.resources = config;
    }

    /**
     * Get the binary configuration
     * @return the binary configuration
     */
    @JsonProperty
    public BinaryConfiguration getBinaries() {
        return binaries;
    }

    /**
     * Set the binary configuration
     * @param config the binary configuration
     */
    @JsonProperty
    public void setBinaries(final BinaryConfiguration config) {
        this.binaries = config;
    }
}
