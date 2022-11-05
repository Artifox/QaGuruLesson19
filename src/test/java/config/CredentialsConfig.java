package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/authConfig.properties")
public interface CredentialsConfig extends Config {
    String email();
    String password();
}