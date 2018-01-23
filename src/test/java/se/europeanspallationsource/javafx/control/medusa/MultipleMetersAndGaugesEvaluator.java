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
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.TickMarkType;
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
public class MultipleMetersAndGaugesEvaluator extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(MultipleMetersAndGaugesEvaluator.class.getName());
    private static final double MAX = 100;
    private static final double MIN = -20;
    private static final double RANGE = MAX - MIN;

    private final Set<Gauge> gauges = new HashSet<>(4);
    private long lastTimerCall;
    private AnimationTimer timer;

    @Override
    public void init() throws Exception {

        super.init();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {

            private int counter = 0;
            private boolean changed = false;

            @Override
            public void handle( long now ) {
                if ( now > lastTimerCall + 1_000_000_000L ) {

                    gauges.stream().forEach(g -> g.setValue(RANGE * Math.random() + MIN));
//                    gauges.stream().forEach(g -> g.setValue(-15));

                    if ( counter++ >= 4 ) {
                        if ( !changed ) {
                            changed = true;
                            gauges.stream().forEach(g -> {
//                                g.setTitle(g.getClass().getSimpleName());
//                                g.setAnimated(true);
//                                g.setBackgroundPaint(Color.DARKSLATEBLUE);
//                                g.setBarBackgroundColor(Color.DARKRED.deriveColor(0, 1, 1, 0.5));
//                                g.setBarEffectEnabled(false);
//                                g.setTitle(g.getSkinType().name());
//                                g.setKnobColor(Color.DARKRED);
//                                g.setKnobType(Gauge.KnobType.FLAT);
//                                g.setLcdDesign(LcdDesign.DARKBLUE);
//                                g.setLcdVisible(false);
//                                g.setLcdFont(LcdFont.DIGITAL);
//                                g.setTitle("FUFFA FUFFONA FUFFETTA FUFFETTINA");
                            });
                            System.out.println("*** CHANGED");
                        }
                    }

                    lastTimerCall = now;

                }
            }

        };

    }

    @Override
    public void start( Stage stage ) throws Exception {

        GridPane pane = new GridPane();
        int colNum = 6;

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

        pane.add(createGauge(Gauge.SkinType.HORIZONTAL,     Pos.BOTTOM_CENTER, Orientation.HORIZONTAL, true,  false), 0, 0);
        pane.add(createGauge(Gauge.SkinType.LINEAR,         Pos.TOP_LEFT,      Orientation.HORIZONTAL, true,  true),  1, 0, 3, 1);
        pane.add(createGauge(Gauge.SkinType.DASHBOARD,      Pos.CENTER,        Orientation.HORIZONTAL, false, false), 4, 0);
        pane.add(createGauge(Gauge.SkinType.DIGITAL,        Pos.CENTER,        Orientation.HORIZONTAL, false, false), 5, 0);
        pane.add(createGauge(Gauge.SkinType.HORIZONTAL,     Pos.TOP_CENTER,    Orientation.HORIZONTAL, true,  false), 0, 1);
        pane.add(createGauge(Gauge.SkinType.VERTICAL,       Pos.CENTER_RIGHT,  Orientation.HORIZONTAL, true,  false), 1, 1);
        pane.add(createGauge(Gauge.SkinType.VERTICAL,       Pos.CENTER_LEFT,   Orientation.HORIZONTAL, true,  false), 2, 1);
        pane.add(createGauge(Gauge.SkinType.LINEAR,         Pos.TOP_LEFT,      Orientation.VERTICAL,   true,  true),  3, 1, 1, 3);
        pane.add(createGauge(Gauge.SkinType.FLAT,           Pos.CENTER,        Orientation.HORIZONTAL, false, false), 4, 1);
        pane.add(createGauge(Gauge.SkinType.MODERN,         Pos.CENTER,        Orientation.HORIZONTAL, false, false), 5, 1);
        pane.add(createGauge(Gauge.SkinType.QUARTER,        Pos.BOTTOM_RIGHT,  Orientation.HORIZONTAL, true,  false), 0, 2);
        pane.add(createGauge(Gauge.SkinType.QUARTER,        Pos.BOTTOM_LEFT,   Orientation.HORIZONTAL, true,  false), 1, 2);
        pane.add(createGauge(Gauge.SkinType.GAUGE,          Pos.CENTER,        Orientation.HORIZONTAL, true,  false), 2, 2);
        pane.add(createGauge(Gauge.SkinType.SIMPLE_DIGITAL, Pos.CENTER,        Orientation.HORIZONTAL, false, false), 4, 2);
        pane.add(createGauge(Gauge.SkinType.SIMPLE_SECTION, Pos.CENTER,        Orientation.HORIZONTAL, false, false), 5, 2);
        pane.add(createGauge(Gauge.SkinType.QUARTER,        Pos.TOP_RIGHT,     Orientation.HORIZONTAL, true,  false), 0, 3);
        pane.add(createGauge(Gauge.SkinType.QUARTER,        Pos.TOP_LEFT,      Orientation.HORIZONTAL, true,  false), 1, 3);
        pane.add(createGauge(Gauge.SkinType.SLIM,           Pos.CENTER,        Orientation.HORIZONTAL, false, false), 4, 3);
//        pane.add(createGauge(Gauge.SkinType.BAR,            Pos.CENTER,        Orientation.HORIZONTAL, false, false), 5, 3);

//        pane.setGridLinesVisible(true);
        pane.setMaxSize(10000, 10000);
        pane.setScaleShape(true);
        pane.setPadding(new Insets(20));
        pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        Scene scene = new Scene(pane);

        stage.initStyle(DECORATED);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setTitle("ESS-Medusa Multiple Gauges and Meters Test");
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

    private Gauge createGauge ( Gauge.SkinType skin, Pos position, Orientation orientation, boolean highlight, boolean showAreas ) {

        Gauge gauge = GaugeBuilder.create()
                            //  NO: .alertMessage("fuffa")
                            //  NO: .angleRange(200)
                            .animated(false)
                            //  NO: .animationDuration(lastTimerCall)
                            .areaIconsVisible(false)
                            .areaTextVisible(false)
                            .areas(
                                new Section(MIN,                MIN + 0.10 * RANGE, Color.rgb(255,   0, 0, .99)),
                                new Section(MIN + 0.10 * RANGE, MIN + 0.25 * RANGE, Color.rgb(255, 127, 0, .99)),
                                new Section(MIN + 0.70 * RANGE, MIN + 0.88 * RANGE, Color.rgb(255, 127, 0, .99)),
                                new Section(MIN + 0.88 * RANGE, MAX,                Color.rgb(255,   0, 0, .99))
                            )
                            .areasVisible(showAreas)
                            .averageColor(Color.BLUE)
                            .averageVisible(true)
                            .averagingEnabled(true)
                            .averagingPeriod(250)
                            .autoScale(true)
//                            .backgroundPaint(Color.ANTIQUEWHITE)
                            .barBackgroundColor(Color.DARKRED.deriveColor(0, 1, 1, 0.25))
                            //  NO: .barBorderColor(Color.ROYALBLUE)
                            .barColor(Color.GREEN.brighter())
                            .barEffectEnabled(true)
                            //  NO: .borderPaint(Color.BROWN)
                            //  NO: .borderWidth(7.77)
                            //  NO: .buttonTooltipText("fuffa")
                            .checkAreasForValue(false)
                            .checkSectionsForValue(false)
                            .checkThreshold(false)
                            //  NO: .customFont(FONT)
                            //  NO: .customFontEnabled(ENABLED)
                            //  NO: .customTickLabelFontSize(SIZE)
                            //  NO: .customTickLabels(TICK_LABELS)
                            //  NO: .customTickLabelsEnabled(ENABLED)
//                            .decimals(3)
                            //  NO: .foregroundBaseColor(Color.GOLD)
//                            .foregroundPaint(Color.ROYALBLUE)
                            //  NO: .gradientBarEnabled(true)
                            //  NO: .gradientBarStops(STOPS)
                            //  NO: .gradientLookup(GRADIENT_LOOKUP)
                            .highlightAreas(false)
                            .highlightSections(highlight)
                            .innerShadowEnabled(false)
                            .interactive(false)
                            //  NO: .keepAspect(KEEP)
                            .knobColor(Color.rgb(177, 166, 155))
                            .knobPosition(position)
                            .knobType(Gauge.KnobType.STANDARD)
                            .knobVisible(true)
                            //  NO: .lcdCrystalEnabled(ENABLED)
                            .lcdDesign(LcdDesign.AMBER)
//                            .lcdVisible(true)
                            .lcdFont(LcdFont.ELEKTRA)
                            //  NO: .ledBlinking(BLINKING)
                            //  NO: .ledColor(Color.RED)
                            //  NO: .ledOn(ON)
                            //  NO: .ledType(Gauge.LedType.FLAT)
                            .ledVisible(false)
                            //  NO: .locale(Locale.ITALY)
                            .majorTickMarkColor(Color.MAGENTA)
                            .majorTickMarkLengthFactor(0.515)
                            .majorTickMarkType(TickMarkType.LINE)
                            //  NO: .majorTickMarkWidthFactor(FACTOR)
                            .majorTickMarksVisible(true)
                            //  NO: .majorTickSpace(30)
                            //  NO: .markers(
                            //    new Marker( 0, "LoLo", Color.RED,    Marker.MarkerType.DOT),
                            //    new Marker(25, "Lo",   Color.ORANGE, Marker.MarkerType.DOT),
                            //    new Marker(60, "Hi",   Color.ORANGE, Marker.MarkerType.DOT),
                            //    new Marker(80, "HiHi", Color.RED,    Marker.MarkerType.DOT)
                            //)
                            //  NO: .markersVisible(false)
                            //  NO: .maxHeight(MAX_HEIGHT)
                            //  NO: .maxMeasuredValueVisible(true)
                            //  NO: .maxSize(WIDTH, HEIGHT)
                            .maxValue(MAX)
                            //  NO: .maxWidth(MAX_WIDTH)
                            .mediumTickMarkColor(Color.RED)
                            .mediumTickMarkLengthFactor(0.475)
                            .mediumTickMarkType(TickMarkType.LINE)
                            //  NO: .mediumTickMarkWidthFactor(FACTOR)
                            .mediumTickMarksVisible(true)
                            //  NO: .minHeight(MIN_HEIGHT)
                            //  NO: .minMeasuredValueVisible(VISIBLE)
                            //  NO: .minSize(WIDTH, HEIGHT)
                            .minValue(MIN)
                            //  NO: .minWidth(MIN_WIDTH)
                            .minorTickMarkColor(Color.BLUE)
                            //  NO: .minorTickMarkLengthFactor(0.2)
                            .minorTickMarkType(TickMarkType.TICK_LABEL)
                            //  NO: .minorTickMarkWidthFactor(FACTOR)
                            .minorTickMarksVisible(true)
                            //  NO: .minorTickSpace(SPACE)
                            .needleBehavior(Gauge.NeedleBehavior.STANDARD)
                            .needleBorderColor(Color.RED.darker())
                            .needleColor(Color.RED)
                            .needleShape(Gauge.NeedleShape.ANGLED)
                            .needleSize(Gauge.NeedleSize.STANDARD)
                            .needleType(Gauge.NeedleType.STANDARD)
                            //  NO: .numberFormat(FORMAT)
                            //  NO: .oldValueVisible(VISIBLE)
                            //  NO: .onButtonPressed(HANDLER)
                            //  NO: .onButtonReleased(HANDLER)
                            //  NO: .onThresholdExceeded(HANDLER)
                            //  NO: .onThresholdUnderrun(HANDLER)
                            //  NO: .onValueChanged(LISTENER)
//                            .onlyFirstAndLastTickLabelVisible(true)
                            .orientation(orientation)
                            //  NO: .padding(new Insets(20))
                            //  NO: .prefHeight(PREF_HEIGHT)
                            //  NO: .prefSize(WIDTH, HEIGHT)
                            //  NO: .prefWidth(PREF_WIDTH)
                            .returnToZero(false)
                            .scaleDirection(Gauge.ScaleDirection.CLOCKWISE) //  Solo Meters e valori CLOCKWISE e COUNTER_CLOCKWISE
                            //  NO: .scaleX(SCALE_X)
                            //  NO: .scaleY(SCALE_Y)
                            .sectionIconsVisible(false)
                            .sectionTextVisible(false)
                            .sections(
                                highlight ? new Section(MIN,                MIN + 0.10 * RANGE, "LoLo", null, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99), Color.BLUE) : new Section(-20,   0, "LoLo", null, Color.rgb(255,   0, 0, .99), Color.BLUE),
                                highlight ? new Section(MIN + 0.10 * RANGE, MIN + 0.25 * RANGE, "Low",  null, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99), Color.BLUE) : new Section(  0,  25, "Low",  null, Color.rgb(255, 127, 0, .99), Color.BLUE),
                                highlight ? new Section(MIN + 0.70 * RANGE, MIN + 0.88 * RANGE, "High", null, Color.rgb(255, 127, 0, .20), Color.rgb(255, 127, 0, .99), Color.BLUE) : new Section( 60,  80, "High", null, Color.rgb(255, 127, 0, .99), Color.BLUE),
                                highlight ? new Section(MIN + 0.88 * RANGE, MAX,                "HiHi", null, Color.rgb(255,   0, 0, .20), Color.rgb(255,   0, 0, .99), Color.BLUE) : new Section( 80, 100, "HiHi", null, Color.rgb(255,   0, 0, .99), Color.BLUE)
                            )
                            //  NO: .sectionsAlwaysVisible(VISIBLE)
                            .sectionsVisible(true)
                            .shadowsEnabled(false)  //  Solo Meters
                            .skinType(skin)
                            //  NO: .smoothing(SMOOTING)
                            //  NO: .startAngle(ANGLE)
                            //  Solo Gauges, e non tutti la usano.
                            .startFromZero(true)
                            //  NO: .subTitle("FUFFA")
                            //  NO: .subTitleColor(Color.BLUE)
                            .threshold(45)     //  NO threshold in GAUGES & LINEAR
                            .thresholdVisible(true)
                            .thresholdColor(Color.CORAL)
//                            .tickLabelColor(Color.GREEN)    //  Solo METERS
//                            .tickLabelDecimals(0)   //  Solo METERS
                            //  NO: .tickLabelLocation(TickLabelLocation.OUTSIDE)
                            //  NO: .tickLabelOrientation(TickLabelOrientation.TANGENT)
                            //  NO: .tickLabelSections(SECTIONS)
                            //  NO: .tickLabelSectionsVisible(highlight)
//                            .tickLabelsVisible(true)    //  Solo METERS
                            .tickMarkColor(Color.DARKGRAY)  //  Solo METERS
                            .tickMarkRingVisible(true)  //  Solo METERS
                            //  NO: .tickMarkSections(SECTIONS)
                            //  NO: .tickMarkSections(SECTIONS)
                            //  NO: .tickMarkSectionsVisible(VISIBLE)
                            .title(skin.name())
                            .titleColor(Color.DARKRED)
                            .unit("\u00B0C")
                            .unitColor(Color.MAROON)
                            .value(40)
                            .valueColor(Color.TEAL)
                            .valueVisible(true)
                            .zeroColor(Color.BROWN)     //  Only METERs
                            .build();

//        gauge.setDisable(true);
//        gauge.setEffect(new ColorAdjust(0, -1, -.025, 0));
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
