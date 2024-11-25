#version 460 core

in vec2 uv; // Input color from vertex shader
out vec4 finalColor; // Final output color

uniform sampler2D testTexture;

void main() {
    finalColor = texture(testTexture, uv); // Set the output color to be fully opaque
}