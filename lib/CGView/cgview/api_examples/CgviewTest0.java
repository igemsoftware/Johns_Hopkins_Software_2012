import ca.ualberta.stothard.cgview.*;
import java.io.*;

public class CgviewTest0 implements CgviewConstants {
   
    public static void main( String args[] ) {

	int length = 9000;
	Cgview cgview = new Cgview(length);
	
	//some optional settings
	cgview.setWidth(600);
	cgview.setHeight(600);
	cgview.setBackboneRadius(160.0f);
	cgview.setTitle("Example");
	cgview.setLabelPlacementQuality(10);
	cgview.setShowWarning(true);
	cgview.setLabelLineLength(8.0d);
	cgview.setLabelLineThickness(0.5f);

	//create a FeatureSlot to hold sequence features
	FeatureSlot featureSlot = new FeatureSlot(cgview, DIRECT_STRAND);

	//create random sequence features
	for (int i = 1; i <= 100; i = i + 1) {

	    int j = Math.round((float)((float)(length - 2) * Math.random())) + 1;
	    
	    //a Feature to add to our FeatureSlot
	    Feature feature = new Feature(featureSlot, "label");

	    //a single FeatureRange to add the Feature
	    FeatureRange featureRange = new FeatureRange (feature, j, j + 1);
	    featureRange.setDecoration(DECORATION_CLOCKWISE_ARROW);
	    
	}
	
	try {
	    //create a PNG file
	    CgviewIO.writeToPNGFile(cgview, "random_1.png");
	}
        catch (IOException e) {
	    e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
