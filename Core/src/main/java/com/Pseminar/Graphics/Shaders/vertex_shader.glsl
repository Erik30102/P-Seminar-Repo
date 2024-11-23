#version 460 core

layout(location = 0) in vec3 position; // Vertex position input
layout(location = 1) in vec3 color;    // Vertex color input

out vec3 fragColor; // Output color to fragment shader

void main() {
    gl_Position = vec4(position, 1.0); // Set the position of the vertex
    fragColor = color;                 // Pass the color to the fragment shader
}