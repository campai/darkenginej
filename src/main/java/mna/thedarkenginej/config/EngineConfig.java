package mna.thedarkenginej.config;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Getter
@Builder(setterPrefix = "with")
public class EngineConfig {
    private static final String WINDOW_WIDTH_PROP_NAME = "window.width";
    private static final String WINDOW_HEIGHT_PROP_NAME = "window.height";
    private static final String WINDOW_TITLE_PROP_NAME = "window.title";

    private final int windowWidth;
    private final int windowHeight;

    private final String windowTitle;

    public static EngineConfig of(final Properties props) {
        return EngineConfig.builder()
                .withWindowWidth(getIntProp(props, WINDOW_WIDTH_PROP_NAME))
                .withWindowHeight(getIntProp(props, WINDOW_HEIGHT_PROP_NAME))
                .withWindowTitle(getStringProp(props, WINDOW_TITLE_PROP_NAME))
                .build();
    }

    private static String getStringProp(Properties props, String name) {
        return props.getProperty(name, "");
    }

    private static int getIntProp(Properties props, String name) {
        String value = props.getProperty(name);

        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
    }
}

