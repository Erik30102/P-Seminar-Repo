#version 460 core
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 i_uv;
layout(location = 2) in int i_texId;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out int texId;
out vec2 uv;

void main() {
    texId = i_texId;
    uv = i_uv;
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
}