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


import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import java.util.Locale;
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
public class ClockEvaluator extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(ClockEvaluator.class.getName());

    private Clock clock;
    private long lastTimerCall;
    private AnimationTimer timer;

    @Override
    public void init() throws Exception {

        super.init();

//        Clock.ClockSkinType skin = Clock.ClockSkinType.LCD;
//        Clock.ClockSkinType skin = Clock.ClockSkinType.TEXT;
        Clock.ClockSkinType skin = Clock.ClockSkinType.CLOCK;

        clock = ClockBuilder.create()
                            .skinType(skin)
//                            .backgroundPaint(Color.web("#7f7ea3"))
//                            .borderPaint(Color.web("#03af07"))
//                            .borderWidth(4.7)
//                            .dateColor(Color.RED)
                            .dateVisible(true)
//                            .discreteHours(false)
//                            .discreteMinutes(false)
//                            .discreteSeconds(false)
//                            .foregroundPaint(Color.web("#765600"))
//                            .hourColor(Color.CORAL)
//                            .hourTickMarkColor(Color.AQUA)
//                            .hourTickMarksVisible(true)
//                            .knobColor(Color.MAGENTA)
//                        .lcdCrystalEnabled(true)
//                        .lcdDesign(LcdDesign.BLUE_LIGHTBLUE)
                            .locale(Locale.ITALY)
//                            .minuteColor(Color.FIREBRICK)
//                            .minuteTickMarkColor(Color.BROWN)
//                            .minuteTickMarksVisible(true)
//                            .secondColor(Color.YELLOW)
                            .secondsVisible(true)
                            .shadowsEnabled(true)
//                            .text("FUFFA")
                            .textColor(Color.ORCHID)
                            .textVisible(true)
                            .tickLabelColor(Color.ORANGE)
                            .tickLabelsVisible(true)
                            .title(skin.name())
                            .titleColor(Color.ORANGERED)
                            .titleVisible(true)
                            .running(true)
                            .build();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle( long now ) {
                if ( now > lastTimerCall + 5_000_000_000L ) {

//                    clock.setSkinType(Clock.ClockSkinType.SLIM);
//                    clock.setKnobColor(Color.GREEN);
//                    clock.setLcdCrystalEnabled(false);
//                    System.out.println("*** HIDING SECONDS");
//                    clock.setSecondsVisible(false);
                    System.out.println("Changing LOCALE");
                    clock.setLocale(Locale.ITALY);

                    lastTimerCall = now;

                }
            }
        };

    }

    @Override
    public void start( Stage stage ) throws Exception {

        StackPane pane = new StackPane(clock);

        pane.setPadding(new Insets(20));
        pane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        Scene scene = new Scene(pane);

        stage.initStyle(DECORATED);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setTitle("ESS-Medusa Clock Test");
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
