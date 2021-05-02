package mna.thedarkenginej.config;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Getter
@Builder(setterPrefix = "with")
public class EngineConfig {
    private final int windowWidth;
    private final int windowHeight;

    private final String windowTitle;

    public static EngineConfig of(final Properties props) {
        return EngineConfig.builder()
                .withWindowWidth(getInProp(props, "window.width"))
                .withWindowHeight(getInProp(props, "window.height"))
                .withWindowTitle(getStringProp(props, "window.title"))
                .build();
    }

    private static String getStringProp(Properties props, String name) {
        return props.getProperty(name, "");
    }

    private static int getInProp(Properties props, String name) {
        String value = props.getProperty(name);

        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
    }
}

