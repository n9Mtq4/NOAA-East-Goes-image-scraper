package com.n9mtq4.weatherimages

import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by will on 11/13/15 at 6:20 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class WeatherWorker : Runnable {
	
	companion object {
		val SLEEP_TIME: Long = 1000 * 60 * 60 * 4 // four hour sleep time
		val DAY_LENGTH: Long = 86400000 / SLEEP_TIME // ms in a day
		val WORKING_DIR: File = File("img") // directory with images is in ./img
		val IMAGE_SELECTOR: String = "body > table > tbody > tr > td > a"
		val ROOT_URL: String = "http://www.ssd.noaa.gov/goes/east/natl/img/"
		val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	}
	
	val thread: Thread
	var ticks: Int
	var running: Boolean
	
	constructor() {
		this.ticks = 0
		this.running = true
		this.thread = Thread(this)
		thread.start()
	}
	
	override fun run() {
		
		while (running) {
			
//			update ticks
			ticks++
			
//			download all the images
			println("Started download: #$ticks at ${getCurrentTimestamp()}")
			work()
			println("Finished download #$ticks at ${getCurrentTimestamp()}")
			println("The next download will be at ${getCurrentTimestamp(SLEEP_TIME)}")
			
//			every day, compress the images into a zip
			if (ticks % DAY_LENGTH == 0L) {
//				TODO: compress day's images
			}
			
//			sleep
			Thread.sleep(SLEEP_TIME)
			
		}
		
	}
	
	private fun work() {
		
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
	
	private fun processImage(imageName: String) {
		
		print("Downloading $imageName...")
		
//		first make sure that we want to download it
		val targetFile = File(WORKING_DIR, imageName)
		if (targetFile.exists()) {
			// the image has already been downloaded
			println("already have it")
			return
		}
		
//		download the file
		val url = URL(ROOT_URL + imageName)
		val rbc = Channels.newChannel(url.openStream())
		val fos = FileOutputStream(targetFile)
		fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
		
		println("done")
		
	}
	
	private fun getCurrentTimestamp(ms: Long): String {
		return DATE_FORMAT.format(Date(System.currentTimeMillis() + ms))
	}
	
	private fun getCurrentTimestamp(): String {
		return getCurrentTimestamp(0)
	}
	
	/**
	 * Note: this method may take anywhere from 0 to the SLEEP_TIME
	 * to register and stop the run method's loop
	 * THIS DOES NOT STOP THE THREAD, ONLY STOPS THE RUN METHOD
	 * */
	fun stop() {
		this.running = false
	}
	
}
