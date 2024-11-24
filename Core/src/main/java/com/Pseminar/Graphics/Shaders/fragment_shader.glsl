#version 460 core

in vec2 uv; // Input color from vertex shader
out vec4 finalColor; // Final output color

void main() {
    finalColor = vec4(uv.x, uv.y, 0.0, 1.0); // Set the output color to be fully opaque
}