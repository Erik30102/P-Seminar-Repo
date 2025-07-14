#version 460 core
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 i_uv;
layout(location = 2) in float i_texId;
layout(location = 3) in vec4 i_tintColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out int texId;
out vec2 uv;
out vec4 tintColor;

void main() {
    texId = int(i_texId);
    uv = i_uv;
    tintColor = i_tintColor;
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
}