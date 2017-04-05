/**
 * Created by aliras on 2017.04.05..
 */
public class CrossRail extends Rail{

    private Rail from2;
    private Rail to2;

    /**
     * Create a new {@link Rail} object.
     *
     * @param from Connected to this {@link Rail}.
     * @param to   Connected to this {@link Rail}. If Tunnel will build this neighbor will be replaced.
     */
    public CrossRail(Rail from, Rail to, Rail from2, Rail to2) {
        super(from, to);
        this.from2 = from2;
        this.to2 = to2;
    }

    public void setFrom2(Rail f2){
        from2 = f2;
    }

    public void setTo2(Rail t2){
        to2 = t2;
    }

    @Override
    public String toString() {
        return "CrossRail{" +
                super.toString() +
                ", from2=" + from2.id +
                ", to2=" + to2.id + "}";
    }
}
