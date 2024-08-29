package com.opethic.genivis.utils;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

public class InputNextFocus {


    public static void inputFocus(Node src, Node target){
        src.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                target.requestFocus();
            }
            event.consume();
        });
    }

}
