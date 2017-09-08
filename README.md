# WeatherImages

## About
This is a tiny program that scrapes and downloads images from 
NOAA's goes east satellite.
The point of this project is to be able to keep a locally
stored database of these images, so you can look back further than
the ~44 image limit of the online directory list.

## Running
1. Obtain a compiled jar file: See the "Building from source" instructions or head over the [releases page](https://github.com/n9Mtq4/NOAA-Goes-East-image-scraper/releases).
2. cd to the directory containing the jar file. The images will also be downloaded in this directory.
3. run the WeatherImages.jar with "java -jar WeatherImages.jar".
4. The images will appear in a newly created "./img/" directory.

## License
This program is copyrighted to Will Bresnahan or n9Mtq4 under the MIT License. More info in the [LICENSE File](https://github.com/n9Mtq4/NOAA-Goes-East-image-scraper/blob/master/LICENSE).

## Building from source
1. Clone or download the source code
2. Extract the code if you downloaded the zip file
3. cd to the directory with the code
4. Either run "./gradlew build" on unix systems or "gradlew.bat build" on windows
5. Your shiny new jar will be located in "build/libs/"

## Image Sources
A director listing of the images can be found [here](http://www.ssd.noaa.gov/goes/east/natl/img/),
and a short loop of some recent images can be found [here](http://www.ssd.noaa.gov/goes/east/natl/h5-loop-vis.html).
About two months of images downloaded using this program in a video can be found [here](https://youtu.be/1EmzPW0YhKU).
