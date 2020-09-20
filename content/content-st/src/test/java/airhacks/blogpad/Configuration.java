package airhacks.blogpad;

import org.eclipse.microprofile.config.ConfigProvider;

public interface Configuration {
    static String getValue(String key){
        final var config = ConfigProvider.getConfig();
        return config.getValue(key, String.class);
    }
}
