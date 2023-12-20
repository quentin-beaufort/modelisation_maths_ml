public class Vector {
   
    public float x, y;

    Vector(float _x, float _y) {
        this.x = _x;
        this.y = _y;
    }

    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void mult(double n) {
        this.x *= n;
        this.y *= n;
    }

}
