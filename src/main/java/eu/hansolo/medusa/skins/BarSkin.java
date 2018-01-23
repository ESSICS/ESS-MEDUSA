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

package eu.hansolo.medusa.skins;

import eu.hansolo.medusa.Fonts;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.ScaleDirection;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.tools.ConicalGradient;
import eu.hansolo.medusa.tools.Helper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import static eu.hansolo.medusa.tools.Helper.formatNumber;


/**
 * Created by hansolo on 11.04.16.
 */
public class BarSkin extends GaugeSkinBase {

    private static final double                 ANGLE_RANGE        = 360;
    private static final Map<Color, List<Stop>> GRADIENT_STOPS_MAP = new HashMap<>(1);

    private              double          size;
    private              Text            titleText;
    private              Text            valueText;
    private              Text            unitText;
    private              Circle          dot;
    private              Circle          fakeDot;
    private              Arc             arc;
    private              Circle          circle;
    private              Pane            pane;
    private              DropShadow      shadow;
    private              ConicalGradient gradient;
    private              double          center;
    private              double          range;
    private              double          angleStep;
    private              InvalidationListener currentValueListener;
    private              InvalidationListener barColorListener;
    private              InvalidationListener titleListener;
    private              InvalidationListener unitListener;
    private              boolean         sectionsVisible;
    private              List<Section>   sections;
    private              double          minValue;



    // ******************** Static Methods ************************************
    private static List<Stop> getGradientStops ( Color color ) {

        List<Stop> gradient_stops = GRADIENT_STOPS_MAP.get(color);

        if ( gradient_stops == null ) {

            gradient_stops = Arrays.asList(
                new Stop(0.0, color),
                new Stop(0.01, color),
                new Stop(0.75, color.deriveColor(-10, 1, 1, 1)),
                new Stop(1.0, color.deriveColor(-20, 1, 1, 1))
            );

            GRADIENT_STOPS_MAP.put(color, gradient_stops);

        }

        return gradient_stops;

    }


    // ******************** Constructors **************************************
    public BarSkin(Gauge gauge) {
        super(gauge);
        if (gauge.isAutoScale()) gauge.calcAutoScale();
        minValue             = gauge.getMinValue();
        range                = gauge.getRange();
        angleStep            = -ANGLE_RANGE / range;
        sectionsVisible      = gauge.getSectionsVisible();
        sections             = gauge.getSections();
        currentValueListener = o -> redraw();
        barColorListener     = o -> redraw();
        titleListener = o -> {
            titleText.setText(gauge.getTitle());
            resizeTitleText();
        };
        unitListener = o -> {
            unitText.setText(gauge.getUnit());
            resizeUnitText();
        };

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        // Set initial size
        if (Double.compare(gauge.getPrefWidth(), 0.0) <= 0 || Double.compare(gauge.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(gauge.getWidth(), 0.0) <= 0 || Double.compare(gauge.getHeight(), 0.0) <= 0) {
            if (gauge.getPrefWidth() > 0 && gauge.getPrefHeight() > 0) {
                gauge.setPrefSize(gauge.getPrefWidth(), gauge.getPrefHeight());
            } else {
                gauge.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        Color barBackgroundColor = gauge.getBarBackgroundColor();
        Color barColor = gauge.getBarColor();
        gauge.setGradientBarStops(new Stop(0.0, barColor),
                                           new Stop(0.01, barColor),
                                           new Stop(0.75, barColor.deriveColor(-10, 1, 1, 1)),
                                           new Stop(1.0, barColor.deriveColor(-20, 1, 1, 1)));

        shadow = new DropShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.45), 0.01 * PREFERRED_WIDTH, 0, 0.01 * PREFERRED_WIDTH, 0);

        circle = new Circle();
        circle.setFill(null);
        circle.setStroke(Color.color(barBackgroundColor.getRed(), barBackgroundColor.getGreen(), barBackgroundColor.getBlue(), 0.13));

        arc = new Arc(PREFERRED_WIDTH * 0.5, PREFERRED_HEIGHT * 0.5, PREFERRED_WIDTH * 0.96, PREFERRED_WIDTH * 0.48, 90, 0);
        arc.setStrokeWidth(PREFERRED_WIDTH * 0.008);
        arc.setStrokeType(StrokeType.CENTERED);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setFill(null);

        fakeDot = new Circle();
        fakeDot.setStroke(null);

        dot = new Circle();
        dot.setStroke(null);
        dot.setVisible(false);
        dot.setEffect(shadow);

        titleText = new Text(gauge.getTitle());
        titleText.setFont(Fonts.robotoLight(PREFERRED_WIDTH * 0.5));
        titleText.setFill(gauge.getTitleColor());
        Helper.enableNode(titleText, !gauge.getTitle().isEmpty());

        valueText = new Text(formatNumber(gauge.getLocale(), gauge.getFormatString(), gauge.getDecimals(), gauge.getCurrentValue()));
        valueText.setFont(Fonts.robotoRegular(PREFERRED_WIDTH * 0.27333));
        valueText.setFill(gauge.getValueColor());
        Helper.enableNode(valueText, gauge.isValueVisible());

        unitText = new Text(gauge.getUnit());
        unitText.setFont(Fonts.robotoLight(PREFERRED_WIDTH * 0.08));
        unitText.setFill(gauge.getUnitColor());
        Helper.enableNode(unitText, !gauge.getUnit().isEmpty());

        pane = new Pane(circle, arc, fakeDot, dot, titleText, valueText, unitText);
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), new CornerRadii(1024), Insets.EMPTY)));

        getChildren().setAll(pane);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
        gauge.currentValueProperty().addListener(currentValueListener);
        gauge.barColorProperty().addListener(barColorListener);
        gauge.titleProperty().addListener(titleListener);
        gauge.unitProperty().addListener(unitListener);
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);
        if ("RECALC".equals(EVENT_TYPE)) {
            minValue  = gauge.getMinValue();
            range     = gauge.getRange();
            angleStep = -ANGLE_RANGE / range;
            sections  = gauge.getSections();
            redraw();
        } else if ("VISIBILITY".equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !gauge.getTitle().isEmpty());
        }
    }

    @Override public void dispose() {
        gauge.currentValueProperty().removeListener(currentValueListener);
        gauge.barColorProperty().removeListener(barColorListener);
        gauge.titleProperty().removeListener(titleListener);
        gauge.unitProperty().removeListener(unitListener);
        super.dispose();
    }


    // ******************** Resizing ******************************************
    private void resizeTitleText() {
        double maxWidth = 0.48 * size;
        double fontSize = 0.08 * size;
        titleText.setFont(Fonts.robotoLight(fontSize));
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        titleText.relocate((size - titleText.getLayoutBounds().getWidth()) * 0.5, size * 0.25);
    }
    private void resizeValueText() {
        double maxWidth = 0.5 * size;
        double fontSize = 0.3 * size;
        valueText.setFont(Fonts.robotoRegular(fontSize));
        if (valueText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(valueText, maxWidth, fontSize); }
        valueText.relocate((size - valueText.getLayoutBounds().getWidth()) * 0.5, (size - valueText.getLayoutBounds().getHeight()) * 0.5);
    }
    private void resizeUnitText() {
        double maxWidth = 0.56667 * size;
        double fontSize = 0.08 * size;
        unitText.setFont(Fonts.robotoLight(fontSize));
        if (unitText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(unitText, maxWidth, fontSize); }
        unitText.relocate((size - unitText.getLayoutBounds().getWidth()) * 0.5, size * 0.68);
    }

    private List<Stop> getGradientBarStops( final double VALUE ) {

        List<Stop> stops = getGradientStops(gauge.getBarColor());

        if ( sectionsVisible ) {
            for ( Section section : sections ) {
                if ( section.contains(VALUE) ) {
                    stops = getGradientStops(section.getColor());
                    break;
                }
            }
        }

        return stops;

    }

    @Override protected void resize() {
        double width  = gauge.getWidth() - gauge.getInsets().getLeft() - gauge.getInsets().getRight();
        double height = gauge.getHeight() - gauge.getInsets().getTop() - gauge.getInsets().getBottom();
        size          = width < height ? width : height;

        if (width > 0 && height > 0) {
            pane.setMaxSize(size, size);
            pane.setPrefSize(size, size);
            pane.relocate((width - size) * 0.5, (height - size) * 0.5);

            center = size * 0.5;

            circle.setCenterX(center);
            circle.setCenterY(center);
            circle.setRadius(size * 0.44);
            circle.setStrokeWidth(size * 0.11);

            arc.setCenterX(center);
            arc.setCenterY(center);
            arc.setRadiusX(size * 0.44);
            arc.setRadiusY(size * 0.44);
            arc.setStrokeWidth(size * 0.11);

            shadow.setRadius(0.03 * size);
            shadow.setOffsetX(0.03 * size);

            Color      barColor           = gauge.getBarColor();
            Color      barBackgroundColor = gauge.getBarBackgroundColor();
            double     currentValue       = gauge.getCurrentValue();

            circle.setStroke(Color.color(barBackgroundColor.getRed(), barBackgroundColor.getGreen(), barBackgroundColor.getBlue(), 0.13));

            arc.setLength(currentValue * angleStep);

            fakeDot.setRadius(size * 0.055);

            dot.setRadius(size * 0.055);

            dot.setCenterX(center + arc.getRadiusX() * Math.sin(Math.toRadians(180 - currentValue * angleStep)));
            dot.setCenterY(center + arc.getRadiusY() * Math.cos(Math.toRadians(180 - currentValue * angleStep)));

            titleText.setFill(barColor);

            resizeTitleText();
            resizeValueText();
            resizeUnitText();

            redraw();
        }
    }

    @Override protected void redraw() {
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), new CornerRadii(1024), Insets.EMPTY)));

        Color      barBackgroundColor = gauge.getBarBackgroundColor();
        double     currentValue       = gauge.getCurrentValue();
        List<Stop> gradientBarStops   = getGradientBarStops(currentValue);
        double     angle              = minValue > 0 ? ( currentValue - minValue ) * angleStep : currentValue * angleStep;
        double     rotate             = angle  < -360 ? angle  + 360 : 0;
        Rectangle  bounds             = new Rectangle(0, 0, size, size);

        sectionsVisible = gauge.getSectionsVisible();

        circle.setStroke(Color.color(barBackgroundColor.getRed(), barBackgroundColor.getGreen(), barBackgroundColor.getBlue(), 0.13));

        gradient = new ConicalGradient(center, center, ScaleDirection.CLOCKWISE, gradientBarStops);
        arc.setStroke(gradient.getImagePattern(bounds));
        arc.setRotate(-rotate);
        arc.setLength(angle);

        dot.setFill(gradientBarStops.get(3).getColor());
        dot.setRotate(-angle);
        dot.setVisible(angle   < -345 ? true : false);

        fakeDot.setFill(gradientBarStops.get(0).getColor());

        if (angle < -360) {
            fakeDot.setCenterX(center + arc.getRadiusX() * Math.sin(Math.toRadians(180 + angle)));
            fakeDot.setCenterY(center + arc.getRadiusY() * Math.cos(Math.toRadians(180 + angle)));
        } else {
            fakeDot.setCenterX(center + arc.getRadiusX() * Math.sin(Math.toRadians(180)));
            fakeDot.setCenterY(center + arc.getRadiusY() * Math.cos(Math.toRadians(180)));
        }

        dot.setCenterX(center + arc.getRadiusX() * Math.sin(Math.toRadians(180 + angle)));
        dot.setCenterY(center + arc.getRadiusY() * Math.cos(Math.toRadians(180 + angle)));

        titleText.setFill(gauge.getTitleColor());
        valueText.setFill(gauge.getValueColor());
        unitText.setFill(gauge.getUnitColor());

        valueText.setText(formatNumber(gauge.getLocale(), gauge.getFormatString(), gauge.getDecimals(), gauge.getCurrentValue()));
        resizeValueText();
    }
}
