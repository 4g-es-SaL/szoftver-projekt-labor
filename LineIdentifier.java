/**
 * Created by matech on 2017. 05. 06..
 */
public class LineIdentifier {
    Rail a;
    Rail b;

    public LineIdentifier(Rail a, Rail b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LineIdentifier)) return false;

        LineIdentifier li = (LineIdentifier) obj;
        return (a == li.a && b == li.b) || (a == li.b && b == li.a);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
