package fr.mistertibax.teamfight.boostrap;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import fr.theshark34.openlauncherlib.bootstrap.Bootstrap;
import fr.theshark34.openlauncherlib.bootstrap.LauncherClasspath;
import fr.theshark34.openlauncherlib.bootstrap.LauncherInfos;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.openlauncherlib.util.GameDir;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;




public class TeamFightBootstrap
{
  private static SplashScreen splash;
  private static SColoredBar bar;
  private static Thread barThread;
  private static final LauncherInfos RG_B_INFOS = new LauncherInfos("Rampageous", "fr.trokana.launcher.LauncherFrame");
  private static final File RG_DIR = GameDir.createGameDir("Rampageous");
  private static final LauncherClasspath RG_B_CP = new LauncherClasspath(new File(RG_DIR, "Launcher/launcher.jar"), new File(RG_DIR, "Launcher/Libs"));
  
  private static ErrorUtil errorUtil = new ErrorUtil(new File(RG_DIR, "Launcher/crashes/"));
  
  public static void main(String[] args) {
    Swinger.setResourcePath("/fr/trokana/bootstrap/resources/");
    displaySplash();
    try {
      doUpdate();
    } catch (Exception e) {
      errorUtil.catchError(e, "Erreur lors de la mise � jour du launcher");
      barThread.interrupt();
    } 
    
    launchLauncher(); 
  }

  
  private static void displaySplash() {
    splash = new SplashScreen("Rampageous", Swinger.getResource("splash.png"));
    splash.setLayout(null);
    
    bar = new SColoredBar(new Color(217, 217, 217, 0), new Color(79, 79, 79));
    bar.setBounds(0, 275, 800, 25);
    splash.add(bar);
    
    splash.setVisible(true);
  }
  
  private static void doUpdate() {
    su = new SUpdate("http://rampageous.fr/update/files/bootstrap/", new File(RG_DIR, "Launcher"));
    
    barThread = new Thread()
      {
        public void run() {
          while (!isInterrupted()) {
            bar.setValue((int)(BarAPI.getNumberOfTotalDownloadedBytes() / 1000L));
            bar.setValue((int)(BarAPI.getNumberOfTotalBytesToDownload() / 1000L));
          } 
        }
      };
    barThread.start();
    
    su.start();
    barThread.interrupt();
  }
  
  private static void launchLauncher() {
    bootstrap = new Bootstrap(RG_B_CP, RG_B_INFOS);
    Process p = bootstrap.launch();
    
    splash.setVisible(false);
    
    try {
      p.waitFor();
    } catch (InterruptedException interruptedException) {}

    
    System.exit(0);
  }
}
