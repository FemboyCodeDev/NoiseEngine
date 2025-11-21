package render.shader.builtins;
import noise.vec2D;
import render.shader.Pixel;
import render.shader.Shader;
import noise.simplex2D;

public class simplex extends Shader {
    @Override
    public Pixel render(float x, float y) {
        x = x*WIDTH*scale;
        y = y*HEIGHT*scale;
        float value = simplex2D.calculate(new vec2D(x,y))*100;
        return new Pixel((int)value,(int)value,(int)value);


    }

    @Override
    public boolean enabled() {
        return true;
    }

}
