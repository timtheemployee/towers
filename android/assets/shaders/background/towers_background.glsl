#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;

//TODO need move it to upside
//TODO make another buildings overlapping this one
void first_tower(in vec2 st, inout vec3 color) {
    float horizontal = 1.0 - step(0.3, st.x) - (1.0 - step(0.2, st.x));
    float vertical = step(0.1, mod(st.y, 0.2)) * (1.0 - step(0.3, st.x));
    color = (vertical + horizontal) * color;
}

void main () {
    vec2 st = gl_FragCoord.xy / u_resolution.xy;

    vec3 color_in = vec3(1.0);

    first_tower(st, color_in);

    gl_FragColor = vec4(color_in, 0.8);
}