---
layout: post
title: Settings permission
---

# Grant secure settings permission

## General
In order to make Color Breeze work it needs to have permission to the developer settings on your Android device.

**Note: Before you follow the steps below, please enable 'Developer settings' and make sure you have enabled USB debugging**

*Requirement*: Make sure you have extracted the [Android SDK tools](https://developer.android.com/studio/releases/platform-tools) correctly.

## Steps
1. Connect your Android device to your computer
2. Execute the following command in terminal / command prompt

    `adb devices`

3.  Make sure your device appears in the list and its current state is online.
4.  Execute the following command to give Color Breeze the correct permissions

    `adb shell pm grant com.reverp.colorbreeze android.permission.WRITE_SECURE_SETTINGS`

5. Click on 'Check permission' in Color Breeze which should work