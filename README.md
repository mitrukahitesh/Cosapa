<img src="https://github.com/mitrukahitesh/Cosapa/blob/main/app/src/main/res/drawable-xxhdpi/cosapalogo.png?raw=true" height=300/>

# Cosapa

## Built at Microsoft Imagine Cup

### A one stop solution for remote health-care and fitness

## What is Cosapa?

Cosapa is a platform driven by Android, IoT and Microsoft Azure Services that aims in making health consultation services completely virtual, physical fitness an engaging activity, and bridge the gap between people who need help and people who can provide help. The IoT device built by us helps in monitoring health factors like heartbeat rate, oxygen level and temperature which can be shared with the doctor, appointed on our platform itself with the help of Beckn protocol, during online consultation. Our app, with the help of Azure Maps, also supports the task of finding the stores for prescribed medicine and labs for recommended tests nearby us and navigate to the exact location of the same through Google Assistant powered voice interaction.

Users can also post about emergency requirements like oxygen, hospital beds, etc and verified volunteers from Cosapa would help them. For every helping hand, Cosapa volunteers get rewarded with Cosapa coins.

Additionally, fitness experts can post challenges for various exercises which are taken by other users which makes fitness activity fun, interactive, and engaging. The best part about this feature is that the number of sets of exercise performed is monitored by our IoT device, thus making our platform more fun and authentic. Cosapa platform is completely gamified and rewards users with in-app currency, Cosapa coins which can further be exchanged for goods and services among Cosapa partner services.

### Our Achievements

We were rewarded the <b>first runner up's</b> postion at Beckn Protocol's Beckn-a-thon 2021 this December. Here is the link for official [Linkedin Post](https://www.linkedin.com/posts/becknprotocol_beckn-a-thon-first-runner-up-cosapa-activity-6874972556143706112-PKcW)

### Hardware Used

- ESP8266
- MAX30100
- lm34
- HC-SR04

<p>
<img height=350px src = "https://github.com/mitrukahitesh/Cosapa/blob/main/videos/cosapa_device.gif" />
</p>

## Development Setup

# Requirements

The project requires Android Studio with Gradle version 4.2.2 to build without errors.

# Testing

The .apk file produced on building the project will require an Android device/emulator with Android version being Oreo 8.0 or higher.

## Usage

# Installation

<ol type='A'>
<li>Clone the repository on your device, and build the project using Android Studio and run on the connected Android device/emulator.</li>
<li>Download the apk from <a href="https://drive.google.com/file/d/1p16y8Ecrlofh2kjyAYeYn6LwQgESiZgF/view?usp=sharing">here </a>
   and install and run on your Android device.</li>
</ol>

# Features

<ol>
  <li><b>Help Posts: </b>Seek medical help from the community</li>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/1.jpg?raw=true" height=300/>
     <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/2.jpg?raw=true" height=300/>
  </div>

  <li><b>Challenges: </b>Add fitness challenge and complete challenges using IoT device</li>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/1.jpg?raw=true" height=300/>
     <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/3.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/4.jpg?raw=true" height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/5.jpg?raw=true" height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/6.jpg?raw=true" height=300/>
  </div>

  <li><b>Health Dashboard: </b>Monitor health with our IoT device and keep it updated on dashboard</li>
  <div style='display:flex;flex-direction:row'>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/7.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/8.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/9.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/11.jpg?raw=true"  height=300/>
  </div>

  <li><b>Online consultation: </b>Appoint a doctor for online consultation</li>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/12.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/13.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/14.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/6.jpg?raw=true"  height=300/>
  </div>

  <li><b>Rewards and Recognization: </b>Get Cosapa coins for in-app activities</li>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/18.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/6.jpg?raw=true"  height=300/>
  </div>

  <li><b>Nearby Services: </b>Search nearby pharmacies and labs for prescribed medicine and test</li>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/16.jpg?raw=true"  height=300/>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/17.jpg?raw=true"  height=300/>
  </div>

</ol>

## Architecture of App

  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/videos/architecture.gif"  height=480 width=1296/>
  </div>

## Survey Results

These are the results of survey conducted by us<br>

<div>
  <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/videos/survey.gif"  height=480 width=1296/>
</div>

## Technologies that have been used in this project

<ol>
  <li>IoT</li>
  <li>Azure IoT Hub</li>
  <li>Azure Data Explorer</li>
  <li>Azure Active Directory</li>
  <li>Azure Maps</li>
  <li>Android</li>
  <li>Node.js</li>
  <li>Firebase Firestore</li>
  <li>Firebase Realtime Database</li>
  <li>Firebase Storage</li>
  <li>Firebase Authentication</li>
  <li>Beckn Protocol</li>
</ol>

## Links

[Android App](https://drive.google.com/file/d/1p16y8Ecrlofh2kjyAYeYn6LwQgESiZgF/view?usp=sharing)<br>
[IoT Repository](https://github.com/naazkakria/Cosapa-imagine-cup)<br>
[Beckn](https://github.com/beckn)<br>
[About Cosapa](https://www.youtube.com/watch?v=8h2DhV9YSqw)

<!-- ## Our Team

<div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/naaz.png?raw=true"  height=72 width=72 style="border-radius: 50%;"/>
    <br>
    <a href="https://www.linkedin.com/in/naazk3/"><b>Naaz Kakria</b></a>
    <br>
    <br>
  </div>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/shikhar.png?raw=true"  height=72 width=72 style="border-radius: 50%;"/>
    <br>
    <a href="https://www.linkedin.com/in/shikhar236/"><b>Shikhar Srivastava</b></a>
    <br>
    <br>
  </div>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/hitesh.png?raw=true"  height=72 width=72 style="border-radius: 50%;"/>
    <br>
    <a href="https://www.linkedin.com/in/mitrukahitesh/"><b>Hitesh Mitruka</b></a>
    <br>
    <br>
  </div>
  <div>
    <img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/sumit.png?raw=true"  height=72 width=72 style="border-radius: 50%;"/>
    <br>
    <a href="https://in.linkedin.com/in/nxsumityadav"><b>Sumit Kumar</b></a>
    <br>
    <br>
  </div> -->

<p align = "center"><h1  align = "center"> ❤️ Our Team </h1> </p>
<table align = "center">
<tr>
<td align="center"><a href="https://github.com/naazkakria"><img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/naaz.png?raw=true" width=100px height=100px /></a></br> 
	<a href = "https://www.linkedin.com/in/naaz-kakria-b63a30193/" ">Naaz</a>
</td>

<td align="center" ><a href="https://github.com/shikhar8434"><img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/shikhar.png?raw=true" width=100px height=100px /></a></br> 	<a href = "https://www.linkedin.com/in/shikhar236/" style="color:red;">Shikhar</a>
</td>

<td align="center"><a href="https://github.com/mitrukahitesh"><img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/hitesh.png?raw=true" width=100px height=100px /></a></br> 
<a href = "https://www.linkedin.com/in/mitrukahitesh">Hitesh</a>
</td>

<td align="center"><a href="https://github.com/nxsumityadav"><img src="https://github.com/mitrukahitesh/Cosapa/blob/main/screenshots/sumit.png?raw=true" width=100px height=100px /></a></br> 
		<a href = "https://www.linkedin.com/in/nxsumityadav/">Sumit</a>
</td>

</tr>
</table>
