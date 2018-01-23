/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright (C) 2017 by European Spallation Source ERIC.
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
package se.europeanspallationsource.javafx.control.medusa;


import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static javafx.stage.StageStyle.DECORATED;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 */
public class SingleGaugeEvaluator extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(SingleGaugeEvaluator.class.getName());

    private Gauge gauge;
    private long lastTimerCall;
    private AnimationTimer timer;

    @Override
    public void init() throws Exception {

        super.init();

        Gauge.SkinType skin = Gauge.SkinType.GAUGE;

        gauge = GaugeBuilder.create()
                            .skinType(skin)
                            .title(skin.name())
                            .minValue(0)
                            .maxValue(100)
                            .value(40)
                            .unit("\u00B0C")
                            .sections(
                                new Section(-20,   0, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99)),
                                new Section(  0,  25, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 60,  80, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 80, 100, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99))
                            )
                            .sectionsVisible(true)
                            .build();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {

            private int counter = 0;
            private boolean changed = false;

            @Override
            public void handle( long now ) {
                if ( now > lastTimerCall + 2_000_000_000L ) {

                    gauge.setValue(120 * Math.random() - 20);

                    if ( counter++ >= 2 ) {
                        if ( !changed ) {
                            changed = true;
                            gauge.setSections(
                                new Section(-20, -10, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99)),
                                new Section(-10,  15, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 65,  85, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 85, 100, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99))
                            );
                            System.out.println("*** SECTIONS CHANGED");
                            gauge.setMajorTickMarksVisible(false);
                        }
                    }

                    lastTimerCall = now;

                }
            }
        };

    }

    @Override
    public void start( Stage stage ) throws Exception {

        StackPane pane = new StackPane(gauge);

        pane.setPadding(new Insets(20));
        pane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        Scene scene = new Scene(pane);

        stage.initStyle(DECORATED);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setTitle("ESS-Medusa Single Gauge Test");
        stage.setScene(scene);
        stage.show();

        timer.start();

    }

    /**
     * Fake test made to avoid TestFX closing the test window at the end of the
     * tests. Window's close button is the only way to close the application.
     */
    @Test
    public void test() {
        while ( true ) {
            waitForFxEvents();
        }
    }

}
