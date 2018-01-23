/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.medusa;

import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.medusa.Gauge.SkinType;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



/**
 * User: hansolo
 * Date: 04.01.16
 * Time: 06:31
 */
public class Test extends Application {
    private static final Random          RND       = new Random();
    private static       int             noOfNodes = 0;
    private              Gauge           gauge1;
    private              Gauge           gauge2;
    private              Clock           clock;
    private              long            lastTimerCall;
    private              AnimationTimer  timer;


    @Override public void init() {

        gauge1 = GaugeBuilder.create()
                             .skinType(SkinType.GAUGE)
                             .decimals(2)
                             .minValue(0)
                             .maxValue(2.40)
                             .autoScale(true)
                             .majorTickSpace(.6)
                             .minorTickSpace(.1)
                             .tickLabelDecimals(2)
                             .animated(true)
                             .build();

        gauge2 = GaugeBuilder.create()
                             .skinType(SkinType.GAUGE)
                             .decimals(2)
                             .minValue(0)
                             .maxValue(360)
                             .autoScale(true)
                             .majorTickSpace(60)
                             .minorTickSpace(10)
                             .animated(true)
                             .build();
        clock = ClockBuilder.create()
                            .skinType(ClockSkinType.DIGI)
                            .running(true)
                            .build();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            long counter = 0;
            @Override public void handle(long now) {
                if (now > lastTimerCall + 2_000_000_000l) {

                    lastTimerCall = now;

                    if ( counter++ == 5 ) {
                        gauge1.setMaxValue(36);
                        gauge2.setMaxValue(1600);
                    }

                }
            }
        };
    }

    @Override public void start(Stage stage) {

        StackPane pane = new StackPane(clock);

        Scene scene = new Scene(pane);

        stage.setTitle("Medusa");
        stage.setScene(scene);
        stage.show();

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println(noOfNodes + " Nodes in SceneGraph");

        //timer.start();
    }

    @Override public void stop() {
        System.exit(0);
    }



    // ******************** Misc **********************************************
    private static void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
