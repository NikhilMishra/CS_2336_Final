package cs2336_GA;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.util.List;

import cs2336_GA.AbstractGraph.Edge;

public class GraphEvent extends Pane {
	private CircleVertex startV = null;
	private boolean lineExists = false;
	private AbstractGraph.Tree tree = null;
	private List path = null;
	private Line line = new Line();
	private ArrayList<CircleVertex> vertices = new ArrayList<>();
	private ArrayList<VertexEdge> edges = new ArrayList<>();
	
	public GraphEvent() {
		setOnMousePressed(e->
		{
			if(e.getButton() == MouseButton.PRIMARY) {
				CircleVertex vAdd = new CircleVertex(e.getX(), e.getY());
				if(!doesExist(vAdd)) {
					if(!lineExists) {
						startV = vAdd;
						lineExists = true;
						line.setStartX(e.getX());
						line.setStartY(e.getY());
						line.setEndX(e.getX());
						line.setEndY(e.getY());
					}
				}
				else { //v does exist, save for drag
					vertices.add(new CircleVertex(e.getX(),e.getY()));
					this.set_Tree(null);}
				}
			else if(e.getButton() == MouseButton.SECONDARY) {
				CircleVertex vDel = new CircleVertex(e.getX(), e.getY());
				if(!doesExist(vDel)) {
					vertices.remove(vDel);
					removeAdj(vDel);
					this.set_Tree(null);
				}
			}
		});
		setOnMouseReleased(e-> {
			CircleVertex vRel = new CircleVertex(e.getX(),e.getY());
			if(lineExists && doesExist(vRel) && !vRel.equals(startV)) {
				edges.add(new VertexEdge(startV,vRel));
				this.set_Tree(null);
				this.set_Path(null);
			}
			else {
				//clear line
				line.setStartX(0);
				line.setStartY(0);
				line.setEndX(0);
				line.setEndY(0);
				lineExists = false;
				repaint();
			}
		});
		setOnMouseDragged(e-> {
			if(e.isControlDown()) {
				lineExists = false;
				CircleVertex vMove = new CircleVertex(e.getX(),e.getY());
				if(doesExist(vMove)) {
					vMove.setX(e.getX());
					vMove.setY(e.getY());
					this.set_Tree(null);
					this.set_Path(null);
				}
			}
			else if(lineExists) {
				line.setEndX(e.getX());
				line.setEndY(e.getY());
				repaint();
			}
		});
	}
public void set_Tree(AbstractGraph.Tree tree) {
	this.tree = tree;
	repaint();
}
public void set_Path(List path) {
	this.path = path;
	repaint();
}
public void removeAdj(CircleVertex vIn) {
	for(int i = 0; i < edges.size(); i++) {
		if(edges.get(i).u.equals(vIn) || edges.get(i).v.equals(vIn)) {
			edges.remove(i--);
		}
	}
}
protected void repaint() {
	getChildren().clear();
	getChildren().add(line);
	for(int i = 0; i < edges.size(); i++) {
		double x1 = edges.get(i).u.getX();
		double y1 = edges.get(i).u.getY();
		double x2 = edges.get(i).v.getX();
		double y2 = edges.get(i).v.getY();
		getChildren().addAll(new Line(x1,y1,x2,y2));
		double weight = CircleVertex.getWeight(x1, y1,x2,y2);
		getChildren().addAll(new Text((x1+x2)/2, (y1+y2)/2, (int)weight + ""));
	}
	//draw(20,20);
	if(tree!=null) {
		for(int i = 0; i <   )
	}
}
public boolean doesExist(CircleVertex v) {
	for(int i = 0; i < vertices.size(); i++) {
		if(vertices.get(i).contains(v.x, v.y)) {
			return true;
		}
	}
	return false;
}
public boolean isClose(CircleVertex v) {
	for(int i = 0; i < vertices.size(); i++) {
		if(CircleVertex.getWeight(vertices.get(i), v) <=2 * CircleVertex.RADIUS_ + 5) { //dont know what the math means
			return true;
		}
		
	}
	return false;
}
static class CircleVertex{
	final static int RADIUS_ = 20;
	double x;
	double y;
	public CircleVertex() {
		
	}
	public CircleVertex(double inX, double inY) {
		this.x = inX;
		this.y = inY;
	}
	public CircleVertex(Point2D p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	public double getX () {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public void setX(double inX) {
	this.x = inX;
	}
	public void setY(double inY) {
		this.y= inY;
	}
	public double getWeight(CircleVertex v) {
		return getWeight(x,y,v.x,v.y);
	}
	public static double getWeight(CircleVertex v1, CircleVertex v2) {
		return getWeight(v1.x,v1.y,v2.x,v2.y);
	}
	public static double getWeight(double x1, double x2, double y1, double y2) {
		double c = Math.pow((x1-x2), 2) + Math.pow((y1-y2),2);
		return Math.sqrt(c);
	}
	public boolean equals(Object o) {
		CircleVertex v = (CircleVertex)o;
		return v.getX() ==x && v.getY() == y;
	}
	public boolean contains(double u, double v) {
		return getWeight(x,y,u,v)<=RADIUS_;
	}
	public boolean contains(Point2D p) {
		return getWeight(x,y,p.getX(),p.getY()) <=RADIUS_;
	}
}
	
	public static class VertexEdge{
		CircleVertex u,v;
		public VertexEdge(CircleVertex u, CircleVertex v) {
			this.u=u;
			this.v=v;
		}
	}
}
