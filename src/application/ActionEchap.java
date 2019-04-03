package application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ActionEchap implements EventHandler<KeyEvent> {
	private Menu menu;
    private Group root;
    public ActionEchap(Menu menu){
        this.menu=menu;
        this.root=menu.getRoot();
    }
	@Override
    public void handle(KeyEvent event) {
    	if(event.getCode() == KeyCode.ESCAPE){
            if(menu.getCompteur()==1) {
            	menu.setCompteur(0);
            	menu.menuRemove(3);
            	menu.textEntrer();
            } 
            if(menu.getCompteur()==2){
            	menu.setCompteur(1);
            	menu.menuRemove(2);
            	menu.menu1();
            }
            if(menu.getCompteur()==3){
            	menu.setCompteur(2);
            	menu.menuRemove(5);
            	menu.menu1();
            }
        }
    }
}
