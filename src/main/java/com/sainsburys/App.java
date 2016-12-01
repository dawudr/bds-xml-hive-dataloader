package com.sainsburys;

import java.io.File;

public class App {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Usage: FileXmlDataLoader <Enter Path to Xml data file(*.xml)>");
            System.exit(1);
        } else if (!new File(args[0]).exists()) {
            System.err.println("Error: File or Folder speicifed doesn't exist!: " + args[0]);
            System.exit(1);
        } else {
            FileXmlDataLoader.loadXMLfromFile(args[0]);
        }
    }
}
