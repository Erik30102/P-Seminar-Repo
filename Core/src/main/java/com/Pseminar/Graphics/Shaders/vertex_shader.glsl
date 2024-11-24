#version 460 core

layout(location = 0) in vec2 position; // Vertex position input
layout(location = 1) in vec2 i_uv;    // Vertex color input

out vec2 uv; // Output texcoords to fragment shader

void main() {
    uv = i_uv;                 // Pass the texcoords to the fragment shader
    gl_Position = vec4(position.x, position.y, 0.0, 1.0); // Set the position of the vertex
}