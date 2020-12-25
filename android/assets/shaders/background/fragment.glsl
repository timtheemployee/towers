#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_position;

uniform vec2 u_resolution;
uniform vec3 u_bottom_color;
uniform vec3 u_top_color;

void main() {
    vec2 st = v_position.xy / u_resolution;

    float p = smoothstep(0.0, 1.0, st.y);

    gl_FragColor = vec4(u_bottom_color, 1.0) + vec4(u_top_color, 1.0)  * vec4(p);
}