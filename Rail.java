
/**
 * Represents a {@link Rail} object.
 */
public class Rail {

    /**
     * Create a new {@link Rail} object.
     * @param from Connected to this {@link Rail}.
     * @param to Connected to this {@link Rail}. If Tunnel will build this neighbor will be replaced.
     */
    public Rail(Rail from, Rail to) {
        id = idGenerator++;
        this.from = from;
        this.to = to;
    }

    public static int idGenerator = 0;
    protected int id;
    protected Rail from;
    protected Rail to;
    protected Car car;

    protected float x, y;
    protected float angle;

    /**
     * Moves the {@link Car} to the next Rail in the network.
     * @param c The {@link Car} that moves.
     * @param prev The Rail where the {@link Car} came from.
     * @return The {@link Rail} where c stands.
     * @throws Exception In occasion of collision!
     */
    public Rail carMoves(Car c, Rail prev) throws Exception {
        car = null;
        Rail nextRail = getNextRail(prev);
        nextRail.addCar(c);
        return nextRail;
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    protected Rail getNextRail(Rail prev) {
        if(from == prev) {
            return to;
        } else {
            return from;
        }
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    protected void addCar(Car c) throws Exception {
        System.out.println(this);
        if (car != null) throw new Exception("Collision!");
        car = c;
        System.out.println(this);
    }
    
    /**
     * Sets 'from' of the rail.
     * @param r Rail to be set.
     */
    public void setFrom(Rail r) {
        from = r;
    }

    /**
     * Sets 'to' of the rail.
     * @param r Rail to be set.
     */
    public void setTo(Rail r) {
        to = r;
    }

    /**
     * Sets 'x' of the rail.
     * @param x 'x' to be set.
     */
    public void setX(float x) { this.x = x; }

    /**
     * Sets 'y' of the rail.
     * @param y 'y' to be set.
     */
    public void setY(float y) { this.y = y; }

    /**
     * Sets 'angle' of the rail.
     * @param angle 'angle' to be set.
     */
    public void setAngle(float angle) { this.angle = angle; }

    /**
     * Gives 'from' back.
     * @return Rail 'from' of this rail.
     */
    public Rail getFrom() {
        return from;
    }

    /**
     * Gives 'to' back.
     * @return Rail 'to' of this rail.
     */
    public Rail getTo() {
        return to;
    }

    /**
     * Gives back the id of the rail.
     * @return The id.
     */
    public int getId(){return id;}

    @Override
    public String toString() {
        return "Rail{" +
                "id=" + id +
                ", from=" + (from != null ? from.id : "-1") +
                ", to=" + (to != null ? to.id : "-1") +
                ", car=" + (car != null ? car.getColor() : "null") +
                '}';
    }
}