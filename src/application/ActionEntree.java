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
    private Group root;
    public ActionEntree(Menu m){
        this.m=m;
        this.root=m.getRoot();
    }
    @Override
      public void handle(KeyEvent e) {
          if(e.getCode() == KeyCode.ENTER&& m.getCompteur()==0){
              this.m.getTimeline().stop();
              root.getChildren().remove(1);
              m.setCompteur(1);
              m.menu1();
          }  
    }
}
