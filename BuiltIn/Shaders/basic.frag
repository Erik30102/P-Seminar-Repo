#version 460 core
#define MAXTEXTURES 8 // TODO: set in sprite batch based on max textures bound at one time

in int texId;
in vec2 uv;

out vec4 colorOut;

uniform sampler2D textures[MAXTEXTURES];

void main() {
    colorOut = texture(textures[texId], uv);
}