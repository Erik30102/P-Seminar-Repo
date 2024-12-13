package com.Pseminar.renderer;

import org.lwjgl.opengl.GL46;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class Shader {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public Shader() throws Exception {
        programId = GL46.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL46.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL46.GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = GL46.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        GL46.glShaderSource(shaderId, shaderCode);
        GL46.glCompileShader(shaderId);

        if (GL46.glGetShaderi(shaderId, GL46.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + GL46.glGetShaderInfoLog(shaderId, 1024));
        }

        GL46.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        GL46.glLinkProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            GL46.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL46.glDetachShader(programId, fragmentShaderId);
        }

        GL46.glValidateProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        GL46.glUseProgram(programId);
    }

    public void unbind() {
        GL46.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            GL46.glDeleteProgram(programId);
        }
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(programId, uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, int value) {
        GL46.glUniform1i(GL46.glGetUniformLocation(programId, uniformName), value);
    }

    public void setUniform(String uniformName, int[] value) {
        GL46.glUniform1iv(GL46.glGetUniformLocation(programId, uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        GL46.glUniform1f(GL46.glGetUniformLocation(programId, uniformName), value);
    }

    public static String loadResource(String fileName) throws Exception {
        String basePath = System.getProperty("user.dir") + "\\..\\Core\\src\\main\\java\\com\\Pseminar\\Graphics\\shaders\\";
        String fullPath = basePath + fileName;
        String result;
        try (BufferedReader in = new BufferedReader(new FileReader(fullPath))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (IOException e) {
            throw new Exception("Error loading shader file: " + fullPath);
        }
        return result;
    }
}
