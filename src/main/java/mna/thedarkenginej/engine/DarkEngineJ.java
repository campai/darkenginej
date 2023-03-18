package mna.thedarkenginej.engine;

import lombok.extern.log4j.Log4j;
import mna.thedarkenginej.config.EngineConfig;
import mna.thedarkenginej.engine.exception.EngineInitException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.Locale;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

@Log4j
public class DarkEngineJ implements GameEngine {

    private final EngineConfig engineConfig;
    private long windowHandle;
    private VkInstance vkInstance;
    private VkPhysicalDevice device;

    public DarkEngineJ(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    @Override
    public void run() {
        initEngine();
        initVulkan();

        gameLoop();

        cleanUp();
    }

    private void initEngine() {
        System.out.println("Using LWJGL version: " + Version.getVersion());

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new EngineInitException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(
                engineConfig.getWindowWidth(),
                engineConfig.getWindowHeight(),
                engineConfig.getWindowTitle(), NULL, NULL);

        if (windowHandle == NULL) {
            throw new EngineInitException("Failed to create the GLFW window");
        }

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

    private void initVulkan() {
        VkApplicationInfo appInfo = VkApplicationInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                .pApplicationName(memUTF8("Dark Engine J"))
                .pEngineName(memUTF8(""))
                .apiVersion(VK_API_VERSION_1_0);

        VkInstanceCreateInfo vulkanInfo = VkInstanceCreateInfo.calloc()
                .sType(VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                .pNext(0)
                .pApplicationInfo(appInfo);

        PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
        vulkanInfo.ppEnabledExtensionNames(requiredExtensions);

        PointerBuffer pInstance = memAllocPointer(1);
        vkCreateInstance(vulkanInfo, null, pInstance);
        long instance = pInstance.get(0);
        memFree(pInstance);

        vkInstance = new VkInstance(instance, vulkanInfo);

        vulkanInfo.free();
        appInfo.free();

        printDevicesInfo();
    }

    private void printDevicesInfo() {
        IntBuffer devicesCountBuffer = memAllocInt(1);
        int errCode = vkEnumeratePhysicalDevices(vkInstance, devicesCountBuffer, null);
        if (errCode != VK_SUCCESS) {
            throw new EngineInitException("Failed to get number of physical devices: " + errCode);
        }

        int devicesCount = devicesCountBuffer.get(0);
        if (devicesCount == 0) {
            throw new EngineInitException("No devices supporting Vulkan found :(");
        } else {
            log.info("Vulkan supported devices: " + devicesCount);
        }

        PointerBuffer physicalDeiceBuffer = memAllocPointer(devicesCount);
        vkEnumeratePhysicalDevices(vkInstance, devicesCountBuffer, physicalDeiceBuffer);

        for (int i = 0; i < devicesCount; i++) {
            long physicalDevice = physicalDeiceBuffer.get(i);

            VkPhysicalDevice device = new VkPhysicalDevice(physicalDevice, vkInstance);

            VkPhysicalDeviceProperties props = VkPhysicalDeviceProperties.calloc();
            nvkGetPhysicalDeviceProperties(device, props.address());

            final String deviceString = String.format("ID: %d, NAME: %s, VENDOR ID: %d",
                    props.deviceID(),
                    props.deviceNameString(),
                    props.vendorID()
            );

            log.info(deviceString);

            // TODO: move to config
            if (props.deviceNameString().toUpperCase(Locale.ROOT).contains("AMD")) {
                log.info("Picking device: " + deviceString);
                this.device = device;
            }

            props.free();
        }

        memFree(devicesCountBuffer);
        memFree(physicalDeiceBuffer);
    }

    private void gameLoop() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }

    private void cleanUp() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);


        glfwTerminate();
        vkDestroyInstance(vkInstance, null);

        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

    }
}
