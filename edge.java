import javafx.scene.shape.Line;

public class LineEdge extends Line {
	public int u;
	public int v;
	public int weight;
	
	public LineEdge() {
		
	}
	
	public LineEdge(int u1, int v1, int weight1) {
		this.u = u1;
		this.v = v1;
		this.weight = weight1;
	}
	
	public void setU(int u1) {
		this.u = u1;
	}
	public void setV(int v1) {
		this.v = v1;
	}
	
	public void setWeight(int weight1) {
		this.weight = weight1;
	}
	
	public int getU() {
		return u;
	}
	
	public int getV() {
		return v;
	}
	
	public int getWeight() {
		return weight;
	}
	
	
}
