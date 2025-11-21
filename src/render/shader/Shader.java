package render.shader;

public abstract class Shader{

    public int WIDTH = 800;
    public int HEIGHT = 600;
    public float scale = 0.1f;


    public abstract Pixel render(float x, float y);

    public Pixel render(double x,double y){
        return render((float) x,(float) y);
    }

    public boolean enabled(){
        return false;
    }
}
