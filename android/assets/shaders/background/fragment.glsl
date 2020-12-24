#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_position;

uniform vec2 u_resolution;
uniform float u_time;

void main() {
    vec3 bottomColor = vec3(0.45, 0.44, 0.36);
    vec3 topColor = vec3(0.34, 0.59, 1.0);
    vec2 st = v_position.xy / u_resolution;

    float p = smoothstep(0.0, 1.0, st.y);

    gl_FragColor = vec4(bottomColor, 1.0)  + vec4(topColor, 1.0) * vec4(p);
}