@file:JvmName("WeatherImages")

package com.n9mtq4.weatherimages

import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by will on 11/13/15 at 6:16 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
val SLEEP_TIME: Long = 1000 * 60 * 60 * 2 // 2 hour sleep time (4 hours default)
val CHECK_SLEEP_TIME: Long = SLEEP_TIME / 12 // accuracy of target time is 1/12th of the original sleep time
val WORKING_DIR: File = File("img") // directory with images is in ./img
val IMAGE_SELECTOR: String = "body > table > tbody > tr > td > a"
val ROOT_URL: String = "http://www.ssd.noaa.gov/goes/east/natl/img/"
val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
val USER_AGENT: String = "n9Mtq4-goes-east-scrapper/0.1 (+https://github.com/n9Mtq4/NOAA-Goes-East-image-scraper)"

fun main(args: Array<String>) {
	
	val weatherWorker = WeatherWorker()
	weatherWorker.run()
	
}

internal fun work() {
	
	if (!WORKING_DIR.exists()) WORKING_DIR.mkdirs() // make sure we can write before doing an io
	
	try {
		
		val document = Jsoup.connect(ROOT_URL).userAgent(USER_AGENT).get()
		val images = document.select(IMAGE_SELECTOR)
		
		images.map { it.attr("href") }.
				filter { it.endsWith("vis.jpg") }.
				forEach { processImage(it) }
		
	} catch(e: Exception) {
		println("Error downloading the images! Will try again at ${getTimestamp(SLEEP_TIME)}.")
	}
	
}


/**
 * download the specified image if necessary
 * */
internal fun processImage(imageName: String) {
	
//	first make sure that we want to download it
	val targetFile = File(WORKING_DIR, imageName)
	if (targetFile.exists()) return // the image has already been downloaded
	
	print("Downloading $imageName...")
	
//	download the file
	val url = URL(ROOT_URL + imageName)
	val rbc = Channels.newChannel(url.openStream())
	val fos = FileOutputStream(targetFile)
	fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
	
	println("done")
	
}

/**
 * gets the timestamp in ms milliseconds in the future
 * @return the timestamp in the format of yyyy/MM/dd HH:mm:ss
 * */
internal fun getTimestamp(ms: Long): String {
	return DATE_FORMAT.format(Date(System.currentTimeMillis() + ms))
}

/**
 * @return the current timestamp
 * */
internal fun getTimestamp(): String {
	return getTimestamp(0)
}
