package renderer;

import constant.Const;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

	private int shaderProgramID;

	private String vertexSource, fragmentSource, filepath;

	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(" + Const.TYP + ")( )+([" + Const.RNG + Const.RNGD + "]+)");

			int index = source.indexOf(Const.TYP) + 6;
			int eol = source.indexOf(Const.LEND, index);
			String firstPattern = source.substring(index, eol).trim();

			index = source.indexOf(Const.TYP, eol) + 6;
			eol = source.indexOf(Const.LEND, index);
			String secondPattern = source.substring(index, eol).trim();

			if (firstPattern.equals(Const.VRT)) {
				vertexSource = splitString[1];
			} else if (firstPattern.equals(Const.FRG)) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException(Const.UNT + firstPattern + Const.QT);
			}

			if (secondPattern.equals(Const.VRT)) {
				vertexSource = splitString[2];
			} else if (secondPattern.equals(Const.FRG)) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException(Const.UNT + secondPattern + Const.QT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			assert Const.NRM : Const.ERR + "Could not open file for shader: '" + filepath + Const.QT;
		}
	}

	public void compile() {
		int vertexID, fragmentID;
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);

		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println(Const.ERR2 + filepath + Const.SPC +"Vertex" + Const.SCF);
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);

		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println(Const.ERR2 + filepath + Const.SPC +"Fragment" + Const.SCF);
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}

		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);

		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println(Const.ERR2 + filepath + "'\n\tLinking of shaders failed.");
			System.out.println(glGetProgramInfoLog(shaderProgramID, len));
			assert false : "";
		}
	}

	public void use() {
		glUseProgram(shaderProgramID);
	}

	public void detach() {
		glUseProgram(0);
	}
}
