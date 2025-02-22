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

public class sketch_180912a extends PApplet {

FractalRoot[] _pentagons;
int _pentagonCount = 3;
int _maxlevels = 30;
float _lineStrength = 0.001f;
float _strutFactor = 0.001f;
float _fractalScale = 0.7f;
int _sides = 7;
float _elipseBaseScale = 3.0f;
float _angleOffsetStep = 22.5f;
float _fractalScaleStep = 0.1f;
int _levelStep = 10;
float _modifier = 0.0f;

public void setup() {
	background(255);
	/* size commented out by preprocessor */;
	//noStroke();
	/* smooth commented out by preprocessor */;
	_pentagons = new FractalRoot[_pentagonCount];
	for (int i = 0; i < _pentagonCount; i++) 
	{
		_pentagons[i] = new FractalRoot(i * _angleOffsetStep, (_fractalScale - (i * _fractalScaleStep)));
	}
}

public void draw() 
{
	background(255);
	int m = millis() % 128;
	for (int i = 0; i < _pentagonCount; i++) 
	{

		_pentagons[i] = new FractalRoot((i) * _angleOffsetStep + m, (_fractalScale - (i * _fractalScaleStep)));
		_pentagons[i].drawShape((i) * _angleOffsetStep + m, (_fractalScale - (i * _fractalScaleStep)));
	}
}

class PointObject {
	float x, y;
	PointObject(float ex, float why) {
		x = ex; y = why;
	}
	public void drawMe(float ex, float why) {
		x = ex; y = why;
	}
}

class FractalRoot {
	PointObject[] pointArr = new PointObject[_sides];
	Branch rootBranch;

	FractalRoot(float offset, float fractScale) {
		float centerX = width/2;
		float centerY = width/2;
		int count = 0;
		for(int i = 0; i < _sides; i+=1)
		{
			float input = ((360 / _sides)) * i;
			float x = centerX + ((width * fractScale) * cos(radians(input + offset)));
			float y = centerY + ((width * fractScale) * sin(radians(input + offset)));
			pointArr[count] = new PointObject(x,y);
			count++;
		}
		rootBranch = new Branch(0, 0, pointArr);	
	}

	public void drawShape(float offset, float fractScale) 
	{
		float centerX = width/2;
		float centerY = width/2;
		int count = 0;
		for(int i = 0; i < _sides; i+=1)
		{
			float input = ((360 / _sides)) * i;
			float x = centerX + ((width * fractScale) * cos(radians(input + offset)));
			float y = centerY + ((width * fractScale) * sin(radians(input + offset)));
			pointArr[count].drawMe(x, y);
			count++;
		}
		rootBranch.drawMe();
	}
}

class Branch 
{
	int level, num;
	PointObject[] outerPoints = {};
	PointObject[] midPoints = {};
	PointObject[] projPoints = {};
	Branch[] myBranches = {};

	Branch(int lev, int n, PointObject[] points) 
	{
		level = lev;
		num = n;
		outerPoints = points;
		midPoints = calculateMidpoints();
		projPoints = calculateStructPoints();
		if((level+1) < _maxlevels) 
		{
			Branch childBranch = new Branch(level+1, 0, projPoints);
			myBranches = (Branch[])append(myBranches, childBranch);
		}
	}

	public void drawMe() {
		strokeWeight((_maxlevels - level) * _lineStrength);
		for (int i = 0; i < outerPoints.length; i++) 
		{
			int nexti = i+1;
			if(nexti == outerPoints.length) 
			{ 
				nexti = 0; 
			}
			line(outerPoints[i].x, outerPoints[i].y, outerPoints[nexti].x, outerPoints[nexti].y);
		}

		strokeWeight((_maxlevels - level) * _lineStrength);
		fill(255, 150);
		for(int j = 0; j < midPoints.length; j++) 
		{
			float levelFloat = level * 1.0f;
			float ellipseScale = (_elipseBaseScale * (1.0f - (levelFloat / _maxlevels)));
			ellipse(midPoints[j].x, midPoints[j].y, ellipseScale, ellipseScale);
			line(midPoints[j].x, midPoints[j].y, projPoints[j].x, projPoints[j].y);
			ellipse(projPoints[j].x, projPoints[j].y, ellipseScale, ellipseScale);
		}

		for(int k = 0; k < myBranches.length; k++) 
		{
			myBranches[k].drawMe();
		}
	}

	public PointObject[] calculateMidpoints() 
	{
		PointObject[] mpArray = new PointObject[outerPoints.length];
		for (int i = 0; i < outerPoints.length; i++) 
		{
			int nexti = i+1;
			if(nexti == outerPoints.length) 
			{ 
				nexti = 0; 
			}
			PointObject thisMp = calcMidpoint(outerPoints[i], outerPoints[nexti]);
			mpArray[i] = thisMp;
		}
		return mpArray;
	}

	public PointObject calcMidpoint(PointObject end1, PointObject end2) 
	{
		float mx, my;

		if(end1.x > end2.x) 
		{
			mx = end2.x + ((end1.x - end2.x)/2);
		} 
		else 
		{
			mx = end1.x + ((end2.x - end1.x)/2);
		}

		if(end1.y > end2.y) {
			my = end2.y + ((end1.y - end2.y)/2);
		} else {
			my = end1.y + ((end2.y - end1.y)/2);
		}

		return new PointObject(mx,my);
	}

	public PointObject[] calculateStructPoints() {
		PointObject[] structArray = new PointObject[midPoints.length];
		for (int i = 0; i < midPoints.length; i++) {
			int nexti = i+3;
			if(nexti >= midPoints.length) { nexti -= midPoints.length; }
			PointObject thisSP = calcProjectionPoint(midPoints[i], outerPoints[nexti]);
			structArray[i] = thisSP;
		}
		return structArray;
	}

	public PointObject calcProjectionPoint(PointObject mp, PointObject op) {
		float px, py;
		float adj, opp;
		if(op.x > mp.x) {
			opp = op.x - mp.x;
		} else {
			opp = mp.x - op.x;
		}

		if(op.y > mp.y) {
			adj = op.y - mp.y;
		} else {
			adj = mp.y - op.y;
		}

		if(op.x > mp.x) {
			px = mp.x + (opp * _strutFactor);
		} else {
			px = mp.x - (opp * _strutFactor);
		}

		if(op.y > mp.y) {
			py = mp.y + (adj * _strutFactor);
		} else {
			py = mp.y - (adj * _strutFactor);
		}

		return new PointObject(px,py);
	}


}


  public void settings() { size(1000, 1000);
smooth(); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_180912a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
