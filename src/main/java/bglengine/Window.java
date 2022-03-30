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

	private final int width, height;
	private final String title;
	private static Window window;
	private long glfwWindow;

	public float r, g, b, a;
	private static Scene currentScene;

	public Window() {
		this.width = 960;
		this.height = 540;
		this.title = "Mario";
		this.r = this.g = this.b = this.a = 1;
	}

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;
	}

	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currentScene = new LevelEditorScene();
				currentScene.init();
				break;
			case 1:
				currentScene = new LevelScene();
				currentScene.init();
				break;
			default:
				assert Const.NRM : "Unknown scene '" + newScene + "'";
				break;
		}
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

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (glfwWindow == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		try (MemoryStack st = stackPush()) {
			IntBuffer pW = st.mallocInt(1);
			IntBuffer pH = st.mallocInt(1);
			glfwGetWindowSize(glfwWindow, pW, pH);
			GLFWVidMode vmd = glfwGetVideoMode(glfwGetPrimaryMonitor());

			assert vmd != null;
			glfwSetWindowPos(glfwWindow, (vmd.width() - pW.get(0)) / 2, (vmd.height() - pH.get(0)) / 2
			);
		}
		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);

		glfwShowWindow(glfwWindow);
		GL.createCapabilities();
		Window.changeScene(0);
	}

	private void loop() {
		double beginTime = glfwGetTime(), endTime, dt = -1.0f;
		GL.createCapabilities();

		while (!glfwWindowShouldClose(glfwWindow)) {
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (dt >= 0) {
				currentScene.update((float) dt);
			}

			glfwSwapBuffers(glfwWindow);
			glfwPollEvents();
			endTime = glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
	}
}