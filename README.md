# Motive Android App

## To Run This Application Locally

1. Make sure the server is running locally on `8080` port.
2. Run `ifconfig | grep inet` in a command line and find out your local IP address there. It should look something like `192.168.0.13`.
3. Modify `network_security_config.xml` and `application.properties` files to use your local IP address and `8080` port.
4. Run your application on a simulator by e.g. Android Studio.

Note: if during login via VK "redirect_uri has wrong domain, check application settings" error happens, this means that you have to update the IP address from the 2nd paragraph above in the configuration of your VK app.

## To Release

1. Increase the values of `versionCode` and `versionName` in `./app/build.gradle`.
2. Make sure the back end URLs are updated to `api.motivepick.com` in `network_security_config.xml` and to `https://api.motivepick.com` in `application.properties`.
3. Specify values for `MOTIVE_RELEASE_STORE_PASSWORD` and `MOTIVE_RELEASE_KEY_PASSWORD` in `./gradle.properties`. If you do not have the signature file, you can use e.g. Android Studio to generate it.
4. Run `./gradlew clean assembleRelease`
5. The _signed_ assembly should appear in `./app/build/outputs/apk/release`
