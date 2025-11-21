package render.shader;

public abstract class Shader{



    public abstract Pixel render(float x, float y);

    public Pixel render(double x,double y){
        return render((float) x,(float) y);
    }

    public boolean enabled(){
        return false;
    }
}
