# wav-checker
Useful for inspecting wav files in Java

I leveraged RIFFReader.java from Sun, thus the GPL2 license.  I wrote this for a specific purpose, so it is not a general utility for wav files.

I created this for a Prophecy IVR project, running call recording at the ccxml level.  The IVR records the SIP outgoing audio on the left, and the incoming audio on the right.  WaveFileChecker will throw an exception if there is one-way audio (no audio coming in).

I also added a folder monitor.  When new calls result in new audio files, the app will validate the recordings after they are written/closed.  When there is a one-way audio exception FolderMonitor will write an alert to the console.

