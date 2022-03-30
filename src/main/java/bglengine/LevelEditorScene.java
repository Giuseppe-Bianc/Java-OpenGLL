package bglengine;

import constant.Const;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

	private int vertexID, fragmentID, shaderProgram;

	private static final float[] vertexArray = {
			100.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
			0.5f, 100.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
			100.5f, 100.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
			0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
	};
	private static final int[] elementArray = {
			2, 1, 0,
			0, 1, 3
	};

	private int vaoID, vboID, eboID;

	private Shader defaultShader;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		this.camera = new Camera(new org.joml.Vector2f(-200, -300));
		defaultShader = new Shader(Const.SHPT);
		defaultShader.compile();
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();

		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

		int positionsSize = 3;
		int colorSize = 4;
		int vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, (long) positionsSize * Float.BYTES);
		glEnableVertexAttribArray(1);
	}

	@Override
	public void update(float dt) {
		camera.position.x -= dt * 50.0f;
		camera.position.y -= dt * 20.0f;

		defaultShader.use();
		defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		defaultShader.uploadMat4f("uView", camera.getViewMatrix());
		defaultShader.uploadFloat("uTime", (float) glfwGetTime());
		glBindVertexArray(vaoID);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		defaultShader.detach();
	}
}
