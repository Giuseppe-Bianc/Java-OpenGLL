package bglengine;

import constant.Const;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static java.util.Objects.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	private int width, height;
	private String title;
	private long glfwWindow;

	public float r, g, b, a;
	private boolean fadeToBlack = false;

	private static Window window = null;

	private static Scene currentScene;

	private Window() {
		this.width = 1920;
		this.height = 1080;
		this.title = "Mario";
		r = 1;
		b = 1;
		g = 1;
		a = 1;
	}

	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currentScene = new LevelEditorScene();
				currentScene.init();
				currentScene.start();
				break;
			case 1:
				currentScene = new LevelScene();
				currentScene.init();
				currentScene.start();
				break;
			default:
				assert false : "Unknown scene '" + newScene + "'";
				break;
		}
	}

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;
	}

	public static Scene getScene() {
		return get().currentScene;
	}

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		glfwTerminate();
		requireNonNull(glfwSetErrorCallback(null)).free();
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		// Create the window
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}

		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		try (org.lwjgl.system.MemoryStack st = stackPush()) {
			java.nio.IntBuffer pW = st.mallocInt(1);
			java.nio.IntBuffer pH = st.mallocInt(1);
			glfwGetWindowSize(glfwWindow, pW, pH);
			org.lwjgl.glfw.GLFWVidMode vmd = glfwGetVideoMode(glfwGetPrimaryMonitor());

			assert vmd != null;
			glfwSetWindowPos(glfwWindow, (vmd.width() - pW.get(0)) / 2, (vmd.height() - pH.get(0)) / 2
			);
		}

		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);
		glfwShowWindow(glfwWindow);
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

		Window.changeScene(0);
	}

	public void loop() {
		float beginTime = (float) glfwGetTime();
		float endTime;
		float dt = -1.0f;

		while (!glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			glfwPollEvents();

			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);

			if (dt >= 0) {
				currentScene.update(dt);
			}

			glfwSwapBuffers(glfwWindow);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
	}
}
