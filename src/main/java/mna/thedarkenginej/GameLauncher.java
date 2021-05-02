package mna.thedarkenginej;

import mna.thedarkenginej.config.EngineConfig;
import mna.thedarkenginej.utils.FileUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Properties;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameLauncher {
    private static final String ENGINE_PROPS_FILE = "engine.properties";

    private static EngineConfig engineConfig;

    private long windowHandle;

    public static void main(String[] args) throws IOException {
        Properties engineProps = FileUtils.getConfigProps(ENGINE_PROPS_FILE);
        engineConfig = EngineConfig.of(engineProps);

        new GameLauncher().run(engineConfig);
    }

    private void run(final EngineConfig engineProps) {
        System.out.println("Using LWJGL version: " + Version.getVersion());

        initEngine(engineProps);

        gameLoop();

        cleanUp();
    }

    private void cleanUp() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void gameLoop() {
        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }

    private void initEngine(final EngineConfig engineProps) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(engineConfig.getWindowWidth(), engineConfig.getWindowHeight(),
                engineConfig.getWindowTitle(), NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHandle, pWidth, pHeight);
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    windowHandle,
                    (Objects.requireNonNull(videoMode).width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);
    }
}
