# Cordova-Plugin-FCMNotification

Firebase Notifications is a free service that enables targeted user notifications for mobile app developers.

Built on Firebase Cloud Messaging and the FCM SDK, Firebase Notifications (Notifications) provides an option for developers and organizations seeking a flexible notification platform that requires minimal coding effort to get started, and a graphical console for sending messages. Using the Notifications console GUI, you can reengage and retain your user base, foster app growth, and support marketing campaigns.

Reference: [Firebase Cloud Messaging](https://firebase.google.com/docs/notifications/)

First Create project on [console firebase](https://console.firebase.google.com/)

Download google-services.json file on firebase console and add "platform/android" path.

###Like below image :

<img src="https://dl.dropboxusercontent.com/s/l5xw8890243805h/3.png?dl=0" alt="Image 1" width="750" height="360">

=============

## Master branch:

 ```
cordova plugin add https://github.com/LokeshPatel/Cordova-Plugin-FCMNotification.git
 ```
## local folder:

 ```
cordova plugin add Cordova-Plugin-FCMNotification -- Search local path

```

### After Install Plugin Need To Update On "build.gradle" File Of Android App
```

Steps:

1.) Open file "build.gradle" (Path : platform > android > build.gradle)

2.) find "buildscript" text in "build.gradle" file.

3.) There you will find one classpath line, after that line, please add this line :

      classpath 'com.google.gms:google-services:3.0.0'

```
###Like below image :

<img src="https://dl.dropboxusercontent.com/s/sa0xmdh3b1e8b8j/1.png?dl=0" alt="Image 0" width="836" height="286">

```
4.) then find "dependencies" (Select that dependencies where you have text "compile" and where that dependencies is getting ended, just after that, add this line :

           apply plugin: 'com.google.gms.google-services'
```
###Like below image :

<img src="https://dl.dropboxusercontent.com/s/nsqyjmpwx6kzniw/2.png?dl=0" alt="Image 1" width="800" height="200">

## 1) Get FCM Notification Token-Id
  ```
     navigator.FCMNotification.fcmTokenID(function(tokenID){
        // retrun token id for notification service
        console.log("Token ID = " + tokenID);
        //Token ID use for call notification form FCM server.
     }, function(error){
        console.log(error);
     });
```

## 2) Message store in local store : Read all message  
  ```
 navigator.FCMNotification.fcmReadAllMessages(function(result){
     /* Return all message in json formate with id and message */
   console.log(result);
 },function(e){console.log(e)}
 );

 /* Result format : {"msg_1":"Hello World One !","msg_2":"Hello World Two!","msg_3":"Hello World Three!","totalCount":3} */
```

## 3) Remove all messages from local store  
  ```
     navigator.FCMNotification.fcmClearAllMessages(function(result){
        console.log(result);

     }, function(error){
        console.log(error);
     });
```

## 4) Remove messages from local store (one by one) :
  ```
     navigator.FCMNotification.fcmClearByMessagesId(function(result){
        console.log(result);
      }, function(error){
        console.log(error);
     },"msg_1");
     
     /* Message get from local store list */
```

Reference: [Firebase Cloud Messaging](https://firebase.google.com/docs/notifications/)

<a href="https://www.paypal.me/LokeshPatel" target="_blank"><img src="https://dl.dropboxusercontent.com/s/r5azqieu9stu0pc/pay-now-button-afme.png?dl=0" alt="Count 0" width="160"/></a>
