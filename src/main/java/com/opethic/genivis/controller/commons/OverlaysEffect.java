package com.opethic.genivis.controller.commons;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Stage;
import javafx.util.Duration;

public  class OverlaysEffect {
    public static void removeOverlaysEffect(Stage stage){
        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(0);

        stage.getScene().getRoot().setEffect(dim);

        double targetBrightness = -0.4;
        Duration duration = Duration.seconds(0.5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dim.brightnessProperty(), 0)),
                new KeyFrame(duration, new KeyValue(dim.brightnessProperty(), targetBrightness))
        );

        Timeline reverseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dim.brightnessProperty(), targetBrightness)),
                new KeyFrame(duration, new KeyValue(dim.brightnessProperty(), 0))
        );

        timeline.play();

        stage.getScene().getRoot().setEffect(dim);
        reverseTimeline.play();
    }

    public static void setOverlaysEffect(Stage stage){
        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(0);

        stage.getScene().getRoot().setEffect(dim);

        double targetBrightness = -0.50;
        Duration duration = Duration.seconds(0.5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dim.brightnessProperty(), 0)),
                new KeyFrame(duration, new KeyValue(dim.brightnessProperty(), targetBrightness))
        );

        timeline.play();
    }
}
