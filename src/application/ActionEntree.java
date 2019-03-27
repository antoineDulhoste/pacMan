package application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
