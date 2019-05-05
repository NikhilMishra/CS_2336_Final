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

public class mainDriver extends Application {
	// User-clicked vertices on graph
	private ArrayList<Point2D> points = new ArrayList<>();

	// Lines connecting vertices
	private ArrayList<Line> lines = new ArrayList<>();

	// User-created tree
	private AbstractGraph<Integer>.Tree tree = null;

	// Creating buttons objects for Min. Spanning tree, Shortest Path from
	// source,
	// and Shortest Path from specified points.
	private Button mstButton = new Button("Show MST");
	private Button spSrcButton = new Button("Show All SP From the Source");
	private Button allSPButton = new Button("Show Shortest Path");

	// Textfields for Source Vertex, Starting vertex, and Ending vertex
	private Label srcV = new Label("Source Vertex");
	private TextField srcVertexTxt = new TextField();

	private Label startV = new Label("Starting Vertex");
	private TextField startVertexTxt = new TextField();

	private Label endV = new Label("Ending Vertex");
	private TextField endVertexTxt = new TextField();

	private Label spLab = new Label("Find Shortest Path");

	// Instruction Labels
	private Label instructTitle = new Label("INSTRUCTIONS:");
	private Label instructAdd = new Label("Add:			Left Click");
	private Label instructMove = new Label("Move:		Ctrl Drag");
	private Label instructConnect = new Label("Connect:		Drag");
	private Label instructRemove = new Label("Remove:		Right Click");

	public class Line {

		public Point2D p1;
		public Point2D p2;

		public Line(Point2D p1, Point2D p2) {

			this.p1 = p1;
			this.p2 = p2;

		}

		public double getWidth() {

			return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
					+ (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));

		}

	}

	private WeightedGraph<String> getGraph() {
		List<Integer> vertices = new ArrayList<>();
		String[] vertice = {"0", "1", "2", "3", "4"};

		int[][] edgess =     {{0, 1, 2}, {0, 3, 8}, 
			      {1, 0, 2}, {1, 2, 7}, {1, 3, 3},
			      {2, 1, 7}, {2, 3, 4}, {2, 4, 5},
			      {3, 0, 8}, {3, 1, 3}, {3, 2, 4}, {3, 4, 6},
			      {4, 2, 5}, {4, 3, 6}};
		WeightedGraph<String> graph1 = new WeightedGraph<>(vertice, edgess);
		for (int i = 0; i < points.size(); i++) {

			vertices.add(i);

		}

		List<WeightedEdge> edges = new ArrayList<>();

		for (int i = 0; i < lines.size(); i++) {

			Line line = lines.get(i);
			int u = points.indexOf(line.p1);
			int v = points.indexOf(line.p2);
			double weight = line.getWidth();

			if (!edges.contains(new WeightedEdge(u, v, weight))) {

				edges.add(new WeightedEdge(u, v, weight));

				edges.add(new WeightedEdge(v, u, weight));

			}

		}

		WeightedGraph<Integer> graph = new WeightedGraph<Integer>(vertices, edges);

		return graph1;

	}

	// Pane for box to be in
	BorderPane pane = new BorderPane();

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage) throws Exception {
		//if shortest path button is pressed
		allSPButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//get user input
				String value1 = String.valueOf(startVertexTxt.getText());
				String value2 = String.valueOf(endVertexTxt.getText());
				//initialize graph
				WeightedGraph thisGraph = getGraph();
				WeightedGraph<String>.ShortestPathTree tree1 = thisGraph.getShortestPath(getGraph().getIndex(value2));
				java.util.List<String> path = tree1.getPath(getGraph().getIndex(value1));
				//print path
				for (String s : path) {
					System.out.print(s + " ");
				}
				System.out.println();
			}
		});
		//if mst button is pressed
		mstButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Initialize graph
				WeightedGraph thisGraph = getGraph();
				if ((thisGraph.getVertices()).size() >= 2) {
					WeightedGraph<String>.MST tree1 = thisGraph.getMinimumSpanningTree();
					java.util.List<String> path =tree1.getPath(getGraph().getSize()-1);
					//print path
					for (String s : path) {
						System.out.print(s + " ");
					}
					System.out.println();
					}
			}
		});
		// Set text field space
		srcVertexTxt.setPrefColumnCount(2);
		startVertexTxt.setPrefColumnCount(2);
		endVertexTxt.setPrefColumnCount(2);

		// Instructions
		VBox instruct = new VBox();

		instruct.getChildren().addAll(instructTitle, instructAdd, instructMove, instructConnect, instructRemove);

		instruct.setMargin(instructTitle, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructAdd, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructMove, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructConnect, new Insets(10, 0, 5, 20));
		instruct.setMargin(instructRemove, new Insets(10, 0, 5, 20));

		instruct.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-height: 5;" + "-fx-border-insets: 5;" + "-fx-border-radius: 5;"
				+ "-fx-border-color: black;");

		pane.setLeft(instruct);
		BorderPane.setAlignment(instruct, Pos.TOP_LEFT);

		// Starting vert/Ending Vert
		HBox boxRight = new HBox(5);
		boxRight.setStyle("-border-color: brown");

		boxRight.getChildren().addAll(startV, startVertexTxt, endV, endVertexTxt, allSPButton);

		boxRight.setAlignment(Pos.CENTER);

		// HBox for left side options
		HBox boxLeft = new HBox(5);

		boxLeft.getChildren().addAll(mstButton, srcV, srcVertexTxt, spSrcButton);

		boxLeft.setAlignment(Pos.CENTER);

		// Bottom box
		HBox bottomBox = new HBox();

		bottomBox.getChildren().addAll(boxLeft, boxRight);

		bottomBox.setMargin(boxLeft, new Insets(20, 20, 20, 20));

		bottomBox.setMargin(boxRight, new Insets(20, 20, 20, 20));

		pane.setBottom(bottomBox);

		pane.setOnMouseClicked(e -> {

			double x = e.getX();
			double y = e.getY();

			if (e.getButton() == MouseButton.PRIMARY) {

				Circle c = drawPoint(x, y);

				pane.getChildren().add(c);

			} else if (e.getButton() == MouseButton.SECONDARY) {

				removePoint(x, y);

			}

		});

		// Creating a scene object
		Scene scene = new Scene(pane, 980, 350);

		// Setting title to the Stage
		stage.setTitle("Weighted Graph");

		// Adding scene to the stage
		stage.setScene(scene);

		// Displaying the contents of the stage
		stage.show();

	}

	private Circle drawPoint(double x, double y) {

		Circle c = new Circle(x, y, 20, Color.TRANSPARENT);

		c.setStroke(Color.BLACK);

		return c;

	}

	private void removePoint(double x, double y) {

		ObservableList<Node> list = pane.getChildren();

		for (int i = list.size() - 1; i >= 0; i--) {

			Node c = list.get(i);

			if (c instanceof Circle && c.contains(x, y)) {

				pane.getChildren().remove(c);

				break;

			}

		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);

	}

}