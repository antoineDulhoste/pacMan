package application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ActionEntree implements EventHandler<KeyEvent>{
    private Menu menu;
    private Group root;
    public ActionEntree(Menu m){
        this.menu=m;
        this.root=m.getRoot();
    }
    @Override
      public void handle(KeyEvent e) {
          if(e.getCode() == KeyCode.ENTER&& menu.getCompteur()==0){
              this.menu.getTimeline().stop();
              root.getChildren().remove(1);
              menu.setCompteur(1);
              menu.menu1();
          }  
    }
}
