import ca.ualberta.stothard.cgview.*;
import java.io.*;
import java.awt.*;

public class CgviewTest2 implements CgviewConstants {
   
    public static void main( String args[] ) {

	int length = 9000;
	Cgview cgview = new Cgview(length);
	
	//some optional settings
	cgview.setWidth(600);
	cgview.setHeight(600);
	cgview.setBackboneRadius(140.0f);
	cgview.setBackboneColor(Color.red);
	cgview.setTitle("Example");
	cgview.setLabelPlacementQuality(10);
	cgview.setShowWarning(true);
	cgview.setLabelLineLength(8.0d);
	cgview.setLabelLineThickness(1.5f);
	cgview.setRulerTextPadding(6.0f);

	//create a FeatureSlot to hold sequence features
	FeatureSlot featureSlot = new FeatureSlot(cgview, DIRECT_STRAND);

	//create random sequence features
	for (int i = 1; i <= 100; i = i + 1) {

	    int j = Math.round((float)((float)(length - 2) * Math.random())) + 1;
	    
	    //a Feature to add to our FeatureSlot
	    Feature feature = new Feature(featureSlot, "label");
	    feature.setColor(Color.black);

	    //a single FeatureRange to add the Feature
	    FeatureRange featureRange = new FeatureRange (feature, j, j + 1);
	    featureRange.setDecoration(DECORATION_CLOCKWISE_ARROW);
	    
	}
	
	try {
	    //create a PNG file
	    CgviewIO.writeToPNGFile(cgview, "output.png");

	    //create a zoomed in PNG file
	    //to draw a zoomed in version, use the setDesiredZoom(double)
	    //and setDesiredZoomCenter(int) methods before drawing.
	    cgview.setDesiredZoom(5.0d);
	    cgview.setDesiredZoomCenter(100);
	    cgview.setLabelLineLength(50.0d);
	    CgviewIO.writeToPNGFile(cgview, "output_zoomed.png");

	    //create a zoomed in SVGZ file
	    cgview.setDesiredZoom(5.0d);
	    cgview.setDesiredZoomCenter(4500);
	    cgview.setLabelLineLength(50.0d);
	    cgview.setWidth(750);
	    boolean embedFonts = true;
	    boolean useCompression = false; //creates SVG instead of SVGZ
	    CgviewIO.writeToSVGFile(cgview, "output_zoomed.svg", embedFonts, useCompression);	    
	}
        catch (Exception e) {
	    e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
