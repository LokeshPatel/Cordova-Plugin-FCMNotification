var exec = require('cordova/exec');
var fcmService = {
  fcmTokenID :function(callbacksucess,callbackfail) {
     exec(callbacksucess,callbackfail,"FCMNotificationPlugin", "FCMTokenID", []);
   },
  fcmInstall :function(callbacksucess,callbackfail) {
    exec(callbacksucess,callbackfail,"FCMNotificationPlugin", "installPlugin", []);
  },
  fcmReadAllMessages :function(callbacksucess,callbackfail) {
      exec(callbacksucess,callbackfail,"FCMNotificationPlugin", "getAllMessages", []);
    },
  fcmClearAllMessages :function(callbacksucess,callbackfail) {
       exec(callbacksucess,callbackfail,"FCMNotificationPlugin", "clearMessageFromStore", []);
     },
  fcmClearByMessagesId :function(callbacksucess,callbackfail,messageID) {
      if(messageID == "")
      {
        callbackfail("Insert Message Id");
        return;
        }
        exec(callbacksucess,callbackfail,"FCMNotificationPlugin", "clearMessageByIdFromStore", [messageID]);
    }
};
module.exports = fcmService;
