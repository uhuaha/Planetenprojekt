package plantenbahnen;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class SpaceObject extends Circle {
    
    private double x, y;
    private double xNew, yNew;
    private double mass;
    private Vector velocityVector;
    private Vector velocityVectorNew;
    private int size;
    //private int thickness;
    //private int[] colour;
    private String name;

    private boolean drawTail;
    private int tailSize;
    //private Line[] tail;
    private ArrayList<Circle> tail;
    private ArrayList<Double[]> pastCoordinates;
    private int tailIndex;
    private int tailIncrement;

    //arrow
    private Line line = new Line();
    private int counter=0;

    private GuiElements gui;

    private final Object lock = new Object();
    
    SpaceObject(String name, double x, double y, double mass, Vector velocityVector, 
        int size, int[] colour, GuiElements gui) {

        this.gui = gui;

        this.x = x;
        this.y = y;
        this.setCircleCoordinates();

        this.mass = mass;
        this.velocityVector = velocityVector;

        pastCoordinates = new ArrayList<>();
        
        this.name = name;
        this.setRadius(size);
        this.setFill(Color.rgb(colour[0],colour[1],colour[2]));

        this.drawTail = false;
        this.tail = new ArrayList<>();
        //this.tailSize = tailSize;
        this.tailIndex = 0;
        this.tailIncrement = 10000;

        Platform.runLater(() -> this.gui.getPaneDraw().getChildren().add(this.line));
        /*
        this.tail = new Line[this.tailSize];
        for (int i=0; i<this.tailSize; i++){
            this.tail[i] = new Line();
        }
        */
    }

    public void setVelocityVector(Vector velocityVector) {
        this.velocityVector = velocityVector;
    }

    public Vector getVelocityVector() {
        return this.velocityVector;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
    
    public void setTailSize(int tailSize) {
        this.tailSize = tailSize;
    }

    public int getTailSize() {
        return this.tailSize;
    }
    
    public void setDrawTail(boolean drawTail) {
        this.drawTail = drawTail;
    }
    
    public boolean getDrawTail() {
        return this.drawTail;
    }
    
    public void setColour(int[] colour) {
        this.setFill(Color.color(colour[0],colour[1],colour[2]));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXNew() {
        return xNew;
    }

    public double getYNew() {
        return yNew;
    }

    public void setX(double value){
        this.x=value;
    }

    public void setY(double value){
        this.y=value;
    }

    public void setXNew(double xNew) {
        this.xNew = xNew;
    }

    public void setYNew(double yNew) {
        this.yNew = yNew;
    }

    public Vector getVelocityVectorNew() {
        return velocityVectorNew;
    }

    public void setVelocityVectorNew(Vector velocityVectorNew) {
        this.velocityVectorNew = velocityVectorNew;
    }

    public double getMass() {
        return this.mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Vector getPositionVector(){
        return new Vector(this.x,this.y);
    }

    
    public void addPositionVectorToCoordinates(Vector v){
        this.xNew = this.x+v.x();
        this.yNew = this.y+v.y();
    }

    public void setNewCoordinates(){

        /*
        this.counter++;
        if (this.counter>=10000){
            drawArrows();
            this.counter=0;
        }
        */

        /*
        if ( this.tailIndex == 0 ) {
            addLineToTail();
        } else if ( this.tailIndex == this.tailIncrement ) {
            addLineToTail();
            this.tailIndex = 1;
        }
        this.tailIndex++;
        */
        //addToPastCoordinates();

        this.tailIndex++;
        if ( this.drawTail && this.tailIndex % this.tailIncrement == 0 ) {
            Circle c = new Circle();
            c.setCenterX(this.x * this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());
            c.setCenterY(this.y * this.gui.getScaleFactor() + this.gui.getPaneHalfHeight());
            c.setRadius(this.getRadius()/4.0);
            c.setFill(this.getFill());
            this.tail.add(0, c);
            // We need Platform.runlater() in order to modify the JavaFX GUI thread
            Platform.runLater(() -> this.gui.getPaneDraw().getChildren().add(c));
            while ( this.tail.size() > (int) this.gui.getSliderTailLength().getValue() ) {
                // some circle objects remain on the pane, so, make them at least
                // fully translucent
                this.tail.get(this.tail.size()-1).setOpacity(0.0);
                // delete from pane and tail
                Platform.runLater(() -> this.gui.getPaneDraw().getChildren().remove(this.tail.get(this.tail.size()-1)));
                this.tail.remove(this.tail.size()-1);
            }
        }
        
        this.x = this.xNew;
        this.y = this.yNew;
        this.velocityVector = this.velocityVectorNew;
    }

    
    public ArrayList<Circle> getTail() {
        return this.tail;
    }
    
    public double getlLength(ArrayList<Double[]> l, Double[] newest) {
        l.add(newest);
        double length = 0;
        for (int i=0; i<l.size()-1; i++) {
            length += Math.sqrt(Math.pow((l.get(i)[0] - l.get(i+1)[0]), 2) + Math.pow((l.get(i)[1] - l.get(i+1)[1]), 2));
        }
        return length;
    }

    public void setCircleCoordinates() {
        this.setCenterX(this.x * this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());
        this.setCenterY(this.y * this.gui.getScaleFactor() + this.gui.getPaneHalfHeight());
    }

    public void drawArrows(){


        //Platform.runLater(() -> this.gui.getPane().getChildren().remove(line));

        this.line.setStartX(this.x * this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());
        this.line.setStartY(this.y * this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());
        this.line.setEndX((this.x+this.velocityVector.x()*5)*this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());
        this.line.setEndY((this.y+this.velocityVector.y()*5)*this.gui.getScaleFactor() + this.gui.getPaneHalfWidth());


    }
}