#version 460 core

in vec3 fragColor; // Input color from vertex shader
out vec4 finalColor; // Final output color

void main() {
    finalColor = vec4(fragColor, 1.0); // Set the output color to be fully opaque
}