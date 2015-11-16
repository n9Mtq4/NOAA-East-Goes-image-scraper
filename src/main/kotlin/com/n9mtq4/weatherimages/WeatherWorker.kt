package com.n9mtq4.weatherimages

/**
 * Created by will on 11/13/15 at 6:20 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class WeatherWorker : Runnable {
	
	var ticks: Int
	var running: Boolean
	var targetTime: Long
	
	constructor() {
		this.ticks = 0
		this.running = true
		this.targetTime = System.currentTimeMillis()
	}
	
	override fun run() {
		
		while (running) {
			
//			spin lock for time
//			thread.sleep doesn't stay consistent against computer sleeping
//			ex: Thread.sleep(1000 * 60 * 60) should sleep for a min
//			if the computer is put to sleep in the middle of that, it will be longer
//			this spin lock will fix that
			while (running) {
				val currentTime = System.currentTimeMillis()
				if (targetTime - currentTime < CHECK_SLEEP_TIME / 2) break
				Thread.sleep(CHECK_SLEEP_TIME) // sleep for a couple of minutes
			}
			
			
//			update ticks
			ticks++
			
//			download all the images
			println("Started download: #$ticks at ${getTimestamp()}")
			work()
			println("Finished download #$ticks at ${getTimestamp()}")
			println("The next download is targeted for ${getTimestamp(SLEEP_TIME)}")
			
//			update target time
			targetTime = System.currentTimeMillis() + SLEEP_TIME
			
		}
		
	}
	
	/**
	 * Note: this method may take anywhere from 0 to the CHECK_SLEEP_TIME
	 * to register and stop the run method's loop
	 * THIS DOES NOT STOP THE THREAD, ONLY STOPS THE RUN METHOD
	 * THE IMAGES WILL BE DOWNLOADED ONE MORE TIME BEFORE THE RUN METHOD STOPS
	 * */
	fun stop() {
		this.running = false
	}
	
}
