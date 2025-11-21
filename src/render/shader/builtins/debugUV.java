package render.shader.builtins;

import render.shader.Shader;
import render.shader.Pixel;

public class debugUV extends Shader {
    @Override
    public Pixel render(float x, float y) {
        return new Pixel((int) (x * 255), (int) (y * 255), 0);
    }

    @Override
    public boolean enabled() {
        return true;
    }

}
