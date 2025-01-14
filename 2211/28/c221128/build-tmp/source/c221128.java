/* autogenerated by Processing revision 1289 on 2022-11-29 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class c221128 extends PApplet {

/**
 * Heartbeats Accelerating.
 * The heartbeat of recurring formula.
 * 
 * Processing 3.5.3
 * @author @deconbatch
 * @version 0.1
 * created 0.1 2021.04.04
 */

public void setup() {
  /* size commented out by preprocessor */;
  colorMode(HSB, 360.0f, 100.0f, 100.0f, 100.0f);
  rectMode(CENTER);
  /* smooth commented out by preprocessor */;
  noStroke();
}

public void draw() {

  int   frmMax    = 24 * 12; // 24fps x 12s
  int   calcMax   = 50;
  int   phaseMax  = 3000;
  float shapeVal  = random(-0.1f, 0.1f);
  float phaseInit = random(TWO_PI);
  float baseHue   = random(360.0f);

  translate(width / 2, height / 2);
  for (int frmCnt = 0; frmCnt < frmMax; ++frmCnt) {
    float frmRatio = map(frmCnt, 0, frmMax, 0.0f, 1.0f);

    blendMode(BLEND);
    background(0.0f, 0.0f, 90.0f, 100.0f);

    blendMode(SUBTRACT);
    for (int phaseCnt = 0; phaseCnt < phaseMax; ++phaseCnt) {

      float phaseRatio = map(phaseCnt, 0, phaseMax, 0.0f, 1.0f);
      float prevX = cos(frmRatio * TWO_PI) * shapeVal;
      float prevY = sin(frmRatio * TWO_PI) * shapeVal;
      float prevT = phaseRatio * TWO_PI;
 
      for (int calcCnt = 0; calcCnt < calcMax; ++calcCnt) {

        float calcRatio = map(calcCnt, 0, calcMax, 0.0f, 1.0f);
        float x = (pow(sin(prevX), 2) + cos(prevY)) * sin(prevT);
        float y = (pow(cos(prevX), 2) + sin(prevY)) * cos(prevT);
        float t = prevT + phaseInit + (calcRatio + x + y) * shapeVal + TWO_PI * frmRatio;

        float eHue = baseHue + calcRatio * 120.0f;
        float eSat = 30.0f + abs(sin((phaseRatio * 2.0f + frmRatio) * TWO_PI)) * 50.0f;
        float eBri = abs(sin((phaseRatio + frmRatio * 2) * TWO_PI)) * 10.0f;
        float eSiz = abs(sin((phaseRatio + frmRatio + calcRatio) * TWO_PI)) * 2.0f;

        fill(eHue % 360.0f, eSat, eBri, 100.0f);
        ellipse(x * width * 0.3f, y * height * 0.3f, eSiz, eSiz);

        prevX = x;
        prevY = y;
        prevT = t;
      }
          
    }

    blendMode(BLEND);
    casing();
    saveFrame("frames/" + String.format("%04d", frmCnt) + ".png");

  }

  exit();

}

/**
 * casing : draw fancy casing
 */
private void casing() {
  fill(0.0f, 0.0f, 0.0f, 0.0f);
  strokeWeight(34.0f);
  stroke(0.0f, 0.0f, 0.0f, 100.0f);
  rect(0.0f, 0.0f, width, height);
  strokeWeight(30.0f);
  stroke(0.0f, 0.0f, 100.0f, 100.0f);
  rect(0.0f, 0.0f, width, height);
  noStroke();
  noFill();
  noStroke();
}


  public void settings() { size(720, 720);
smooth(); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "c221128" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
