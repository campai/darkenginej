package mna.thedarkenginej;

import mna.thedarkenginej.config.EngineConfig;
import mna.thedarkenginej.engine.DarkEngineJ;
import mna.thedarkenginej.engine.GameEngine;
import mna.thedarkenginej.utils.FileUtils;

import java.io.IOException;

public class GameLauncher {
    private static final String ENGINE_PROPS_FILE = "engine.properties";

    public static void main(String[] args) throws IOException {
        var engineConfig = EngineConfig.of(FileUtils.getConfigProps(ENGINE_PROPS_FILE));

        GameEngine darkEngineJ = new DarkEngineJ(engineConfig);

        darkEngineJ.run();
    }
}
