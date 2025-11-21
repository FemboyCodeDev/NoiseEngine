package noise;

public class simplex2D {

    private static final vec2D[] gradients = {
            new vec2D(1, 1), new vec2D(-1, 1), new vec2D(1, -1), new vec2D(-1, -1),
            new vec2D(1, 0), new vec2D(-1, 0), new vec2D(0, 1), new vec2D(0, -1)
    };

    private vec2D getGradient(vec2D vertex) {
        int hash = hashCoord(vertex);
        return gradients[hash & 7];
    }

    public vec2D skew(vec2D in){
        double n = 2;
        double F = (Math.sqrt(n+1)-1)/n;

        float x = in.x + (in.x + in.y) * (float)F;
        float y = in.y + (in.x + in.y) * (float)F;


        return new vec2D(x,y);

    }

    public triangle2D simplicalSubdivision(vec2D in){
        float x = in.x;
        float y = in.y;

        if (x > y) {
            return new triangle2D(new vec2D(0,0), new vec2D(1,0), new vec2D(1,1));

        } else {
            return new triangle2D(new vec2D(0,0), new vec2D(0,1), new vec2D(1,1));
        }
    }
    public triangle2D GradientSelection(vec2D skewedCoord, triangle2D triangle){
        float x = skewedCoord.x;
        float y = skewedCoord.y;

        x = (float)Math.floor(x);
        y = (float)Math.floor(y);


        vec2D v1 = new vec2D(x + triangle.vert1.x,y + triangle.vert1.y);
        vec2D v2 = new vec2D(x + triangle.vert2.x,y + triangle.vert2.y);
        vec2D v3 = new vec2D(x + triangle.vert3.x,y + triangle.vert3.y);

        return new triangle2D(v1,v2,v3);


    }

    public int hashCoord(vec2D coord){
        int x = (int)coord.x;
        int y = (int)coord.y;

        return x | (13 * y);

    }

    private float calculateContribution(vec2D input, vec2D vertex) {
        double n = 2;
        double G = (1.0 - 1.0 / Math.sqrt(n + 1.0)) / n;
        float r_squared = 0.6f;

        double x_unskewed = vertex.x - (vertex.x + vertex.y) * G;
        double y_unskewed = vertex.y - (vertex.x + vertex.y) * G;

        vec2D d = new vec2D(input.x - (float)x_unskewed, input.y - (float)y_unskewed);
        float d_squared = d.x * d.x + d.y * d.y;

        if (r_squared > d_squared) {
            float term = (float)Math.pow(r_squared - d_squared, 4);
            vec2D grad = getGradient(vertex);
            float dot_product = d.x * grad.x + d.y * grad.y;
            return term * dot_product;
        }
        return 0;
    }

    public float kernalSummation(vec2D input, triangle2D simplex){
        float total = 0;
        total += calculateContribution(input, simplex.vert1);
        total += calculateContribution(input, simplex.vert2);
        total += calculateContribution(input, simplex.vert3);
        return total;
    }


    public float calculate(vec2D point){

        vec2D SkewedPoint = skew(point);
        triangle2D simplex = simplicalSubdivision(SkewedPoint);
        triangle2D gradient = GradientSelection(SkewedPoint,simplex);
        return kernalSummation(point,gradient);

    }
    
    public void main(){
        System.out.println(calculate(new vec2D(2,0)));
    }
}
