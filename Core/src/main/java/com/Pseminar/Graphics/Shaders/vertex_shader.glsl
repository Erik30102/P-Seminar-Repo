#version 460 core

layout(location = 0) in vec2 position; // Vertex position input
layout(location = 1) in vec2 i_uv;    // Vertex color input

out vec2 uv; // Output texcoords to fragment shader

uniform mat4 projectionMatrix;
uniform mat4 transformMatrix;

void main() {
    uv = i_uv;                 // Pass the texcoords to the fragment shader
    gl_Position = projectionMatrix * vec4(position.x, position.y, -10.0, 1.0); // Set the position of the vertex
}