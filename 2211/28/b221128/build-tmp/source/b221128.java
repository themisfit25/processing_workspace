/* autogenerated by Processing revision 1289 on 2022-11-30 */
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

public class b221128 extends PApplet {

boolean gen = false;
boolean res = false;

public void setup() {
  /* size commented out by preprocessor */;
  colorMode(HSB, 360.0f, 100.0f, 100.0f, 100.0f);
  rectMode(CENTER);
  /* smooth commented out by preprocessor */;
  noLoop();
}

public void draw() {
  if(frameCount==1 || res) {
    background(0.0f, 0.0f, 90.0f, 100.0f);
    create();
  }
  
  if (gen) {
    gen = false;
    saveTheFrame();
    println("SAVE");
  }

  //exit();
}

public void create() {
  int frmRate  = 12;
  int frmMorph = frmRate * 3;       // morphing duration frames
  int cycles   = 6;                 // animation cycle no
  int frmMax   = frmMorph * cycles; // whole frames

  // shapes
  int   lines  = floor(random(2.0f, 6.0f));
  int   waves  = floor(random(3.0f, 5.0f));
  float radius = min(width, height) * 0.25f;
  float step   = floor(random(1.0f, 6.0f)) * 0.05f;
  ArrayList<ArrayList<PVector>> shapes = new ArrayList<ArrayList<PVector>>();
  for (int i = 0; i < cycles; i++) {
    shapes.add(getShape(lines, waves, i, radius, step));
  }
  int nodeMax = shapes.get(0).size();

  // morphing tools
  ArrayList<PVector> nodesFrom = new ArrayList<PVector>();
  ArrayList<PVector> nodesTo   = new ArrayList<PVector>();
  int cycleCnt = 0;

  translate(width * 0.5f, height * 0.5f);
  for (int frmCnt = 0; frmCnt < frmMax; frmCnt++) {
    //background(0.0, 0.0, 90.0, 100.0);

    // select morphing objects
    if (frmCnt % frmMorph == 0) {
      cycleCnt = frmCnt / frmMorph;
      nodesFrom = shapes.get(cycleCnt);
      nodesTo   = shapes.get((cycleCnt + 1) % cycles);
    }

    // easing calculation
    float frmRatio = map(frmCnt % frmMorph, 0, frmMorph - 1, 0.0f, 1.0f);
    float morphRatio = easeInOutCubic(frmRatio);
    float easeRatio  = InFourthPow(frmRatio);

    // draw
    fill(1.0f, 1.0f); //boxes
    //noFill(); //lines
    stroke(0.0f, 0.0f, 30.0f, 5.0f);
    strokeWeight(2.0f);
    beginShape();
    for (int i = 0; i < nodeMax + 3; i++) {
      // j is for close the curve.
      // ref. https://www.deconbatch.com/2021/01/processing-curvevertex-memo.html
      int j = i % nodeMax;
      float nodeRatio = map(j, 0, nodeMax, 0.0f, 1.0f);
      plotVertex(nodesFrom.get(j), nodesTo.get(j), nodeRatio, easeRatio, morphRatio * morphRatio);
    }
    endShape();
    //casing();

    //saveFrame("frames/" + String.format("%04d", frmCnt) + ".png");
    
  }
  saveTheFrame();
}

public void keyPressed() {
  if (key == 'g') {
    gen = true;
  }
}

public void mousePressed() {
  reset();
}

public void reset() {
  res = true;
}

public void saveTheFrame() {
  String path = sketchPath();
  File f = new File(path+"/render");
  int ind = 0;
  if (f.list() != null) {
    ind = f.list().length;
  }
  String fileName = "render/out"+Integer.toString(ind)+".png";
  saveFrame(fileName);
  println("Saved to " + fileName);
}

/**
 * getShape : get shape points.
 * @param _lines, _waves, _shape : shape parameters
 * @param _radius : shape size 
 * @param _step   : vertex points spacing
 * @return   : PVector array of shape points
 */
private ArrayList<PVector> getShape(int _lines, int _waves, int _shape, float _radius, float _step) {
  ArrayList<PVector> line = new ArrayList<PVector>();
  
  // curve lines
  float phase = random(PI);
  for (int l = 0; l < _lines; l++) {
    for (float theta = 0.0f; theta < TWO_PI; theta += PI * _step) {
      float t = (theta + phase) % TWO_PI;
      float r = _radius * (1.0f + 0.4f * sin(sin(t * (_waves + _shape) + l * TWO_PI / _lines) * TWO_PI));
      float x = r * cos(t);
      float y = r * sin(t);
      line.add(new PVector(x, y));
    }
  }
  return(line);
}

/**
 * plotVertex : plot the vertex with morphing calculation.
 * @param  _from, _to : nodes to draw, morphing from and to
 * @param  _ratioN    : vartex number ratio
 * @param  _ratioM    : morphing ratio
 * @param  _ratioS    : shift ratio for viscous moving
 */
public void plotVertex(PVector _from, PVector _to, float _ratioN, float _ratioM, float _ratioS) {
  float rT = _ratioM * constrain(_ratioN + _ratioS, 0.0f, 1.0f);
  float rF = 1.0f - rT;
  float nX = (_from.x * rF + _to.x * rT);
  float nY = (_from.y * rF + _to.y * rT);
  blendMode(DIFFERENCE); //lines
  //rect(nX, nY, nX, nY); //lines
  curveVertex(nX, nY); //curves
}

/**
 * easeInOutCubic easing function.
 * @param  t     0.0 - 1.0 : linear value.
 * @return float 0.0 - 1.0 : eased value.
 */
public float easeInOutCubic(float t) {
  t *= 2.0f;
  if (t < 1.0f) {
    return pow(t, 3) / 2.0f;
  }
  t -= 2.0f;
  return (pow(t, 3) + 2.0f) / 2.0f;
}
  
/**
 * InFourthPow : easing function.
 * @param  _t    0.0 - 1.0 : linear value.
 * @return       0.0 - 1.0 : eased value.
 */
private float InFourthPow(float _t) {
  return 1.0f - pow(1.0f - _t, 4);
}

/**
 * casing : draw fancy casing
 */
private void casing() {
  fill(0.0f, 0.0f, 0.0f, 0.0f);
  strokeWeight(54.0f);
  stroke(0.0f, 0.0f, 0.0f, 100.0f);
  rect(0.0f, 0.0f, width, height);
  strokeWeight(50.0f);
  stroke(0.0f, 0.0f, 100.0f, 100.0f);
  rect(0.0f, 0.0f, width, height);
  noStroke();
  noFill();
  noStroke();
}


  public void settings() { size(1000, 1000);
smooth(); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "b221128" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}