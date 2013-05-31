package com.doylecnn;

import org.apache.commons.cli.*;

import java.util.List;

public class Main {
    private Options options = new Options();

    public static void main(String[] args) {
        Main m = new Main();
        m.command(args);
    }

    private void command(String[] args) {
//        options.addOption(OptionBuilder.withLongOpt("center").withArgName( "lat,lng" )
//                .hasArg()
//                .withDescription( "center coordinate" )
//                .create( "c"));
//
//        options.addOption(OptionBuilder.withLongOpt("range").withArgName( "km" )
//                .hasArg()
//                .withDescription( "range" )
//                .create( "r"));

        options.addOption(OptionBuilder.withLongOpt("northEastCoord").withArgName( "lat,lng" )
                .hasArg()
                .withDescription( "north east coordinate" )
                .create( "ne"));

        options.addOption(OptionBuilder.withLongOpt("southWestCoord").withArgName("lat,lng")
                .hasArg()
                .withDescription("south west coordinate")
                .create("sw"));

        options.addOption(OptionBuilder.withLongOpt("minZoomLevel").withArgName( "minZoomLevel" )
                .hasArg()
                .withDescription( "map min zoom level, default=16" )
                .create( "m"));

        options.addOption(OptionBuilder.withLongOpt("maxZoomLevel").withArgName("maxZoomLevel")
                .hasArg()
                .withDescription("map max zoom level, default=16")
                .create("M"));

        options.addOption(OptionBuilder.withLongOpt("help")
                .withDescription("show this info.")
                .create("h"));

        CommandLineParser parser = new PosixParser();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse( options, args );
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            showHelp(1);
        }

        if (null == line || line.hasOption("help")){
            showHelp(0);
        }

        Double neLat=0.0,neLng=0.0,swLat=0.0,swLng=0.0;
//        if ((line.hasOption("c") || line.hasOption("r")) && !(line.hasOption("ne") && line.hasOption("sw"))){
//            String[] center= line.getOptionValue("ne").split(",");
//            if(center.length!=2)
//            {
//                System.err.println( "Parsing failed.  Reason: -c 参数格式错误" );
//                showHelp(1);
//            }
//            center = format(center);
//
//
//            if(line.hasOption("r")){
//
//            }
//
//            try {
//                neLat = Double.parseDouble(center[0])+1.0;
//                neLng = Double.parseDouble(center[1])+1.0;
//                swLat = Double.parseDouble(center[0])-1.0;
//                swLng = Double.parseDouble(center[1])-1.0;
//            }
//            catch( NumberFormatException exp ) {
//                // oops, something went wrong
//                System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
//                showHelp(1);
//            }
//
//        }
//        else
        if (line.hasOption("ne") && line.hasOption("sw") && !(line.hasOption("c") || line.hasOption("r"))){
            String[] ne = line.getOptionValue("ne").split(",");
            String[] sw = line.getOptionValue("sw").split(",");

            if(ne.length!=2 || sw.length!=2)
            {
                System.err.println( "Parsing failed.  Reason: -ne, -sw wrong format" );
                showHelp(1);
            }

            ne = format(ne);
            sw = format(sw);

            try {
                neLat = Double.parseDouble(ne[0]);
                neLng = Double.parseDouble(ne[1]);
                swLat = Double.parseDouble(sw[0]);
                swLng = Double.parseDouble(sw[1]);
            }
            catch( NumberFormatException exp ) {
                // oops, something went wrong
                System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
                showHelp(1);
            }
        }
        else{
            if((line.hasOption("ne") || line.hasOption("sw")) && !(line.hasOption("ne") && line.hasOption("sw"))){
                System.err.println( "Parsing failed.  Reason: -ne and -sw must together use");
                showHelp(1);
            }
        }

        Integer minZoomLevel=16, maxZoomLevel=16;
        if(line.hasOption("m") && line.hasOption("M")){
            try {
                minZoomLevel = Integer.parseInt(line.getOptionValue("m"));
                maxZoomLevel = Integer.parseInt(line.getOptionValue("M"));
            }
            catch( NumberFormatException exp ) {
                // oops, something went wrong
                System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
                showHelp(1);
            }
        }

        LocationCoordinate2D neCoord = new LocationCoordinate2D(neLat,neLng);
        LocationCoordinate2D swCoord = new LocationCoordinate2D(swLat,swLng);
        S2Geometry s = new S2Geometry();
        List<String> cellIds = s.cellsForRegion(neCoord,swCoord,minZoomLevel,maxZoomLevel);
        StringBuffer sb = new StringBuffer();
        for(String cellId : cellIds)
        {
            sb.append(cellId).append(',');
        }
        String result = sb.toString();
        System.out.println(result.substring(0,result.length()-1));
    }

    // 把Lat6，Lng6转成Double
    private String[] format(String[] coordString){
        String[] result = new String[coordString.length];
        for(int i=0;i<coordString.length;i++){
            String s = coordString[i];
            if(s.length()>=8 && s.indexOf(".")<0){
                s = s.substring(0,s.length()-6)+"."+s.substring(s.length()-6);
                result[i] = s;
            }
        }
        return result;
    }

    private void showHelp(int exitCode)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar s2-cellids.jar", options );
        System.exit(exitCode);
    }
}
