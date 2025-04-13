package com.example.rewear.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.*;
import java.net.URL;

public class SVGLoader {
    
    public static ImageView loadSvg(String resourcePath, double width, double height) {
        try {
            URL url = SVGLoader.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("SVG resource not found: " + resourcePath);
                return new ImageView();
            }

            // Create a PNG transcoder
            PNGTranscoder transcoder = new PNGTranscoder();
            
            // Set the transcoding hints
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);
            
            // Create the transcoder input
            TranscoderInput input = new TranscoderInput(url.openStream());
            
            // Create the transcoder output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);
            
            // Perform the transcoding
            transcoder.transcode(input, output);
            
            // Convert the output stream to an input stream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            
            // Create the JavaFX image
            Image image = new Image(inputStream);
            
            // Create and return the image view
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
            
        } catch (IOException | TranscoderException e) {
            System.err.println("Error loading SVG: " + e.getMessage());
            e.printStackTrace();
            return new ImageView();
        }
    }
} 