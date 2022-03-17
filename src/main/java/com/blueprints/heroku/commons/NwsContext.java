package com.blueprints.heroku.commons;

import java.util.Objects;
import java.util.Optional;

public class NwsContext {
    private String nwsUrl;
    private String tenant;
    private NwsEnvironment environment;
    private Credential credentials;
    private String targetUrl;

    private Optional<NwsAccessToken> nwsAccessToken = Optional.empty();

    public String getNwsUrl() {
        return nwsUrl;
    }

    public String getTenant() {
        return tenant;
    }

    public NwsEnvironment getEnvironment() {
        return environment;
    }

    public Credential getCredentials() {
        return credentials;
    }

    public Optional<NwsAccessToken> getNwsAccessToken() {
        return nwsAccessToken;
    }

    public NwsContext setNwsAccessToken(NwsAccessToken nwsAccessToken) {
        this.nwsAccessToken = Optional.of(nwsAccessToken);
        return this;
    }

    public String getTargetUrl() {
        if (Objects.isNull(this.targetUrl)) {
            synchronized (this) {
                if (Objects.isNull(this.targetUrl)) {
                    this.targetUrl = String.format("https://%s.%s.%s", this.tenant, this.environment.getEnv(), this.nwsUrl);
                }
            }
        }
        return this.targetUrl;
    }


    public static final class Builder {
        public String nwsUrl;
        public String tenant;
        public NwsEnvironment environment;
        public Credential credentials;
        private Builder() {

        }

        public static Builder start() {
            return new Builder();
        }

        public Builder withNwsUrl(String nwsUrl) {
            this.nwsUrl = nwsUrl;
            return this;
        }

        public Builder withTenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public Builder withEnvironment(NwsEnvironment environment) {
            this.environment = environment;
            return this;
        }

        public Builder withCredentials(Credential credentials) {
            this.credentials = credentials;
            return this;
        }

        public NwsContext build() {
            NwsContext nwsContext = new NwsContext();
            nwsContext.nwsUrl = this.nwsUrl;
            nwsContext.tenant = this.tenant;
            nwsContext.credentials = this.credentials;
            nwsContext.environment = this.environment;
            return nwsContext;
        }
    }
}
