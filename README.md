Many BLE Beacon scanner apps exist in App store, but I think they’re not enough for who wants to work or research on the beacon proximity detection by RSS values. Actually they just show raw RSS values. I saw many ones that at first say raw RSS value isn’t confident for this purpose. So at the second step people usually think about some simple algorithms like **Moving Average of RSS values**. 
According to my experience, these algorithms work well. But in some occasions there exists some issues, e.g. Ask yourself these questions :
1. Which RSS value can be a threshold or a bound between when I far from or near the beacon?
2. Which Beacon's TX power setting is better?
3. What is the minimum possible distance between two beacons?
4. What is the best window length in moving average algorithm?
and So on. 

Generally, After 3 years of working on the field of Indoor Positioning Systems, I found it out that for BLE Beacons, the most confident solution isn’t Triangulation or Fingerprinting. The best solution is the most simplest one, Proximity detection(Believe me!). Again, according to my own research experience, heuristic and practical algorithms works well in the proximity detection. So After all of these, I can say that We provide a simple android code, that you can write and test your own practical algorithm to detect proximity of a beacon.
I should say that I think simple tools can be better than complex tools, So we avoid to provide any map to detect proximity. Just name your beacon, place them in the area and try to improve your proximity algorithm.


Notes
============

### 1. Run it

I found Altbeacon library very attractive. At first steps I wrote my own beacon scanner. But according to the Android update, change and deprecations, It isn’t a good solution to write your own. Altbeacon lib did it before!
Don’t forget to turn on your phone bluetooth sensor and gps.

All we tried to provide is a simple android code that you can edit it completely, So don’t hesitate yourself to find details in this doc! Read the code directly, It’s so simple.

Track the first beacon(has the strongest RSS) to find out that your algorithm works or not. I mean if you walk and cross near a beacon, you should see that beacon on the top the list. 

### 2. Change the settings

There are two things that you should pay attention when the app can’t detect your beacons:
A. Beacon_Layouts
B. Beacon_UUID
Add your own ones to the Constants java class.

### 3. Write your own algorithm



