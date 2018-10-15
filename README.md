# Color Breeze

Color Breeze is an open source Android app which mimics the behavior of Google's Wind Down feature introduced in Android Pie (9.0).

## Getting Started
In order for Color Breeze to work it is required to grant the 'WRITE_SECURE_SETTINGS' permission. The permission is needed to grayscale your device. 

This permission can be granted by giving the app root priviliges or by manually granting it via adb.

### How to manually grant permission
Before following the steps below, make sure you've installed [adb](https://developer.android.com/studio/releases/platform-tools) correctly and it's properly set up in your path variables.

1. Connect your Android device to your computer
2. Execute the following command in your terminal or command prompt: `adb devices`
3. Make sure your device appears in the output and its current state is online
4. Execute `adb shell pm grant com.reverp.colorbreeze android.permission.WRITE_SECURE_SETTINGS`

## Screenshots

[<img width=200 alt="Main Activity" src="assets/screenshot1.png?raw=true">](assets/screenshot1.png?raw=true)
[<img width=200 alt="Settings Activity" src="assets/screenshot2.png?raw=true">](/assets/screenshot2.png?raw=true)
[<img width=200 alt="Example" src="assets/screenshot3.png?raw=true">](/assets/screenshot3.png?raw=true)



## Downloads

Color Breeze can be downloaded from the following sources:

- [Google Play](https://play.google.com/store/apps/details?id=com.reverp.colorbreeze)

## Libraries

- [Fancybuttons](https://github.com/medyo/Fancybuttons) by Medyo

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE.md) file for details
