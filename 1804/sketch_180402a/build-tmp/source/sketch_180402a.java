/* autogenerated by Processing revision 1289 on 2022-11-29 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class sketch_180402a extends PApplet {




int radius=100;
public void setup() {
	/* size commented out by preprocessor */;
	background(255);
	stroke(0);


}

public void draw() {
	background(255);
	translate(width/2,height/2,0);
	rotateY(frameCount*0.03f);
	rotateX(frameCount*0.04f);

	float s= 0;
	float t= 0;
	float lastx = 0;
	float lasty = 0;
	float lastz = 0;
	while (t<180) {

		s+=18;
		t +=1;
		float radianS=radians(s);
		float radianT=radians(t);


		float thisx= 0 +(radius*cos(radianS)*sin(radianT));
		float thisy= 0 +(radius*sin(radianS)*sin(radianT));
		float thisz= 0 +(radius*cos(radianT));

		if(lastx != 0) {
			line(thisx,thisy,thisz,lastx,lasty,lastz);
		}
		lastx=thisx;
		lasty=thisy;
		lastz=thisz;
	}

}


//ellipse(x, y, width, height);


  public void settings() { size(500, 300, OPENGL); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_180402a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
