/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package screenmaster;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 *
 * @author cybergnome
 */
public class ManagedImage {
	private final Image img;
	
	public ManagedImage(InputStream imgStream){
		img = new javafx.scene.image.Image(imgStream);
	}
	
	public void setImage(ImageView imgView, Region container, ZoomMode zoomMode){
		double imgAspectRatio = (img.getWidth())/(img.getHeight());
		double parentAspectRatio = (container.getWidth())/(container.getHeight());
		imgView.setPreserveRatio(true);
		imgView.setImage(img);
		switch(zoomMode){
			case NOZOOM: // do not scale
				centerViewOn(0,0,imgView,container);
				imgView.setSmooth(false);
				imgView.setFitHeight(img.getHeight()); // 0 means ignore fitting/ otherwise this is the dimension measurement
				imgView.setFitWidth(img.getWidth());
				break;
			case FIT: // scale but don't crop
				imgView.setSmooth(true);
				if(imgAspectRatio > parentAspectRatio){
					// image is wider than container
					imgView.setFitHeight(0); // 0 means ignore fitting/ otherwise this is the dimension measurement
					imgView.setFitWidth(container.getWidth());
				} else {
					// image is taller than container
					imgView.setFitHeight(container.getHeight()); // 0 means ignore fitting/ otherwise this is the dimension measurement
					imgView.setFitWidth(0);
				}
				break;
			case FILL: // scale and crop
				// TODO: not yet working
				imgView.setSmooth(true);
				if(imgAspectRatio > parentAspectRatio){
					// image is wider than container
					imgView.setFitHeight(container.getHeight()); // 0 means ignore fitting/ otherwise this is the dimension measurement
					imgView.setFitWidth(container.getHeight() * imgAspectRatio );
				} else {
					// image is taller than container
					imgView.setFitWidth(container.getWidth()); // 0 means ignore fitting/ otherwise this is the dimension measurement
					imgView.setFitHeight(container.getWidth() / imgAspectRatio );
				}
				break;
		}
	}
	/**
	 * 
	 * @param dx Coordinate of center of view, where (0,0) is the center of the 
	 * image.
	 * @param dy Coordinate of center of view, where (0,0) is the center of the 
	 * image.
	 * @param imgView
	 * @param container 
	 */
	public void centerViewOn(double dx, double dy, ImageView imgView, Region container) {
		double xOffset = (img.getWidth() - container.getWidth()) * 0.5;
		double yOffset = (img.getHeight() - container.getHeight()) * 0.5;
		Rectangle2D viewPortArea = new Rectangle2D(xOffset + dx, yOffset+dy,img.getWidth(),img.getHeight());
		imgView.setViewport(viewPortArea);
	}
}
