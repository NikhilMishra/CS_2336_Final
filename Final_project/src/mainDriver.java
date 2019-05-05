import java.awt.Graphics;
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
import javafx.scene.layout.FlowPane;

public class mainDriver extends Application {
	@Override
	public void start(Stage stage) throws Exception {


		// the pane for the graph
		Pane circlePane = new CirclePane();
		circlePane.setStyle(" -fx-border-width: 5");
		circlePane.setPrefHeight(350);
		circlePane.setPrefWidth(805);
		
		// pane for shortest path button
		FlowPane spPane = new FlowPane();
		Label startVertex = new Label("Starting vertex:");
		TextField startVertexField = new TextField();
		startVertexField.setPrefWidth(40);
		Label endVertex = new Label("Ending vertex:");
		TextField endVertexField = new TextField();
		endVertexField.setPrefWidth(40);
		Button shortestPathButton = new Button("Show Shortest Path");
		spPane.setHgap(5);
		spPane.getChildren().addAll(startVertex,
				startVertexField, endVertex, endVertexField,
				shortestPathButton);
		Label shortestPathLabel = new Label("Find a shortest path");
		BorderPane shortestPathPane = new BorderPane();
		shortestPathPane.setTop(shortestPathLabel);
		shortestPathPane.setBottom(spPane);
		shortestPathPane.setStyle(" -fx-border-width: 5");
		shortestPathPane.setPadding(new Insets(5, 5, 5, 5));
		
		// pane for other buttons
		FlowPane buttonsPane = new FlowPane();
		buttonsPane.setAlignment(Pos.BOTTOM_CENTER);
		buttonsPane.setPadding(new Insets(10, 10, 10, 10));
		buttonsPane.setHgap(10);
		buttonsPane.setVgap(10);
		Button showMSTButton = new Button("Show MST");
		Label sourceVertexLabel = new Label("Source vertex: ");
		TextField sourceVertexField = new TextField();
		sourceVertexField.setPrefWidth(40);
		Button showAllSPButton = new Button("Show All SP From the Source");
		buttonsPane.getChildren().addAll(showMSTButton, sourceVertexLabel,
				sourceVertexField, showAllSPButton, shortestPathPane);
		
		// pane for instructions
		GridPane instructionPane = new GridPane();
		instructionPane.setPadding(new Insets(10, 10, 10, 10));
		Label instructTopLabel = new Label("INSTRUCTIONS");
		Label instructLeftLabel1 = new Label("Add:");
		Label instructLeftLabel2 = new Label("Move:");
		Label instructLeftLabel3 = new Label("Connect:");
		Label instructLeftLabel4 = new Label("Remove:");
		Label instructRightLabel1 = new Label("Left Click");
		Label instructRightLabel2 = new Label("Ctrl Drag");
		Label instructRightLabel3 = new Label("Drag");
		Label instructRightLabel4 = new Label("Right Click");
		instructionPane.add(instructTopLabel, 0, 0);
		instructionPane.add(instructLeftLabel1, 0, 1);
		instructionPane.add(instructLeftLabel2, 0, 2);
		instructionPane.add(instructLeftLabel3, 0, 3);
		instructionPane.add(instructLeftLabel4, 0, 4);
		instructionPane.add(instructRightLabel1, 1, 1);
		instructionPane.add(instructRightLabel2, 1, 2);
		instructionPane.add(instructRightLabel3, 1, 3);
		instructionPane.add(instructRightLabel4, 1, 4);
		
		// Align panes
		BorderPane rootPane = new BorderPane();
		rootPane.setBottom(buttonsPane);
		rootPane.setRight(circlePane);
		rootPane.setLeft(instructionPane);

		Scene scene = new Scene(rootPane, 1200, 400);
		stage.setTitle("Weighted Graph");
		stage.setMinHeight(500);
		stage.setMaxHeight(500);
		stage.setMinWidth(1000);
		stage.setMaxWidth(1000);
		stage.setScene(scene);
		stage.show();
	}
	
	//User-clicked vertices on graph
	private ArrayList<Point2D> points = new ArrayList<>();
	
	//Lines connecting vertices
	private ArrayList<Line> lines = new ArrayList<>();
	
	//User-created tree
	private AbstractGraph<Integer>.Tree tree = null;
	
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
		  
		  WeightedGraph<Integer> graph = new WeightedGraph<Integer>(vertices, edges);
		  
		  return graph;
		  
		 }
	
	//Pane for box to be in
	BorderPane pane = new BorderPane();

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);

	}

    
	
	
		
		
	

}