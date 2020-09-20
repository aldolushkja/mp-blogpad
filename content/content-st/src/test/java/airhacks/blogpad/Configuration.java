package airhacks.blogpad;

import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;

public interface Configuration {

    static URI getValue(String key){
        final var config = ConfigProvider.getConfig();
        return config.getValue(key, URI.class);
    }

    static boolean getBooleanValue(String key){
        var config = ConfigProvider.getConfig();
        return config.getOptionalValue(key, Boolean.class).orElse(false);
    }
}
