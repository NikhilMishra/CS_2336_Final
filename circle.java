import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

class CirclePane extends Pane {
	public Integer circleCount = 0;

	public ArrayList<Circle> circleGraph = new ArrayList<Circle>();
	public Circle[] circleArray;
	public ArrayList<Text> textGraph = new ArrayList<Text>();
	public ArrayList<Text> lineTextGraph = new ArrayList<Text>();
	public ArrayList<LineEdge> lineGraph = new ArrayList<LineEdge>();
	public ArrayList<Point2D> pointArray = new ArrayList<Point2D>();
	public ArrayList<Integer> circlesForEdges = new ArrayList<Integer>();
	public int[][] edgeArray;
	public WeightedGraph completeGraph;

	public CirclePane() {
		this.setOnMouseDragged(e -> {

			pointArray.clear();
			int circleToMove2 = isInsideACircle(new Point2D(e.getX(), e.getY()));
			if (circleToMove2 != -1) {
				if (e.isControlDown()) {
					int circleToMove = isInsideACircle(new Point2D(e.getX(), e.getY()));
					(circleGraph.get(circleToMove)).setCenterX(e.getX());
					(circleGraph.get(circleToMove)).setCenterY(e.getY());
					(textGraph.get(circleToMove)).setX(e.getX() - 5);
					(textGraph.get(circleToMove)).setY(e.getY() + 4);
					for (int i = 0; i < lineGraph.size(); i++) {
						if (circleToMove == (lineGraph.get(i)).getU() || circleToMove == (lineGraph.get(i)).getV()) {
							if (circleToMove == (lineGraph.get(i)).getU()) {
								(lineGraph.get(i)).setStartX(circleGraph.get(circleToMove).getCenterX());
								(lineGraph.get(i)).setStartY(circleGraph.get(circleToMove).getCenterY());
							} else if (circleToMove == (lineGraph.get(i)).getV()) {
								(lineGraph.get(i)).setEndX(circleGraph.get(circleToMove).getCenterX());
								(lineGraph.get(i)).setEndY(circleGraph.get(circleToMove).getCenterY());
							}
						}
					}
					for (int i = 0; i < lineGraph.size(); i++) {

						double weight = new Point2D(((lineGraph.get(i)).getStartX()), ((lineGraph.get(i)).getStartY()))
								.distance(((lineGraph.get(i)).getEndX()), ((lineGraph.get(i)).getEndY()));
						int weight1 = (int) weight;
						(lineGraph.get(i)).setWeight(weight1);

						(lineTextGraph.get(i)).setText(new Integer(weight1).toString());

					}
					for (int i = 0; i < lineGraph.size(); i++) {
						if (circleToMove == (lineGraph.get(i)).getU() || circleToMove == (lineGraph.get(i)).getV()) {
							if (circleToMove == (lineGraph.get(i)).getU()) {
								(lineTextGraph.get(i)).setX(((circleGraph.get((lineGraph.get(i)).getU())).getCenterX()
										+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterX()) / 2);
								(lineTextGraph.get(i)).setY(((circleGraph.get((lineGraph.get(i)).getU())).getCenterY()
										+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterY()) / 2);
							} else if (circleToMove == (lineGraph.get(i)).getV()) {
								(lineTextGraph.get(i)).setX(((circleGraph.get((lineGraph.get(i)).getU())).getCenterX()
										+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterX()) / 2);
								(lineTextGraph.get(i)).setY(((circleGraph.get((lineGraph.get(i)).getU())).getCenterY()
										+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterY()) / 2);
							}
						}
					}

				} else {
					int currentCircleInside = (isInsideACircle(new Point2D(e.getX(), e.getY())));
					if (currentCircleInside != -1) {
						if (circlesForEdges.size() >= 2) {
							if (currentCircleInside != circlesForEdges.get(circlesForEdges.size()-1)) {
								circlesForEdges.add(new Integer(currentCircleInside));
							}
						}
						else if (!circlesForEdges.contains(new Integer(currentCircleInside))) {
							circlesForEdges.add(new Integer(currentCircleInside));
						}
					
					}
				}
			}
			circleArray = new Circle[circleGraph.size()];
			circleGraph.toArray(circleArray);

			edgeArray = new int[lineGraph.size()][3];
			for (int i = 0; i < lineGraph.size(); i++) {
				edgeArray[i][0] = (lineGraph.get(i)).u;
				edgeArray[i][1] = (lineGraph.get(i)).v;
				edgeArray[i][2] = (lineGraph.get(i)).weight;
				completeGraph = new WeightedGraph(circleArray, edgeArray);
			}
		});
		this.setOnMouseClicked(e -> {
			String buttonClicked = new String((e.getButton()).toString());
			getChildren().clear();
			if (!e.isPrimaryButtonDown() && !e.isSecondaryButtonDown()) {
				if (!e.isControlDown()) {
					if (!isCloseToCircle(new Point2D(e.getX(), e.getY())) && buttonClicked.equals("PRIMARY")) {
						Circle currentCircle = new Circle(e.getX(), e.getY(), 20);
						currentCircle.setStroke(Color.BLACK);
						currentCircle.setFill(Color.WHITE);

						circleGraph.add(currentCircle);
						textGraph.clear();
						circlesForEdges.clear();
						for (Integer i = 0; i < circleGraph.size(); i++) {
							Circle thisCircle = circleGraph.get(i);
							Text currentText = new Text(thisCircle.getCenterX() - 5, thisCircle.getCenterY() + 4,
									i.toString());
							textGraph.add(currentText);
						}
						circleCount++;

					}
					else if ((isInsideACircle(new Point2D(e.getX(), e.getY())) != -1
							&& buttonClicked.equals("PRIMARY"))) {
						if (!circlesForEdges.isEmpty()) {
							if (circlesForEdges.size() >= 2 && !circleGraph.get(circlesForEdges.get(0))
									.contains(new Point2D(e.getX(), e.getY()))) {
								if (!isLineAlreadyCreated()) {
									LineEdge line1 = new LineEdge();
									line1.setStartX((circleGraph.get(circlesForEdges.get(0))).getCenterX());
									line1.setStartY((circleGraph.get(circlesForEdges.get(0))).getCenterY());
									line1.setEndX((circleGraph.get(circlesForEdges.get((circlesForEdges.size()) - 1)))
											.getCenterX());
									line1.setEndY((circleGraph.get(circlesForEdges.get((circlesForEdges.size()) - 1)))
											.getCenterY());
									line1.setU(circlesForEdges.get(0));
									line1.setV(circlesForEdges.get((circlesForEdges.size()) - 1));

									double weight = new Point2D((circleGraph.get(line1.getU())).getCenterX(),
											(circleGraph.get(line1.getU())).getCenterY()).distance(
													(circleGraph.get(line1.getV())).getCenterX(),
													(circleGraph.get(line1.getV())).getCenterY());
									int weight1 = (int) weight;

									line1.setWeight(weight1);

									lineGraph.add(line1);
									lineTextGraph.clear();
									for (Integer i = 0; i < lineGraph.size(); i++) {
										Text currentText = new Text();
										currentText.setX(((circleGraph.get((lineGraph.get(i)).getU())).getCenterX()
												+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterX()) / 2);
										currentText.setY(((circleGraph.get((lineGraph.get(i)).getU())).getCenterY()
												+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterY()) / 2);
										String weightInteger = new String(
												((new Integer(lineGraph.get(i).getWeight()))).toString());
										currentText.setText(weightInteger);
										lineTextGraph.add(currentText);
									}

									circlesForEdges.clear();
								}
							}
						}

					}
					else if ((isInsideACircle(new Point2D(e.getX(), e.getY()))) != -1
							&& buttonClicked.equals("SECONDARY")) {

						int circleToRemove = isInsideACircle(new Point2D(e.getX(), e.getY()));
						for (int i = 0; i < lineGraph.size(); i++) {
							int currentU = ((lineGraph.get(i)).getU());
							if (currentU > circleToRemove) {
								(lineGraph.get(i)).setU(currentU - 1);
							}
							int currentV = ((lineGraph.get(i)).getV());
							if (currentV > circleToRemove) {
								(lineGraph.get(i)).setV(currentV - 1);
							}
						}
						for (int i = 0; i < lineGraph.size(); i++) {
							if (!lineGraph.isEmpty()) {
								if (!circleGraph.isEmpty()) {
									if (((circleGraph.get(circleToRemove)).contains(new Point2D(
											(lineGraph.get(i)).getStartX(), (lineGraph.get(i)).getStartY())))
											|| (circleGraph.get(circleToRemove)).contains(new Point2D(
													(lineGraph.get(i)).getEndX(), (lineGraph.get(i)).getEndY()))) {
										if (!lineGraph.isEmpty()) {
											lineGraph.remove(i);
										}
										i--;
									}
								}
							}
						}
						circleGraph.remove(circleToRemove);
						lineTextGraph.clear();
						for (Integer i = 0; i < lineGraph.size(); i++) {
							Text currentText = new Text();
							currentText.setX(((circleGraph.get((lineGraph.get(i)).getU())).getCenterX()
									+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterX()) / 2);
							currentText.setY(((circleGraph.get((lineGraph.get(i)).getU())).getCenterY()
									+ (circleGraph.get((lineGraph.get(i)).getV())).getCenterY()) / 2);

							String weightInteger = new String(((new Integer(lineGraph.get(i).getWeight()))).toString());
							currentText.setText(weightInteger);
							lineTextGraph.add(currentText);
						}

						textGraph.clear();
						for (Integer i = 0; i < circleGraph.size(); i++) {
							Circle thisCircle = circleGraph.get(i);
							Text currentText = new Text(thisCircle.getCenterX() - 5, thisCircle.getCenterY() + 4,
									i.toString());
							textGraph.add(currentText);
						}

						circleCount--;

					}
				}
			}
			circleArray = new Circle[circleGraph.size()];
			circleGraph.toArray(circleArray);

			edgeArray = new int[lineGraph.size()][3];
			for (int i = 0; i < lineGraph.size(); i++) {
				edgeArray[i][0] = (lineGraph.get(i)).u;
				edgeArray[i][1] = (lineGraph.get(i)).v;
				edgeArray[i][2] = (lineGraph.get(i)).weight;
			}
			completeGraph = new WeightedGraph(circleArray, edgeArray);

			getChildren().addAll(lineGraph);
			getChildren().addAll(circleGraph);
			getChildren().addAll(textGraph);
			getChildren().addAll(lineTextGraph);

		});
	}
	private boolean isCloseToCircle(Point2D p) {
		for (int i = 0; i < circleGraph.size(); i++) {
			Circle thisCircle1 = circleGraph.get(i);
			Point2D p2 = new Point2D(thisCircle1.getCenterX(), thisCircle1.getCenterY());
			if (p.distance(p2) <= 40)
				return true;
		}

		return false;
	}
	private int isInsideACircle(Point2D p) {

		for (int i = 0; i < circleGraph.size(); i++) {
			Circle thisCircle1 = circleGraph.get(i);
			if (thisCircle1.contains(p)) {
				return i;
			}
		}

		return -1;
	}

	private boolean isLineAlreadyCreated() {
		for (int i = 0; i < lineGraph.size(); i++) {
			if ((circlesForEdges.get(0)) == (lineGraph.get(i)).getU()
					&& circlesForEdges.get(circlesForEdges.size() - 1) == (lineGraph.get(i)).getV()) {
				return true;
			}
			if ((circlesForEdges.get(0)) == (lineGraph.get(i)).getV()
					&& circlesForEdges.get(circlesForEdges.size() - 1) == (lineGraph.get(i)).getU()) {
				return true;
			}
		}
		return false;
	}

}