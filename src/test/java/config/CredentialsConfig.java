package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:credential.properties")
public interface CredentialsConfig  extends Config{
    String login();
    String password();
    String remoteUrlSelenoid();
}