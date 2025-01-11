/*
 *
 * @author: Julia Dymnicki
 * 
 * To generate weather for location at longitude -98.76 and latitude 26.70 for
 * the month of February do:
 * java WeatherGenerator -98.76 26.70 3
 */

public class WeatherGenerator {

    static final int WET = 1; // Use this value to represent a wet day
    static final int DRY = 2; // Use this value to represent a dry day 
    
    // Number of days in each month, January is index 0, February is index 1...
    static final int[] numberOfDaysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    
    public static void populateArrays(double[][] drywet, double[][] wetwet) {

        
        StdIn.setFile("drywet.txt");

    for(int i=0; i < drywet.length; i++){
            for(int j=0; j<14; j++){
                drywet[i][j] = StdIn.readDouble();
            }
        }

    StdIn.setFile("wetwet.txt");

    for(int i=0; i < wetwet.length; i++){
            for(int j=0; j<14; j++){
                wetwet[i][j] = StdIn.readDouble();
            }
        }
    }
// {Longitude, Latitude, January, February, March, April, May, June, July, August, September, October, November, December}
//  {-97.58,    26.02,    0.76,    0.75,     0.77,  0.74, 0.80, 0.86, 0.94, 0.97,   0.89,      0.77,    0.74,     0.77}

    
    public static void populateLocationProbabilities( double[] drywetProbability, double[] wetwetProbability, 
                                     double longitude, double latitude, 
                                     double[][] drywet, double[][] wetwet){
     int input = 2;
     int location = 0;
                
     for(int i=0; i < drywet.length; i++)                                     
     {
         for(int j=0; j < drywet[i].length; j++)                                     
         {
             if(drywet[i][0] == longitude && drywet[i][1] == latitude)
             {
                 location =i;
                 
              }
             }
             
       }
       for(int k=0; k < drywetProbability.length; k++)                                     
              {
                  drywetProbability[k] = drywet[location][input];
                  input++;
              }
           
        location = 0;
         input = 2;
         for(int i=0; i < wetwet.length; i++)                                     
        {
            for(int j=0; j < wetwet[i].length; j++)                                     
            {
                if(wetwet[i][0] == longitude && wetwet[i][1] == latitude)
                {
                    location =i;
                    
                 }
                }
                
          }
          for(int k=0; k < wetwetProbability.length; k++)                                     
                 {
                     wetwetProbability[k] = wetwet[location][input];
                     input++;
                 }
      }
         
    

    
    public static int[] forecastGenerator( double drywetProbability, double wetwetProbability, int numberOfDays) {
        //int[] forecast = forecastGenerator( 0.27,  0.55, 31);
        //                                  dry, wet, # of days
        int[] forecast = new int[numberOfDays];
        double random1 = StdRandom.uniform();
        if (random1 < 0.50)
            {
                forecast[0] = 1; //wet
            }
            else{
                forecast[0] = 2; //dry
            }
        for(int i = 1; i < forecast.length; i++)
        {
            double random = StdRandom.uniform();
            if(forecast[i-1] == 1) //wet
            {
                if(random <= wetwetProbability)
                {
                    forecast[i] = 1;
                }
                else{
                    forecast[i] = 2;
                }
            }
            else{ //dry
                if(random <= drywetProbability)
                {
                    forecast[i] = 1;
                }
                else{
                    forecast[i] = 2;
                }
            }
            
        }
        return forecast;
    }
    
    public static int[] oneMonthForecast(int numberOfLocations, int month, double longitude, double latitude ){
        //oneMonthForecast( 4100, 0, -97.58, 26.02);
        //             # of locations, month, longitude, latitude

        double[][] drywet = new double[numberOfLocations][14];
        double[][] wetwet = new double[numberOfLocations][14];
        populateArrays(drywet, wetwet);

        double[] drywetProbability = new double[12];
        double[] wetwetProbability = new double[12];
        populateLocationProbabilities(drywetProbability, wetwetProbability, longitude, latitude, drywet, wetwet); 

        
        int[] forecast = forecastGenerator(drywetProbability[month], wetwetProbability[month], numberOfDaysInMonth[month]);
        return forecast;
        
    }

    
    public static int numberOfWetDryDays (int[] forecast, int mode) {
        int countwet = 0;
        int countdry = 0;
        for(int i=0; i< forecast.length; i++)
        {
            if(forecast[i] == 1) //wet
            {
                countwet++;
            }
            else{
                countdry++;
            }
        }
        
        if (mode == WET)
        {
            mode = countwet;
        }
        if (mode == DRY) {
            mode = countdry;
        }
        return mode;
    }
    
   
    public static int lengthOfLongestSpell (int[] forecast, int mode) {
        int wet = 0;
        int wetbig = 1;
        int dry = 0;
        int drybig = 1;
        
        for(int i= 0; i < forecast.length; i++)
        {
            if(forecast[i] == 1 )
            {
                wet++;
                if(wet > wetbig)
                {
                    wetbig = wet;
                }
                dry=0;
            }
            
            if(forecast[i] == 2 ){
            //dry
            {
                wet=0;
                dry++;
                if(dry > drybig)
                {
                    drybig = dry;
                }
            }

        }
            
        }
        if (mode == WET)
        {
            mode = wetbig;
        }
        if (mode == DRY) {
            mode = drybig;
        }
        return mode;
    }
    
    
    public static int bestWeekToTravel(int[] forecast){
        int mostdays = 0;
        int index = 0;
        for(int i=0;i<forecast.length - 6;i++) 
        {
           int drydays = 0;
            for(int j=i; j< (i+7);j++)  // 7 days period
            {
                if(forecast[j] == 2)
                {
                    drydays++; 
                }
                
            }
            if ( drydays > mostdays)
            {
                mostdays = drydays;
                index = i;
            }
        }
        return index;
    }
        
    

    
    /*
     * Reads the files containing the transition probabilities for US locations.
     * Execution:
     *   java WeatherGenerator -97.58 26.02 3
     */
    public static void main (String[] args) {

        StdRandom.setSeed(1668027068037L);
        double longitude = Double.parseDouble(args[0]);
        double latitude  = Double.parseDouble(args[1]);
        int    month     = Integer.parseInt(args[2]);
        
        //POPULATE ARRAYS
        double[][] drywet = new double[4100][14];
          double[][] wetwet = new double[4100][14];
          populateArrays(drywet, wetwet);

        //LOCATION PROBABILITIES
        double[] drywetProbability = new double[12];
       double[] wetwetProbability = new double[12];
        populateLocationProbabilities(drywetProbability, wetwetProbability, longitude,    latitude, drywet, wetwet); 

        




        
        int[] forecast =  oneMonthForecast(4100, 6, -100.4, 47.42);
       // int[] forecast =  oneMonthForecast(4100,  month,  longitude,  latitude);
        for ( int i = 0; i < forecast.length; i++ ) {
            // This is the ternary operator. (conditional) ? executed if true : executed if false
            String weather = (forecast[i] == WET) ? "Wet" : "Dry";  
            StdOut.println("Day " + (i) + " is forecasted to be " + weather);
        }
        
        System.out.println(bestWeekToTravel(forecast));

        //int[] forecast = oneMonthForecast( numberOfRows,  month,  longitude,  latitude );
      
        
        
        /* 
        int drySpell = lengthOfLongestSpell(forecast, DRY);
        int wetSpell = lengthOfLongestSpell(forecast, WET);
        int bestWeek = bestWeekToTravel(forecast);

        StdOut.println("There are " + forecast.length + " days in the forecast for month " + month);
        StdOut.println(drySpell + " days of dry spell.");
        StdOut.println("The bestWeekToTravel starts on:" + bestWeek );

       
        */
    }
}
