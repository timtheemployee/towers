attribute vec4 a_position;

uniform mat4 u_projModelView;

varying vec4 v_position;

void main() {
    v_position = a_position;
    gl_Position = u_projModelView * a_position;
}