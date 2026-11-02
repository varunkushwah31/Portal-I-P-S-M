package com.academia.placement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Matching matching = new Matching();

    public static class Jwt {
        private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        private long expirationMs = 86400000L;

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }

        public long getExpirationMs() { return expirationMs; }
        public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
    }

    public static class Matching {
        private double jaccardWeight = 0.40;
        private double cosineWeight = 0.60;
        private double eligibilityThreshold = 0.50;

        public double getJaccardWeight() { return jaccardWeight; }
        public void setJaccardWeight(double jaccardWeight) { this.jaccardWeight = jaccardWeight; }

        public double getCosineWeight() { return cosineWeight; }
        public void setCosineWeight(double cosineWeight) { this.cosineWeight = cosineWeight; }

        public double getEligibilityThreshold() { return eligibilityThreshold; }
        public void setEligibilityThreshold(double eligibilityThreshold) { this.eligibilityThreshold = eligibilityThreshold; }
    }

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }

    public Matching getMatching() { return matching; }
    public void setMatching(Matching matching) { this.matching = matching; }
}
