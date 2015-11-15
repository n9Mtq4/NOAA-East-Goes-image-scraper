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
val SLEEP_TIME: Long = 1000 * 60 * 60 * 4 // four hour sleep time (4 hours default)
val CHECK_SLEEP_TIME: Long = SLEEP_TIME / 12 // accuracy of target time is 1/12th of the original sleep time
val WORKING_DIR: File = File("img") // directory with images is in ./img
val IMAGE_SELECTOR: String = "body > table > tbody > tr > td > a"
val ROOT_URL: String = "http://www.ssd.noaa.gov/goes/east/natl/img/"
val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

fun main(args: Array<String>) {
	
	val weatherWorker = WeatherWorker()
	weatherWorker.run()
	
//	make a new thread for it?
//	sometimes making a thread fails
//	val thread = Thread(weatherWorker, "WeatherImageThread")
//	thread.start()
	
}

internal fun work() {
	
	if (!WORKING_DIR.exists()) WORKING_DIR.mkdirs() // make sure we can write before doing an io
	
	val document = Jsoup.connect(ROOT_URL).get()
	val images = document.select(IMAGE_SELECTOR)
	
	images.forEach {
		val href = it.attr("href")
		if (href.endsWith("vis.jpg")) {
			processImage(href)
		}
	}
	
}

internal fun processImage(imageName: String) {
	
//	first make sure that we want to download it
	val targetFile = File(WORKING_DIR, imageName)
	if (targetFile.exists()) {
//		the image has already been downloaded
		return
	}
	
	print("Downloading $imageName...")
	
	//	download the file
	val url = URL(ROOT_URL + imageName)
	val rbc = Channels.newChannel(url.openStream())
	val fos = FileOutputStream(targetFile)
	fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
	
	println("done")
	
}

internal fun getTimestamp(ms: Long): String {
	return DATE_FORMAT.format(Date(System.currentTimeMillis() + ms))
}

internal fun getTimestamp(): String {
	return getTimestamp(0)
}
