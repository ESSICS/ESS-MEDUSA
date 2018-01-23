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
import eu.hansolo.medusa.LcdDesign;
import eu.hansolo.medusa.LcdFont;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.TickLabelLocation;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static javafx.stage.StageStyle.DECORATED;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 */
public class MultipleMetersEvaluator extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(MultipleMetersEvaluator.class.getName());

    private final Set<Gauge> gauges = new HashSet<>(4);
    private long lastTimerCall;
    private AnimationTimer timer;

    @Override
    public void init() throws Exception {

        super.init();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle( long now ) {
                if ( now > lastTimerCall + 1_000_000_000L ) {

                    gauges.stream().forEach(g -> g.setValue(120 * Math.random() - 20));

                    lastTimerCall = now;

                }
            }
        };

    }

    @Override
    public void start( Stage stage ) throws Exception {

        GridPane pane = new GridPane();
        int colNum = 4;

        for ( int c = 0; c < colNum; c++ ) {

            ColumnConstraints column = new ColumnConstraints();

            column.setPercentWidth(100.0 / colNum);
            pane.getColumnConstraints().add(column);

        }

        int rowNum = 4;

        for ( int r = 0; r < rowNum; r++ ) {

            RowConstraints row = new RowConstraints();

            row.setPercentHeight(100.0 / rowNum);
            pane.getRowConstraints().add(row);

        }

        pane.add(createMeter(Gauge.SkinType.GAUGE,      Pos.CENTER,        Orientation.HORIZONTAL), 0, 0);
        pane.add(createMeter(Gauge.SkinType.AMP,        Pos.BOTTOM_CENTER, Orientation.HORIZONTAL), 1, 0);
        pane.add(createMeter(Gauge.SkinType.LINEAR,     Pos.TOP_LEFT,      Orientation.HORIZONTAL), 2, 0, 2, 1);
        pane.add(createMeter(Gauge.SkinType.HORIZONTAL, Pos.BOTTOM_CENTER, Orientation.HORIZONTAL), 0, 1);
        pane.add(createMeter(Gauge.SkinType.HORIZONTAL, Pos.TOP_CENTER,    Orientation.HORIZONTAL), 1, 1);
        pane.add(createMeter(Gauge.SkinType.LINEAR,     Pos.TOP_LEFT,      Orientation.VERTICAL)  , 3, 1, 1, 2);
        pane.add(createMeter(Gauge.SkinType.VERTICAL,   Pos.CENTER_RIGHT,  Orientation.HORIZONTAL), 0, 2);
        pane.add(createMeter(Gauge.SkinType.VERTICAL,   Pos.CENTER_LEFT,   Orientation.HORIZONTAL), 1, 2);
        pane.add(createMeter(Gauge.SkinType.QUARTER,    Pos.BOTTOM_RIGHT,  Orientation.HORIZONTAL), 0, 3);
        pane.add(createMeter(Gauge.SkinType.QUARTER,    Pos.BOTTOM_LEFT,   Orientation.HORIZONTAL), 1, 3);
        pane.add(createMeter(Gauge.SkinType.QUARTER,    Pos.TOP_RIGHT,     Orientation.HORIZONTAL), 2, 3);
        pane.add(createMeter(Gauge.SkinType.QUARTER,    Pos.TOP_LEFT,      Orientation.HORIZONTAL), 3, 3);

//        pane.setGridLinesVisible(true);
        pane.setMaxSize(10000, 10000);
        pane.setScaleShape(true);
        pane.setPadding(new Insets(20));
        pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        Scene scene = new Scene(pane);

        stage.initStyle(DECORATED);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setTitle("ESS-Medusa Multiple Meters Test");
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

    private Gauge createMeter ( Gauge.SkinType skin, Pos position, Orientation orientation ) {

        Gauge gauge = GaugeBuilder.create()
                            .skinType(skin)
                            .animated(true)
                            .animationDuration(300L)
                            .autoScale(true)
            .averageVisible(true)
            .averagingEnabled(true)
            .averagingPeriod(250)

                            .tickLabelLocation(TickLabelLocation.INSIDE)
                            .tickLabelsVisible(true)
                            .majorTickMarksVisible(true)
                            .mediumTickMarksVisible(true)
                            .minorTickMarksVisible(true)

                            .title(skin.name())
                            .titleColor(Color.GREEN)
            .subTitle("FUFFA")
            .subTitleColor(Color.BLUE)
                            .minValue(-20)
                            .maxValue(100)
                            .value(40)
//                            .ledBlinking(false)
//                            .ledColor(Color.ORANGE)
//                            .ledOn(false)
//                            .ledType(Gauge.LedType.FLAT)
                            .ledVisible(false)
                            .lcdDesign(LcdDesign.AMBER)
                            .lcdVisible(true)
                            .lcdFont(LcdFont.ELEKTRA)
                            .unit("\u00B0C")
                            .checkSectionsForValue(false)
                            .sections(
                                new Section(-20,   0, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99)),
                                new Section(  0,  25, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 60,  80, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99)),
                                new Section( 80, 100, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99))
                            )
                            .sectionsVisible(true)
//                            .sectionTextVisible(true)
                            .highlightSections(true)
                            .markers(
                                new Marker( 0, "LoLo", Color.RED,    Marker.MarkerType.DOT),
                                new Marker(25, "Lo",   Color.ORANGE, Marker.MarkerType.DOT),
                                new Marker(60, "Hi",   Color.ORANGE, Marker.MarkerType.DOT),
                                new Marker(80, "HiHi", Color.RED,    Marker.MarkerType.DOT)
                            )
                            .markersVisible(true)
                            .knobPosition(position)
                            .needleType(Gauge.NeedleType.STANDARD)
                            .needleSize(Gauge.NeedleSize.STANDARD)
                            .needleShape(Gauge.NeedleShape.ANGLED)
                            .orientation(orientation)
                            .build();

//        gauge.getSections().stream().forEach(s -> {
//            s.setOnSectionEntered(e -> {
//                gauge.setLedColor(s.getHighlightColor());
//                gauge.setLedOn(true);
//            });
//            s.setOnSectionLeft(e -> gauge.setLedOn(false));
//        });

        gauges.add(gauge);

        return gauge;

    }

}
