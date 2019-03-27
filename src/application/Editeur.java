package application;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Editeur extends Stage{
	
	Separator sep = new Separator(Orientation.VERTICAL);
	GridPane grid = new GridPane();
	HBox hbox = new HBox(grid, sep);
	Scene scene = new Scene(hbox);
	
	private int lignes;
	private int colonnes;
	
	public Editeur() {
		buildGrid(10, 10);
		this.setScene(scene);
		this.show();
	}
	
	
	private void buildGrid(int lignes, int colonnes) {
		this.lignes = lignes;
		this.colonnes = colonnes;
		for(int l = 0; l < lignes; l++) {
			for(int c = 0; c < colonnes; c++) {
				EditorCase ec = new EditorCase(0);
				ec.setWidth(20);
				ec.setHeight(20);
				ec.setFill(Color.BLACK);
				ec.setOnMouseClicked(e->{
					ec.setValue(ec.getValue()+1);
					switch(ec.getValue()) {
						case 0:
							ec.setFill(Color.BLACK);
							break;
						case 1:
							ec.setFill(Color.BLUE);
							break;
						case 2:
							ec.setFill(Color.GREEN);
							break;
						case 3:
							ec.setFill(Color.YELLOW);
							break;
						case 4:
							ec.setFill(Color.DEEPPINK);
							break;
						case 5:
							ec.setFill(Color.RED);
							break;
						default:
							ec.setValue(0);
							ec.setFill(Color.BLACK);
							break;
					}
				});
				grid.add(ec, c, l);
			}
		}
	}
	
	
	public int[][] export(){
		int[][] map = new int[lignes][colonnes];
 		for(int l = 0; l < lignes; l++) {
			for(int c = 0; c < colonnes; c++) {
				int value = ((EditorCase)getNodeByRowColumnIndex(l, c, grid)).getValue();
				map[l][c] = value;
				if( value == 3 ) {
					System.out.println("SpawnPoint PacMan: x="+c+"; y="+l);
				}
			}
		}
 		return map;
	}
	
	public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
	    Node result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();

	    for (Node node : childrens) {
	        if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
	}

}
