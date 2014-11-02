mac-hopper
==========

<p align="left" >
  <img src="ic_launcher-web.png" alt="mac-hopper logo" height="300px"/>
</p>

Randomise the WiFi MAC address on your Android device to defeat passive location tracking.

## What? Why?

While you're walking around with your WiFi-enabled phone, it's periodically searching for networks to connect to.

The range of WiFi being what it is, this leaks a significant amount of information about where you've been (and also, where you live and work (1)).

A number of advertisers, businesses and analytics companies have realised the value in tracking people using this information. Notable examples that made the press include:

* [Renew - Public recycling bins in London](http://www.engadget.com/2013/08/12/london-recycling-bin-track-smartphones/)
* [Tesco supermarkets](http://www.neilturner.me.uk/2014/07/28/free-wifi-return-data-tracking.html)

(1): _When searching for known networks, phones often send 'probe requests' to BSSIDs of known networks. BSSIDs uniquely identify routers, and mapping providers like Google and Apple have comprehensive databases of router locations thanks to crowd-sourced data from Android and iOS devices._

## Download the app, forget it's installed

MacHopper uses the linux [`ip`](http://linux.die.net/man/8/ip) program to set the public MAC address to a random value at a configurable interval.

To a tracking network, this means your device periodically disappears, and shortly after, a new and apparently different device appears.

## Limitations and outlook

- MacHopper can only help protect against 'passive' tracking in which your device is scanning for networks but is __not connected__.
- Your device may leak other personally-identifiable information as part of network discovery. See ["ArsTechica - Anatomy of an iPhone leak"](http://arstechnica.com/apple/2012/03/anatomy-of-an-iphone-leak/) for more information.
- MAC randomisation should be sufficient to defeat naive tracking if done frequently enough, but if it  MAC randomisation were to become a serious concern to tracking companies it would be easy to 'join up' disconnected sessions by inference or other leaked information.
- At this stage, MacHopper is only a rough prototype, and has not been tested on many devices. It's likely that support for the `ip` command varies between manufacturers and firmware versions. To make sure it works on your device, I'd recommend inspecting your network traffic with WireShark.
- A better solution would involve OS-level support for randomisation (ala iOS 8). If you're interested, consider starring the Android [feature request](https://code.google.com/p/android/issues/detail?id=71084&q=MAC%20randomization&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars), but don't hold your breath because better privacy controls are at odds with Google's business model.
