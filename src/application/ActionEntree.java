package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ActionEntree implements EventHandler<KeyEvent>{
    private Menu m;
    private boolean b;
    private Group root;
    public ActionEntree(Menu m){
        this.m=m;
        this.b=true;
        this.root=m.getRoot();
    }
    @Override
      public void handle(KeyEvent e) {
          if(e.getCode() == KeyCode.ENTER && b==true){
              b=false;
              this.m.getTimeline().stop();
              root.getChildren().remove(1);
              menu();
          }
    }
  //ajoute les boutons solo,multi,quitter  
    private void menu() {
    	Text t1 = new Text();
        t1.setText("solo");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(250);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        t1.setCursor(Cursor.CLOSED_HAND);
        t1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	try {
        			Jeu jeu = new Jeu();
        			ViewJeu vj = new ViewJeu(jeu);
        		} catch(Exception e) {
        			e.printStackTrace();
        		}
            }
        });
        root.getChildren().add(t1);
        Text t2 = new Text();
        t2.setText("multi");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(240);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        t2.setCursor(Cursor.CLOSED_HAND);
        root.getChildren().add(t2);
        Text t3 = new Text();
        t3.setText("quitter");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(225);
        t3.setY(640);
        t3.setCursor(Cursor.CLOSED_HAND);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.exit(0);
            }
        });
        root.getChildren().add(t3);
    }
}
