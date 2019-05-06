package cs2336_GA;//Collin Best Package

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;


public class testCompletedDriver extends Application {
	
	//User-clicked vertices on graph
	private ArrayList<Point2D> points = new ArrayList<>();
	public static int vertexCount = 0;
	private ArrayList<Circle> circles = new ArrayList<>();
	//Lines connecting vertices
	public ArrayList<Line> lines = new ArrayList<>();
	
	//User-created tree
	private AbstractGraph<Integer>.Tree tree = null;
	
	//Creating buttons objects for Min. Spanning tree, Shortest Path from source,
	//and Shortest Path from specified points.
	private Button mstButton = new Button("Show MST");
	private Button spSrcButton = new Button("Show All SP From the Source");
	private Button allSPButton = new Button("Show Shortest Path");
	
	//Textfields for Source Vertex, Starting vertex, and Ending vertex
	private Label srcV = new Label("Source Vertex");
	private TextField srcVertexTxt = new TextField();
	
	private Label startV = new Label("Starting Vertex");
	private TextField startVertexTxt = new TextField();
	
	private Label endV = new Label("Ending Vertex");
	private TextField endVertexTxt = new TextField();
	
	private Label spLab = new Label("Find Shortest Path");
	
	//Instruction Labels
	private Label instructTitle = new Label("INSTRUCTIONS:");
	private Label instructAdd = new Label("Add:			Left Click");
	private Label instructMove = new Label("Move:		Ctrl Drag");
	private Label instructConnect = new Label("Connect:		Drag");
	private Label instructRemove = new Label("Remove:		Right Click");
	private WeightedGraph WG = null;
	private GraphEvent displayGraph = new GraphEvent();
	
	public class Line {
		
		  public Point2D p1;
		  public Point2D p2;
		  
		  public Line(Point2D p1, Point2D p2) {
			  
		   this.p1 = p1;
		   this.p2 = p2;
		   
		  }
		  
		  public double getWidth() {
			  
		   return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
		   
		  }
		  
	}
	
	private WeightedGraph<Integer> getGraph() {
		
		  List<Integer> vertices = new ArrayList<>();
		  
		  for (int i = 0; i < points.size(); i++) {
			  
			  vertices.add(i);
		   
		  }
		  
		  List<WeightedEdge> edges = new ArrayList<>();
		  
		  for (int i = 0; i < lines.size(); i++) {
			  
			  Line line = lines.get(i);
			  int u = points.indexOf(line.p1);
			  int v = points.indexOf(line.p2);
			  double weight = line.getWidth();
			  
		   if(!edges.contains(new WeightedEdge(u, v, weight))) {
			   
			   edges.add(new WeightedEdge(u, v, weight));
		    
			   edges.add(new WeightedEdge(v, u, weight));
		    
		   }
		   
		  }
		  
		  WeightedGraph<Integer> graph = new WeightedGraph<Integer>(edges, vertices);
		  
		  return graph;
		  
		 }
	
	//Pane for box to be in
	BorderPane pane = new BorderPane();

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage) throws Exception {
		
		//Set text field space
		srcVertexTxt.setPrefColumnCount(2);
		startVertexTxt.setPrefColumnCount(2);
		endVertexTxt.setPrefColumnCount(2);  
		
		//Instructions
		VBox instruct = new VBox();
				
		instruct.getChildren().addAll(instructTitle, instructAdd, instructMove, instructConnect, instructRemove);
		
		instruct.setMargin(instructTitle, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructAdd, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructMove, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructConnect, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructRemove, new Insets(10, 0, 5, 20));
		
		instruct.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-height: 5;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: black;");
		
		pane.setLeft(instruct);
		BorderPane.setAlignment(instruct, Pos.TOP_LEFT);
				
	

		//Starting vert/Ending Vert
		HBox boxRight = new HBox(5);
		boxRight.setStyle("-border-color: brown");
		
		boxRight.getChildren().addAll(startV, startVertexTxt, endV , endVertexTxt, allSPButton);
		
		boxRight.setAlignment(Pos.CENTER);
		 
		
		//HBox for left side options
		HBox boxLeft = new HBox(5);
				
	    boxLeft.getChildren().addAll( mstButton, srcV, srcVertexTxt, spSrcButton);  
	    
	    boxLeft.setAlignment(Pos.CENTER);
	    
	    
	    //Bottom box
	    HBox bottomBox = new HBox();
	    
	    bottomBox.getChildren().addAll(boxLeft, boxRight);
	    
	    bottomBox.setMargin(boxLeft, new Insets(20, 20, 20, 20));
	    
	    bottomBox.setMargin(boxRight, new Insets(20, 20, 20, 20));
	    
	    pane.setBottom(bottomBox);
	    

	    
	    pane.setOnMouseClicked(e-> {
	    	
            double x = e.getX();
            double y = e.getY();

            if (e.getButton() == MouseButton.PRIMARY) {
            	
                Circle c = drawPoint(x,y);
                //System.out.println("x,y is " + x + "," + y); //DEBUG CODE
                pane.getChildren().add(c);
                
            } else if ( e.getButton() == MouseButton.SECONDARY ) {

                removePoint(x, y);
                
            }


        });
	    
	    pane.setOnMouseDragged(e-> {
	    	for(int i =0; i < points.size(); i ++) {

	    	}
	    });
	    
	    pane.setOnMouseReleased(e-> {
	    	
	    });
	    
	    //Creating a scene object 
	    Scene scene = new Scene(pane, 980, 350); 
	       
	    //Setting title to the Stage 
	    stage.setTitle("Weighted Graph"); 
	         
	    //Adding scene to the stage 
	    stage.setScene(scene);
	      
	    //Displaying the contents of the stage 
	    stage.show(); 
	    
	    
	    
		
	}
	/*
	private Circle drawPoint(double x, double y) { // WORKING PERFECT
		
		//isclose
        Circle c = new Circle(x, y, 20, Color.TRANSPARENT);
        c.setStroke(Color.BLACK);
        int xVert = (int)x;//NEW
        int yVert = (int)y;//NEW
        Point2D newPoint = new Point2D(xVert,yVert);//NEW
        points.add(vertexCount, newPoint);//NEW
        System.out.println("vertexcount is " + vertexCount + ", new point is at " + points.get(vertexCount));
        vertexCount++;//NEW
        return c;
        
    }

	
	private void removePoint(double x, double y) { //NOT REMOVING FROM ARRAY OR FROM FRAME

        ObservableList<Node> list = pane.getChildren();
        
        for (int i = list.size() - 1; i >= 0; i--) {
        	
            Node c = list.get(i);
            
            if (c instanceof Circle && c.contains(x, y)) {
            	for(int j = 0; j < points.size(); j++) {
            			if(points.get(j).equals(new Point2D(x,y))) {
            				System.out.println(points.get(j) + "at index" + vertexCount + " to be deleted");
            				points.remove(j);
            				vertexCount--;
            				pane.getChildren().remove(c);
            				 break;
            			}
            	}
    			//System.out.println("array x,y=" + points.get(i).getX() + ", " + points.get(i).getY());
    			System.out.println("circle x, y = "+   x + ", " + y);

                
            }
            
        }
        
    }
	
	private void dragPoint(double x, double y) { //needs more javafx knowledge - will work on as a console command first
		ObservableList<Node> list = pane.getChildren();
		
		for(int i = list.size() -1; i>=0;i--) {
			Node c = list.get(i);
			//if(c instanceofCircle)
		}
	}
*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);

	}

    

	class GraphEvent extends Pane {
		private CircleVertex startV = null;
		private boolean lineExists = false;
		private AbstractGraph.Tree tree = null;
		private List path = null;
		private javafx.scene.shape.Line line = new javafx.scene.shape.Line();
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
			if(CircleVertex.getWeight(vertices.get(i), v) <=2 * CircleVertex.Radius + 5) { //dont know what the math means
				return true;
			}
			
		}
		return false;
	}	
	
	}
	public static class CircleVertex{
		static int Radius = 20;
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
		public static double getWeight(CircleVertex v1, CircleVertex v2) {//was static
			return getWeight(v1.x,v1.y,v2.x,v2.y);
		}
		public static double getWeight(double x1, double x2, double y1, double y2) { //was static
			double c = Math.pow((x1-x2), 2) + Math.pow((y1-y2),2);
			return Math.sqrt(c);
		}
		public boolean equals(Object o) {
			CircleVertex v = (CircleVertex)o;
			return v.getX() ==x && v.getY() == y;
		}
		public boolean contains(double u, double v) {
			return getWeight(x,y,u,v)<=Radius;
		}
		public boolean contains(Point2D p) {
			return getWeight(x,y,p.getX(),p.getY()) <=Radius;
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

