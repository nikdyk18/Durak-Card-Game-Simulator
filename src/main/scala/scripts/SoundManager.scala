package scripts

import javax.sound.sampled._
import java.io.File
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SoundManager {
  private var musicClip: Option[Clip] = None

  def playSfx(path: String, volume: Float = 0.0f): Unit = {
    Future {
      try {
        val audioFile = new File(path)
        val audioInputStream = AudioSystem.getAudioInputStream(audioFile)
        val clip = AudioSystem.getClip
        clip.open(audioInputStream)

        // Adjust volume (volume is in decibels)
        val gainControl = clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN)
          .asInstanceOf[javax.sound.sampled.FloatControl]

        gainControl.setValue(volume) // Volume: 0.0f = default, negative = quieter, positive = louder

        clip.start()
      } catch {
        case e: Exception =>
          println(s"Error playing sound effect: ${e.getMessage}")
      }
    }
  }

  def playMusic(path: String, loop: Boolean = true, volume: Float = 0.0f): Unit = {
    stopMusic()
    
    try {
      val audioFile = new File(path)
      if (!audioFile.exists()) {
        println(s"Music file not found: ${audioFile.getAbsolutePath}")
        return
      }
      
      val audioInputStream = AudioSystem.getAudioInputStream(audioFile)
      val clip = AudioSystem.getClip
      clip.open(audioInputStream)

       // Adjust volume (volume is in decibels)
        val gainControl = clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN)
          .asInstanceOf[javax.sound.sampled.FloatControl]

        gainControl.setValue(volume) // Volume: 0.0f = default, negative = quieter, positive = louder
      
        musicClip = Some(clip) // <- MOVE this BEFORE starting
        
        if (loop) {
          clip.loop(Clip.LOOP_CONTINUOUSLY)
        }
        clip.start() // Start AFTER you have assigned it to musicClip
      } catch {
        case e: Exception =>
          println(s"Error playing music: ${e.getMessage}")
          e.printStackTrace()
      }
    }

  def stopMusic(): Unit = {
    musicClip.foreach { clip =>
      if (clip.isRunning) {
        clip.stop()
      }
      clip.close()
    }
    musicClip = None
  }
}